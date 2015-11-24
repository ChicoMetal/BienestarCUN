package com.co.edu.cun.www1104379214.bienestarcun.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.CirclesManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;


import java.util.ArrayList;

/**
 * Created by root on 26/10/15.
 */
public class HypedActivitiesAdapter extends RecyclerView.Adapter<HypedActivitiesAdapter.HypedActivitiesViewHolder> {

    ArrayList<CircleList> activities;
    CodMessajes mss = new CodMessajes();
    IconManager icons = new IconManager();

    FragmentManager fragmentManager;

    Context context;
    DBManager DB;
    int INSTANCE;

    public HypedActivitiesAdapter(
                                Context context,
                                DBManager db,
                                int instance,
                                FragmentManager fragmentManager) {

        this.context = context;
        if( fragmentManager != null ){
            this.fragmentManager = fragmentManager;
        }
        this.DB = db;
        this.INSTANCE = instance;

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

        holder.setCircleSource(
                CurrentCircle.getIdActiviti(),
                CurrentCircle.getNameActiviti(),
                CurrentCircle.getDescriptionActiviti(),
                CurrentCircle.getAdminActiviti());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public void AddAll(@NonNull ArrayList<CircleList> circles){
        if( circles == null)
            throw new NullPointerException("The items can not be null");

        this.activities.addAll(circles);
        notifyItemRangeChanged(getItemCount() - 1, circles.size());
    }

    public class HypedActivitiesViewHolder extends RecyclerView.ViewHolder{

        CardView vistaItem;
        TextView CircleName;
        TextView CircleDescription;
        TextView CircleAdmin;
        ImageView imgAdmin;
        ImageView imgcard;



        public HypedActivitiesViewHolder(View itemView) {
            super(itemView);

            View view;
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {//evento click de las cardView
                @Override
                public void onClick(View v) {//evento de touch para agregar usuario a circulo
                    // item clicked
                    if (INSTANCE == 0) {//pregunto q fragento invoca el adaptador para saber que accion realizar al touch de las card
                        new CirclesManager(context, DB).SaveCircleUser(v.getId());

                    } else if (INSTANCE == 1) {//Desvincularcirculo
                        new CirclesManager(context, DB).DeleteCircleUser(v.getId());
                    } else {//ver itnerarios
                        new CirclesManager(context, DB).ShowItinerariosCircle(v.getId(), fragmentManager);
                    }
                }
            });

            //instancio componentes de las card
            vistaItem = (CardView) itemView;
            imgAdmin = (ImageView) itemView.findViewById( R.id.img_admin);
            imgcard = (ImageView) itemView.findViewById( R.id.img_activities);
            CircleName = (TextView) itemView.findViewById(R.id.txt_nameCircle);
            CircleDescription = (TextView) itemView.findViewById(R.id.txt_description);
            CircleAdmin = (TextView) itemView.findViewById(R.id.txt_administrator);

        }

        public void setCircleSource(String idActiviti, String name, String description, String admin) {

            //asigno los valores a los compnentes de las card
            icons.SetIconCards(imgcard, imgAdmin);
            vistaItem.setId(Integer.parseInt(idActiviti));
            CircleName.setText(name);
            CircleDescription.setText(description);
            CircleAdmin.setText(admin);

        }



    }
}
