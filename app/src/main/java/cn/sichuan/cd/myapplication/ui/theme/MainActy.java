package cn.sichuan.cd.myapplication.ui.theme;

import android.Manifest;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import cn.sichuan.cd.myapplication.R;
import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import okhttp3.OkHttpClient;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
//import okhttp3.Request;
//import okhttp3.RequestBody;

//import MediaType;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActy extends AppCompatActivity {


    private static final String baseUrl = "http://1.116.41.216:9999";  //http://101.204.24.30:8000   http://1.116.41.216:9999
    private static final String accessKey = "Og30uh8IWxHRxto04rio";
    private static final String secretKey = "L5nsJMxqBXDRjqpXOJWy79iXYCb6g8S9Z4CB02LK";
    private static final String bucketName = "android";//eapc  android

//     static String  bucketName= new MediaTyeses().sanitizeBucketName("jb/"+"dian/"+System.currentTimeMillis());
//      static  String bucketName="folder/subfolder/";


    ImageView mbuttons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.mlayout);
        Button mbutton=findViewById(R.id.button);


        mbuttons=findViewById(R.id.imageView);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setDialog();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }



    private  void setDialog() throws IOException {
//        File dir = new File(getFilesDir(), "images"); // 存储在应用的内部存储目录
//        File dir = new File(Environment.getExternalStorageDirectory(), "images");
//        File dir = new File(Environment.getRootDirectory(), "images");
           String npath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/aaab/";
           File dir=new File(npath);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建目录
        }

        File file = new File(dir, "22.jpg");
        if (!file.exists()){
            file.createNewFile();
        }

        try (InputStream is = getAssets().open("22.jpg");
             OutputStream os = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length); // 批量写入，性能更好
            }
            os.flush();
            System.out.println("-----------图片已保存到: " + file.getAbsolutePath());
           Log.d("------------------","---------out---"+   file.getAbsolutePath());
            upLoad(bucketName,file.getAbsolutePath());

//            mbuttons.setImageDrawable();

        } catch (Exception e) {
            e.printStackTrace();
        }}

    private  void upLoad(String key,String imagePath){

        Log.d("----------------","------------------imagePath--"+imagePath);
        if (imagePath.isEmpty()||imagePath==null){
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                MinioClient minioClient = MinioClient.builder()
                        .endpoint(baseUrl)
                        .credentials(accessKey, secretKey)
                        .region("cn-north-1")
                        .build();


                try {
                    boolean isBucketExist = minioClient.bucketExists(BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());

//                    boolean found =
//                            minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
                    Log.d("------------------------","----------bucketName-----isBucketExist---"+isBucketExist);

                    if (!isBucketExist ){
//                        minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
                        minioClient.makeBucket(MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());
                        Log.d("------------------------","-------------------bucketName-----创建成功---");
                    }else {
                        Log.d("------------------------","-------------------bucketName-----已成功---");
                    }

                } catch (ErrorResponseException e) {
                    throw new RuntimeException(e);
                } catch (InsufficientDataException e) {
                    throw new RuntimeException(e);
                } catch (InternalException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (InvalidResponseException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (ServerException e) {
                    throw new RuntimeException(e);
                } catch (XmlParserException e) {
                    throw new RuntimeException(e);
                }

                Log.d("-------------------","-----------------bucketName--"+bucketName);
//                boolean isBucketExist = minioClient.bucketExists(BucketExistsArgs.builder()
//                        .bucket(bucketName)
//                        .build());

                try {
                    InputStream inputStream = new FileInputStream(new File(imagePath));

                    PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
//                            .object(folderPath)
                            .contentType("image/jpeg")
//                            .contentType(""+ MediaType.JPEG)
//                            .contentType(""+ MediaType.ANY_VIDEO_TYPE)
//                            .stream(inputStream, inputStream.available(), -1)
                            .stream(inputStream,inputStream.available(),-1)
                            .build();

                       minioClient.putObject(uploadObjectArgs);

                    Log.d("-------","---------------完成-------Upload Successful!-----");

//
//                    OkHttpClient client = new OkHttpClient();
//                    // 构造 JSON 数据
//                    String jsonPayload = String.format("{\"bucket\":\"%s\", \"object\":\"%s\"}", bucketName, bucketName);
//                    MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
//                    RequestBody body = RequestBody.create(jsonPayload,JSON );
//                    Request request = new Request.Builder()
//                            .url(baseUrl)
//                            .post(body)
//                            .build();
//
//                    try (Response response = client.newCall(request).execute()) {
//                        if (response.isSuccessful()) {
//                            System.out.println("Callback success: " + response.body().string());
//                        } else {
//                        int mcode=    response.code();
//                            System.err.println("Callback failed with code: " +mcode);
//                        }
//                    } catch (Exception e) {
//                        System.err.println("Error during callback: " + e.getMessage());
//                    }


//                    URL presignedUrl = minioClient.getPresignedObjectUrl(
//                            GetObjectArgs.builder()
//                                    .bucket(bucketName)
//                                    .object(objectName)
//                                    .method(Method.GET)  // 设置为 GET 方法，表示下载
//                                    .expiry(60 * 60) // 1 小时有效期
//                                    .build()
//                    );



                } catch (IOException | ErrorResponseException | InsufficientDataException |
                         InternalException | InvalidKeyException | InvalidResponseException |
                         NoSuchAlgorithmException | ServerException | XmlParserException e) {
//                    System.err.println("Error occurred: " + e);
Log.d("","-------------------Error occurred: ---"+e);
                    throw new RuntimeException(e);
                }
//                    throw new RuntimeException(e);
//                } catch (ServerException e) {
//                    throw new RuntimeException(e);
//                } catch (InsufficientDataException e) {
//                    throw new RuntimeException(e);
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                } catch (ErrorResponseException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (InvalidResponseException e) {
//                    throw new RuntimeException(e);
//                } catch (XmlParserException e) {
//                    throw new RuntimeException(e);
//                } catch (InternalException e) {
//                    throw new RuntimeException(e);
//                }

            }
        }).start();



    }


}
