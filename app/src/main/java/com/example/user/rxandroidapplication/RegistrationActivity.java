package com.example.user.rxandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.user.rxandroidapplication.fragment.SigninFragment;
import com.example.user.rxandroidapplication.utils.DataUtils;

public class RegistrationActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fragmentManager = getSupportFragmentManager();
        if (TextUtils.isEmpty(DataUtils.getToken(this))) {
            if (getIntent().hasExtra("active")) {
                //nothing to be done.
            } else if (getIntent().hasExtra("complete")) {
                //nothing to be done.
            } else {
                replaceFragment(SigninFragment.newInstance(null, null));
            }
        } else {
            startActivity(new Intent(this, RxMapvsFlatMapSwitchMapActivity.class));
            finish();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.register_container, fragment);
        fragmentTransaction.commit();
    }
}