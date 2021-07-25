package com.example.geopet;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>{
    OnItemListener onItemListener;
    Context context;
    ArrayList<String> emails;
    //int[] images;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mEmail;
        ImageView mImage;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull @NotNull View itemView,OnItemListener onItemListener) {

            super(itemView);
            mEmail = itemView.findViewById(R.id.user_email);
            mImage = itemView.findViewById(R.id.imageView2);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListener.OnItemClick(getAdapterPosition());
        }
    }
    public ChatsAdapter(Context context,ArrayList<String> emails,OnItemListener onItemListener){
        this.context = context;
        this.emails = emails;
        this.onItemListener = onItemListener;

    }
    @NonNull
    @NotNull
    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_chat_views_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,onItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatsAdapter.ViewHolder holder, int position) {
        holder.mEmail.setText(emails.get(position));
        holder.mImage.setImageResource(R.drawable.ic_baseline_person_24);
    }

    @Override
    public int getItemCount() {

        return emails.size();
    }
    public interface OnItemListener{
        void OnItemClick(int position);

    }
}
