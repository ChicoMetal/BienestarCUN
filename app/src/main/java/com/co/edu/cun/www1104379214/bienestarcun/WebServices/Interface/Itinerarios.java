package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 29/09/2016.
 */

public interface Itinerarios {

    @FormUrlEncoded
    @POST("saveNewItinerario")//Guardar un nuevo itinerario
    Call<ResponseContent> SaveNewItinerario(@Field("user") String user,
                                            @Field("token") String token,
                                            @Field("nameActiviti") String nameActiviti,
                                            @Field("detailActivitie") String detailActivitie,
                                            @Field("date") String date,
                                            @Field("hour") String hour);

}
