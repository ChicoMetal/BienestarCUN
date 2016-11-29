package com.co.edu.cun.www1104379214.bienestarcun.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView contentCC;

        public HypedChatViewHolder(final View itemView) {
            super(itemView);

            View view;
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {//evento click de las cardView
                @Override
                public void onClick(View v) {//evento de touch para agregar usuario a circulo

                    TextView contentcc = (TextView) v.findViewById( R.id.ContentCC );//ontener cedula

                    // item clicked
                    new ChatPsicologiaManager(context, DB).
                            OpenChatPsicologia(Long.parseLong( contentcc.getText().toString() ),
                                    fragmentManager);

                }
            });

            //instancio componentes de las card
            vistaItem = (CardView) itemView;
            imgAdmin = (ImageView) itemView.findViewById( R.id.img_pacientPsicologia);
            imgcard = (ImageView) itemView.findViewById( R.id.img_chatPendientes);
            contentCC = (TextView) itemView.findViewById( R.id.ContentCC);


        }

        public void setCircleSource(String idActiviti) {

            String hexColor = String.format("#%06X",
                    (0xFFFFFF & Long.parseLong(idActiviti) ));//generar color a partir de cc
            int color = Color.parseColor( hexColor );//generar recurso de color

            //asigno los valores a los compnentes de las card
            icons.SetIconCards(imgcard, imgAdmin);
            imgAdmin.setBackgroundColor( color );//asignar color

            contentCC.setText( idActiviti );//guardar la cedula en la carta, para evitar problemas con cedulas largas
            vistaItem.setId( getIntCC(  idActiviti ) );//aseguro que el id sea un entero
        }

        //<editor-fold desc="Devolver un id valido para la card a partir de la identificacion">
        private int getIntCC( String cc ){
            String intCc;

            if( cc.length() > 10 )
                intCc = cc.substring(0,9);
            else
                intCc = cc;

            int ccInt = Integer.parseInt( intCc );

            return ccInt;
        }
        //</editor-fold>


    }
}
