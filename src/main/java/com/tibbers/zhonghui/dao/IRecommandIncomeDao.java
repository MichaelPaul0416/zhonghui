package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.RecommandIncome;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 22:32
 * @description:
 */
@Repository
public interface IRecommandIncomeDao {
    void insertRecommandIncomeOrBatch(List<RecommandIncome> list);

    List<RecommandIncome> queryRecommandIncomeByPager(Map<String,Object> param);

    void updateIncomeSerial(RecommandIncome recommandIncome);

    List<Map<String,String>> queryTotalRecommandIncome(String accountid);
}
