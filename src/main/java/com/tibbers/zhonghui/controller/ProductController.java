package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IProductService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 22:11
 * @description:
 */
@Controller
@RequestMapping("product")
public class ProductController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IProductService productService;

    @Autowired
    private ServiceConfigBean serviceConfigBean;

    @RequestMapping("/insertSingleProduct")
    @ResponseBody
    public String  insertSingleProduct(String productInfo,String accountid){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtils.isEmpty(productInfo)) {
            try {
                Product product = JSONObject.parseObject(productInfo,Product.class);
                String productId = StringUtil.generateUUID();
                logger.info(String.format("为产品[%s]生成序列id[%s]",product.getProductname(),productId));
                product.setProductid(productId);
                productService.insertSingleProduct(product,accountid);
                response = new Response(true, productId);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                response = new Response(false, e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
            }
        }else {
            response = new Response(false,"传入产品信息为空，无法添加");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/insertBatchProduct")
    @ResponseBody
    public String insertBatchProduct(String productList){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(productList)){
            try{
                List<String> productIdList = new ArrayList<>();
                List<Product> products = JSONObject.parseArray(productList,Product.class);
                for(Product product : products){
                    String productid = StringUtil.generateUUID();
                    product.setProductid(productid);
                    productIdList.add(productid);
                }
                logger.info(String.format("开始插入[%s]条产品记录",products.size()));
                productService.insertBatchProduct(products);
                Map<String,Object> responseMap = new HashMap<>();
                responseMap.put("size",productIdList.size());
                responseMap.put("tip","批量插入产品ID");
                responseMap.put("list",productIdList);
                response = new Response(true, responseMap);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false, e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
            }
        }else {
            response = new Response(false,"没有传入产品的批量信息");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadFile(@RequestParam("upload") MultipartFile file, HttpServletRequest request) {
        APIResponse apiResponse;
        Response response;
        try {
            if(file.getInputStream() != null) {
                Product product = new Product();
                product.setProductid(request.getParameter("productid"));
                product.setProductname(file.getOriginalFilename());
                productService.uploadImage(file.getInputStream(), product);
                response = new Response(true, "文件上传成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }else {
                response = new Response(false,"没有接收到上传的文件");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }catch (APIException e){
            logger.error(e.getMessage(),e);
            response = new Response(false, e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            response = new Response(false, "读取上传的文件流失败");
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryByProductId")
    @ResponseBody
    public String queryByProductId(String productid){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(productid)){
            try{
                Product product = productService.queryByProductId(productid);
                logger.info(String.format("查询到匹配的产品信息[%s]",product));
                response = new Response(true,product);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"入参productid为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryProductByPage")
    @ResponseBody
    public String queryProductByPage(int startLine,int offset){
        APIResponse apiResponse;
        Response response;
        try{
            Pager pager = new Pager(startLine,offset);
            logger.info(String.format("开始分页查询...,页码参数[%s]",pager));
            List<Product> products = productService.queryProductByPage(pager);
            logger.info(String.format("分页查询结果[%s]",products));
            response = new Response(true,products);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryByProductStates")
    @ResponseBody
    public String queryByProductStates(String states, @Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(states)){
            try {
                Pager pager = null;
                if(!StringUtils.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }
                String[] productStates = states.split(",");
                List<Product> result = productService.queryByProductStates(productStates,pager);
                response = new Response(true, result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"请输入需要查询的状态[0:未上架,1:在售,2:售罄,3:下架]");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/updateProductState")
    @ResponseBody
    public String updateProductState(String productid,String targetstate){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(productid) && !StringUtils.isEmpty(targetstate)){
            try {
                productService.updateProductState(productid, targetstate);
                response = new Response(true,String.format("产品[%s]状态更新成功",productid));
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"输入的产品id和需要更改的产品目标状态不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/showProductImage")
    public void showProductImage(String productid, HttpServletResponse response){
        File image;
        if(!StringUtils.isEmpty(productid)){
            Product product = productService.queryByProductId(productid);
            if(product != null){
                image = new File(product.getImagepath());
                if(!image.exists()){
                    image = new File(serviceConfigBean.getDefaultImagePath());
                }
            }else {
                image = new File(serviceConfigBean.getDefaultImagePath());
            }
        }else{
            image = new File(serviceConfigBean.getDefaultImagePath());
        }
        FileInputStream inputStream = null;
        OutputStream outputStream;
        try {
            inputStream = new FileInputStream(image);
            byte[] data = new byte[(int)image.length()];
            inputStream.read(data);
            response.setContentType("image/jpeg");
            outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            logger.error(e.getMessage(),e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
    }

}
