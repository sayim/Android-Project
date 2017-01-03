package com.example.mdijajsayim.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RateActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Spinner mSpinner;
    private RatingBar mRatingBar;
    private EditText mDesc;
    private Button mPostBtn;
    private University subUniversity;
    private  Users mUser;
    private String mUserProfilePic;
    private ProgressDialog mProgress;

    private ArrayList<String >  mUniversityList=new ArrayList<String>();
    private ArrayList<University> mSaveList=new ArrayList<University>();

    private DatabaseReference mRef;
    private  DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Map<String,String> mMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rate Universities");

        mImageView =(ImageView)findViewById(R.id.universityIcon);
        mSpinner=(Spinner)findViewById(R.id.spinner);
        mRatingBar=(RatingBar)findViewById(R.id.ratingBar);
        mDesc=(EditText)findViewById(R.id.description);
        mPostBtn=(Button)findViewById(R.id.postBtn);
        mRef= FirebaseDatabase.getInstance().getReference().child("University");
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        mProgress=new ProgressDialog(this);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(RateActivity.this, android.R.layout.simple_list_item_1,mUniversityList);

        mSpinner.setAdapter(arrayAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                University university=dataSnapshot.getValue(University.class);

                mSaveList.add(university);

                mMap.put(university.getName(),university.getIcon());

                mUniversityList.add(university.getName());

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Picasso.with(getApplicationContext()).load(mMap.get(adapterView.getItemAtPosition(i).toString())).fit().centerCrop().into(mImageView);
                subUniversity=mSaveList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserProfilePic=dataSnapshot.child("UserProfilePicture").child(mAuth.getCurrentUser().getUid()).getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mDataFef=FirebaseDatabase.getInstance().getReference();
        mDataFef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser=dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).getValue(Users.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Posting...");
                mProgress.show();
                double rating=(double)mRatingBar.getRating();
                String desc=mDesc.getText().toString().trim();
                Post subPost=new Post(mUser,subUniversity,rating,desc,mUserProfilePic);
                DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference().child("Post").push();
                mDatabase.setValue(subPost);
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(),"Posted",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(RateActivity.this,MainActivity.class));
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        //startActivity(new Intent(RateActivity.this,MainActivity.class));
        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
            //startActivity(new Intent(this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
