package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.dao.IAuditingProsDao;
import com.tibbers.zhonghui.dao.IProductBelongDao;
import com.tibbers.zhonghui.dao.IProductDao;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.AuditingPros;
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
import java.util.*;

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

    @Autowired
    private IAuditingProsDao auditingProsDao;


    @Transactional()
    @Override
    public Map<String, String> insertSingleProduct(Product product, String accountid, Integer number) {
        iProductDao.insertSingleProduct(product);
        ProductBelong productBelong = new ProductBelong();
        productBelong.setSerialid(StringUtil.generateUUID());
        productBelong.setProductid(product.getProductid());
        productBelong.setAccountid(accountid);
        productBelong.setSalestate("0");
        productBelong.setRemaindernum(number);
        productBelong.setFamilyid(product.getFamilyid());
        iProductBelongDao.insertSingleRelation(productBelong);
        logger.info(String.format("记录[%s]插入数据库成功",product));
        AuditingPros auditingPros = new AuditingPros();
        logger.info(String.format("添加产品[%s]的审核申请",product.getProductid()));
        auditingPros.setProductid(product.getProductid());
        auditingPros.setSerialid(StringUtil.generateUUID());
        auditingPros.setApplydatetime(StringUtil.currentDateTime());
        auditingPros.setAuditstate("0");
        auditingPros.setRejectreason("");
        auditingProsDao.applyAudit4Product(auditingPros);

        Map<String,String> map = new HashMap<>();
        map.put("pid",product.getProductid());
        map.put("auditid",auditingPros.getSerialid());

        return map;
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

            List<String> productList = Arrays.asList(productids);
            List<Map<String,String>> proList = iProductDao.queryImagesByProductids(productList);


            String index = request.getParameter("index");
            String headImage = "";
            StringBuilder otherPaths = new StringBuilder();
            //筛选出是否有商品图片
            for(String singlePath : allPath){
                singlePath = singlePath.replaceAll("\\\\","\\/");

                if("true".equals(index)){
                    headImage = singlePath;
                }else {
                    otherPaths.append(singlePath).append("|");
                }
            }

            if(otherPaths.length() > 0) {
                otherPaths.deleteCharAt(otherPaths.length() - 1);
            }
            String detailPaths = otherPaths.toString();//描述图片地址

            List<Product> updatePros = new ArrayList<>();
            for(Map<String,String> map : proList){
                String productid = map.get("productid");
                String imagepath = map.get("imagepath");

                Product product = new Product();
                product.setProductid(productid);
                if(StringUtil.isEmpty(imagepath)){//之前没有上传
                    if(StringUtil.isEmpty(headImage)){//没有产品图片
                        throw new APIException(String.format("请先为产品[%s]上传产品图片",productid));
                    }else {
//                        if(StringUtil.isEmpty(detailPaths)){
                            product.setImagepath(headImage);
                            logger.info(String.format("产品图片路径[%s]",headImage));
//                        }else {
//                            product.setImagepath(headImage + "|" + detailPaths);
//                        }
                    }
                }else {//之前有上传
                    if(StringUtil.isEmpty(headImage)){//没有重新上传产品图片
                        product.setImagepath(imagepath + "|" + detailPaths);
                        logger.info(String.format("产品描述图片路径[%s]",detailPaths));
                    }else {//上传了产品图片
                        if(StringUtil.isEmpty(detailPaths)){
                            product.setImagepath(headImage + "|" + imagepath);
                        }else {
                            product.setImagepath(headImage + "|" + imagepath + "|" + detailPaths);
                        }
                    }
                }

                updatePros.add(product);
            }

            Map<String,Object> map = new HashMap<>();
            map.put("imagepaths",updatePros.get(0).getImagepath());
            map.put("list",updatePros);
            iProductDao.updateImages4Products(map);

            logger.info(String.format("图片上传成功"));

//            if(allPath.size() > 0){
//                StringBuilder builder = new StringBuilder();
//                for (String path : allPath){
//                    builder.append(path).append("|");
//                }
//
//                builder.deleteCharAt(builder.length() - 1);
//                String imagePath = builder.toString().replaceAll("\\\\","\\/");
//                List<Product> products = new ArrayList<>();
//                for(String productid : productids){
//                    Product product = new Product();
//                    product.setProductid(productid);
//                    product.setImagepath(imagePath);
//                    products.add(product);
////                }
//
//
//
//            }else {
//                throw new APIException("请上传图片");
//            }

            return allPath;

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new APIException(e.getMessage(),e);
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
                String storePath = localPath + "/" + file.getOriginalFilename();
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
    public void updateProductInfo(Product product, int remain) {
        logger.info(String.format("更新产品[%s]的信息",product.getProductid()));
        iProductDao.updateProductInfo(product);
        logger.info(String.format("更新产品[%s]信息[%s]成功",product.getProductid(),product));
        if(remain > 0){
            logger.info(String.format("更新产品[%s]的库存信息",product.getProductid()));
            ProductBelong productBelong = new ProductBelong();
            productBelong.setProductid(product.getProductid());
            productBelong.setRemaindernum(remain);
            iProductBelongDao.updateProductBelongRemaindernum(productBelong);
            logger.info(String.format("更新产品[%s]的库存成功",product.getProductid()));
        }
    }

    @Override
    public Map<String, List<String>> insertProductsBatch(String productList, String accountid) {
        logger.info(String.format("开始检验用户[%s]在此之前是否上传过产品，并且该产品有效[未上架|销售中|售罄，待补货|下架]",accountid));
        List<Map<String,String>> resultList = iProductDao.checkOnlyUploadOneProduct(accountid);
        String familyid ;
        Map<String,List<String>> result = new HashMap<>();
        if(resultList.size() > 1){
            throw new APIException(String.format("账户[%s]已经上传过产品了,平台规定一个VIP用户只能上传一种产品（产品规格可以不同）",accountid));
        }
        if(resultList.size() == 1){
            String info = String.format("用户[%s]之前已经上传过产品，本次上传的产品可能不是同一种产品，予以警示，管理员需严格审核",accountid);
            logger.warn(info);
            familyid = resultList.get(0).get("familyid");
            result.put("warn",new ArrayList<>());
        }else {
            familyid = StringUtil.generateUUID();
        }

        List<String> productIdList = new ArrayList<>();
        List<Product> products = JSONObject.parseArray(productList,Product.class);
        List<ProductBelong> productBelongs = new ArrayList<>();
        List<AuditingPros> auditingProsList = new ArrayList<>();
        List<String> auditings = new ArrayList<>();
        for(Product product : products){
            String productid = StringUtil.generateUUID();
            product.setProductid(productid);
            product.setFamilyid(familyid);
            productIdList.add(productid);

            ProductBelong productBelong = new ProductBelong();
            productBelong.setSerialid(StringUtil.serialId());
            productBelong.setAccountid(accountid);
            productBelong.setRemaindernum(Integer.parseInt(product.getReverse1()));
            productBelong.setProductid(product.getProductid());
            productBelong.setSalestate("0");
            productBelong.setFamilyid(product.getFamilyid());
            productBelongs.add(productBelong);

            product.setReverse1("");
            product.setReverse2("");

            AuditingPros auditingPros = new AuditingPros();
            logger.info(String.format("添加产品[%s]的审核申请",productid));
            auditingPros.setProductid(productid);
            auditingPros.setSerialid(StringUtil.generateUUID());
            auditingPros.setApplydatetime(StringUtil.currentDateTime());
            auditingPros.setAuditstate("0");
            auditingPros.setRejectreason("");
            auditingProsList.add(auditingPros);
            auditings.add(auditingPros.getSerialid());

        }
        logger.info(String.format("开始插入[%s]条产品记录",products.size()));
        iProductDao.insertBatchProduct(products);
        logger.info(String.format("更新产品归属表，新增[%s]的上传产品记录",accountid));
        iProductBelongDao.insertBatchRelation(productBelongs);
        logger.info(String.format("更新产品审核信息[%s]",auditingProsList));
        auditingProsDao.applyAudit4Products(auditingProsList);


        result.put("pids",productIdList);
        result.put("auditids",auditings);

        return result;
    }

    @Override
    public String checkOnlyUploadOneProduct(String accountid) {
        logger.info(String.format("开始查询账户[%s]是否已经上传过产品，并且该产品有效[未上架|销售中|售罄，待补货|下架]",accountid));
        List<Map<String,String>> result = iProductDao.checkOnlyUploadOneProduct(accountid);
        if(result.size() > 1){
            throw new APIException(String.format("账户[%s]已经上传过产品了,平台规定一个VIP用户只能上传一种产品（产品规格可以不同）",accountid));
        }
        if(result.size() == 1){
            return result.get(0).get("familyid");
        }
        return "";
    }

}
