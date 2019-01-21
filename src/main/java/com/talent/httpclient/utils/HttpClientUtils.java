package com.talent.httpclient.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guobing
 * @Title: HttpClientUtils
 * @ProjectName httpClient
 * @Description: HttpClient工具类
 * @date 2019/1/21上午9:25
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    /**
     * 发送GET请求，请求地址为http://www.baidu.com?name=zhangsan
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = URI.create(url);
        // 发送GET请求
        HttpGet get = new HttpGet(uri);
        // 设置请求头信息
        get.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel …) Gecko/20100101 Firefox/64.0");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            logger.info("响应状态 status : [{}]", status);
            HttpEntity entity = response.getEntity();
            // 获取页面内容
            result = EntityUtils.toString(entity, "utf-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return null;
    }

    /**
     * 发送GET请求，参数包装在map集合中
     * @param url
     * @param map
     * @return
     */
    public static String httpGet(String url, Map<String, String> map) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 将map数据存储在list集合中
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(pairs);
            HttpGet get = new HttpGet(builder.build());
            get.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel …) Gecko/20100101 Firefox/64.0");
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            return result;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return null;
    }

    public static String httpPost(String url, Map<String, Object> map) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
            response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            logger.info("响应状态 status : [{}]", status);
            HttpEntity entity = response.getEntity();
            logger.info("得到响应流信息, inputStream : [{}]", entity.getContent());
            result = EntityUtils.toString(entity, "utf-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return null;
    }

    /**
     * 发送POST请求，参数为json字符串
     * @param url
     * @param jsonString
     * @return
     */
    public static String httpJson(String url, String jsonString) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = URI.create(url);
        HttpPost post = new HttpPost(uri);
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new ByteArrayEntity(jsonString.getBytes("utf-8")));
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(httpClient, response);
        }
        return null;
    }

    private static void close(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            httpClient.close();
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
