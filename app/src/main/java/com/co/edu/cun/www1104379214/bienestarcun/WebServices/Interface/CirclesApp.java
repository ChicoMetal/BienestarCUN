package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 23/09/2016.
 */

public interface CirclesApp {

    @FormUrlEncoded
    @POST("GetCirclesExists")//buscar actividades sin vincular
    Call<ResponseContent> getActivities(@Field("user") String user,
                                        @Field("token") String token);

    @FormUrlEncoded
    @POST("SearchCircleAdd")//buscar actividades vinculadas
    Call<ResponseContent> getActivitiesAdd(@Field("user") String user,
                                           @Field("token") String token);

    @FormUrlEncoded
    @POST("saveAddCircle")//vincular actividad
    Call<ResponseContent> SaveActivityUser(@Field("user") String user,
                                           @Field("token") String token,
                                           @Field("circle") int circle);

    @FormUrlEncoded
    @POST("DeleteCircleUser")//desvincular actividad
    Call<ResponseContent> DeleteActivityUser(@Field("user") String user,
                                             @Field("token") String token,
                                             @Field("circle") int circle);

    @FormUrlEncoded
    @POST("GetItinerariosCircle")//buscar actividades vinculadas
    Call<ResponseContent> getItinerariosActivity(@Field("user") String user,
                                                 @Field("token") String token,
                                                 @Field("circle") int circle);

}
