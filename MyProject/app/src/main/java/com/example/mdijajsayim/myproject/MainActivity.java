package com.example.mdijajsayim.myproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static Users currentUser;
    static String mUserProfilePic;
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference refdata;
    private StorageReference mStorage;
    //private  Users currentser;
    //private  String userpropic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            //return;
        }

        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        //currentUser=mAuth.getCurrentUser().getUid();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");

        mDatabase.keepSynced(true);


        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserProfilePic = dataSnapshot.child("UserProfilePicture").child(mAuth.getCurrentUser().getUid()).getValue(String.class);
                //Toast.makeText(getApplicationContext(),mUserProfilePic,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refdata = FirebaseDatabase.getInstance().getReference();

        refdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).getValue(Users.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(getApplicationContext(),mUserProfilePic,Toast.LENGTH_LONG).show();
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
                viewHolder.setUserName(model.getUser());
                viewHolder.setUserProPic(getApplicationContext(), model.getUserProfilePic());
                viewHolder.setUniversity(getApplicationContext(), model.getUniversity());
                viewHolder.setRating(model.getRating());
                viewHolder.setDesc(model.getDescription());
                //viewHolder.setComment(getApplicationContext(), model.getUniqueId(), MainActivity.currentUser, MainActivity.mUserProfilePic, model.getUser());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "Log in first", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
        if (item.getItemId() == R.id.new_rating) {

            startActivity(new Intent(MainActivity.this, RateActivity.class));
        }
        if (item.getItemId() == R.id.action_info) {
            startActivity(new Intent(MainActivity.this, InformationActivity.class));

        }

        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CommentItem commentItem;
        CommentItem postComment;
        String userpropic;
        String comment;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUserName(final Users user) {
            TextView username = (TextView) mView.findViewById(R.id.userNameText);
            username.setText(user.getName());
        }

        public void setUserProPic(Context ctx, String filepath) {
            ImageView mProfile = (ImageView) mView.findViewById(R.id.userImage);
            //ImageView mImg=(ImageView)mView.findViewById(R.id.proImg);
            Picasso.with(ctx).load(filepath).fit().centerCrop().into(mProfile);
            //Picasso.with(ctx).load(filepath).fit().centerCrop().into(mImg);
        }

        public void setUniversity(Context ctx, University university) {
            TextView universityName = (TextView) mView.findViewById(R.id.university);
            ImageView universityIcon = (ImageView) mView.findViewById(R.id.universityIcon);

            universityName.setText(university.getName());
            Picasso.with(ctx).load(university.getIcon()).fit().centerCrop().into(universityIcon);
        }

        public void setRating(double rating) {
            TextView rate = (TextView) mView.findViewById(R.id.rateText);
            rate.setText(String.valueOf(rating));
        }

        public void setDesc(String desc) {
            TextView descText = (TextView) mView.findViewById(R.id.descOfPost);
            descText.setText(desc);
        }

        /*public void setComment(final Context ctx, final String uniqueId, final Users currentUser, final String userProfilePic, final Users user) {
            Button addBtn;
            addBtn = (Button) mView.findViewById(R.id.addComment);


            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText mCommentBtn;
                    final ListView mListView;
                    mCommentBtn = (EditText) mView.findViewById(R.id.commentBtn);
                    mListView = (ListView) mView.findViewById(R.id.commentList);


                    //Toast.makeText(ctx, userProfilePic + "\n", Toast.LENGTH_LONG).show();


                    comment = mCommentBtn.getText().toString().trim();
                    //final FirebaseAuth currentAuth=FirebaseAuth.getInstance();


                    commentItem = new CommentItem(currentUser.getName(), userProfilePic, comment);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("Comment").push().setValue(commentItem);


                    Toast.makeText(ctx,uniqueId,Toast.LENGTH_SHORT).show();


                    DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("Comment");

                   //Toast.makeText(ctx,uniqueId,Toast.LENGTH_SHORT).show();


                    myRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            postComment=dataSnapshot.getValue(CommentItem.class);

                            ArrayList<CommentItem> arrayList=new ArrayList<CommentItem>();

                            ItemAdapter itemAdapter=new ItemAdapter(ctx,R.layout.commentrow,arrayList);

                            mListView.setAdapter(itemAdapter);


                            arrayList.add(postComment);

                            itemAdapter.notifyDataSetChanged();

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








                    //arrayList.add(mCommentBtn.getText().toString().trim());
                    //    ArrayList<CommentItem> commentItems = new ArrayList<CommentItem>();
                    //     ItemAdapter itemAdapter = new ItemAdapter(ctx, R.layout.commentrow, commentItems);
                    //     commentItems.add(commentItem);
                    //     itemAdapter.notifyDataSetChanged();
                    //     mListView.setAdapter(itemAdapter);
                    //arrayAdapter.notifyDataSetChanged();
                    //mListView.setAdapter(arrayAdapter);

                    //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                    //FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                    //databaseReference.child("Comment").child(uniqueId).child(firebaseAuth.getCurrentUser().getUid()).setValue(comment);
                    //arrayList.add(mCommentBtn.getText().toString().trim());
                    //arrayAdapter.notifyDataSetChanged();
                }
            });

        }*/
    }
}
