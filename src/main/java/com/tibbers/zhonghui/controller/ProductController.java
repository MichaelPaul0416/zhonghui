package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.dao.IAuditingProsDao;
import com.tibbers.zhonghui.dao.IProductBelongDao;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.AuditingPros;
import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.ProductBelong;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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


    @RequestMapping("/updateProductInfo")
    @ResponseBody
    public String updateProductInfo(String productInfo){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(productInfo)){
            try{
                Product product = JSONObject.parseObject(productInfo,Product.class);
                productService.updateProductInfo(product);
                Map<String,Object> result = new HashMap<>();
                result.put("flag",true);
                result.put("msg","update successfully");
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"更新产品信息productInfo不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/salerQueryProductsByState")
    @ResponseBody
    public String salerQueryProductsByState(String accountInfo,String productInfo,String salestates, String startLine, String offset){
        APIResponse apiResponse;
        Response response;

        if(StringUtil.argsNotEmpty(new String[]{accountInfo,productInfo,salestates})){
            try{
                Pager pager = null;
                if(!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }

                Product product = JSONObject.parseObject(productInfo,Product.class);
                Account account = JSONObject.parseObject(accountInfo,Account.class);
                List<Map<String,Object>> result = productService.salerQueryProductsByState(product,account,salestates,pager);

                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"用户信息accountInfo,产品查询信息productInfo,销售状态salestates不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/updateProductBelongRemaindernum")
    @ResponseBody
    public String updateProductBelongRemaindernum(String productid,String remaindernum){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(productid)){
            try{
                productService.updateProductBelongRemaindernum(productid,remaindernum);
                response = new Response(true,String.format("产品[%s]的归属信息已经更新",productid));
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"产品编号productid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/vipQueryUploadProducts")
    @ResponseBody
    public String vipQueryUploadProducts( String productinfo,String accountinfo, String startLine, String offset){
        APIResponse apiResponse;
        Response response;
        if(StringUtil.argsNotEmpty(new String[]{accountinfo})){
            try {
                Pager pager = null;
                if (StringUtil.argsNotEmpty(new String[]{startLine, offset})) {
                    pager = new Pager(Integer.parseInt(startLine), Integer.parseInt(offset));
                }
                Product product = null;
                if(!StringUtil.isEmpty(productinfo)){
                    product = JSONObject.parseObject(productinfo,Product.class);
                }
                Account account = JSONObject.parseObject(accountinfo,Account.class);
                List<Map<String, Object>> result = productService.vipQueryUploadProducts(product,account,pager);
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"VIP账户信息accountinfo不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return JSONObject.toJSONString(apiResponse);
    }


    @RequestMapping("/queryProducts")
    @ResponseBody
    public String queryProducts(String productQueryInfo,String states,int startLine,int offset){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(productQueryInfo)){
            try {

                String statesAry[] = null;
                if(!StringUtil.isEmpty(states)){
                    statesAry = states.split(",");
                }
                Pager pager = new Pager(startLine,offset);

                List<Product> productList = productService.queryProducts(productQueryInfo,pager,statesAry);

                response = new Response(true,productList);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"产品的查询条件不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/insertSingleProduct")
    @ResponseBody
    public String  insertSingleProduct(String productInfo,String accountid){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtils.isEmpty(productInfo)) {
            try {
                String familyid = productService.checkOnlyUploadOneProduct(accountid);
                boolean alreadyUpload = false;
                if(StringUtil.isEmpty(familyid)){
                    logger.info(String.format("用户[%s]未上传过产品，生成新的产品组id",accountid));
                    familyid = StringUtil.generateUUID();
                }else {
                    alreadyUpload = true;
                }
                Product product = JSONObject.parseObject(productInfo,Product.class);
                String productId = StringUtil.generateUUID();
                logger.info(String.format("为产品[%s]生成序列id[%s]",product.getProductname(),productId));
                product.setProductid(productId);
                product.setFamilyid(familyid);
                int number = Integer.parseInt(product.getReverse1());
                product.setReverse1("");
                product.setReverse2("");
                Map<String,String> map = productService.insertSingleProduct(product,accountid, number);
                if(alreadyUpload){
                    String info = String.format("用户[%s]之前已经上传过产品，根据众惠平台规定，一个VIP用户只能上传一种产品（规格可以不同），本次上传的产品可能不是同一种产品，予以警示，管理员会严格审核",accountid);
                    map.put("warn",info);
                }
                response = new Response(true, map);
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
    public String insertBatchProduct(String productList, String accountid){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(productList) && !StringUtil.isEmpty(accountid)){
            try{
                Map<String,List<String>> map = productService.insertProductsBatch(productList,accountid);
                Map<String,Object> responseMap = new HashMap<>();
                responseMap.put("tip","批量插入产品ID/批量产品审核申请ID");
                responseMap.put("accountid",accountid);
                responseMap.put("data",map);
                if(map.get("warn") != null){
                    String info = String.format("用户[%s]之前已经上传过产品，根据众惠平台规定，一个VIP用户只能上传一种产品（规格可以不同），本次上传的产品可能不是同一种产品，予以警示，管理员会严格审核",accountid);
                    responseMap.put("warn",info);
                }
                response = new Response(true, responseMap);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false, e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
            }
        }else {
            response = new Response(false,"产品的批量信息productList以及产品归属者accountid必传");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadFile(HttpServletRequest request) {
        APIResponse apiResponse;
        Response response;
        try {
            String productids = request.getParameter("productids");
            if(!StringUtil.isEmpty(productids)) {
                String[] pids = productids.split(",");
                List<String> paths = productService.uploadImage(request,pids);
                Map<String,Object> result = new HashMap<>();
                result.put("flag",true);
                result.put("size",paths.size());
                result.put("msg","upload successfully");
                result.put("paths",paths);
                response = new Response(true, result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }else {
                response = new Response(false,"请输入需要更新图片的产品[productids]");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }catch (APIException e){
            logger.error(e.getMessage(),e);
            response = new Response(false, e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

//    @RequestMapping("/productpidupload")
//    public void productpidupload(HttpServletRequest request,HttpServletResponse response) throws Exception{
//
//        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
//        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
//        //检查form中是否有enctype="multipart/form-data"
//        if(multipartResolver.isMultipart(request)){
//            //将request变成多部分request
//            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
//            //获取multiRequest 中所有的文件名
//            Iterator iter=multiRequest.getFileNames();
//
//            while(iter.hasNext()){
//                //一次遍历所有文件
//                MultipartFile file=multiRequest.getFile(iter.next().toString());
//                if(file!=null){
//                    String path="E:/springUpload"+file.getOriginalFilename();
//                    //上传
//                    file.transferTo(new File(path));
//                }
//            }
//
//        }
//        response.getWriter().write("hello");
//        response.getWriter().flush();
//        response.getWriter().close();
//
//    }

    @RequestMapping("/updateImages4Products")
    @ResponseBody
    public String updateImages4Products(@RequestParam("proImageDetails") MultipartFile[] files,HttpServletRequest request){
        APIResponse apiResponse;
        Response response;

        try{
            String productids = request.getParameter("productids");
            if(!StringUtil.isEmpty(productids)){
                String[] pids = productids.split(",");
                productService.updateImages4Products(files,pids);
                Map<String,Boolean> map = new HashMap<>();
                map.put("flag",true);

                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }else {
                throw new APIException("需要更新的productids不能为空");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getCause().getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/queryByProductId")
    @ResponseBody
    public String queryByProductId(String productid){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(productid)){
            try{
                Map<String,Object> product = productService.queryByProductId(productid);
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
    public String queryByProductStates(String states,  String startLine, String offset){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(states)){
            try {
                Pager pager = null;
                if(!StringUtils.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }
                String[] productStates = states.split(",");
                List<Map<String,Object>> result = productService.queryByProductStates(productStates,pager);
                response = new Response(true, result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"请输入需要查询的状态[0:未上架,1:在售,2:售罄,3:下架,4:已删除]");
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
    public void showProductImage(String productid, String index ,HttpServletResponse response){
        File image;
        if(!StringUtils.isEmpty(productid)){
            Map<String,Object> product = productService.queryByProductId(productid);
            if(product != null){
                Integer number = Integer.parseInt(index);
                String[] paths = String.valueOf(product.get("imagepath")).split("\\|");
                if(number >= paths.length){
                    throw new APIException("请求的图片不存在");
                }else {
                    image = new File(paths[number]);
                    if (!image.exists()) {
                        image = new File(serviceConfigBean.getDefaultImagePath());
                    }
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
