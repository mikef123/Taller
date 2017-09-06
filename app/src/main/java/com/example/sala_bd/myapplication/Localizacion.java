package com.example.sala_bd.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Localizacion extends AppCompatActivity {

    private LocationRequest mLocationRequest;
    TextView latitud;
    TextView longitud;
    TextView altitud;
    private static final int localizacion = 3;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);
        pedirPermisoLocalizacion();
        latitud = (TextView) findViewById(R.id.latitud);
        longitud = (TextView) findViewById(R.id.longitud);
        altitud = (TextView) findViewById(R.id.altitud);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this,	 new OnSuccessListener<Location>()	 {
                @Override
                public	void	onSuccess(Location	 location) {
                    Log.i("LOCATION", "onSuccess location");
                    if (location != null) {
                        Log.i("	LOCATION	",
                                "Longitud:	 " + location.getLongitude());
                       String la= Double.toString(location.getLatitude());
                        latitud.setText(la);
                        String lo= Double.toString(location.getLongitude());
                        longitud.setText(lo);
                        String al= Double.toString(location.getAltitude());
                        altitud.setText(al);
                        Log.i("	LOCATION	",
                                "Latitud:	 " + location.getLatitude());
                    }
                }
            });

        }
        mLocationRequest =	createLocationRequest();







    }

    protected	LocationRequest createLocationRequest()	 {
        LocationRequest mLocationRequest =	new	LocationRequest();
        mLocationRequest.setInterval(10000);	 //tasa de	refresco en	milisegundos
        mLocationRequest.setFastestInterval(5000);	 //máxima tasa de	refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return	mLocationRequest;
    }

    public void pedirPermisoLocalizacion()
    {
        int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {

            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    localizacion);
        }

    }
}
