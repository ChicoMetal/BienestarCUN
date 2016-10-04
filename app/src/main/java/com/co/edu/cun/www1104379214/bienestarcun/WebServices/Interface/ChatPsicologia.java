package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 3/10/2016.
 */

public interface ChatPsicologia {

    @FormUrlEncoded
    @POST("getChatPendientes")//Buscar chats pendientes
    Call<ResponseContent> getChatsPendientes(@Field("tipuser") String tipuser);

    @FormUrlEncoded
    @POST("getMensajes")//Buscar chats pendientes
    Call<ResponseContent> getMensajesPendientes( @Field("remitente") String remitente,
                                                 @Field("receptor") String receptor );

    @FormUrlEncoded
    @POST("getUserPsicologo")//Buscar chats pendientes
    Call<ResponseContent> getPsicologiaUser( @Field("usuario") Long usuario  );

    @FormUrlEncoded
    @POST("UpdateStatusMessage")//Buscar chats pendientes
    Call<ResponseContent> setMensajesPendientes( @Field("remitente") Long remitente,
                                                 @Field("receptor") Long receptor );

}
