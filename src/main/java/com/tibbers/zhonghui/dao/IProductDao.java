package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.common.Pager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 21:11
 * @description:
 */
@Repository
public interface IProductDao {
    void insertSingleProduct(Product product);

    void insertBatchProduct(List<Product> productList);

    void uploadImage(Product product);

    Map<String,Object> queryByProductId(String productid);

    List<Product> queryProductByPage(Pager pager);

    List<Map<String, Object>> queryByProductStates(Map<String,Object> params);

    List<Product> queryProducts(Map<String,Object> params);

    List<Map<String,Object>> vipQueryUploadProducts(Map<String,Object> params);

    List<Map<String,Object>>  salerQueryProductsByState (Map<String,Object> params);

}
