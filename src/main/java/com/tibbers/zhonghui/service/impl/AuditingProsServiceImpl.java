package com.tibbers.zhonghui.service.impl;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.dao.IAuditingProsDao;
import com.tibbers.zhonghui.dao.IProductBelongDao;
import com.tibbers.zhonghui.model.AuditingPros;
import com.tibbers.zhonghui.model.ProductBelong;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IAuditingProsService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 23:15
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditingProsServiceImpl implements IAuditingProsService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IAuditingProsDao auditingProsDao;

    @Autowired
    private IProductBelongDao productBelongDao;


    @Override
    public void applyAudit4Product(String productid) {
        AuditingPros auditingPros = new AuditingPros();
        try{
            logger.info(String.format("添加产品[%s]的审核申请",productid));
            auditingPros.setProductid(productid);
            auditingPros.setSerialid(StringUtil.generateUUID());
            auditingPros.setApplydatetime(StringUtil.currentDateTime());
            auditingPros.setAuditstate("0");
            auditingPros.setRejectreason("");
            auditingProsDao.applyAudit4Product(auditingPros);
            logger.info(String.format("产品[%s]的审核记录已经添加",productid));
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void auditProductApply(String auditor, String auditstate, String serialid, String rejectreason) {
        AuditingPros auditingPros = new AuditingPros();
        try{
            Map<String,Object> param = new HashMap<>();
            param.put("auditstate","0");
            param.put("serialid",serialid);
            List<Map<String,String>> result = auditingProsDao.querySerialByAuditState(param);
            if(result != null && result.size() == 1) {
                String productid = result.get(0).get("productid");
                auditingPros.setSerialid(serialid);
                auditingPros.setAuditstate(auditstate);
                auditingPros.setAuditor(auditor);
                auditingPros.setBegindatetime(StringUtil.currentDateTime());
                auditingPros.setEnddatetime(StringUtil.currentDateTime());
                auditingPros.setRejectreason(rejectreason);
                logger.info(String.format("管理员[%s]即将审核[%s]申请", auditor, serialid));
                auditingProsDao.auditProductApply(auditingPros);
                logger.info(String.format("审核申请[%s]状态修改成功,修改为[%s]", serialid, auditstate));
                if ("1".equals(auditstate)) {
                    auditingPros.setProductid(productid);
                    logger.info(String.format("开始更新产品[%s]的销售状态为[%s]", auditingPros.getProductid(), auditingPros));
                    ProductBelong productBelong = new ProductBelong();
                    productBelong.setProductid(auditingPros.getProductid());
                    productBelong.setSalestate("1");
                    productBelongDao.updateProductState(productBelong);
                }
            }
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public List<Map<String,String>> querySerialByAuditState(String auditstate, Pager pager) {
        Map<String,Object> map = new HashMap<>();
        try{
            map.put("auditstate",auditstate);
            map.put("pager",pager);
            List<Map<String,String>> list = auditingProsDao.querySerialByAuditState(map);
            logger.info(String.format("查询结果[%s]",list));
            return list;
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }
}
