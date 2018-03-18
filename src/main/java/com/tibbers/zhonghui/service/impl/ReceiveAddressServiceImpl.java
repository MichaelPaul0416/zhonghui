package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.dao.IReceiveAddressDao;
import com.tibbers.zhonghui.model.ReceiveAddress;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IReceiveAddressSerivce;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/22 21:56
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiveAddressServiceImpl implements IReceiveAddressSerivce {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IReceiveAddressDao receiveAddressDao;


    @Override
    public String addOneReceiveAddress(String addressInfo, boolean isDefault) {
        try{
            ReceiveAddress receiveAddress = JSONObject.parseObject(addressInfo,ReceiveAddress.class);
            receiveAddress.setSerialid(StringUtil.generateUUID());
            receiveAddress.setDeleteflag("0");
            List<ReceiveAddress> list = receiveAddressDao.checkAndFindDefaultAddress(receiveAddress.getAccountid());
            if(list != null){
                if(list.size() == 0){//设为默认地址
                    receiveAddress.setDefaultaddress("1");
                    logger.info(String.format("账户[%s]之前暂未添加收货地址，所以该地址设为默认地址",receiveAddress.getAccountid()));
                }else if(list.size() == 1){
                    if(isDefault){
                        receiveAddress.setDefaultaddress("1");
//                        更改之前的默认地址为0
                        ReceiveAddress update = list.get(0);
                        update.setDefaultaddress("0");
                        if(StringUtils.isEmpty(update.getReverse())){
                            update.setReverse("");
                        }
                        receiveAddressDao.updateReceiveAddress(update);
                    }else{
                        receiveAddress.setDefaultaddress("0");
                    }
                }else{
                    throw new APIException("一个账户的默认地址只能有一个，请联系系统管理员，进行数据核查");
                }
            }else{
                throw new APIException("系统异常，查询收货地址列表为null");
            }
            receiveAddress.setTotaladdress(contactAddress(receiveAddress));
            logger.info(String.format("账户[%s]新增收货地址[%s]",receiveAddress.getAccountid(),receiveAddress));
            receiveAddressDao.addOneReceiveAddress(receiveAddress);
            logger.info(String.format("账户[%s]的收货地址新增成功",receiveAddress.getAccountid()));
            return receiveAddress.getSerialid();
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void updateReceiveAddress(String addressInfo) {
        try{
            ReceiveAddress receiveAddress = JSONObject.parseObject(addressInfo,ReceiveAddress.class);
            if(StringUtils.isEmpty(receiveAddress.getReverse())){
                receiveAddress.setReverse("");
            }
            logger.info(String.format("更新收货地址[%s]的信息为[%s]",receiveAddress.getSerialid(),receiveAddress));
            receiveAddress.setTotaladdress(contactAddress(receiveAddress));
            receiveAddressDao.updateReceiveAddress(receiveAddress);
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void deleteReceiveAddress(String serialid, String accountid) {
        logger.warn(String.format("即将删除账户[%s]的收货地址[%s]",accountid,serialid));
        try{
            //先确认要删除的是否是默认收货地址，如果不是，直接删除，否则抛异常
            List<ReceiveAddress> currentList = receiveAddressDao.checkAndFindDefaultAddress(accountid);
            if(currentList == null || currentList.size() == 0){
                logger.error(String.format("当前账户[%s]没有默认的收货地址",accountid));
                throw new APIException("当前账户没有默认收货地址");
            }else if(currentList.size() == 1){
                ReceiveAddress receiveAddress = currentList.get(0);
                if(receiveAddress.getSerialid().equals(serialid)){
                    throw new APIException("不能删除默认收货地址，请先勾选其他地址为默认收货地址，然后删除此地址");
                }else {
                    receiveAddressDao.deleteReceiveAddress(serialid);
                }
            }else{
                throw new APIException("一个账户的默认地址只能有一个，请联系系统管理员，进行数据核查");
            }
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public List<ReceiveAddress> queryAddressByPager(String accountid, Pager pager) {
        if(pager != null){
            logger.info(String.format("分页查询[%s]的有效收货地址",accountid));
        }else {
            logger.info(String.format("查询[%s]的全部有效收货地址",accountid));
        }
        try{
            Map<String,Object> params = new HashMap<>();
            ReceiveAddress receiveAddress = new ReceiveAddress();
            receiveAddress.setAccountid(accountid);
            params.put("receiveaddress",receiveAddress);
            params.put("pager",pager);
            List<ReceiveAddress> list = receiveAddressDao.queryAddressByPager(params);
            logger.info(String.format("查询返回账户[%s]有效收货地址[%s]",accountid,list));
            return list;
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    private String contactAddress(ReceiveAddress receiveAddress){
        StringBuilder builder = new StringBuilder();
        builder.append(receiveAddress.getProvince()).append(AppConstants.DEFAULT_BLANK);
        builder.append(receiveAddress.getCity()).append(AppConstants.DEFAULT_BLANK);
        builder.append(receiveAddress.getArea()).append(AppConstants.DEFAULT_BLANK);
        builder.append(receiveAddress.getStreet()).append(AppConstants.DEFAULT_BLANK);
        builder.append(receiveAddress.getAddressmore());
        return  builder.toString();

    }
}
