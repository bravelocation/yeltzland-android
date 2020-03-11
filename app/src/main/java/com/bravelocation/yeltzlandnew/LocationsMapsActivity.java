package com.bravelocation.yeltzlandnew;

import android.Manifest;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class LocationsMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static int MY_LOCATION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_maps);

        // Add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(!this.checkPermission()) {
            this.requestPermission(MY_LOCATION_REQUEST_CODE);

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<LocationDataItem> locations = LocationsDataPump.getData();

        for (int i = 0; i < locations.size(); i++) {
            LocationDataItem location = locations.get(i);
            LatLng position = new LatLng(location.latitude, location.longitude);
            mMap.addMarker(new MarkerOptions().position(position).title(location.opponent).snippet(location.description));
        }

        // Center the map and zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LocationsDataPump.getCenter(), 7.0f));

        // Start off in road view
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Show current location if allowed
        if (this.checkPermission()) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == 0) {
            // Switch map type
            if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(Menu.NONE, 0, Menu.NONE, "Map type switcher").setIcon(R.drawable.ic_map_o)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        // Change color to white
        Drawable menuItem = menu.getItem(0).getIcon();
        menuItem.mutate();
        menuItem.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.white), PorterDuff.Mode.SRC_IN);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == this.getPackageManager().PERMISSION_GRANTED) {
                if (this.checkPermission()) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    //  CHECK FOR LOCATION PERMISSION
    private boolean checkPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == this.getPackageManager().PERMISSION_GRANTED;
    }

    //REQUEST FOR PERMISSSION
    private void requestPermission(final int code){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this,"GPS permission allows us to access location data to show where you are. Please enable this in the App Settings", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
        }
    }
}

