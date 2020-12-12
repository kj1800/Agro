package com.example.agro_lens.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.agro_lens.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class Map_locate extends AppCompatActivity implements OnMapReadyCallback {
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;
    private GoogleMap mMap;
    Button btn;
    private FirebaseDatabase firebase;
    private DatabaseReference databaseReference;
    private DatabaseReference dbReference;
    Spinner spinner;
    String valufromspinner;
    public LatLng latlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_locate);
        checkPermission();
        firebase = FirebaseDatabase.getInstance();

        spinner=findViewById(R.id.spinnertext);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.crop_names, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinner.setAdapter(adapter);


    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        databaseReference = firebase.getReference("marker");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                valufromspinner= adapterView.getItemAtPosition(i).toString();


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mMap.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if(child.child("cropname").getValue().toString().equals(valufromspinner)){
                                String lat = child.child("latitude").getValue().toString();
                                String lng = child.child("longitude").getValue().toString();

                                double latitude = Double.parseDouble(lat);
                                double longitude = Double.parseDouble(lng);
                                LatLng loc = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions().position(loc).title(child.child("generatetext").getValue().toString()));




                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(
                        latlng, 15);
                mMap.animateCamera(cameraUpdate);
                mMap.addMarker(new MarkerOptions().position(latlng).title("MY location"));



            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                dbReference = firebase.getReference("marker");
                final String markertitle=marker.getTitle();
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(Map_locate.this);
                bottomSheetDialog.setContentView(R.layout.map_bottom);

                bottomSheetDialog.setCanceledOnTouchOutside(true);

                final TextView crop_name_bottom=bottomSheetDialog.findViewById(R.id.bottom_crop_name);
                final TextView crop_location_bottom=bottomSheetDialog.findViewById(R.id.bottom_crop_location);
                final TextView crop_coordinates_bottom=bottomSheetDialog.findViewById(R.id.bottom_crop_coordinates);
                final TextView crop_username_bottom=bottomSheetDialog.findViewById(R.id.bottom_crop_username);
                final ImageView crop_image_bottom=bottomSheetDialog.findViewById(R.id.cropImage_bottom);
                final TextView crop_dateandtime_bottom=bottomSheetDialog.findViewById(R.id.bottom_crop_dateandtime);

                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if(child.child("generatetext").getValue().toString().equals(markertitle)){
                                String lat = child.child("latitude").getValue().toString();
                                String lng = child.child("longitude").getValue().toString();

                                double latitude = Double.parseDouble(lat);
                                double longitude = Double.parseDouble(lng);
                                String location=getCompleteAddressString(latitude,longitude);
                                String coordinates=lat+", "+lng;
                                String cropname=child.child("cropname").getValue().toString();
                                String username=child.child("username").getValue().toString();
                                String cropImage_url=child.child("image").getValue().toString();
                                String date=child.child("date").getValue().toString();
                                String time=child.child("time").getValue().toString();

                                Picasso.get().load(cropImage_url).placeholder(R.drawable.logobg).into(crop_image_bottom);
                                crop_dateandtime_bottom.setText(date+" "+time);
                                crop_name_bottom.setText(cropname);
                                crop_location_bottom.setText(location);
                                crop_coordinates_bottom.setText(coordinates);
                                crop_username_bottom.setText(username);




                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                bottomSheetDialog.show();



                return false;
            }
        });


    }


}