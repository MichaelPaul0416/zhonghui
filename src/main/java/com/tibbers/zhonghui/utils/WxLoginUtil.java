package com.tibbers.zhonghui.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/21
 * @Description:
 * @Resource:
 */
public class WxLoginUtil {

    private static Logger logger = Logger.getLogger(WxLoginUtil.class);

    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String URL_CODE = "https://api.weixin.qq.com/wxa/getwxacode?access_token=8_nAuVD5NPuZUYjprrBUITCDGm9lPIUD9Scg_hvhOS1KXb19CVZZGF6P1KuDfk12zVH9KF2WbjdpRZQ2-BZuuGEV6eekYugI3V-oZ7x0RiN0JSGrjJEia7r9bqq7xpna4IB2c9X5Uo4FuC2iStUFVaADABMC";

    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while(it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

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
        }
        finally{//使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static JSONObject doLoginAuth(String code){

        Map<String,String> requestParam = new HashMap<>();
        requestParam.put("appid","wxb4b01bcd56f57b44");
        requestParam.put("secret","4db67ca43450e6ddd7f63cc71a87db35");
        requestParam.put("js_code",code);
        requestParam.put("grant_type","authorization_code");

        JSONObject jsonObject = JSON.parseObject(sendPost(URL,requestParam));

        return jsonObject;
    }

    public static String landAccountCodeImage(int hashcode) throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("scene", hashcode);
        params.put("path", "/pages/index/index");
        params.put("width", 430);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSON.toJSONString(params);
        StringEntity entity;
        entity = new StringEntity(body);
        entity.setContentType("image/png");

        httpPost.setEntity(entity);
        HttpResponse response;

        response = httpClient.execute(httpPost);
        InputStream inputStream = response.getEntity().getContent();

        File targetFile = new File("C:\\");
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        String path =
        FileOutputStream out = new FileOutputStream("C:\\Users\\Paul\\Desktop\\aaa.jpg");

        byte[] buffer = new byte[8192];
        int bytesRead ;
        while((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        out.flush();
        out.close();
    }

    public static void main(String args[]) throws Exception{


    }
}
