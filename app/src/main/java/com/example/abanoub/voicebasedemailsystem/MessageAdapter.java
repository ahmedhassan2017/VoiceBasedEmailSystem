package com.example.abanoub.voicebasedemailsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ahmed Hassan on 12/4/2017.
 */

public class MessageAdapter extends BaseAdapter {
    ArrayList<NewEmail> emails_list;
    Context context;

    public MessageAdapter(Context context, ArrayList<NewEmail> emails_list) {
        this.context = context;
        this.emails_list = emails_list;
    }

    @Override
    public int getCount() {
        return emails_list.size();
    }

    @Override
    public Object getItem(int position) {
        return emails_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        NewEmail email = emails_list.get(position);

        ((TextView) convertView.findViewById(R.id.sender)).setText(email.sender);
        ((TextView) convertView.findViewById(R.id.date)).setText(email.date);
        ((TextView) convertView.findViewById(R.id.title)).setText(email.title);
        ((TextView) convertView.findViewById(R.id.body)).setText(email.body);
        if (email.isFavorite.equals("yes"))
            ((ImageView) convertView.findViewById(R.id.star)).setImageResource(R.drawable.ic_star_24dp);
        else
            ((ImageView) convertView.findViewById(R.id.star)).setImageResource(R.drawable.ic_star_border_24dp);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        imageView.setImageResource(R.drawable.man2);

        return convertView;
    }
}
