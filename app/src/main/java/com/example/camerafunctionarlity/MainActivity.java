package com.example.camerafunctionarlity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    Button btOpen;
    Button btSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btOpen = findViewById(R.id.bt_open);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }


        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
/*        btSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivityForResult(,200);
            }
        });*/
    }

    private String saveLocationAlternateTest(Bitmap image){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";
        ContextWrapper cw = new ContextWrapper((getApplicationContext()));
        File storageLocation = cw.getDir("imageDir",MODE_PRIVATE);
        File path = new File(storageLocation,imageFileName);

        FileOutputStream outWrite = null;
        try {
            outWrite = new FileOutputStream(path);
            image.compress(Bitmap.CompressFormat.PNG, 100, outWrite);
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                outWrite.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return storageLocation.getAbsolutePath();
        }




    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            saveLocationAlternateTest(imageBitmap);
        }
        /*if(requestCode == 200){
            Bitmap imageBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            saveLocationAlternateTest(imageBitmap);
        }*/
    }
}
