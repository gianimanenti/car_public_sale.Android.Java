package com.moondu.leilao.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.moondu.leilao.R;
import com.moondu.leilao.view.activity.Home;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

public class Tracking extends FragmentActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;
    LatLng userLocation;
    Location local;
    private GoogleMap mMap;
    private LocationCallback locationCallback;
    private Integer locationRequest = PRIORITY_BALANCED_POWER_ACCURACY;
    private ArrayList<Location> listLocs = new ArrayList<Location>();
    private Button btnTracking;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnTracking = this.findViewById(R.id.btnBack);

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tracking.this, Home.class);
                startActivity(i);
            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        LatLng userNow = new LatLng(location.getLatitude(), location.getLongitude());

                        if (listLocs.size() == 0) {
                            mMap.addMarker(new MarkerOptions().position(userNow)
                                    .title("Está aqui agora"));
                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userNow));
                        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

                       // Log.d("TESTE TESTE", Double.toString(location.getLongitude()) + Double.toString(location.getLatitude()));

                        listLocs.add(location);
                        drawPrimaryLinePath(listLocs);


                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);






    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;

            // Add a marker in Sydney, Australia,
            // and move the map's camera to the same location.
      //  LatLng sydney = new LatLng(-33.852, 151.211);
     //   mMap.addMarker(new MarkerOptions().position(sydney)
     //               .title("Marker in Sydney"));
     //   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(2);

        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );




    }

    @Override
    protected void onResume() {
        super.onResume();
      //  if (requestingLocationUpdates) {
      //      startLocationUpdates();
       // }
    }

    private void drawPrimaryLinePath( ArrayList<Location> listLocsToDraw )
    {
        if ( mMap == null )
        {
            return;
        }

        if ( listLocsToDraw.size() < 2 )
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );

        for ( Location locRecorded : listLocsToDraw )
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }

        mMap.addPolyline( options );

    }
}
