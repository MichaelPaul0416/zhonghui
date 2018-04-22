package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Precentage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/26
 * @Description:
 * @Resource:
 */
@Repository
public interface IPrecentageDao {

    void insertBatchPrecentage(List<Precentage> list);
}
