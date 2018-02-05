package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Recommand;
import com.tibbers.zhonghui.model.common.Pager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 0:39
 * @description:
 */
@Repository
public interface IRecommandDao {
    void insertRecommand(Recommand recommand);

    List<Recommand> queryRecommandByPager(Map<String,Object> param);
}
