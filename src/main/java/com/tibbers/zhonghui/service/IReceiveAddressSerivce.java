package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.ReceiveAddress;
import com.tibbers.zhonghui.model.common.Pager;

import java.util.List;

/**
 * @author: Paul
 * @time:2018/1/22 21:55
 * @description:
 */
public interface IReceiveAddressSerivce {
    void addOneReceiveAddress(String addressInfo,boolean isDefault);

    void updateReceiveAddress(String addressInfo);

    void deleteReceiveAddress(String serialid,String accountid);

    List<ReceiveAddress> queryAddressByPager(String accountid , Pager pager);
}
