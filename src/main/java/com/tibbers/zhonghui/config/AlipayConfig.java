package com.tibbers.zhonghui.config;

/**
 * @author Tibbers
 * @create 2017-12-16 1:16
 */

public class AlipayConfig {
    /*//生产环境
    // 商户appid
    public static String APPID = "";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "";
     // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "";//生产
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";//生产
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "";
    // RSA2
    public static String SIGNTYPE = "RSA2";*/

    //测试环境
    public static String APPID = "2016082700322740";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDn2IuET8b/7rlcSiBdxDfRrdC9bHWhQHA4N8fRNiwXiq9ByKM/zMyCuIaLYGQecUIZ2TlThoGavRa2WO6R0wdvr14oiUqu3AykssRZsfvIthtqMxQyzZ2wr9+B0QdjIPdl713Qj55BdZL0dnQlXWTfO+f9hcYc84Ana9uhTvvTQCwz0Dx/CMnbPoFlXrfJjatyX6Z2+kBrH30n6twXK2OafxUSX9enFHEA3tqmlDBDIXWR6gmZYZrZnp9wKFueYNePBphZnWOP239QQuTKWGccO+10DOf2H732mQNfOWEIw4AAfyjMGX4IVwAHFGIpNNad6VosEvE0TwnBg3pIBKA3AgMBAAECggEALZqHnuf+UbtIVKH0Cd2dyS2yYIAkByrgp/443KsWMy9BV+Q6UENdet0HAgVczCwk/2aeAxLzDVfiTs3hZ2emD87j639N5GU+S2sqeiYrS4iejd4sO+379ZQyl5thI5uJblCOGIcXbvLMjC98mxjqzYpAELsug+VVsOtjUTBzElMuK/ARN8ViPhzl0lew47bv7mxCsSOcGMiC+CEFtpJF4UOlBA4MWEYAL+h66F789krGFbBi2Jp7Y+dtm1QwYXvXttaYNSgBbsqtcdmfQMz0G+tHOi/nf0FTcrkpv4M6inPajFv7/xtApMg2kfbpXLprTee2pQjXTn0FykXfpu31AQKBgQD/Wq8Y4v8Ugx6MHbCwvH5j/ow1YddoHoZBxTq8kiF2Nchw9OI7rr+H2uaDEfqZvPW5HWV3OM2P47oXCZngSjeYkVCFTAz6V4Kur8HcB3oxlz9r0Ju0c6YwGHmMsiW9bk9tT+JotcHWzngruFRkYpRqV+IDOBf/FsNLzEwJmd4ytwKBgQDobqRInBXmw1Hng1hpXSxwWr2OFp6ZskiSBEyYB/LIZ9c5TPOzoqdlJglJC/kuhJZe2fKE+QclBwjTGC2JakeG3c/UlQRQstBmaVrS5UyQ+MuXWtDeDdPgweQyDOpzaY2chpSZ5Z+vpT/4vrlpW7YMba+2LlsEFMMA6nrIa6F+gQKBgQCUwhyNT/gv1+7cy5MacsTitfCyTIxzgIzQFfkQz8Skm9JoU5DUGgpEN7bgFoI6O8WekMyCdtnyAEVxlEtLSZE31TfmX5aT5JUh9QvnhYefCjCNO5rpthHTpP5yg3nO8EtfMBGC1JfW7rM9LQYk8PTrOsVVAMGh/n3JU3vcgAHc4wKBgHNm88RlUu53TcQvz4bIzxadxv20N1zCSwvMfKcDU0SzYgW+MkWfXZN39iZBfp/dDrfbaHEjH6v/uR7uIzXWAdtxaA35SuIyVgxzBLgvRvu3u4XTfl6x6N/wBLfg98sDvl7X5xIeE1HnL0L2lyI7ecDj+NjwXRpCoKtkwD+gSNeBAoGBAOFX4+K8yxSUfAhHQyL44z4QJyzCXp3b5FJgFDnkrPvLxl4m7rgxnowmKdDokQ2jVtLEpmVYJFCkjez/hT5SNMhMyWcJk6XdS8mLXBDZHPKGZznPtTLuUKX2MW5NOi5H71EXy0OjJjPzZ3+DKDQqpQEVnwOd6+U0j+hmh+h1azZw";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://1p7w341849.iok.la/alipay/notify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://1p7w341849.iok.la/resources/views/deposit_alipay_result.html";//测试
    // 请求网关地址
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtTYQTkmhm+pgxiHkLvusHcjF3fhczLOBFFTV9wiKna3Tw/M6ArN4w6/j60zqnmHIcb4CSJV6u6mt/t7uC7FtLart/pYsasb3gPDd1S8thg32PbQYEFJ0kM52Q0zhshYl2/TOkb6G3yzfdinMeNoVB8BCfuIT+/OcJlZX5VjIplxv5woYODFlFNxnPxLbUMdWBTX2tkJBiw/XDd2qCriQ8/mLGTlGH3+vCVanOXSBe9X/6l4a0+BCg05tWqsulNe3vnjPRqgO78eUWm4LPBx2tRc++OFnmnURPtGYUYS+S22y1mMwkXZXJFws4pwWFq82XjL3ABHRIz9wF31ae0cZCQIDAQAB";
    // RSA2
    public static String SIGNTYPE = "RSA2";

}
