package com.tibbers.zhonghui.service.impl;

import com.tibbers.zhonghui.dao.IRefundCertsDao;
import com.tibbers.zhonghui.service.IRefundCertsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Paul
 * @time:2018/1/23 21:35
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RefundCertsServiceImpl implements IRefundCertsService{

    private Logger logger = Logger.getLogger(RefundCertsServiceImpl.class);

    @Autowired
    private IRefundCertsDao refundCertsDao;
}
