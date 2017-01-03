package com.example.mdijajsayim.myproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.mdijajsayim.myproject.R.id.universityIcon;

/**
 * Created by Md Ijaj Sayim on 31-Dec-16.
 */

public class ItemAdapter extends ArrayAdapter {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<CommentItem> objects;

    private Context ctx;

    public ItemAdapter(Context context, int resource, ArrayList<CommentItem>  objects) {
        super(context, resource, objects);
        this.ctx=context;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.commentrow, null);
        }

        CommentItem i=objects.get(position);
        if(i!=null){
            ImageView userImg=(ImageView)v.findViewById(R.id.userImg);
            TextView userNm=(TextView)v.findViewById(R.id.userName_comment);
            TextView userCom=(TextView)v.findViewById(R.id.user_comment);

            if(userNm!=null){
                userNm.setText(i.getUserName());
            }
            if(userCom!=null){
                userCom.setText(i.getUserComment());
            }
            if(userImg!=null){
                Picasso.with(ctx).load(i.getUserImage()).fit().centerCrop().into(userImg);
            }


        }
        return v;
    }
}
