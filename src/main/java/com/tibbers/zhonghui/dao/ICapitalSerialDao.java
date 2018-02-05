package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.CapitalSerial;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 22:29
 * @description:
 */
@Repository
public interface ICapitalSerialDao {
    void insertCapitalSerialOrBatch(List<CapitalSerial> list);

    List<CapitalSerial> queryCapitalSerialByPager(Map<String,Object> params);

    void updateCapitalSerialInfo(CapitalSerial capitalSerial);
}
