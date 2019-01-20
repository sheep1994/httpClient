package com.talent.httpclient.get;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: httpClient
 * @author: Mr.Guo
 * @description: 发送GET请求的实例
 * @create: 2019-01-20 12:21
 */
public class HttpGetDemo {

    /**
     * 发送GET请求，参数直接拼接在url后面
     * <code>
     *     http://test.com?a=1&b=2
     * </code>
     * @param url: url路径
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = URI.create(url);
        // 发送GET请求实例
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            // 将流转换成String输出
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
     * 发送GET请求，参数放置在一个map集合中
     * @param url
     * @param map
     * @return
     */
    public static String httpGet(String url, Map<String, String> map) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建一个List集合对象
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        // 循环map集合，将map中的数据存放在list集合中
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(pairs);
            HttpGet get = new HttpGet(builder.build());
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

    /**
     * 发送POST请求，参数放置在一个map集合中
     * @param url
     * @param map
     * @return
     */
    public static String httpPost(String url, Map<String, String> map) {
        String result = null;
        // 创建一个可关闭的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = URI.create(url);
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
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
        HttpPost post = new HttpPost(url);
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
