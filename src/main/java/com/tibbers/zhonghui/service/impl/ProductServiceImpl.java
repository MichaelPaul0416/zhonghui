package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.dao.IProductBelongDao;
import com.tibbers.zhonghui.dao.IProductDao;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.ProductBelong;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IProductService;
import com.tibbers.zhonghui.utils.StringUtil;
import com.tibbers.zhonghui.utils.WxLoginUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 21:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements IProductService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IProductDao iProductDao;

    @Autowired
    private IProductBelongDao iProductBelongDao;

    @Autowired
    private ServiceConfigBean serviceConfigBean;


    @Transactional()
    @Override
    public void insertSingleProduct(Product product, String accountid, Integer number) {
        iProductDao.insertSingleProduct(product);
        ProductBelong productBelong = new ProductBelong();
        productBelong.setSerialid(StringUtil.generateUUID());
        productBelong.setProductid(product.getProductid());
        productBelong.setAccountid(accountid);
        productBelong.setSalestate("0");
        productBelong.setRemaindernum(number);
        iProductBelongDao.insertSingleRelation(productBelong);
        logger.info(String.format("记录[%s]插入数据库成功",product));

    }

    @Override
    public void insertBatchProduct(List<Product> productList) {
        iProductDao.insertBatchProduct(productList);
        logger.info(String.format("批量记录插入成功，共计[%s]条记录",productList.size()));
    }

    @Override
    public List<String> uploadImage(HttpServletRequest request, String[] productids) {
        try {
            String localPath = serviceConfigBean.getAbsoluteProductPathPrefix();
            List<String> allPath = WxLoginUtil.upload(request,localPath);
            logger.info(String.format("文件[%s]落地完成",allPath));
            if(allPath.size() > 0){
                StringBuilder builder = new StringBuilder();
                for (String path : allPath){
                    builder.append(path).append("|");
                }

                builder.deleteCharAt(builder.length() - 1);
                String imagePath = builder.toString().replaceAll("\\\\","\\/");
                List<Product> products = new ArrayList<>();
                for(String productid : productids){
                    Product product = new Product();
                    product.setProductid(productid);
                    product.setImagepath(imagePath);
                    products.add(product);
                }


                Map<String,Object> map = new HashMap<>();
                map.put("imagepaths",imagePath);
                map.put("list",products);
                iProductDao.updateImages4Products(map);

                logger.info(String.format("产品描述图片上传成功"));
            }else {
                throw new APIException("请上传图片");
            }

            return allPath;

        } catch (Exception e) {
            throw new APIException(e.getCause());
        }
    }

    @Override
    public Map<String, Object> queryByProductId(String productId) {
        return iProductDao.queryByProductId(productId);
    }

    @Override
    public List<Product> queryProductByPage(Pager pager) {
        return iProductDao.queryProductByPage(pager);
    }

    @Override
    public List<Map<String, Object>> queryByProductStates(String[] states, Pager pager) {
        Map<String,Object> params = new HashMap<>();
        params.put("states",states);
        params.put("pager",pager);
        return iProductDao.queryByProductStates(params);
    }

    @Override
    public void updateProductState(String productid, String targteState) {
        logger.info(String.format("开始更新产品[%s]的销售状态，变更为[%s]",productid,targteState));
        ProductBelong productBelong = new ProductBelong();
        productBelong.setProductid(productid);
        productBelong.setSalestate(targteState);
        try {
            iProductBelongDao.updateProductState(productBelong);
            logger.info(String.format("产品[%s]销售状态更新成功",productid));
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public List<Product> queryProducts(String productQueryInfo, Pager pager, String[] productStates) {
        Product product = JSONObject.parseObject(productQueryInfo,Product.class);
        product.setReverse2("");
        logger.info(String.format("查询符合下列条件的产品信息[%s]",product));

        try{
            Map<String,Object> param = new HashMap<>();
            param.put("product",product);
            param.put("states",productStates);
            param.put("pager",pager);

            List<Product> products = iProductDao.queryProducts(param);
            logger.info(String.format("查询到符合条件的产品信息[%s]",products));
            return products;
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> vipQueryUploadProducts(Product product, Account account, Pager pager) {
        logger.info(String.format("开始为VIP用户[%s]查询他上传的产品信息",account.getAccountid()));
        if("1".equals(account.getIsvip())){
            Map<String,Object> map = new HashMap<>();
            map.put("product",product);
            map.put("pager",pager);
            map.put("account",account);
            List<Map<String,Object>> productList = iProductDao.vipQueryUploadProducts(map);

            logger.info(String.format("查询到VIP用户[%s]的上传产品[%s]",account.getAccountid(),productList));
            return  productList;
        }else {
            throw new APIException(String.format("只有VIP用户才能上传商品，您[%s]当前为非VIP用户，若想在商城中销售商品，请先向管理员申请成为VIP用户",account.getAccountid()));
        }
    }

    @Override
    public void updateProductBelongRemaindernum(String productid, String remaindernum) {
        ProductBelong productBelong = new ProductBelong();
        productBelong.setProductid(productid);

        if(StringUtil.isEmpty(remaindernum)){
            throw new APIException("产品余量remainerdernum必须输入");
        }
        productBelong.setRemaindernum(Integer.parseInt(remaindernum));

        logger.info(String.format("更新产品[%s]的归属信息[%s]",productid,productBelong));

        iProductBelongDao.updateProductBelongRemaindernum(productBelong);

        logger.info(String.format("产品归属信息[%s]更新成功",productBelong));
    }

    @Override
    public List<Map<String, Object>> salerQueryProductsByState(Product product, Account account, String salestates, Pager pager) {
        logger.info(String.format("开始为VIP用户[%s]按照状态[%s]查询他的上传产品信息",account.getAccountid(),salestates));
        if("1".equals(account.getIsvip())){
            Map<String,Object> param = new HashMap<>();
            param.put("account",account);
            param.put("prodduct",product);
            param.put("salestate",salestates);
            param.put("pager",pager);

            List<Map<String,Object>> result = iProductDao.salerQueryProductsByState(param);
            logger.info(String.format("VIP用户[%s]根据产品状态[%s]查询到的产品信息列表[%s]",account.getAccountid(),salestates,result));

            return result;
        }else {
            throw new APIException(String.format("只有VIP用户才能上传商品，您[%s]当前为非VIP用户，若想在商城中销售商品，请先向管理员申请成为VIP用户",account.getAccountid()));
        }
    }

    @Override
    public void updateImages4Products(MultipartFile[] files, String[] productids) {
        String localPath = serviceConfigBean.getAbsoluteProductPathPrefix();
        try {
            File dir = new File(localPath);
            if(!dir.exists()){
                logger.info(String.format("目录[%s]不存在，创建文件夹",localPath));
            }
            StringBuilder builder = new StringBuilder();
            List<Product> products = new ArrayList<>();
            for(MultipartFile file : files){
                String storePath = localPath + "\\" + file.getOriginalFilename();
                FileUtils.copyInputStreamToFile(file.getInputStream(),new File(storePath));
                logger.info(String.format("文件[%s]落地完成",storePath));
                builder.append(storePath).append("|");
            }

            builder.deleteCharAt(builder.length() - 1);

            String allPath = builder.toString();

            for(String productid : productids){
                Product product = new Product();
                product.setProductid(productid);
                product.setImagepath(allPath);
                products.add(product);
            }


            Map<String,Object> map = new HashMap<>();
            map.put("imagepaths",allPath);
            map.put("list",products);
            iProductDao.updateImages4Products(map);

            logger.info(String.format("产品描述图片上传成功"));

        } catch (Exception e) {
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void updateProductInfo(Product product) {
        logger.info(String.format("更新产品[%s]的信息",product.getProductid()));
        iProductDao.updateProductInfo(product);
        logger.info(String.format("更新产品[%s]信息[%s]成功",product.getProductid(),product));
    }

}
