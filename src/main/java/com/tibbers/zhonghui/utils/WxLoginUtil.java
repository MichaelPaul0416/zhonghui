package com.tibbers.zhonghui.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/21
 * @Description:
 * @Resource:
 */
public class WxLoginUtil {

    private static Logger logger = Logger.getLogger(WxLoginUtil.class);

    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session";
//    private static final String URL_CODE = "https://api.weixin.qq.com/wxa/getwxacode?access_token=";
    private static final String URL_CODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static String dealByType(Map<String,?> map,boolean requestMethod){
        if(requestMethod){
            return (String) map.get("xml");
        }else {
            String param = "";
            Iterator<String> it = map.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next();
                param += key + "=" + map.get(key) + "&";
            }
            return param;
        }
    }

    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = dealByType(paramMap,paramMap.containsKey("xml"));

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {//使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static String accessToken(){
        BufferedReader reader = null;
        try{
            HttpClient client = HttpClientBuilder.create().build();
            String getUrl = String.format(URL_ACCESS_TOKEN,"wxb4b01bcd56f57b44","4db67ca43450e6ddd7f63cc71a87db35");
            logger.info(String.format("获取access_token的url为[%s]",getUrl));
            HttpGet httpGet = new HttpGet(getUrl);
            HttpResponse httpResponse = client.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            StringBuilder builder = new StringBuilder();
            String buffer;
            reader = new BufferedReader(new BufferedReader(new InputStreamReader(inputStream)));
            while ((buffer = reader.readLine()) != null){
                builder.append(buffer);
            }
            logger.info(String.format("获取access_token微信返回消息[%s]",builder.toString()));
            String content = builder.toString();
            JSONObject jsonObject = JSONObject.parseObject(content);
            return (String) jsonObject.get("access_token");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new APIException(e.getMessage(),e);
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }

    }
    public static JSONObject doLoginAuth(String code) {

        Map<String, String> requestParam = new HashMap<>();
        requestParam.put("appid", "wxb4b01bcd56f57b44");
        requestParam.put("secret", "4db67ca43450e6ddd7f63cc71a87db35");
        requestParam.put("js_code", code);
        requestParam.put("grant_type", "authorization_code");

        JSONObject jsonObject = JSON.parseObject(sendPost(URL, requestParam));

        return jsonObject;
    }

    public static String landAccountCodeImage(String accountid, String basePath) throws IOException {

        Map<String, Object> params = new HashMap<>();
//        int hashcode = Math.abs((accountid + personid).hashCode());
        int hashcode = Math.abs((accountid).hashCode());
        params.put("scene", hashcode);
        params.put("page", "pages/index/index");
        params.put("width", 400);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(URL_CODE + accessToken());
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSON.toJSONString(params);
        StringEntity entity;
        entity = new StringEntity(body);
        entity.setContentType("image/png");

        httpPost.setEntity(entity);
        HttpResponse response;

        response = httpClient.execute(httpPost);
        InputStream inputStream = response.getEntity().getContent();

        String path = basePath;
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        String finalPath = path + "/" + accountid + ".png";
        FileOutputStream out = new FileOutputStream(finalPath);

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        out.flush();
        out.close();

        return finalPath;
    }


    public static List<String> upload(HttpServletRequest request, String basePath) throws IOException {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<String> list = new ArrayList<>();
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    File directory = new File(basePath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    String path = basePath + "/" + file.getOriginalFilename();
                    //上传
                    file.transferTo(new File(path));

                    list.add(path);
                }
            }

            if(list.size() == 0){
                throw new APIException("没有上传附件，请重新选择");
            }
        }

        return list;
    }

    public static void main(String args[]) throws Exception {
//        landAccountCodeImage("3a17c62c50517861","9_A29m4QtvwUF6q1f09Plul4CDSgnz-P-tUBCYYB3-wGOmrEkXbLZzDQ73lWEIeOZeD0cMk1Bu7U-LkFAmoNwNeBIgkDKI4pbAh03n7YTZvOebO_hoWAChM00xLYfxNBJrHDcBVRH2SGEGVxtGRYGbAJAWDL","D://");
//        System.out.println(accessToken());
    }
}
