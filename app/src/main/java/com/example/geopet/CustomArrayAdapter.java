package com.example.geopet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomArrayAdapter  extends ArrayAdapter<String>{
    ArrayList<String> emails;
    String chatId;
    Context mContext;
    public CustomArrayAdapter(@NonNull Context context,ArrayList<String> emails,String chatId) {
        super(context, R.layout.activity_chat_views_item);
        this.emails = emails;
        this.mContext = context;
        this.chatId = chatId;
    }

    @Override
    public int getCount(){
        return emails.size();
    }
    public String getChatId(){
        return chatId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.activity_chat_views_item,parent,false);
        viewHolder.mFlag = (ImageView) convertView.findViewById(R.id.imageView2);
        viewHolder.mEmail = (TextView) convertView.findViewById(R.id.user_email);
        convertView.setTag(viewHolder);


        viewHolder.mFlag.setImageResource(R.mipmap.ic_launcher_round);
        viewHolder.mEmail.setText(emails.get(position));

        return convertView;
    }
    static class ViewHolder{
        ImageView mFlag;
        TextView mEmail;
    }
}
