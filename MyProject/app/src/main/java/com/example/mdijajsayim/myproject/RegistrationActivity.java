package com.example.mdijajsayim.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText emailText,mUserName,mUserUniversityNmae;
    private EditText passwordText;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private Users mSetUser;
    private  FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        buttonRegister=(Button)findViewById(R.id.signupButton);
        emailText=(EditText)findViewById(R.id.EditTextEmail);
        mUserName=(EditText)findViewById(R.id.userName);
        mUserUniversityNmae=(EditText)findViewById(R.id.universityName) ;
        passwordText=(EditText)findViewById(R.id.EditTextPass);
        buttonRegister.setOnClickListener(this);
        emailText.setOnClickListener(this);
        passwordText.setOnClickListener(this);
        mProgress=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            //satrt home here...
            finish();
            //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        //startActivity(new Intent(this,LoginActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if(view==buttonRegister){
            registerUser();
        }
        if(view==emailText){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
        }
        if(view==passwordText){
            Toast.makeText(this,"Password should contain at leat 6 characters",Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {
        String email=emailText.getText().toString().trim();
        String pass=passwordText.getText().toString().trim();
        final String userName=mUserName.getText().toString().trim();
        final String universityName=mUserUniversityNmae.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter your email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_LONG).show();
            return;
        }
        mProgress.setMessage("Registering user...");
        mProgress.show();

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            mSetUser= new Users(userName,universityName,mAuth.getCurrentUser().getUid());
                            mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(mSetUser);
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration successful",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(RegistrationActivity.this,ProfileActivity.class));
                        }
                        else {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Could not register , please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
