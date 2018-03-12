package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Recommand;
import com.tibbers.zhonghui.model.common.Pager;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 0:41
 * @description:
 */
public interface IRecommandService {

    void insertRecommand(String accountid,String recommander);

    List<Recommand> queryRecommandByPager(Pager pager);

    List<Map<String,String>> queryMyRecommandAccounts(String accountid,Pager pager);
}
