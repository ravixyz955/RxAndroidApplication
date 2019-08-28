package com.example.user.rxandroidapplication.network.service;

import com.example.user.rxandroidapplication.network.RemoteServerAPI;
import com.example.user.rxandroidapplication.network.model.ActivateUserRequest;
import com.example.user.rxandroidapplication.network.model.AuthenticateUserRequest;
import com.example.user.rxandroidapplication.network.model.RegisterUserRequest;
import com.example.user.rxandroidapplication.network.model.User;

import io.reactivex.Single;

public class UserAPIServiceImpl implements UserAPIService {

    private final RemoteServerAPI remoteServerAPI;

    public UserAPIServiceImpl(RemoteServerAPI remoteServerAPI) {
        this.remoteServerAPI = remoteServerAPI;
    }

    @Override
    public Single<User> registerUser(RegisterUserRequest request) {
        return remoteServerAPI.registerUser(request);
    }

    @Override
    public Single<User> authenticate(AuthenticateUserRequest request) {
        return remoteServerAPI.authenticate(request);
    }

    @Override
    public Single<Void> activateUser(final ActivateUserRequest request) {
        return remoteServerAPI.activateUser(request);
    }
}
