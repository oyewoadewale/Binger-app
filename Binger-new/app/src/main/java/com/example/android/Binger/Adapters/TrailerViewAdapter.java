package com.example.android.Binger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.Binger.Objects.Youtube;
import com.example.android.Binger.R;

import java.util.ArrayList;


/**
 * Created by Adewale on 22/11/2018.
 */

public class TrailerViewAdapter extends ArrayAdapter<Youtube> {

    public TrailerViewAdapter(Context context, ArrayList<Youtube> keys) {
        super(context, 0, keys);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Youtube k = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_view, parent, false);
        }

        final TextView l = convertView.findViewById(R.id.trailer);

        if (k.getKey().length() == 0){
            l.setVisibility(View.GONE);
        }

        l.setText("Trailer " + (getPosition(k)+1));
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(getContext(), k.getKey());
            }
        });
        return convertView;
    }

    public static void sendIntent(Context context, String id){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id)));
        } catch (Exception e){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id)));
        }
    }
}