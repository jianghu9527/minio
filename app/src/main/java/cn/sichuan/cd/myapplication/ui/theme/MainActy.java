package cn.sichuan.cd.myapplication.ui.theme;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.os.Environment;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import cn.sichuan.cd.myapplication.R;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

public class MainActy extends AppCompatActivity {


    private static final String baseUrl = "";
    private static final String accessKey = "Og30uh8IWxHRxto04rio";
    private static final String secretKey = "L5nsJMxqBXDRjqpXOJWy79iXYCb6g8S9Z4CB02LK";
    private static final String bucketName = "eapc";

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
           String npath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/aaa/";
           File dir=new File(npath);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建目录
        }

        File file = new File(dir, "Screen_Recording.mp4");
        if (!file.exists()){
            file.createNewFile();
        }

        try (InputStream is = getAssets().open("Screen_Recording.mp4");
             OutputStream os = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length); // 批量写入，性能更好
            }
            os.flush();
            System.out.println("--图片已保存到: " + file.getAbsolutePath());

            upLoad("eapc",file.getAbsolutePath());

//            mbuttons.setImageDrawable();

        } catch (Exception e) {
            e.printStackTrace();
        }}

    private  void upLoad(String key,String imagePath){

        Log.d("","------------------imagePath--"+imagePath);
        if (imagePath.isEmpty()||imagePath==null){
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                MinioClient minioClient = MinioClient.builder()
                        .endpoint(baseUrl)
                        .credentials(accessKey, secretKey)
                        .build();

                try {
                    InputStream inputStream = new FileInputStream(new File(imagePath));

                    PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .contentType("image/jpeg")
//                            .stream(inputStream, inputStream.available(), -1)
//                            . stream(inputStream,inputStream.available(),-1)
                            .stream(inputStream,inputStream.available(),-1)
//                            .region("cn-north-1")

                            .build();


                    minioClient.putObject(uploadObjectArgs);

                    Log.d("-------","---------------完成------------");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (ServerException e) {
                    throw new RuntimeException(e);
                } catch (InsufficientDataException e) {
                    throw new RuntimeException(e);
                } catch (ErrorResponseException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (InvalidResponseException e) {
                    throw new RuntimeException(e);
                } catch (XmlParserException e) {
                    throw new RuntimeException(e);
                } catch (InternalException e) {
                    throw new RuntimeException(e);
                }


            }
        }).start();



    }


}
