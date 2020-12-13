package com.example.agro_lens.CropDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agro_lens.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class crop_complete_details extends AppCompatActivity {
    TextView Crop_name,crop_state,state_detail;
    ImageView crop_soil,crop_temperature,crop_places,crop_season,crop_image;
    String Basic,Temperature,Soil,Places,Season,CropImageurl,cropName;
    String CurrentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_complete_details);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title").toLowerCase();



        crop_soil=findViewById(R.id.soiltype);
        crop_temperature=findViewById(R.id.temperature);
        crop_places=findViewById(R.id.places);
        crop_season=findViewById(R.id.season);
        crop_image=findViewById(R.id.cropimg);

        Crop_name=findViewById(R.id.crop_name_detail);
        crop_state=findViewById(R.id.topic);
        state_detail=findViewById(R.id.infotext);
        String lang="english";

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("crops").child(lang).child(title);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                cropName= dataSnapshot.child("cropname").getValue(String.class);
                Temperature=dataSnapshot.child("temperature").getValue(String.class);
                Season=dataSnapshot.child("season").getValue(String.class);
                Basic=dataSnapshot.child("basic").getValue(String.class);
                Soil=dataSnapshot.child("soiltype").getValue(String.class);
                Places=dataSnapshot.child("places").getValue(String.class);
                CropImageurl=dataSnapshot.child("cropimageurl").getValue(String.class);


                crop_state.setText("General Information");
                state_detail.setText(Basic);
                Crop_name.setText(cropName);

                try {
                    Picasso.get().load(CropImageurl).placeholder(R.drawable.agrologo).into(crop_image);

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        crop_soil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crop_state.setText("Soil Type");
                state_detail.setText(Soil);

            }
        });
        crop_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crop_state.setText("Temperature");
                state_detail.setText(Temperature);

            }
        });
        crop_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crop_state.setText("Places");
                state_detail.setText(Places);

            }
        });
        crop_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crop_state.setText("Season");
                state_detail.setText(Season);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}