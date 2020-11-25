package com.example.camerafunctionarlity;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button btOpen;
    Button btSave;
    private ImageView imageView;
    /*Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Photo Saved",
            Snackbar.LENGTH_SHORT);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*This is the button associated with the activation of the camera function*/
        imageView = findViewById(R.id.imageView);
        btOpen = findViewById(R.id.bt_open);
        btSave = findViewById(R.id.bt_save);

        /*This if statement will associate the camera functionality with the request code, 100,  and the intent (Camera permissions) */
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }


        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            private View view;

            @Override
            public void onClick(View view) {

                saveLocationAlternateTest(imageView.getDrawingCache());
            }


        });
    }


    private String saveLocationAlternateTest(Bitmap image) {
        /*This function saves a picture with a name associated with the system's time at that moment (under the variable name timeStamp)
        and will write to a file named com.example.camerafunctionality */
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";
        ContextWrapper cw = new ContextWrapper((getApplicationContext()));
        File storageLocation = cw.getDir("imageDir", MODE_PRIVATE);
        File path = new File(storageLocation, imageFileName);

        FileOutputStream outWrite = null;
        try {
            outWrite = new FileOutputStream(path);
            image.compress(Bitmap.CompressFormat.PNG, 100, outWrite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storageLocation.getAbsolutePath();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        /*Using the request code that was initialized by the btOpen button for the camera, this if statement will save the photo as a
        * viewable thumbnail (or preview) under the variable name called imageView using an imageBitmap. */
        if (requestCode == 100) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            //mySnackbar.show();
            /*Using the imageBitmap, it will save it to the location listed in the function above */
        }

    }
}
