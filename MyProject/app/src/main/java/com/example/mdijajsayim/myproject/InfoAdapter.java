package com.example.mdijajsayim.myproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md Ijaj Sayim on 01-Jan-17.
 */

public class InfoAdapter extends ArrayAdapter {

    ArrayList<University> objects;
    Context ctx;

    public InfoAdapter(Context context, int resource, ArrayList<University> objects) {
        super(context, resource, objects);
        this.objects=objects;
        this.ctx=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.info_row, null);
        }

        final University i=objects.get(position);

        if(i!=null){
            ImageView icon=(ImageView)v.findViewById(R.id.icon);
            final TextView  university_name=(TextView)v.findViewById(R.id.varsity_name);
            final Button link=(Button) v.findViewById(R.id.link);
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri=Uri.parse(i.getLink());
                        Intent intent=new Intent();
                        intent.setData(uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        ctx.startActivity(intent);
                    }
                });
                university_name.setText(i.getName());
                Picasso.with(ctx).load(i.getIcon()).fit().centerCrop().into(icon);
           // rating.setText(String.valueOf(i.getRating()));
        }


        return v;
    }
}
