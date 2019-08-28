package com.example.user.rxandroidapplication.network.service;

import com.example.user.rxandroidapplication.network.model.ActivateUserRequest;
import com.example.user.rxandroidapplication.network.model.AuthenticateUserRequest;
import com.example.user.rxandroidapplication.network.model.RegisterUserRequest;
import com.example.user.rxandroidapplication.network.model.User;

import io.reactivex.Single;

public interface UserAPIService {

    Single<User> registerUser(RegisterUserRequest request);

    Single<User> authenticate(AuthenticateUserRequest request);

    Single<Void> activateUser(ActivateUserRequest request);

}
