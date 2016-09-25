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
    @POST("GetCirclesExists")
    Call<ResponseContent> getActivities(@Field("user") String user);

}
