package com.example.camerafunctionarlity;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    Button btOpen;
    Button btSave;
    Button btPlay;
    Button btStop;
    Button btRecord;

    private ImageView imageView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    MediaRecorder mediaRecorder = new MediaRecorder();
    private static String fileName = null;
    private static final String LOG_TAG = "AudioRecordTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaRecorder recorder = null;
        setContentView(R.layout.activity_main);
        /*This is the button associated with the activation of the camera function*/
        imageView = findViewById(R.id.imageView);
        btOpen = findViewById(R.id.bt_open);
        btSave = findViewById(R.id.bt_save);
        btPlay = findViewById(R.id.bt_play);
        btStop = findViewById(R.id.bt_stop);
        btRecord = findViewById(R.id.bt_record);


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
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayer("/data/data/com.example.camerafunctionarlity/app_audioDir", "12pm.mp3");
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                releaseMediaPlayer();
            }
        });
        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecorder();
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
    public void audioPlayer(String path, String fileName){
        {
            try {
                mediaPlayer.setDataSource(path + File.separator + fileName);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void audioRecorder(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = timeStamp + ".MPEG_4";
        ContextWrapper cw = new ContextWrapper((getApplicationContext()));
        File storageLocation = cw.getDir("audioDir", MODE_PRIVATE);
        File path = new File(storageLocation, audioFileName);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(path.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mediaRecorder.start();
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
            /*Using the imageBitmap, it will save it to the location listed in the function above */
        }

    }
}
