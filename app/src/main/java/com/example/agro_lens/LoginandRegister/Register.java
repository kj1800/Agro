package com.example.agro_lens.LoginandRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agro_lens.Dashboard;
import com.example.agro_lens.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText emailiduser,passworduser,nameuser;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailiduser=findViewById(R.id.registermail);
        mAuth = FirebaseAuth.getInstance();
        passworduser=findViewById(R.id.registerpassword);
        nameuser=findViewById(R.id.registername);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering user.......");
    }

    public void Register(View view) {
        String Email=emailiduser.getText().toString().trim();
        String Password=passworduser.getText().toString().trim();
        String name=nameuser.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            emailiduser.setError("Invalid Email");
            emailiduser.setFocusable(true);
        }
        else if(passworduser.length()<6){
            passworduser.setError("Password length should be 6 characters");
            passworduser.setFocusable(true);

        }
        else if(TextUtils.isEmpty(nameuser.getText().toString())){
            nameuser.setError("User Name is must");

        }
        else {
            registerUser(Email,Password,name);
        }
    }

    private void registerUser(String email, String password, final String name) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user=mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                            String Email_id=user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",Email_id);
                            hashMap.put("uid",uid);
                            hashMap.put("name",name);
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("cover","");
                            hashMap.put("languages","");

                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("Users");
                            reference.child(uid).child("userdetails").setValue(hashMap);

                            startActivity(new Intent(Register.this, Dashboard.class));
                            finish();
                        }
                        else {
                            progressDialog.dismiss();

                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void AlreadyLogin(View view) {
        startActivity(new Intent(Register.this,Login.class));
        finish();
    }
}