package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.common.Pager;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 23:14
 * @description:
 */
public interface IAuditingProsService {
    void applyAudit4Product(String productid);

    void auditProductApply(String auditor,String auditstate,String serialid);

    List<Map<String,String>> querySerialByAuditState(String auditstate, Pager pager);
}
