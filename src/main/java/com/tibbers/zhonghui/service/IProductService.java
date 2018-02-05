package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.ProductBelong;
import com.tibbers.zhonghui.model.common.Pager;

import java.io.InputStream;
import java.util.List;

/**
 * @author: Paul
 * @time:2018/1/20 21:33
 * @description:
 */
public interface IProductService {
    void insertSingleProduct(Product product,String accountid);

    void insertBatchProduct(List<Product> productList);

    void uploadImage(InputStream inputStream,Product product);

    Product queryByProductId(String productId);

    List<Product>  queryProductByPage(Pager pager);

    List<Product> queryByProductStates(String[] states,Pager pager);

    void updateProductState(String productid,String targteState);

}
