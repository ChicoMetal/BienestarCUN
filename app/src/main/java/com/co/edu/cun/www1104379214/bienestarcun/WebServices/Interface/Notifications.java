package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 26/09/2016.
 */

public interface Notifications {

    @FormUrlEncoded
    @POST("getNotifications")//buscar actividades sin vincular
    Call<ResponseContent> getNotifications(@Field("user") String user);

}
