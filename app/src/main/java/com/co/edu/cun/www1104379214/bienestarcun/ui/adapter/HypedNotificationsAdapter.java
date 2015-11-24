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
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.NotificationsList;

import java.util.ArrayList;

/**
 * Created by root on 26/10/15.
 */
public class HypedNotificationsAdapter extends RecyclerView.Adapter<HypedNotificationsAdapter.HypedNotificationsViewHolder> {

    ArrayList<NotificationsList> notifications;
    CodMessajes mss = new CodMessajes();
    IconManager icons = new IconManager();

    FragmentManager fragmentManager;

    Context context;
    DBManager DB;

    public HypedNotificationsAdapter(
            Context context,
            DBManager db) {

        this.context = context;
        this.DB = db;
        this.notifications = new ArrayList<>();

    }

    @Override
    public HypedNotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context)
                            .inflate(R.layout.item_hyped_notifications, parent, false);

        return new HypedNotificationsViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(HypedNotificationsViewHolder holder, int position) {

        NotificationsList CurrentNotification = notifications.get(position);

        holder.setNotificationSource(
                CurrentNotification.getId_notifications(),
                CurrentNotification.getDate_notification(),
                CurrentNotification.getCircle_notification(),
                CurrentNotification.getStatus(),
                CurrentNotification.getDetails_notification()
        );

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void AddAll(@NonNull ArrayList<NotificationsList> notifications){
        if( notifications == null)
            throw new NullPointerException("The items can not be null");

        this.notifications.addAll(notifications);
        notifyItemRangeChanged(getItemCount() - 1, notifications.size());
    }

    public class HypedNotificationsViewHolder extends RecyclerView.ViewHolder{

        CardView vistaItem;
        TextView dateNotification;
        TextView circleNotification;
        TextView statusNotification;
        TextView detailNotification;
        ImageView imgcard;



        public HypedNotificationsViewHolder(View itemView) {
            super(itemView);

            View view;
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {//evento click de las cardView
                @Override
                public void onClick(View v) {//evento de touch para agregar usuario a circulo
                    // item clicked
                    //new CirclesManager(context, DB).SaveCircleUser(v.getId());

                }
            });

            //instancio componentes de las card
            vistaItem = (CardView) itemView;
            imgcard = (ImageView) itemView.findViewById( R.id.img_activities);
            dateNotification = (TextView) itemView.findViewById(R.id.txt_date_notification);
            circleNotification = (TextView) itemView.findViewById(R.id.txt_circle_notification);
            statusNotification = (TextView) itemView.findViewById(R.id.txt_status_notification);
            detailNotification = (TextView) itemView.findViewById(R.id.txt_description_notification);

        }

        public void setNotificationSource(String idNotification1,
                                          String dateNotification1,
                                          String circleNotification1,
                                          String statusNotifications1,
                                          String detailNotification1) {

            //asigno los valores a los compnentes de las card
            vistaItem.setId(Integer.parseInt(idNotification1));
            dateNotification.setText(dateNotification1);
            circleNotification.setText(circleNotification1);
            statusNotification.setText(statusNotifications1);
            detailNotification.setText(detailNotification1);

        }



    }
}
