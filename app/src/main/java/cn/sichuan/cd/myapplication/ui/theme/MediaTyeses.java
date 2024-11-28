package cn.sichuan.cd.myapplication.ui.theme;

//import com.google.common.net.MediaType;

import okhttp3.MediaType;
import okhttp3.RequestBody;


//import okhttp3.RequestBody;
//
//import okhttp3.RequestBody;


public class MediaTyeses {


    private static final String bucketName = "eapc";

    private  void getJson(){

        String jsonPayload = String.format("{\"bucket\":\"%s\", \"object\":\"%s\"}", bucketName, bucketName);
        MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(jsonPayload,JSON);

        RequestBody bodys =  RequestBody. create( jsonPayload,JSON);



    }
    public static String sanitizeBucketName(String bucketName) {
        // 将名称转为小写
        bucketName = bucketName.toLowerCase();

        // 替换非法字符
        bucketName = bucketName.replace("_", "-").replaceAll("[^a-z0-9-\\.]", "");

        // 确保长度在 3 到 63 之间
        if (bucketName.length() < 3) {
            bucketName = bucketName + "-default";
        } else if (bucketName.length() > 63) {
            bucketName = bucketName.substring(0, 63);
        }

        // 确保以字母或数字开头和结尾
        if (!bucketName.matches("^[a-z0-9].*[a-z0-9]$")) {
            bucketName = "bucket-" + bucketName.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");
        }

        return bucketName;
    }
}
