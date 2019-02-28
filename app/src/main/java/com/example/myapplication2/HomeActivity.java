package com.example.myapplication2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    ImageView imageView;

    private static int SPLASH_TIME_OUT =4000;

    private static final int IMAGE_PICK_CODE=1000;
    private static final int CAMERA_PICK_CODE=1001;
    private static final int STORAGE_PERMISSION_CODE=1002;
    private static final int CAMERA_PERMISSION_CODE=1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        Button btnGallery = (Button)findViewById(R.id.btnGallery);
        imageView = (ImageView)findViewById(R.id.imageView);

        //control camera button click
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String [] permissions ={Manifest.permission.CAMERA};
                        // show popup for runtime permission
                        requestPermissions(permissions,CAMERA_PERMISSION_CODE);
                    }else{
                        //permission already granted
                        openCamera();
                    }
                }else{
                    // system os is less than mashmellow
                    openCamera();
                }

            }
        });

        //control gallery button click
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check runtime permission
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String [] permissions ={Manifest.permission.READ_EXTERNAL_STORAGE};
                        // show popup for runtime permission
                        requestPermissions(permissions,STORAGE_PERMISSION_CODE);
                    }else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }else{
                    // system os is less than mashmellow
                    pickImageFromGallery();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==IMAGE_PICK_CODE){
            // set image on image view
            imageView.setImageURI(data.getData());
        }
        if(resultCode==RESULT_OK && requestCode==CAMERA_PICK_CODE){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_PICK_CODE);
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        startActivityForResult(intent1, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case STORAGE_PERMISSION_CODE:{
                if (grantResults.length>0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else{
                    //permission was denied
                    Toast.makeText(this,"Permission denied!",Toast.LENGTH_SHORT).show();
                }
            }

            case CAMERA_PERMISSION_CODE:{
                if (grantResults.length>0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    openCamera();
                }
                else{
                    //permission was denied
                    Toast.makeText(this,"Permission denied!",Toast.LENGTH_SHORT).show();
                }
            }


        }

    }


}
