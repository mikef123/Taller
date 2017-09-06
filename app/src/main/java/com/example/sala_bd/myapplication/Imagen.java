package com.example.sala_bd.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Imagen extends AppCompatActivity {

    private static final int camara = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView image;
    Button boton1;
    Button boton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        image = (ImageView) findViewById(R.id.imageView);
        boton1 = (Button) findViewById(R.id.button1) ;
        boton2 = (Button) findViewById(R.id.button2) ;

        boton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent pickImage=new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage,camara);
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture();
                Intent pickImage=new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage,camara);
            }
        });



    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, camara);
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case camara:
                if(resultCode==RESULT_OK){
                    try{
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    image.setImageBitmap(imageBitmap);
                }

        }
    }
}
