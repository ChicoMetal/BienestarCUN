package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 4/10/2016.
 */

public interface AdminCircles {

    @FormUrlEncoded
    @POST("getInscritosCircle")//Buscar usuarios inscritos a la actividad
    Call<ResponseContent> GetUsuariosCircle(@Field("circle") int circle);

    @FormUrlEncoded
    @POST("getCircle")//Buscar circulo al cual esta acargo el docente
    Call<ResponseContent> getCircleDocente(@Field("user") String user);

    @FormUrlEncoded
    @POST("saveListAsitencia")//Guardar la asistencia tomada
    Call<ResponseContent> saveAsistenciaItinerario(@Field("listObject") String listObject);

    @FormUrlEncoded
    @POST("delItinerario")//Guardar la asistencia tomada
    Call<ResponseContent> delItinerario(@Field("idItinerario") int idItinerario );

}
