package com.example.agro_lens.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agro_lens.CropHistory.AdapterHistory;
import com.example.agro_lens.CropHistory.Modelhistory;
import com.example.agro_lens.CropHistory.cropHistory;
import com.example.agro_lens.R;
import com.example.agro_lens.coursevideo.AdapterNews;
import com.example.agro_lens.coursevideo.AdapterVideo;
import com.example.agro_lens.coursevideo.ModelVideo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AccountFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    ProgressDialog pd;
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE=300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;
    private FirebaseAuth mAuth;

    String cameraPermission[];
    String storagePermisssion[];
    Uri image_uri;
    String profileorcover;

    StorageReference storageReference;
    String storagepath="Users_Profile_Cover_Imgs/";

    List<ModelVideo> modelVideos,modelVideoList,modelVideossadsd;
    AdapterVideo adapterVideo;
    RecyclerView recyclerView,recyclerViewlatest,recyclerViewnews;

    AdapterNews adapterNews;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_account, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference=database.getReference("Users");

        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermisssion=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(getActivity());


        modelVideos=new ArrayList<>();
        recyclerView=view.findViewById(R.id.learncourse);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        modelVideoList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);

        recyclerViewlatest=view.findViewById(R.id.latestvideo);
        recyclerViewlatest.setHasFixedSize(true);
        recyclerViewlatest.setLayoutManager(linearLayoutManager1);



        modelVideossadsd=new ArrayList<>();
        recyclerViewnews=view.findViewById(R.id.latestnews);
        recyclerViewnews.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        recyclerViewnews.setLayoutManager(linearLayoutManager2);


getlatestlinks();
        getlinks();
        getnewsLinks();




        return view;
    }

    private void getnewsLinks() {
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("newsimage");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ModelVideo modelhistory=dataSnapshot.getValue(ModelVideo.class);
                    modelVideossadsd.add(modelhistory);
                    adapterNews=new AdapterNews(getActivity(),modelVideossadsd);
                    recyclerViewnews.setAdapter(adapterNews);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getlatestlinks() {
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("news");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ModelVideo modelhistory=dataSnapshot.getValue(ModelVideo.class);
                    modelVideoList.add(modelhistory);
                    adapterVideo=new AdapterVideo(getActivity(),modelVideoList);
                    recyclerViewlatest.setAdapter(adapterVideo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getlinks() {

        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("links");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ModelVideo modelhistory=dataSnapshot.getValue(ModelVideo.class);
                    modelVideos.add(modelhistory);
                    adapterVideo=new AdapterVideo(getActivity(),modelVideos);
                    recyclerView.setAdapter(adapterVideo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        requestPermissions(storagePermisssion,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }
    private void requestCameraPermission(){
        requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
    }
    private void showEditProfileDialog() {
        String Options[]={"Edit Profile Picture","Edit Cover Photo","Edit Name","Edit Phone number"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(Options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    pd.setMessage("Updating Profile Picture");
                    profileorcover="image";
                    showImagePicDialog();

                }
                else if(i==1){
                    pd.setMessage("Updating cover Photo");
                    profileorcover="cover";
                    showImagePicDialog();

                }
                else if(i==2){
                    pd.setMessage("Updating Name");
                    showNamePhoneupdatedialog("name");

                }
                else if(i==3){
                    pd.setMessage("Updating Phone number");
                    showNamePhoneupdatedialog("phone");

                }

            }
        });
        builder.create().show();
    }

    private void showNamePhoneupdatedialog(final String key) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"+key);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10 );

        final EditText editText=new EditText(getActivity());
        editText.setHint("Enter"+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String values=editText.getText().toString().trim();
                if(!TextUtils.isEmpty(values)){
                    pd.show();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put(key,values);
                    reference.child(firebaseUser.getUid()).child("userdetails").updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"updated",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else {
                    Toast.makeText(getActivity(),"Please Enter"+key,Toast.LENGTH_SHORT).show();

                }

            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.create().show();
    }

    private void showImagePicDialog() {
        String Options[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick the Image from");
        builder.setItems(Options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                }
                else if(i==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }


                }
            }
        });
        builder.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getActivity(),"Please enable storage and camera permission",Toast.LENGTH_SHORT).show();                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean writeStorageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getActivity(),"Please enable storage permission",Toast.LENGTH_SHORT).show();                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== RESULT_OK){
            if(requestCode==IMAGE_PICK_CAMERA_REQUEST_CODE){

                uploadPrfileCoverPhoto(image_uri);

            }
            else if(requestCode==IMAGE_PICK_GALLERY_REQUEST_CODE){
                image_uri=data.getData();
                uploadPrfileCoverPhoto(image_uri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadPrfileCoverPhoto(Uri uri) {
        pd.show();
        String filepathandname=storagepath+""+profileorcover+"_"+firebaseUser.getUid();
        StorageReference storageReference2=storageReference.child(filepathandname);
        storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri downloaduri=uriTask.getResult();

                if(uriTask.isSuccessful()){
                    HashMap<String,Object> results=new HashMap<>();
                    results.put(profileorcover,downloaduri.toString());
                    reference.child(firebaseUser.getUid()).child("userdetails").updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"Image updated",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"image updation error",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else{
                    pd.dismiss();
                    Toast.makeText(getActivity(),"Some error occured",Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void pickFromGallery() {
        Intent galleryintent=new Intent(Intent.ACTION_PICK);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    private void pickFromCamera() {
        ContentValues Values=new ContentValues();
        Values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        Values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        image_uri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,Values);

        Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraintent,IMAGE_PICK_CAMERA_REQUEST_CODE);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


}