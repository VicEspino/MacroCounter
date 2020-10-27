package com.example.macrocounter.UI.Login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.macrocounter.UI.model.User;

public class LoginViewModel extends ViewModel {

    MutableLiveData<User> logingUserStats;

    public LoginViewModel() {

        this.logingUserStats = new MutableLiveData<User>();
    }

    public MutableLiveData<User> getLogingUserStats() {
        return logingUserStats;
    }
}
