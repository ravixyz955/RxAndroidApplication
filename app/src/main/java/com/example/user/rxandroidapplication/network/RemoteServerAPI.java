package com.example.user.rxandroidapplication.network;

import com.example.user.rxandroidapplication.network.model.ActivateUserRequest;
import com.example.user.rxandroidapplication.network.model.AuthenticateUserRequest;
import com.example.user.rxandroidapplication.network.model.RegisterUserRequest;
import com.example.user.rxandroidapplication.network.model.User;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemoteServerAPI {

    String BASE_CONTEXT = "/api";

    @POST(BASE_CONTEXT + "/register")
    Single<User> registerUser(@Body RegisterUserRequest request);

    @POST(BASE_CONTEXT + "/v1/auth/authenticate")
    Single<User> authenticate(@Body AuthenticateUserRequest request);

    @POST("/activate")
    Single<Void> activateUser(@Body ActivateUserRequest request);
}