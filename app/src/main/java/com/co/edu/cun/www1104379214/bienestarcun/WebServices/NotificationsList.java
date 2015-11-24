package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 23/11/15.
 */
public class NotificationsList {

    CodMessajes mss = new CodMessajes();
    private String id_notifications;
    private String date_notification;
    private String circle_notification;
    private String details_notification;
    private String status;

    public NotificationsList(String resulObject, JSONObject index) {

        setearObjext(resulObject, index);

    }

    private void setearObjext( String objectString, JSONObject index ){
        //extraigo los valores del objeto para asignarlo a las variables globales

        try {

            JSONObject object = new JSONObject( objectString );

            this.id_notifications = object.getString( index.getString("0") );
            this.date_notification = object.getString( index.getString("1")  );
            this.details_notification = object.getString( index.getString("2") );
            this.status = mss.msmServices.getString( object.getString( index.getString("3") ) );
            this.circle_notification = object.getString( index.getString("4") );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getId_notifications() {
        return this.id_notifications;
    }

    public String getDate_notification() {
        return this.date_notification;
    }

    public String getCircle_notification() {
        return this.circle_notification;
    }

    public String getDetails_notification() {
        return this.details_notification;
    }

    public String getStatus() {
        return this.status;
    }
}
