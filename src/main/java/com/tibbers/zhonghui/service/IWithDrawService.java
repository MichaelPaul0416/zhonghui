package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.WithDraw;
import com.tibbers.zhonghui.model.common.Pager;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/20
 * @Description:
 * @Resource:
 */
public interface IWithDrawService {
    List<WithDraw> applyWithDrawOrBatch(List<WithDraw> withDraws);//申请提现[批量]

    void auditWithDraw(WithDraw withDraw);//审核提现

    List<WithDraw> queryWithDraws(WithDraw withDraw, Pager pager);

    List<Map<String,String>> queryTotalWithdraw(String accountid);
}
