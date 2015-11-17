package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.co.edu.cun.www1104379214.bienestarcun.R;


/**
 * Created by root on 12/11/15.
 */
public class IconManager {

    public IconManager() {
    }

    public void SetIconMenu( NavigationView menu ){//Agregar iconos al navigationview

        menu.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.icon_home);
        menu.getMenu().findItem(R.id.nav_add_activities).setIcon(R.drawable.icon_actividades);
        menu.getMenu().findItem(R.id.nav_del_activities).setIcon(R.drawable.icon_actividades);
        menu.getMenu().findItem(R.id.nav_itinerarios).setIcon(R.drawable.icon_itinerario);
        menu.getMenu().findItem(R.id.nav_add_desertion).setIcon(R.drawable.icon_desercion);
        menu.getMenu().findItem(R.id.nav_add_laboral).setIcon(R.drawable.icon_laboralhystorial);
        menu.getMenu().findItem(R.id.nav_update_laboral_state).setIcon(R.drawable.icon_updatestatuslaboral);
        menu.getMenu().findItem(R.id.nav_show_notifications).setIcon(R.drawable.icon_notificaciones);
        menu.getMenu().findItem(R.id.nav_new_chat).setIcon(R.drawable.icon_chat);
        menu.getMenu().findItem(R.id.nav_new_itinerario).setIcon(R.drawable.icon_newitinerario);
        menu.getMenu().findItem(R.id.nav_asistencia).setIcon(R.drawable.icon_asistencia);
        menu.getMenu().findItem(R.id.nav_evidencias).setIcon(R.drawable.icon_evidencias);
        menu.getMenu().findItem(R.id.nav_login).setIcon(R.drawable.icon_login);
        menu.getMenu().findItem(R.id.nav_logout).setIcon(R.drawable.icon_logout);

    }

    public void SetIconCards( ImageView fondo, ImageView admin){

        fondo.setBackgroundResource( R.drawable.cardfondocircles);
        admin.setBackgroundResource( R.drawable.admincircle);

    }

}
