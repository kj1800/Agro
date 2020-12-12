package com.example.agro_lens.CropDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.agro_lens.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Crop_Details extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterUser adapterUser;
    List<Modeluser> userlist;
    SearchView searchView;
    String CurrentLang;
    String lang;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop__details);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        Intent intent=getIntent();
        CurrentLang=intent.getStringExtra("lang");
        if(CurrentLang.equals("en")){
            lang="english";

        }
        else if (CurrentLang.equals("hi")){
            lang="hindi";

        }
        else {
            lang="tamil";
        }
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Users");
        HashMap<String,Object> result=new HashMap<>();
        result.put("languages",lang);
        reference.child(user.getUid()).child("userdetails").updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(Crop_Details.this,"updated",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Crop_Details.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });



        recyclerView=findViewById(R.id.recycleruser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userlist=new ArrayList<>();
        getAllUsers();

        searchView=findViewById(R.id.cropsearch);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterUser.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getAllUsers() {
        final FirebaseUser fbuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("crops").child("english");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Modeluser modeluser=ds.getValue(Modeluser.class);
                    userlist.add(modeluser);
                    adapterUser=new AdapterUser(getApplicationContext(),userlist);
                    recyclerView.setAdapter(adapterUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}