package com.co.edu.cun.www1104379214.bienestarcun.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;


import java.util.ArrayList;

/**
 * Created by root on 26/10/15.
 */
public class HypedActivitiesAdapter extends RecyclerView.Adapter<HypedActivitiesAdapter.HypedActivitiesViewHolder> {

    ArrayList<CircleList> activities;
    CodMessajes mss = new CodMessajes();

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

        CircleList CurrentCircle = activities.get(position);

        holder.setArtistsName(
                CurrentCircle.getIdActiviti(),
                CurrentCircle.getNameActiviti(),
                CurrentCircle.getDescriptionActiviti(),
                CurrentCircle.getAdminActiviti() );
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

        CardView vistaItem;
        TextView CircleName;
        TextView CircleDescription;
        TextView CircleAdmin;
        ImageView imgAdmin;



        public HypedActivitiesViewHolder(View itemView) {
            super(itemView);

            View view;
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {//evento click de las cardView
                @Override
                public void onClick(View v) {
                    // item clicked
                    Log.i(mss.TAG, v.getId()+"");
                    v.getId();
                }
            });

            //instancio componentes de las card
            vistaItem = (CardView) itemView;
            imgAdmin = (ImageView) itemView.findViewById( R.id.img_person);
            CircleName = (TextView) itemView.findViewById(R.id.txt_nameCircle);
            CircleDescription = (TextView) itemView.findViewById(R.id.txt_description);
            CircleAdmin = (TextView) itemView.findViewById(R.id.txt_administrator);

        }

        public void setArtistsName(String idActiviti, String name, String description, String admin) {
            //asigno los valores a los compnentes de las card


            vistaItem.setId( Integer.parseInt( idActiviti ) );
            imgAdmin.setImageResource(R.drawable.icon_login);
            CircleName.setText(name);
            CircleDescription.setText(description);
            CircleAdmin.setText(admin);

        }



    }
}
