package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        menu.getMenu().findItem(R.id.nav_del_itinerario).setIcon(R.drawable.icon_del_itinerarios);
        menu.getMenu().findItem(R.id.nav_asistencia).setIcon(R.drawable.icon_asistencia);
        menu.getMenu().findItem(R.id.nav_evidencias).setIcon(R.drawable.icon_evidencias);
        menu.getMenu().findItem(R.id.nav_login).setIcon(R.drawable.icon_login);
        menu.getMenu().findItem(R.id.nav_logout).setIcon(R.drawable.icon_logout);

    }

    public void SetIconCards( ImageView fondo, ImageView admin){
    //cambiar fondo cards y image de la foto del admin del circulo
        fondo.setBackgroundResource( R.drawable.cardfondocircles);
        admin.setBackgroundResource( R.drawable.admincircle);
    }

    public void SetBackgroundCards(ImageView fondo ){
    //cambiar la imagen de las card de itinerarios

        fondo.setBackgroundResource( R.drawable.cardfondocircles);

    }


    public void setBackgroundApp(Resources recurso, FrameLayout main_content) {
        if (recurso.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            main_content.setBackgroundResource(R.drawable.fondonew_landascape);
        }else{
            main_content.setBackgroundResource(R.drawable.background);
        }
    }

    public void setBackgroundApp(Resources recurso,RelativeLayout main_content) {
        if (recurso.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            main_content.setBackgroundResource(R.drawable.fondonew_landascape);
        }else{
            main_content.setBackgroundResource(R.drawable.background);
        }

    }

    public void setBackgroundApp(Resources recurso,LinearLayout main_content) {
        if (recurso.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            main_content.setBackgroundResource(R.drawable.fondonew_landascape);
        }else{
            main_content.setBackgroundResource(R.drawable.background);
        }
    }


}
