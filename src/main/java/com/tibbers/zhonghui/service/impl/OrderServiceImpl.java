package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.WxPayConfiguration;
import com.tibbers.zhonghui.dao.*;
import com.tibbers.zhonghui.model.*;
import com.tibbers.zhonghui.model.common.PayResult;
import com.tibbers.zhonghui.service.IAccountService;
import com.tibbers.zhonghui.service.IOrderService;
import com.tibbers.zhonghui.utils.EncryptUtil;
import com.tibbers.zhonghui.utils.StringUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Order;
import java.util.*;

/**
 * @author: Paul
 * @time:2018/1/24 19:44
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements IOrderService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IOrdersDao ordersDao;

    @Autowired
    private IOrderItemsDao orderItemsDao;

    @Autowired
    protected WxMpService wxMpService;

    @Autowired
    private WxPayConfiguration wxPayConfiguration;

    @Autowired
    private WxPayService payService;

    @Autowired
    private IShoppingCarDao shoppingCarDao;

    @Autowired
    private ICapitalSerialDao capitalSerialDao;

    @Autowired
    private IRecommandIncomeDao recommandIncomeDao;

    @Autowired
    private IProductDao productDao;

    @Autowired
    private IAccountServiceDao accountService;

    @Override
    public PayResult createOrder(String orderInfo, String itemlist, String code, String clientip, String recommandinfo) {
        logger.info(String.format("根据[%s]获取鉴权信息",code));
        try {
            WxMpOAuth2AccessToken accessToken = this.wxMpService.oauth2getAccessToken(code);
            String openid = accessToken.getOpenId();
            if(!StringUtils.isEmpty(openid)){
//            if(true){
                Map<String,Object> map = new HashMap<>();
                logger.info(String.format("即将为客户生成一笔订单"));
                Orders orders = JSONObject.parseObject(orderInfo,Orders.class);
                orders.setOrderid(StringUtil.serialId());
                orders.setCreatedatetime(StringUtil.currentDateTime());
                //订单状态 0未发货，1已发货待收货，2已收货，3退款
                orders.setOrderstate("0");
                //该订单是是否有效[0:无效，1:有效]
                orders.setIsvalid("1");
                List<OrderItems> orderItemsList = JSONObject.parseArray(itemlist,OrderItems.class);

                map.put("orders",orders);
                map.put("orderitems",orderItemsList);
//                map.put("openid","oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
                map.put("openid",openid);
                map.put("clientip",clientip);
                WxPayUnifiedOrderRequest request = assembelyOrderRequest(map);
                //统一下单
                WxPayUnifiedOrderResult result = payService.unifiedOrder(request);
                PayResult payResult = buildPayResult(result,orders);
                if(AppConstants.RETURN_CODE.equals(payResult.getReturn_code()) ){
                    if(!AppConstants.RESULT_CODE.equals(payResult.getResult_code())){
                        orders.setIsvalid("0");
                    }
                    //更新订单表
                    ordersDao.insertSingelOrder(orders);
                    logger.info(String.format("客户[%s]的订单生成成功[%s]",orders.getAccountid(),orders));
                    for(OrderItems orderItems : orderItemsList){
                        orderItems.setSerialid(StringUtil.serialId());
                        orderItems.setOrderid(orders.getOrderid());
                        orderItems.setReverse1(orders.getAddressid());
                    }
                    logger.info(String.format("即将开始生成订单[%s]的订单明细[%s]",orders.getOrderid(),orderItemsList));

                    //更新订单明细
                    orderItemsDao.insertItemsBatch(orderItemsList);
                    logger.info(String.format("订单[%s]的明细已经生成",orders.getOrderid()));

                    //如果预支付成功的话
                    if(AppConstants.RESULT_CODE.equals(payResult.getResult_code())){
                        //根据订单明细，获取哪些是从购物车结账的
                        Map<String,OrderItems> shoppingCarMap = new HashMap<>();
                        List<String> shopcarids = new ArrayList<>();
                        for(OrderItems orderItems : orderItemsList){
//                            orderItems.setSerialid(StringUtil.generateUUID());
//                            orderItems.setOrderid(orders.getOrderid());
                            if(!StringUtils.isEmpty(orderItems.getShopcarid())){
                                shopcarids.add(orderItems.getShopcarid());
                                shoppingCarMap.put(orderItems.getShopcarid(),orderItems);
                            }
                        }
                        if(shopcarids.size() > 0) {
                            logger.info(String.format("更新购物车中的明细记录"));
                            List<ShoppingCar> shoppingCarList = shoppingCarDao.queryListBySerialid(shopcarids);
                            List<ShoppingCar> batchRemoveList = new ArrayList<>();
                            for (ShoppingCar shoppingCar : shoppingCarList) {
                                OrderItems orderItems = shoppingCarMap.get(shoppingCar.getSerialid());
                                if (shoppingCar.getNumber() == orderItems.getPronumber()) {//如果订单明细的商品数量=购物车中商品数量，则从购物车中删除该明细，否则只是数量减少
                                    batchRemoveList.add(shoppingCar);
                                } else {
                                    shoppingCar.setNumber(shoppingCar.getNumber() - orderItems.getPronumber());
                                    shoppingCar.setModifydatetime(StringUtil.currentDateTime());
                                    logger.info(String.format("更新购物车明细信息[%s]", shoppingCar));
                                    shoppingCarDao.updateGoodsInShopCar(shoppingCar);
                                }
                            }
                            if (batchRemoveList.size() > 0) {
                                logger.info(String.format("将下列购物车明细[%s]删除", batchRemoveList));
                                shoppingCarDao.removeBatchFromShopCar(batchRemoveList);
                            }
                        }
                        //预支付流水生成
                        logger.info(String.format("根据微信预支付号[%s]生成一条支付流水",payResult.getPrepay_id()));
                        List<CapitalSerial> capitalSerials = new ArrayList<>();
                        CapitalSerial capitalSerial = new CapitalSerial();
                        capitalSerial.setEmcapitalserial(request.getOutTradeNo());
                        capitalSerial.setThirdpartserial(payResult.getPrepay_id());
                        capitalSerial.setOrderid(orders.getOrderid());
                        capitalSerial.setCapitaldatetime(StringUtil.currentDateTime());
                        capitalSerial.setThirdpartmsg("获取预支付ID成功");
                        capitalSerial.setCapitaldirect("0");
                        capitalSerial.setReverse1("");
                        capitalSerial.setReverse2("");
                        capitalSerials.add(capitalSerial);
                        capitalSerialDao.insertCapitalSerialOrBatch(capitalSerials);

                        //推荐人关系收益
                        if(!StringUtils.isEmpty(recommandinfo)){
                            RecommandIncome recommandIncome = JSONObject.parseObject(recommandinfo,RecommandIncome.class);
                            recommandIncome.setIncomeserialno(orders.getOrderid());//关联订单
                            double total = StringUtil.formatStr2Dobule(orders.getAmount());
                            recommandIncome.setIncome(StringUtil.caculteIncome(total,Integer.parseInt(wxPayConfiguration.getRecommandfee())));
                            recommandIncome.setIncomedatetime(StringUtil.currentDateTime());
                            recommandIncome.setAlreadydone("0");
                            recommandIncome.setDescription("");
                            recommandIncome.setReverse1("");
                            recommandIncome.setReverse2("");
                            List<RecommandIncome> list = new ArrayList<>();
                            list.add(recommandIncome);
                            recommandIncomeDao.insertRecommandIncomeOrBatch(list);
                            logger.info(String.format("新增推荐人[%s]/被推荐人[%s]收益关系[%s]成功",recommandIncome.getAccountid(),recommandIncome.getComefrom(),recommandIncome));
                        }

                        //账户余额和积分等微信进行扣款成功通知之后在做修改
                    }

                }else{
                    payResult.setAppid(wxPayConfiguration.getAppId());
                    payResult.setMchid(wxPayConfiguration.getMchId());
                    payResult.setNonce_str(request.getNonceStr());
                    payResult.setSign("");
                    payResult.setPrepay_id("");
                    payResult.setTrade_type(AppConstants.TRADE_TYPE);
                }

                payResult.setOrderid(orders.getOrderid());
                return payResult;
            }else{
                throw new APIException(String.format("微信鉴权接口根据code[%s]返回的openid为空",code));
            }
        }
        catch (WxErrorException e) {
            throw new APIException("调用微信鉴权接口异常：" + e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            if(e instanceof WxPayException){
                WxPayException exception = (WxPayException) e;
                logger.error(e.getMessage(),e);
                String errorcode = exception.getReturnCode(),errormsg = exception.getReturnMsg();
                if(org.apache.commons.lang3.StringUtils.isNoneEmpty(exception.getErrCode())){
                    errorcode = exception.getErrCode();
                    errormsg = exception.getErrCodeDes();
                }
                String info = String.format("微信返回错误代码[%s],错误描述[%s]",errorcode,errormsg);
                throw new APIException(info);
            }else{
                throw new APIException(e.getCause().getMessage());
            }
        }

    }

    @Override
    public String dealWithAsynNotifyOrder(String xmlData) {
        String response;
        try {
            WxPayOrderNotifyResult result = this.payService.parseOrderNotifyResult(xmlData);
            logger.info(String.format("微信支付异步通知结果[%s]",result));
            if(AppConstants.RETURN_CODE.equals(result.getReturnCode())){
                CapitalSerial capitalSerial = new CapitalSerial();
                String orderid = result.getOutTradeNo();//商户订单号
                capitalSerial.setOrderid(orderid);
                capitalSerial.setCapitaldatetime(StringUtil.currentDateTime());

                Orders orders = new Orders();
                orders.setOrderid(orderid);
                if(AppConstants.RESULT_CODE.equals(result.getResultCode())){
                    //更新支付流水状态
                    capitalSerial.setState("1");
                    capitalSerial.setThirdpartmsg("微信支付成功");

                    orders.setIsvalid("1");
                    //修改用户账户的积分(先根据orderid获取所有的产品，计算总积分，然后更新)
                    changeAccountScore(orderid);

                    //修改推荐收益流水信息(根据orderid)
                    updateRecommandIncome(orderid);

                }else{
                    capitalSerial.setState("0");
                    capitalSerial.setThirdpartmsg("微信支付失败");

                    orders.setIsvalid("0");
                }
                //更新流水信息
                capitalSerialDao.updateCapitalSerialInfo(capitalSerial);
                //修改订单状态
                ordersDao.updatePartOrderMsg(orders);

                logger.info(String.format("订单[%s]的流水信息更新成功[%s]",orderid,capitalSerial));

                response = AppConstants.NOTIFY_RESPONSE_SUCCESS;
            }else{
                response = String.format(AppConstants.NOTIFY_RESPONSE_FAILED_TEMPLATE,"报文为空");
            }
            return response;
        } catch (WxPayException e) {
            throw new APIException(e.getCause().getMessage());
        }
    }

    private void updateRecommandIncome(String orderid) {
        RecommandIncome recommandIncome = new RecommandIncome();
        recommandIncome.setIncomeserialno(orderid);
        recommandIncome.setIncomedatetime(StringUtil.currentDateTime());
        recommandIncome.setAlreadydone("1");
        recommandIncome.setDescription("微信支付成功，获取收益费率");
        recommandIncomeDao.updateIncomeSerial(recommandIncome);
        logger.info(String.format("更新推荐收益[%s]信息成功[%s]",recommandIncome.getIncomeserialno(),recommandIncome));
    }

    private void changeAccountScore(String orderid) {
        Map<String,Object> params = new HashMap<>();
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderid(orderid);
        params.put("orderitem",orderItems);

        List<OrderItems> orderItemss = orderItemsDao.queryItemsByPager(params);
        int totalScore = 0;
        for(OrderItems items : orderItemss){
            String productid = items.getProductid();
            Product product = productDao.queryByProductId(productid);
            totalScore += product.getValuescore();
        }
        Map<String,Object> map = new HashMap<>();
        Orders orderQuery = new Orders();
        orderQuery.setOrderid(orderid);
        map.put("orders",orderQuery);
        List<Map<String,String>> targetOrders = ordersDao.queryOrdersByPager(map);
        String accountid = targetOrders.get(0).get("accountid");

        Account account = new Account();
        account.setAccountid(accountid);
        account.setScore(String.valueOf(totalScore));
        accountService.updateAccountInfo(account);
        logger.info(String.format("账户[%s]积分更新成功[%s]",accountid,account));
    }

    private PayResult buildPayResult(WxPayUnifiedOrderResult result,Orders orders){
        PayResult payResult = new PayResult();
        logger.info(String.format("与微信服务端通讯成功，接受返回信息[%s]",result));
        if(!StringUtils.isEmpty(result.getReturnCode()) && AppConstants.RETURN_CODE.equals(result.getReturnCode())){
            payResult.setReturn_code(result.getReturnCode());
            payResult.setResult_code(result.getResultCode());
            payResult.setAppid(result.getAppid());
            payResult.setMchid(result.getMchId());
            payResult.setNonce_str(result.getNonceStr());
            payResult.setSign(result.getSign());
            payResult.setErr_code(result.getErrCode());
            payResult.setErr_code_msg(result.getErrCodeDes());
            if(!StringUtils.isEmpty(result.getResultCode()) && AppConstants.RESULT_CODE.equals(result.getResultCode())) {
                logger.info(String.format("微信处理接受预支付订单[%s]成功", orders.getOrderid()));
                payResult.setTrade_type(result.getTradeType());
                payResult.setPrepay_id(result.getPrepayId());
            }else{
                logger.info(String.format("微信处理接受与支付订单[%s]异常，错误代码[%s]，错误原因[%s]",orders.getOrderid(),payResult.getErr_code(),payResult.getErr_code_msg()));
            }
        }else{
            payResult.setReturn_code(result.getReturnCode());
            payResult.setErr_code_msg(result.getReturnMsg());
        }

        return payResult;
    }

    private WxPayUnifiedOrderRequest assembelyOrderRequest(Map<String,Object> params){
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder().build();
        request.setAppid(wxPayConfiguration.getAppId());
        request.setMchId(wxPayConfiguration.getMchId());
        request.setAttach(AppConstants.ATTACH);//附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
        request.setBody(wxPayConfiguration.getAppname());//应用市场上APPNAME-商品描述
        request.setDetail(StringUtil.formatXMLDate(params.get("orderitems")));
        request.setNonceStr(StringUtil.randomStr(32));
        request.setNotifyURL(wxPayConfiguration.getAsynNotifyUrl());
        request.setOpenid((String) params.get("openid"));
        request.setSpbillCreateIp((String) params.get("clientip"));//客户端ip，小程序
        Orders orders = (Orders) params.get("orders");
        request.setOutTradeNo(orders.getOrderid());//商户订单号
        request.setTotalFee((int) (StringUtil.formatStr2Dobule(orders.getAmount()) * 100));//订单总金额
        request.setTradeType(AppConstants.TRADE_TYPE);
        request.setSignType(AppConstants.SIGN_TYPE_MD5);

        Map<String,String> urlparams = new TreeMap<>();
        urlparams.put("appid",request.getAppid());
        urlparams.put("attach",request.getAttach());
        urlparams.put("body",request.getBody());
        urlparams.put("mch_id",request.getMchId());
        urlparams.put("detail",request.getDetail());
        urlparams.put("nonce_str",request.getNonceStr());
        urlparams.put("notify_url",request.getNotifyURL());
        urlparams.put("openid",request.getOpenid());
        urlparams.put("out_trade_no",request.getOutTradeNo());
        urlparams.put("spbill_create_ip",request.getSpbillCreateIp());
        urlparams.put("total_fee",String.valueOf(request.getTotalFee()));
        urlparams.put("trade_type",request.getTradeType());

        String contactParams = EncryptUtil.contactParams(urlparams);
        contactParams += "key=" + wxPayConfiguration.getMchKey();
        logger.info(String.format("param[%s]",contactParams));
        request.setSign(EncryptUtil.encodeMD5String(contactParams));

        return  request;
    }
}
