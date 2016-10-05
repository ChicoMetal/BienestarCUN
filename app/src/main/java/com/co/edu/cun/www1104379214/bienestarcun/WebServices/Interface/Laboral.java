package com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Satellite on 4/10/2016.
 */

public interface Laboral {

    @FormUrlEncoded
    @POST("saveNewLaboralHistory")//Guardar un nuevo historial laboral del egresado
    Call<ResponseContent> saveNewLaboral(@Field("user") String user,
                                        @Field("empresa") String empresa,
                                        @Field("cargo") String cargo,
                                        @Field("dateStart") String dateStart,
                                        @Field("dateEnd") String dateEnd);

    @FormUrlEncoded
    @POST("updateStatusLaboral")//Actualizar el estado laboral del egresado
    Call<ResponseContent> UpdateStatusLaboral(@Field("user") String user,
                                                @Field("status") String status);

}
