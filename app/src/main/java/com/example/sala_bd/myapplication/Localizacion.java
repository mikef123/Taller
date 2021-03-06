package com.example.sala_bd.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Localizacion extends AppCompatActivity {

    private LocationRequest mLocationRequest;
    TextView latitud;
    TextView longitud;
    String la;
    String lo;
    String al;
    TextView altitud;
    TextView distancia;
    Button guardar;
    private static final int localizacion = 3;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    public	final	static	double	RADIUS_OF_EARTH_KM	 =	6371;
    double plazaLatitud = 4.5983;
    double plazalongitud = -74.0753;
    double distanciaT=0;
    ListView lista;
    List<String> ubicacion = new ArrayList<String>();
    String texto;
    JSONArray localizaciones = new JSONArray();

    public String[] mApps = {
            "Instagram",
            "Pinterest",
            "Pocket",
            "Twitter"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);
        latitud = (TextView) findViewById(R.id.latitud);
        longitud = (TextView) findViewById(R.id.longitud);
        altitud = (TextView) findViewById(R.id.altitud);
        distancia = (TextView) findViewById(R.id.distancia);
        guardar = (Button) findViewById(R.id.guardar);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();


        check();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location	update	in	the	callback:	" + location);
                if (location != null) {
                    la=String.valueOf(location.getLatitude());
                    lo=String.valueOf(location.getLongitude());
                    al=String.valueOf(location.getAltitude());
                    latitud.setText("Latitude:	 	" + la);
                    longitud.setText("Longitude:	 	" + lo);
                    altitud.setText("Altitude:			" + al);
                    distanciaT = distance(location.getLatitude(), location.getLongitude(), plazaLatitud, plazalongitud);
                    distancia.setText(distancia.getText() + ": " + distanciaT);
                }
            }
        };




        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                texto= "Latitud: " + la + " Longitud: " + lo + " Fecha: " + Calendar.getInstance().getTime().toString();
                ubicacion.add(texto);
                ArrayAdapter<String> adaptador;
                lista = (ListView) findViewById(R.id.listView);
                adaptador = new ArrayAdapter<String>(Localizacion.this, android.R.layout.simple_list_item_1, ubicacion);
                lista.setAdapter(adaptador);
                Toast.makeText(getApplicationContext(),
                        "Ubicación guardada", Toast.LENGTH_SHORT);
                writeJSONObject();
            }
        });
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

    public void check()
    {

        LocationSettingsRequest.Builder builder	=	new
                LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client	 =	LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task	=	client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this,	 new	OnSuccessListener<LocationSettingsResponse>()
        {
            @Override
            public	void	onSuccess(LocationSettingsResponse locationSettingsResponse)	 {
                startLocationUpdates();	 //Todas las condiciones para	recibir localizaciones
            }
        });
        task.addOnFailureListener(this,	 new	OnFailureListener()	 {
            @Override
            public	void	onFailure(@NonNull Exception	 e)	{
                int statusCode =	((ApiException)	e).getStatusCode();
                switch	(statusCode)	{
                    case	CommonStatusCodes.RESOLUTION_REQUIRED:
//	Location	settings	are	not	satisfied,	but	this	can	be	fixed	by	showing	the	user	a	dialog.
                        try	{//	Show	the	dialog	by	calling	startResolutionForResult(),	and	check	the	result	in	onActivityResult().
                            ResolvableApiException resolvable	 =	(ResolvableApiException)	 e;
                            resolvable.startResolutionForResult(Localizacion.this,
                                    localizacion);
                        }	catch	(IntentSender.SendIntentException sendEx)	{
//	Ignore	the	error.
                        }	break;
                    case	LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//	Location	settings	are	not	satisfied.	No	way	to	fix	the	settings	so	we	won't	show	the	dialog.
                        break;
                }
            }
        });

    }
    protected	void	onActivityResult(int requestCode,	 int resultCode,	 Intent data)	 {
        switch	(requestCode)	 {
            case	localizacion:	 {
                if	(resultCode ==	RESULT_OK)	 {
                    startLocationUpdates();	 	//Se	encendió la	localización!!!
                }	else	{
                    Toast.makeText(this,
                            "Sin	acceso a	localización,	hardware	deshabilitado!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private	void	startLocationUpdates()	 {
        if	(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)	 ==
                PackageManager.PERMISSION_GRANTED)	 {				//Verificación de	permiso!!
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,	 mLocationCallback,
                    null);
        }
    }
    public	double	distance(double	 lat1,	double	long1,	double	lat2,	double	long2)	{
        double	latDistance =	Math.toRadians(lat1	 - lat2);
        double	lngDistance =	Math.toRadians(long1	 - long2);
        double	a	=	Math.sin(latDistance /	2)	*	Math.sin(latDistance /	2)
                +	Math.cos(Math.toRadians(lat1))	 *	Math.cos(Math.toRadians(lat2))
                *	Math.sin(lngDistance /	2)	*	Math.sin(lngDistance /	2);
        double	c	=	2	*	Math.atan2(Math.sqrt(a),	 Math.sqrt(1	- a));
        double	result	=	RADIUS_OF_EARTH_KM	 *	c;
        return	Math.round(result*100.0)/100.0;
    }
    public JSONObject toJSON ()	{
        JSONObject obj =	new	JSONObject();
        try	{
            obj.put("latitud",	latitud.getText().toString());
            obj.put("longitud",	longitud.getText().toString());
            obj.put("date",	Calendar.getInstance().getTime().toString());
        }	catch	(JSONException e)	{
            e.printStackTrace();
        }
        return	obj;
    }
    private	void	writeJSONObject(){
        localizaciones.put(toJSON());
        Writer output	=	null;
        String	filename=	"locations.json";
        try	{
            File file	=	new	File(getBaseContext().getExternalFilesDir(null),	 filename);
            Log.i("LOCATION",	"Ubicacion de	archivo:	"+file);
            output	=	new BufferedWriter(new FileWriter(file));
            output.write(localizaciones.toString());
            output.close();
            Toast.makeText(getApplicationContext(),	 "Location	saved",
                    Toast.LENGTH_LONG).show();
        }	catch	(Exception	e)	 {
            Toast.makeText(getBaseContext(),	 e.getMessage(),	 Toast.LENGTH_LONG).show();
        }
    }
}

