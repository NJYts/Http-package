package com.njy.cn;

import com.wiseweb.browser.VirtualBrowser;
import com.wiseweb.entity.VirtualBrowserResponseContentEntity;
import com.wiseweb.lang.VirtualBrowserCharsetConsts;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengbing on 2015/6/16.
 */
public class VirtualBrowserVisitByProxyExample {

    public static void main(String[] args) {

//        VirtualBrowser browser = new VirtualBrowser("218.200.66.199", 80, true, false);
//        VirtualBrowser browser = new VirtualBrowser("139.196.4.29", 80, true, false);
        VirtualBrowser browser = new VirtualBrowser("218.200.66.200", 80, true, false);
        try {
            //添加请求头
            Map<String, String> requestHeaderMap = new HashMap<String, String>();
            requestHeaderMap.put("Connection", "Keep-Alive");
//            VirtualBrowserResponseContentEntity entity = browser.requestGet("http://city.ip138.com/ip2city.asp");
            VirtualBrowserResponseContentEntity entity = browser.requestGet("http://www.baidu.com", requestHeaderMap);

            //获取所有响应头
            HashMap<String, String>[] headerMapArray = entity.getResponseHeaders();
            System.out.println(Arrays.toString(headerMapArray));
            //获取指定的响应头
            headerMapArray = entity.getResponseHeader("location");
            System.out.println(Arrays.toString(headerMapArray));
            //获取状态码
            int statusCode = entity.getHttpStatus();
            System.out.println(statusCode);

            //如果浏览器响应的内容需要重复读取，则在读取结果之前先调用，以下三种方式一起使用时不会出现任何异常。
            entity = entity.getBufferedEntity();

            //如果浏览器响应的内容不需要重复读取
            //在使用以下三种方式获取浏览器响应的内容时，只能选用其中的一种，且只能调用一次，避免重复读取；
            //如果重复读取则会出现（java.io.IOException: Attempted read from closed stream.）的错误。
            //1、以字符串形式输出响应内容
            System.out.println(entity.getContent(VirtualBrowserCharsetConsts.UTF_8));
            //2、获取响应内容的字节流形式
            InputStream inputStream = entity.getContentStream();
            //3、将响应内容写到输出流中
            OutputStream outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);

            entity.release();
            browser.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
