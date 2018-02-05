package com.tibbers.zhonghui.config;

/**
 * @author: Paul
 * @time:2018/1/20 12:01
 * @description:
 */
public class AppConstants {
    public static final String PRODUCT = "product";//对应tsysparameter表中caption="product"

    public static final String  REFUND = "refund";

    public static final String CUSTTYPE = "custtype";

    public static final String MAX_CATEGORYID = "maxCategory";//字符串中maxCategory最大id

    public static final String MAX_CATEGORY_ITEMID = "maxCategoryItemId";//一个大类中最大的子类id

    public static final String RESPONSE_SUCCEED_CODE = "0000";

    public static final String RESPONSE_FAILED_CODE = "9999";

    public static final String REQUEST_STATUS_MESSAGE = "request successfully";

    public static final String SERVICE_SUCCEED_MESSAGE = "service successfully";

    public static final String DEFAULT_BLANK = " ";

    public static final String WXAPP_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String TRADE_TYPE = "APP";

    public static final String ATTACH = "众惠商城购物";

    public static final String RETURN_CODE = "SUCCESS";

    public static final String RESULT_CODE = "SUCCESS";

    public static final String SIGN_TYPE_MD5 = "MD5";

    public static final String SIGN_TYPE_SHA = "HMAC-SHA256";

    public static final String NOTIFY_RESPONSE_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    public static final String NOTIFY_RESPONSE_FAILED_TEMPLATE = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>";

}
