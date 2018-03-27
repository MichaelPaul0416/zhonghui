package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Precentage;
import org.springframework.stereotype.Repository;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/26
 * @Description:
 * @Resource:
 */
@Repository
public interface IPrecentageDao {

    void insertSinglePrecentage(Precentage precentage);
}
