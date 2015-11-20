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
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ItinerarioList;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by root on 26/10/15.
 */
public class HypedItinerarioAdapter extends RecyclerView.Adapter<HypedItinerarioAdapter.HypedItinerariosViewHolder> {

    ArrayList<ItinerarioList> itinerario;
    CodMessajes mss = new CodMessajes();
    IconManager icons = new IconManager();
    int idCircle;

    Context context;


    public HypedItinerarioAdapter( Context context) {

        this.context = context;
        this.itinerario = new ArrayList<>();

    }

    @Override
    public HypedItinerariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context)
                            .inflate(R.layout.item_hyped_itinerarios, parent, false);

        return new HypedItinerariosViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(HypedItinerariosViewHolder holder, int position) {

        ItinerarioList CurrentItinerario = itinerario.get(position);

        holder.setItinerarioSource(
                CurrentItinerario.getIdItinerario(),
                CurrentItinerario.getNameItinerario(),
                CurrentItinerario.getDateItinerario(),
                CurrentItinerario.getStatusItinerario(),
                CurrentItinerario.getDetailsItinerario());
        }

    @Override
    public int getItemCount() {
        return itinerario.size();
    }

    public void AddAll(@NonNull ArrayList<ItinerarioList> itinerario){
        if( itinerario == null)
            throw new NullPointerException("The items can not be null");

        this.itinerario.addAll(itinerario);
        notifyItemRangeChanged(getItemCount() - 1, itinerario.size());
    }

    public class HypedItinerariosViewHolder extends RecyclerView.ViewHolder{

        CardView vistaItem;
        TextView itinerarioName;
        TextView itinerarioDate;
        TextView itinerarioStatus;
        TextView ItinerarioDescription;
        ImageView imgcard;



        public HypedItinerariosViewHolder(View itemView) {
            super(itemView);

            //instancio componentes de las card
            vistaItem = (CardView) itemView;
            imgcard = (ImageView) itemView.findViewById( R.id.img_itinerario);
            itinerarioName = (TextView) itemView.findViewById(R.id.txt_nameActiviti);
            itinerarioDate = (TextView) itemView.findViewById(R.id.txt_fechaActiviti);
            itinerarioStatus = (TextView) itemView.findViewById(R.id.txt_statusActiviti);
            ItinerarioDescription = (TextView) itemView.findViewById(R.id.txt_description);

        }

        public void setItinerarioSource(String idItinerario,
                                        String name,
                                        String date,
                                        String status,
                                        String details) {

            //asigno los valores a los compnentes de las card
            icons.SetIconCardsItinerarios(imgcard);
            vistaItem.setId(Integer.parseInt(idItinerario));
            itinerarioName.setText(name);
            itinerarioDate.setText(date);
            itinerarioStatus.setText( msmStatusItinerario( status ) );
            ItinerarioDescription.setText(details);

        }

        private String msmStatusItinerario(String codigo){
            try {
                return mss.msmServices.getString( codigo );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

    }
}