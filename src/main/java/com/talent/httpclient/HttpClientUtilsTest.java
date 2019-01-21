package com.talent.httpclient;

import com.alibaba.fastjson.JSON;
import com.talent.httpclient.utils.Base64Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author guobing
 * @Title: HttpClientUtilsTest
 * @ProjectName httpClient
 * @Description: TODO
 * @date 2019/1/21上午9:39
 */
public class HttpClientUtilsTest {

    public static void main(String[] args) {
        testPingualuit();
    }

    /**
     * 生成签证
     * @param param
     * @return
     */
    private static String generateSign(Map<String, Object> param, String appSecret) {
        TreeMap<String, Object> map = new TreeMap<String, Object>(param);
        StringBuilder signString = new StringBuilder();
        for(String k : map.keySet()){
            Object v = map.get(k);
            signString.append(k).append("=").append(v).append("&");
        }
        signString.delete(signString.length()-1, signString.length());

        String base64 = Base64Util.encrypt(signString.toString());

        return DigestUtils.sha1Hex(appSecret.concat(":").concat(base64));
    }

    /**
     * 测试pingualuit和开放平台的连通性
     */
    public static void testPingualuit() {
        String result = null;
        long timestamp = System.currentTimeMillis()/1000;

        String appKey = "d2f0a651df7910cf6393ab8448b33744";
        String appSecret = "c66ea9f4c603cb5fcd45a4ebf90c33e0";
        String api = "com.souche.heimdall.api.AuditService#auditResultNotify";

        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("api", api);
        reqMap.put("timestamp", timestamp);
        reqMap.put("appKey", appKey);
        String json = "{\"applicantId\":\"rxgXzsmW4U\",\"applicantName\":\"陈帅瑾\",\"auditType\":\"GH-SECSTAGEFUND\",\"msg\":\"mq消息体审批失败：已经发起审批\",\"orderCode\":\"820454818291\",\"startAuditStatus\":1}";
        reqMap.put("data", json);

        String sign = generateSign(reqMap, appSecret);
        reqMap.put("sign", sign);

        String reqJson = JSON.toJSONString(reqMap);


        String realUrl = "http://openapi.proxy.dasouche.com/v3";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(realUrl);
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new StringEntity(reqJson, "application/json", "utf-8"));
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            System.out.println("返回结果为..." + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
