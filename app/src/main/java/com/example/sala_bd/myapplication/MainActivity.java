package com.example.sala_bd.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileNotFoundException;
        import java.io.InputStream;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

                ImageButton botonContacto;
                ImageButton botonCamara;
                ImageButton botonLocalizacion;
                int RESULT_OK =1;

                ListView list;
                String[] mProjection;
                ContactsCursor mCursorAdapter;
                Cursor mContactsCursor;
                private static final int contactos = 1;
                private static final int camara = 2;
                private static final int localizacion = 3;


                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);

                    setContentView(R.layout.activity_main);


                    botonContacto = (ImageButton) findViewById(R.id.imageButton);
                    botonCamara = (ImageButton) findViewById(R.id.imageButton2);
                    botonLocalizacion = (ImageButton) findViewById(R.id.imageButton3);

                    botonContacto.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {


                                pedirPermiso();

                            } else {

                                llamarActividad();
                            }
                        }
                    });

                    botonCamara.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                                pedirPermisoCamara();

                            } else {

                                llamarActividadCamara();
                            }
                        }
                    });

                    botonLocalizacion.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {

                                               llamarActividadLocalizacion();
                                           }
                                       }
                    );
                }




        public void pedirPermiso()
        {
        int permissionCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS))
        {

        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},
        contactos);
        }

        }

                public void pedirPermisoCamara() {
                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        }
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                camara);
                    }
                }



@Override
public void onRequestPermissionsResult(int requestCode,String permissions[],int[]grantResults){
        switch(requestCode){
        case contactos:{
//Ifrequestiscancelled,theresultarraysareempty.
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

         llamarActividad();
        }else{
        }
        return;
        }
            case camara:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                    llamarActividadCamara();
                }else{
                }
                return;
            }

            }
        }


        public void llamarActividad()
        {
        Intent sendIntent=new Intent(getBaseContext(),Permitido.class);
        startActivity(sendIntent);
        }

        public void llamarActividadCamara()
        {
            Intent sendIntent3=new Intent(getBaseContext(),Imagen.class);
            startActivity(sendIntent3);
        }

        public void llamarActividadLocalizacion()
         {

                 Intent sendIntent3=new Intent(getBaseContext(),Localizacion.class);
                 startActivity(sendIntent3);

         }





        }