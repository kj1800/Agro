package com.example.agro_lens.CropHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;

import com.example.agro_lens.CropDetails.AdapterUser;
import com.example.agro_lens.CropDetails.Modeluser;
import com.example.agro_lens.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class cropHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterHistory adapterHistory;
    List<Modelhistory> userlist;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_history);


        recyclerView=findViewById(R.id.recyclerhistoryuser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userlist=new ArrayList<>();
        getAllUsers();
        searchView=findViewById(R.id.crophistorysearch);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterHistory.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getAllUsers() {
        final FirebaseUser fbuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Users").child(fbuser.getUid()).child("crophistory");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Modelhistory modelhistory=ds.getValue(Modelhistory.class);
                    userlist.add(modelhistory);
                    adapterHistory=new AdapterHistory(cropHistory.this,userlist);
                    recyclerView.setAdapter(adapterHistory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}