package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.ProductBelong;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Paul
 * @time:2018/1/21 0:09
 * @description:
 */
@Repository
public interface IProductBelongDao {
    void insertSingleRelation(ProductBelong productBelong);

    boolean updateProductState(ProductBelong productBelong);

    void updateProductBelongRemaindernum(ProductBelong productBelong);

    void insertBatchRelation(List<ProductBelong> belongs);
}
