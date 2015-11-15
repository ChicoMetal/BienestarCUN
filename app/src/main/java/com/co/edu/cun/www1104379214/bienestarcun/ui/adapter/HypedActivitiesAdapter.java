package com.co.edu.cun.www1104379214.bienestarcun.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;


import java.util.ArrayList;

/**
 * Created by root on 26/10/15.
 */
public class HypedActivitiesAdapter extends RecyclerView.Adapter<HypedActivitiesAdapter.HypedActivitiesViewHolder> {

    ArrayList<CircleList> activities;

    Context context;

    public HypedActivitiesAdapter(Context context) {

        this.context = context;

        this.activities = new ArrayList<>();

    }

    @Override
    public HypedActivitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context)
                            .inflate(R.layout.item_hyped_activities, parent, false);
        return new HypedActivitiesViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(HypedActivitiesViewHolder holder, int position) {

        CircleList CurrentArtists = activities.get(position);
        holder.setArtistsName(CurrentArtists.getName());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public void AddAll(@NonNull ArrayList<CircleList> artists){
        if( artists == null)
            throw new NullPointerException("The items can not be null");

        this.activities.addAll(artists);
        notifyItemRangeChanged( getItemCount() - 1, artists.size());
    }

    public class HypedActivitiesViewHolder extends RecyclerView.ViewHolder{

        TextView artistsName;
        ImageView imgArtist;


        public HypedActivitiesViewHolder(View itemView) {
            super(itemView);

            imgArtist = (ImageView) itemView.findViewById(R.id.img_artist);
            artistsName = (TextView) itemView.findViewById(R.id.txt_name);

        }

        public void setArtistsName(String name) {
            artistsName.setText(name);
        }

    }
}
