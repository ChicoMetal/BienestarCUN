package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 29/09/2016.
 */

public interface ReporteDesercion {

    @FormUrlEncoded
    @POST("saveDesertion")//Guardar el reporte de posible desercion
    Call<ResponseContent> SendReporte(@Field("user") String user,
                                      @Field("facultad") String facultad,
                                      @Field("desertor") String desertor,
                                      @Field("descripcion") String descripcion,
                                      @Field("horario") String horario);

    @POST("getFacultades")//Obtener facultades
    Call<ResponseContent> getFacultades();

}
