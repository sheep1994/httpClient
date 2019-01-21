package com.talent.httpclient.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/**
 * @author guobing
 * @Title: Base64Util
 * @ProjectName httpClient
 * @Description: TODO
 * @date 2019/1/21下午2:17
 */
public class Base64Util {

    public static String encrypt(String s){
        if(s == null){
            return "";
        }
        s = new String(Base64.encodeBase64(s.getBytes(Charset.forName("UTF-8"))));
        return s;
    }

    public static String decrypt(String s){
        if(s == null){
            return "";
        }
        s = new String(Base64.decodeBase64(s.getBytes(Charset.forName("UTF-8"))));
        return s;
    }

}
