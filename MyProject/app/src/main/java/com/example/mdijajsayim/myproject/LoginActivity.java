package com.example.mdijajsayim.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginBtn;
    private EditText emailText;
    private EditText passText;
    private  Button createNew;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loginBtn=(Button)findViewById(R.id.signinButton);
        createNew=(Button)findViewById(R.id.createNewAccount);
        emailText=(EditText)findViewById(R.id.TextEmail);
        passText=(EditText)findViewById(R.id.TextPass);

        loginBtn.setOnClickListener(this);
        createNew.setOnClickListener(this);
        mProgress=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            //satrt home here...

            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

    }

    @Override
    public void onClick(View view) {

        if(view==loginBtn){
            userLogin();
        }
        if(view==createNew){
            Intent createnewaccoutnIntent =new Intent(this,RegistrationActivity.class);
            //finish();
            startActivity(createnewaccoutnIntent);
        }
    }

    private void userLogin() {

        String email=emailText.getText().toString().trim();
        String pass=passText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter your email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_LONG).show();
            return;
        }
        mProgress.setMessage("User logging in...");
        mProgress.show();

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mProgress.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Log in failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
