package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.AuditingPros;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 23:14
 * @description:
 */
@Repository
public interface IAuditingProsDao {
    void applyAudit4Product(AuditingPros auditingPros);

    void auditProductApply(AuditingPros auditingPros);

    List<Map<String,String>> querySerialByAuditState(Map<String,Object> map);

    void applyAudit4Products(List<AuditingPros> list);
}
