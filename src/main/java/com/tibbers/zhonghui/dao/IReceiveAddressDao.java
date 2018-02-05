package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.ReceiveAddress;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/22 21:54
 * @description:
 */
@Repository
public interface IReceiveAddressDao {
    void addOneReceiveAddress(ReceiveAddress receiveAddress);

    List<ReceiveAddress> checkAndFindDefaultAddress(String accountid);

    void updateReceiveAddress(ReceiveAddress receiveAddress);

    void deleteReceiveAddress(String serialid);

    List<ReceiveAddress> queryAddressByPager(Map<String,Object> map);

}
