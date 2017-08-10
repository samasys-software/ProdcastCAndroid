package com.ventruxinformatics.prodcast.connect;

import com.ventruxinformatics.prodcast.domain.CustomerLoginDTO;
import com.ventruxinformatics.prodcast.domain.LoginDTO;

import businessObjects.FormDataLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by sarathan732 on 8/5/2017.
 */

public interface ProdcastService {
    @POST("prodcast/customer/login")
    @FormUrlEncoded
    Call<CustomerLoginDTO> login(@Field("userid") String userId, @Field("password") String password, @Field("country") String country);
}
