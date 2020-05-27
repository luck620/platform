package com.plantform.controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class UploadController {
    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private static String string = "abcdefghijklmnopqrstuvwxyz";

    private static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }

    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone2());
    String ACCESS_KEY = "PPRuGKppOWCOUP9f5oOcx-ZVOxubOy2dEAxyuZUB";
    String SECRET_KEY = "bEfG61sJqpE-mbVBip9Gycp0hdI4PtdhLQztsD-h";
    // 要上传的空间
    String bucketname = "images620";
    // 上传到七牛后保存的文件名
    String key = getRandomString(10) + ".doc";//七牛云服务器里用来对应唯一上传的文件
    // String audioUrl = "在七牛云中找到";
    // String imagesUrl = "在七牛云中找到";
    // 上传文件的路径
//    String FilePath = "D:\\download\\2020年研究生入学考试991考试大纲.doc"; // 本地要上传文件路径

    // 密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    // 创建上传对象

    UploadManager uploadManager = new UploadManager(cfg,null);

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了 //
    @ResponseBody
    @GetMapping("/getUpToken")
    public String getupToken() {
//        StringMap putPolicy = new StringMap();
//        putPolicy.put("returnBody","{\"fileUrl\": \""+fileName+"$(key)\"}");
//        long expireSecods = 3600;//过期时间en = "+upToken);
        String upToken = auth.uploadToken(bucketname);
        System.out.println("upToken="+upToken);
        return upToken;
    }


    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    // 普通上传
    @ResponseBody
    @PostMapping("/upload")
    public boolean upload(@RequestBody String filePath) throws IOException {
        try {
            // 调用put方法上传
            Response res = uploadManager.put(filePath, key, getUpToken());
            // 打印返回的信息

            System.out.println(res.isOK());

            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
        }
        return true;
    }
}
