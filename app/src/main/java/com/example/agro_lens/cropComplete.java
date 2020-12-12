package com.example.agro_lens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.util.jar.Pack200.Packer.ERROR;

public class cropComplete extends AppCompatActivity {
    TextView crop_history_name,crop_history_desc,crop_history_coordinate,crop_history_dateandtime,crop_history_age,crop_history_id;
    ImageView crop_history_image;
    FirebaseAuth auth;
    FirebaseUser user;
    String name,desc,latitude,longitude,age,id,date,time,imageurl;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_complete);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");


        crop_history_name=findViewById(R.id.history_crop_name);
        crop_history_desc=findViewById(R.id.history_crop_description);
        crop_history_coordinate=findViewById(R.id.history_crop_coordintes);
        crop_history_dateandtime=findViewById(R.id.history_crop_dateandtime);
        crop_history_age=findViewById(R.id.history_crop_age);
        crop_history_id=findViewById(R.id.history_crop_id);

        crop_history_image=findViewById(R.id.history_crop_image);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(user.getUid()).child("crophistory").child(title);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name=snapshot.child("cropname").getValue().toString();
                desc=snapshot.child("desc").getValue().toString();
                latitude=snapshot.child("latitude").getValue().toString();
                longitude=snapshot.child("longitude").getValue().toString();
                date=snapshot.child("date").getValue().toString();
                time=snapshot.child("time").getValue().toString();
                age=snapshot.child("cropage").getValue().toString();
                id=snapshot.child("generatetext").getValue().toString();

                imageurl=snapshot.child("image").getValue().toString();

                crop_history_name.setText(name);
                crop_history_desc.setText(desc);
                crop_history_coordinate.setText(latitude+", "+longitude);
                crop_history_dateandtime.setText(date+" "+time);
                crop_history_age.setText("2 months");
                crop_history_id.setText(id);

                Picasso.get().load(imageurl).placeholder(R.drawable.logobg).into(crop_history_image);
                Picasso.get().load(imageurl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image=bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void downloadDetails(View view) {
        ActivityCompat.requestPermissions(cropComplete.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    createMyPDF();
    }


    public void createMyPDF(){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(960,1280,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = "cropname:"+name+"\n"+"cropDescription:"+desc+"\n"+"coordinates:"+latitude+", "+longitude+"\n"+"Uploaded On:"+date+" "+time+"\n"+"crop id:"+id+"\n"+"crop age:"+"2months"+"\n";
        int x = 10, y=500;
        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

       myPage.getCanvas().drawBitmap(image,10,10, null);

        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/"+id+".pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();

        }
        Toast.makeText(cropComplete.this,"File saved in"+myFilePath,Toast.LENGTH_LONG).show();

        myPdfDocument.close();
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void shareDetails(View view) {
        String mydata = "cropname:"+name+"\n"+"cropDescription:"+desc+"\n"+"coordinates:"+latitude+", "+longitude+"\n"+"Uploaded On:"+date+" "+time+"\n"+"crop id:"+id+"\n"+"crop age:"+"2months"+"\n";

        Uri pictureUri = getImageUri(cropComplete.this,image);


        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, mydata);

        shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);

        shareIntent.setType("image/*");

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share file..."));
    }
}