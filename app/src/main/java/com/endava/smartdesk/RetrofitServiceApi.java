package com.endava.smartdesk;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitServiceApi {

    @GET("retrieveUserData")
    Observable<UserData> retrieveUserData(@Query("") String registrationToken);

    @POST("sendAuthenticationCode")
    Observable<Object> sendAuthenticationCode(@Body UserData userData);
}
