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

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ChatList;

import java.util.ArrayList;

/**
 * Created by root on 26/10/15.
 */
public class HypedChatAdapter extends RecyclerView.Adapter<HypedChatAdapter.HypedChatViewHolder> {

    ArrayList<ChatList> Chats;
    Constantes mss = new Constantes();
    IconManager icons = new IconManager();
    FragmentManager fragmentManager;

    Context context;
    DBManager DB;

    public HypedChatAdapter(
            Context context,
            DBManager db,
            FragmentManager fragmentManager) {

        this.context = context;
        this.DB = db;
        this.fragmentManager = fragmentManager;
        this.Chats = new ArrayList<>();

    }

    @Override
    public HypedChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context)
                            .inflate(R.layout.item_hyped_chat_pendientes, parent, false);

        return new HypedChatViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(HypedChatViewHolder holder, int position) {

        ChatList CurrentCircle = Chats.get(position);

        holder.setCircleSource(
                CurrentCircle.getIdActiviti() );
    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

    public void AddAll(@NonNull ArrayList<ChatList> chats){
        if( chats == null)
            throw new NullPointerException("The items can not be null");

        this.Chats.addAll(chats);
        notifyItemRangeChanged(getItemCount() - 1, chats.size());
    }

    public class HypedChatViewHolder extends RecyclerView.ViewHolder{

        CardView vistaItem;
        ImageView imgAdmin;
        ImageView imgcard;

        public HypedChatViewHolder(final View itemView) {
            super(itemView);

            View view;
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {//evento click de las cardView
                @Override
                public void onClick(View v) {//evento de touch para agregar usuario a circulo

                // item clicked
                new ChatPsicologiaManager(context, DB).OpenChatPsicologia(Long.parseLong(String.valueOf(v.getId())),
                                                                            fragmentManager);

                }
            });

            //instancio componentes de las card
            vistaItem = (CardView) itemView;
            imgAdmin = (ImageView) itemView.findViewById( R.id.img_pacientPsicologia);
            imgcard = (ImageView) itemView.findViewById( R.id.img_chatPendientes);


        }

        public void setCircleSource(String idActiviti) {

            //asigno los valores a los compnentes de las card
            icons.SetIconCards(imgcard, imgAdmin);
            //TODO: cambiar la forma en la que se entrega el ID del remitente, ya que no se debe dejar un int sino un Long por el largo de las cedulas
            vistaItem.setId(Integer.parseInt(idActiviti));
        }



    }
}
