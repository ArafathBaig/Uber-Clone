package arafath.myappcom.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class DriverActivityList extends AppCompatActivity implements View.OnClickListener{

    private Button getReq;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ListView listView;
    private ArrayList<String> driverReq;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listView = findViewById(R.id.requestListView);

          driverReq = new ArrayList<>();
          getReq = findViewById(R.id.btnpassengerReq);
          getReq.setOnClickListener(this);
         arrayAdapter = new ArrayAdapter(DriverActivityList.this,android.R.layout.simple_list_item_1,driverReq);


         listView.setAdapter(arrayAdapter);
         driverReq.clear();

          if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

              try {
                  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000000, 0, locationListener);
              }catch (Exception e){
                  e.printStackTrace();
              }
          }

    }

    @Override
    public void onClick(View v) {



                 locationListener = new LocationListener() {
                     @Override
                     public void onLocationChanged(Location location) {

                         updatePassengerLocation(location);


                     }

                     @Override
                     public void onStatusChanged(String provider, int status, Bundle extras) {

                     }

                     @Override
                     public void onProviderEnabled(String provider) {

                     }

                     @Override
                     public void onProviderDisabled(String provider) {

                     }
                 };

                 if (Build.VERSION.SDK_INT < 23) {

                     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000000, 0, locationListener);

                     Location currentPassengerLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                     updatePassengerLocation(currentPassengerLocation);


                 } else if (Build.VERSION.SDK_INT >= 23) {

                     if (ContextCompat.checkSelfPermission(DriverActivityList.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                         ActivityCompat.requestPermissions(DriverActivityList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);


                     } else {

                        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000000, 0, locationListener);

                         Location currentPassengerLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                         updatePassengerLocation(currentPassengerLocation);


                     }
                 }


             }


             @Override
             public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                 super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                 if (requestCode == 1000 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                     if (ContextCompat.checkSelfPermission(DriverActivityList.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000000, 0, locationListener);

                        // Location currentPassengerLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                         

                     }
                 }

             }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logOutinDriver:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(DriverActivityList.this,"Logged Out",FancyToast.INFO, Toast.LENGTH_SHORT,true).show();
                            Intent intent = new Intent(DriverActivityList.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }

    private void updatePassengerLocation(Location driverLocation){

    if(driverLocation!= null){
        driverReq.clear();
        final ParseGeoPoint driverCurrentLocation = new ParseGeoPoint(driverLocation.getLatitude(),driverLocation.getLongitude()) ;
        ParseQuery<ParseObject> requestCarQuery = ParseQuery.getQuery("RequestCar");
        requestCarQuery.whereNear("PassengerLocation",driverCurrentLocation);

        requestCarQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject nearCar : objects) {
                            Double kmsDistance = driverCurrentLocation.distanceInMilesTo((ParseGeoPoint) nearCar.get("PassengerLocation"));
                            float roundedKMSDistance = Math.round(kmsDistance * 10) / 10;
                            driverReq.add("There are " + roundedKMSDistance + " Kilometers to " + nearCar.get("username"));



                            Log.i("Check","Here once")   ;

                        }

                    }else{
                        FancyToast.makeText(DriverActivityList.this,"There are no requests",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    }

                    arrayAdapter.notifyDataSetChanged();
                    
                }
            }
        });

    }

    }


}
