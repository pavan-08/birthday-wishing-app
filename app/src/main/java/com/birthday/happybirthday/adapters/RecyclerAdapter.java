package com.birthday.happybirthday.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birthday.happybirthday.CustomDrawerLayout;
import com.birthday.happybirthday.Entity.wish;
import com.birthday.happybirthday.R;
import com.birthday.happybirthday.RoundedImageView;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pavan on 10/25/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    public Activity activity;
    private Picasso picasso;
    private String baseLink = "http://bdaymsg.esy.es/DNE/images/";
    private ArrayList<wish> wishes=new ArrayList<wish>();
    public RecyclerAdapter(Activity activity){
        this.activity = activity;
        mLayoutInflater=LayoutInflater.from(activity);
    }
    public void setItems(ArrayList<wish> wishes){
        this.wishes = wishes;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.nav_recycler_layout,parent,false);
        ViewHolder holder=new ViewHolder(item);
        picasso = new Picasso.Builder(activity)
                .memoryCache(new LruCache(24000))
                .build();
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(wishes.get(position).getName());
        picasso.load(baseLink+wishes.get(position).getSrNo()+".jpg").resize(50, 50).centerCrop().placeholder(R.drawable.def).error(R.drawable.def).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return wishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RoundedImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nameTextView);
            imageView=(RoundedImageView)itemView.findViewById(R.id.imgRecycler);
        }
    }
}
