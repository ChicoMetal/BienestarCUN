package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 26/09/2016.
 */

public interface Users {

    @FormUrlEncoded
    @POST("login")//Iniciar sesion
    Call<ResponseContent> LoginUser(@Field("user") String user, @Field("password") String password);

    @FormUrlEncoded
    @POST("logout")//Cerrar sesion
    Call<ResponseContent> LogoutUser(@Field("user") String user, @Field("token") String token);

    @FormUrlEncoded
    @POST("log_save")//Guarda sesion
    Call<ResponseContent> SaveLog(@Field("id_user") String id_user, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("getNameUser")//Obtener nombre de usuario
    Call<ResponseContent> getUserName( @Field("user") String user );


}
