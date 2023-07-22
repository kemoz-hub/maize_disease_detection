package com.example.cropdiseasesdetection;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cropdiseasesdetection.ml.Maizedetection;
import com.example.cropdiseasesdetection.ml.Model;
import com.example.cropdiseasesdetection.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.Tensor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
EditText results,pest;
ImageView imageView;
ImageButton upload,photo;
Bitmap bitmap;
ByteBuffer byteBuffer;

int imagesize=224;
String[]labels=new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getpermission();

        int cnt=0;
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(getAssets().open("labels.txt.txt")));
            String line=bufferedReader.readLine();
            while(line!=null){
                labels[cnt]=line;
                cnt++;
                line=bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        results=findViewById(R.id.diseasetype);
        pest=findViewById(R.id.pestside);
        imageView=findViewById(R.id.plantpic);
        upload=findViewById(R.id.imageButton);



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        
       
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==3){
            if(grantResults.length>0){
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getpermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},3);
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 )
        {
            if (data!= null)
            {
                Uri uri=data.getData();
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    imageView.setImageBitmap(bitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

           /* Bitmap image=(Bitmap)data.getExtras().get("data") ;
            int dimension=Math.min(image.getWidth(),image.getHeight());
            image= ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            imageView.setImageBitmap(image);

            image=Bitmap.createScaledBitmap(image,imagesize,imagesize,false);*/

        }

             if (requestCode==4 && data!=null){
                 try {
                     bitmap=(Bitmap)data.getExtras().get("data");
                     imageView.setImageBitmap(bitmap);
                 }
                 catch (Exception e) {
                     e.printStackTrace();
                 }

            }



        super.onActivityResult(requestCode, resultCode, data);

        classifyImage();
    }





       /* try {
            Maizedetection model = Maizedetection.newInstance(getApplicationContext());
            TensorBuffer input =TensorBuffer.createFixedSize(new int[]{1,224,224,3}, DataType.FLOAT32);
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*imagesize*imagesize*3);
            byteBuffer.order(ByteOrder.nativeOrder());
            
            int[] intvalue=new int[imagesize*imagesize];
            image.getPixels(intvalue,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            
            int pixels=0;
            for(int i=0 ; i < imagesize; i++){
                for(int j=0 ; j<imagesize;j++){
                    int val =intvalue[pixels++];
                    byteBuffer.putFloat(((val>>16)& 0xFF)*(1.f/1));
                    byteBuffer.putFloat(((val>>8)& 0xFF)*(1.f/1));
                    byteBuffer.putFloat((val& 0xFF)*(1.f/1));

                }
            }

            input.loadBuffer(byteBuffer);
            Maizedetection.Outputs outputs=model.process(input);
            TensorBuffer output=outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence=output.getFloatArray();
            int maxpos=0;
            float maxconfidence=0;

            for (int i =0;i<confidence.length;i++){
                if(confidence[i]>maxconfidence){
                    maxconfidence=confidence[i];
                    maxpos=i;
                }

            }

            String[] classes ={"corn rust","Leaf blight","HELLO"};
            results.setText(classes[maxpos]);
            results.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/search?g="+results.getText())));

                }
            });
            model.close();
        }
        catch (IOException e){

        }*/

    int getmax(float[]arr){
        int max=0;
        for (int i=0;i<arr.length;i++){
            if (arr[i]>arr[max])max=i;
        }
        return max;
    }
    private void classifyImage() {

        try {
            Model model = Model.newInstance(MainActivity.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);



            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);


            TensorImage ti=new TensorImage(DataType.UINT8);
            ti.load(bitmap);

            byteBuffer=ti.getBuffer();

            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            results.setText(labels[getmax(outputFeature0.getFloatArray())] + "");

            String dises=results.getText().toString().trim();

            String message="The disease is  " +dises+ "";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                    .setContentTitle("maize disease")
                    .setContentText(message)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = MainActivity.this.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
                builder.setChannelId("My notification");
            }

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(0, builder.build());


            NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, builder.build());

            if (dises.equals("Blight")){
                pest.setText("cereus C1l");
            }
            else if (dises.equals("Common rust")){
                pest.setText("pyraclostrobin");
            }
            else if (dises.equals("Gray leaf spot")){
                pest.setText("Delaro");
            }
            else if (dises.equals("healthy")) {
                pest.setText("Good condition");
            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}