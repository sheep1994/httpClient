package com.talent.httpclient;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @program: httpClient
 * @author: Mr.Guo
 * @description: 模拟浏览器
 * @create: 2019-01-19 19:22
 */
public class SimulateBrowser {

    public static void main(String[] args) {

        // 创建HttpClient实例，返回一个可关闭的HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String str = "http://www.tuicool.com";
        URI uri = URI.create(str);
        // 创建HttpGet实例，发送GET请求
        HttpGet httpGet = new HttpGet(uri);
        // 设置超时间
        RequestConfig build = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        httpGet.setConfig(build);
        // 设置请求头的User-Agent
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel …) Gecko/20100101 Firefox/64.0");
        CloseableHttpResponse response = null;
        try {
            // 通过httpClient对象执行GET请求，返回响应对象，是一个可关闭的response对象
            response = httpClient.execute(httpGet);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            System.out.println("响应状态为: " + status);
            // 获取网页内容
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            // 创建字节数组输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (inputStream != null) {
                byte[] bytes = new byte[1024];
                int len = inputStream.read(bytes, 0, 1024);
                while (len > 0) {
                    outputStream.write(bytes, 0, len);
                    len = inputStream.read(bytes, 0, 1024);
                }
                System.out.println("输出流为" + new String(outputStream.toByteArray(), "utf-8"));
            }
            System.out.println("inputStream为：" + inputStream);
            System.out.println(entity.getContentType().getName() + "为： " + entity.getContentType().getValue()) ;
            System.out.println(EntityUtils.toString(entity, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
