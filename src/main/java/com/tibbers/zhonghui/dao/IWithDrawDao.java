package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.WithDraw;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/20
 * @Description:
 * @Resource:
 */
@Repository
public interface IWithDrawDao {

    void applyWithDrawOrBatch(List<WithDraw> withDraws);//申请提现[批量]

    void auditWithDraw(WithDraw withDraw);//审核提现

    List<WithDraw> queryWithDraws(Map<String,Object> param);

    List<Map<String,String>> queryTotalWithdraw(String accountid);
}
