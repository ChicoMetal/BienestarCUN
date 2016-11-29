package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Satellite on 4/10/2016.
 */

public interface AdminCircles {

    @FormUrlEncoded
    @POST("getInscritosCircle")//Buscar usuarios inscritos a la actividad
    Call<ResponseContent> GetUsuariosCircle(@Field("user") String user,
                                            @Field("token") String token,
                                            @Field("circle") int circle);

    @FormUrlEncoded
    @POST("getCircle")//Buscar circulo al cual esta acargo el docente
    Call<ResponseContent> getCircleDocente(@Field("user") String user,
                                           @Field("token") String token);

    @FormUrlEncoded
    @POST("saveListAsitencia")//Guardar la asistencia tomada
    Call<ResponseContent> saveAsistenciaItinerario(@Field("user") String user,
                                                   @Field("token") String token,
                                                   @Field("listObject") String listObject);

    @FormUrlEncoded
    @POST("delItinerario")//Guardar la asistencia tomada
    Call<ResponseContent> delItinerario(@Field("user") String user,
                                        @Field("token") String token,
                                        @Field("idItinerario") int idItinerario );

    @Multipart
    @POST("SaveEvidencia")//Subir imagen al servidor
    Call<ResponseContent> SaveEvidencia( @Part("itinerario") RequestBody itinerario,
                                         @Part("user") RequestBody user,
                                         @Part("token") RequestBody token,
                                         @Part MultipartBody.Part file );

}
