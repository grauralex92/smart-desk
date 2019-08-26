package com.endava.smartdesk.networking;

import com.endava.smartdesk.model.GuestUserData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitServiceApi {

    @GET("retrieveUserData")
    Observable<GuestUserData> retrieveUserData(@Query("") String registrationToken);

    @POST("sendAuthenticationCode")
    Observable<Object> sendAuthenticationCode(@Body GuestUserData guestUserData);
}
