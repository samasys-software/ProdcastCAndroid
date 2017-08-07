package com.ventruxinformatics.prodcast.connect;

import com.ventruxinformatics.prodcast.domain.LoginDTO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by sarathan732 on 8/5/2017.
 */

public interface ProdcastService {
    @POST("prodcast/customer/login")
    Call<LoginDTO> login(@Path("userid") String loginUser, @Path("password") String password , @Path("country") String country);
}
