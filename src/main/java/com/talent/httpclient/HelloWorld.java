package com.talent.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @program: httpClient
 * @author: Mr.Guo
 * @description: HttpClient的HelloWorld
 * @create: 2019-01-19 18:27
 */
public class HelloWorld {

    public static void main(String[] args) throws Exception {

        // 创建HttpClient实例，返回一个可关闭的httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HttpGet实例，发送GET请求
        HttpGet httpGet = new HttpGet("http://www.tuicool.com");
        // 执行http协议的GET请求，并返回一个可关闭的HttpResponse响应
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        // 获取返回实体
        HttpEntity entity = httpResponse.getEntity();
        // 获取网页内容
        System.out.println(EntityUtils.toString(entity, "utf-8"));

        httpResponse.close();
        httpClient.close();
    }
}
