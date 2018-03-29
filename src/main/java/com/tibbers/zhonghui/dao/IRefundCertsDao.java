package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.RefundCerts;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 21:34
 * @description:
 */
@Repository
public interface IRefundCertsDao {
    void insertCertOrBatch(List<RefundCerts> refundCertss);

    List<RefundCerts> queryCertsByPager(Map<String,Object> params);
}
