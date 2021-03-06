package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.common.Pager;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 21:33
 * @description:
 */
public interface IProductService {
    Map<String, String> insertSingleProduct(Product product, String accountid, Integer number);

    void insertBatchProduct(List<Product> productList);

    List<String> uploadImage(HttpServletRequest request, String[] productids);

    Map<String, Object> queryByProductId(String productId);

    List<Product>  queryProductByPage(Pager pager);

    List<Map<String, Object>> queryByProductStates(String[] states, Pager pager);

    void updateProductState(String productid,String targteState);

    List<Product> queryProducts(String productQueryInfo, Pager pager, String[] productStates);

    List<Map<String,Object>> vipQueryUploadProducts(Product product, Account account,Pager pager);

    void updateProductBelongRemaindernum(String productid,String remaindernum);

    List<Map<String,Object>> salerQueryProductsByState(Product product,Account account,String salestates,Pager pager);

    void updateImages4Products(MultipartFile[] files, String[] productids);

    void updateProductInfo(Product product, int remain);

    Map<String, List<String>> insertProductsBatch(String productList, String accountid);

    String checkOnlyUploadOneProduct(String accountid);
}
