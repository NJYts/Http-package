package com.njy.cn;

import com.wiseweb.browser.VirtualBrowser;
import com.wiseweb.entity.VirtualBrowserRequestBodyEntity;
import com.wiseweb.entity.VirtualBrowserResponseContentEntity;
import com.wiseweb.lang.VirtualBrowserCharsetConsts;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengbing on 2015/6/16.
 */
public class VirtualBrowserVisitPostExample {

    public static void main(String[] args) {

        VirtualBrowser browser = new VirtualBrowser(true, true);

        try {
            VirtualBrowserRequestBodyEntity requestBodyEntity = new VirtualBrowserRequestBodyEntity();
            //编辑表单（模拟表单标签input元素中的name与value值）
            Map<String, String> formMap = new HashMap<String, String>();
//            formMap.put("username", "张三");
//            formMap.put("password", "123456");
            formMap.put("keyword", "nba");
            formMap.put("time", "7");
            formMap.put("begin", "");
            formMap.put("end", "");
            formMap.put("pageNo", "1");
            formMap.put("pageSize", "10");
            requestBodyEntity.setForm(formMap);//默认编码为UTF-8
            requestBodyEntity.setForm(formMap, VirtualBrowserCharsetConsts.UTF_8);//指定表单编码

            //上传图片或文件（二进制文件或文本文件）（模拟表单标签file元素）
            File file = new File("demo.png");
            requestBodyEntity.setFile(file);//默认MIME类型为application/octet-stream，编码为null
            requestBodyEntity.setFile(file, "text/plain", VirtualBrowserCharsetConsts.UTF_8);
            //或者使用上述文件的字节流
            InputStream fileStream = new FileInputStream(file);
            requestBodyEntity.setInputStream(fileStream);//默认MIME类型为application/octet-stream，编码为null
            requestBodyEntity.setInputStream(fileStream, "text/plain", VirtualBrowserCharsetConsts.UTF_8);
            //或者使用上述文件的字节数组
            byte[] bytes = new byte[fileStream.available()];
            fileStream.read(bytes);
            requestBodyEntity.setBytes(bytes);//默认MIME类型为application/octet-stream，编码为null
            requestBodyEntity.setBytes(bytes, "text/plain", VirtualBrowserCharsetConsts.UTF_8);

            //也可以向服务器端直接发送文本
            requestBodyEntity.setContent("你好");//默认MIME类型为text/plain，编码为UTF-8
            requestBodyEntity.setContent("你好", "text/html", VirtualBrowserCharsetConsts.UTF_8);

            //添加请求头
            Map<String, String> requestHeaderMap = new HashMap<String, String>();
            requestHeaderMap.put("Connection", "Keep-Alive");
//            VirtualBrowserResponseContentEntity entity = browser.requestPost("http://www.baidu.com", requestBodyEntity);
            VirtualBrowserResponseContentEntity entity = browser.requestPost("http://192.168.30.33:8081/pom/weiboDataPush/searchReturnJson", requestHeaderMap, requestBodyEntity);
            //获取所有响应头
            HashMap<String, String>[] headerMapArray = entity.getResponseHeaders();
            System.out.println(Arrays.toString(headerMapArray));
            //获取指定的响应头
            headerMapArray = entity.getResponseHeader("Set-Cookie");
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
        }catch (Exception e){

        }
    }
}
