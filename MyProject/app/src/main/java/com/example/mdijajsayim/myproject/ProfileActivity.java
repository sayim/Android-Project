package com.example.mdijajsayim.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.*;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int FINISH_REQ = 11;
    private  TextView emailText, nameTextShow,UniversityText;
    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private ImageView mImageView;
    private Users mGetUser;
    private ProgressDialog mProgress;
    private FirebaseUser user;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private Uri imageUri =null,downloadUri;

    private  static final int GALLERY_REQUEST =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        emailText=(TextView)findViewById(R.id.emailShow);
        nameTextShow=(TextView)findViewById(R.id.NameText);
        UniversityText=(TextView)findViewById(R.id.universityNameText);
        mProgress=new ProgressDialog(this);

        logoutBtn=(Button)findViewById(R.id.logout);
        mImageView=(ImageView)findViewById(R.id.imageShow);


        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mStorage= FirebaseStorage.getInstance().getReference();
        user=mAuth.getCurrentUser();

        logoutBtn.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }



        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mGetUser=  dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).getValue(Users.class);
                        FirebaseUser user=mAuth.getCurrentUser();
                        emailText.setText(user.getEmail());
                        nameTextShow.setText(mGetUser.getName());
                        UniversityText.setText(mGetUser.getUniversityName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String proPic=dataSnapshot.child("UserProfilePicture").child(mAuth.getCurrentUser().getUid()).getValue(String.class);
                        if(proPic!=null) {
                            Picasso.with(ProfileActivity.this).load(proPic).fit().centerCrop().into(mImageView);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Add a profile picture",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        thread1.start();
        thread2.start();



        /*mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGetUser=  dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).getValue(Users.class);
                FirebaseUser user=mAuth.getCurrentUser();
                emailText.setText(user.getEmail());
                nameTextShow.setText(mGetUser.getName());
                UniversityText.setText(mGetUser.getUniversityName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        /*mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String proPic=dataSnapshot.child("UserProfilePicture").child(mAuth.getCurrentUser().getUid()).getValue(String.class);
                if(proPic!=null) {
                    Picasso.with(ProfileActivity.this).load(proPic).fit().centerCrop().into(mImageView);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Add a profile picture",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(mAuth.getCurrentUser()!=null) {
                finish();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
            else finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view==logoutBtn){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(view==mImageView){

            setImage();

        }
    }

    private void setImage() {

        Intent galleryIntent =new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode ==RESULT_OK ){
            imageUri=data.getData();
            mImageView.setImageURI(imageUri);
            mProgress.setMessage("Saving...");
            mProgress.show();
            if(imageUri!=null){
                StorageReference filePath=mStorage.child(mAuth.getCurrentUser().getUid()).child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                        downloadUri=taskSnapshot.getDownloadUrl();
                        mDatabase.child("UserProfilePicture").child(mAuth.getCurrentUser().getUid()).setValue(downloadUri.toString());
                    }
                });
            }
        }
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if(user==null) {
            //finish();
            finishActivity(FINISH_REQ);
        }
        else
        {
            //finish();
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        }
    }*/

}
