package com.example.user.rxandroidapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.rxandroidapplication.R;
import com.example.user.rxandroidapplication.RxMapvsFlatMapSwitchMapActivity;
import com.example.user.rxandroidapplication.SignUpActivity;
import com.example.user.rxandroidapplication.network.model.AuthenticateUserRequest;
import com.example.user.rxandroidapplication.network.model.User;
import com.example.user.rxandroidapplication.network.service.UserAPIService;
import com.example.user.rxandroidapplication.utils.DataUtils;
import com.example.user.rxandroidapplication.utils.NetworkUtils;
import com.example.user.rxandroidapplication.utils.Utils;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SigninFragment extends Fragment {

    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";

    @BindView(R.id.email_txt)
    EditText emailText;
    @BindView(R.id.pwd_txt)
    EditText passwordText;
    @BindView(R.id.login_txt)
    ShimmerTextView login_txt;

    @BindView(R.id.signup_txt)
    TextView signup_txt;
    @BindView(R.id.forgot_pwd_txt)
    TextView forgot_pwd_txt;

    private Unbinder unbinder;
    private UserAPIService userAPIService;
    private CompositeDisposable compositeDisposable;

    public SigninFragment() {
        // Required empty public constructor
    }

    public static SigninFragment newInstance(String param1, String param2) {
        return new SigninFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getActivity().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        userAPIService = NetworkUtils.provideUserAPIService(getActivity(), "https://auth.");
        compositeDisposable = new CompositeDisposable();
        Utils.createProgressDialoge(getActivity(), "Please wait...");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_signin, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.login_txt)
    public void login() {
        if (TextUtils.isEmpty(emailText.getText().toString().trim()))
            Utils.showToast(getActivity(), "Email can't be empty!");
        else if (TextUtils.isEmpty(passwordText.getText().toString().trim()))
            Utils.showToast(getActivity(), "Password can't be empty!");
        else if (!emailText.getText().toString().trim().contains("@"))
            Utils.showToast(getActivity(), "Not a valid email");
        else {
            doLogin();
        }
    }

    @OnClick(R.id.signup_txt)
    public void signUp() {
        startActivity(new Intent(getActivity(), SignUpActivity.class));
    }

    private void doLogin() {
        AuthenticateUserRequest request = new AuthenticateUserRequest();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        request.setEmail(email);
        request.setPassword(password);

        if (NetworkUtils.isConnectingToInternet(getActivity())) {
            Utils.showProgress();

            compositeDisposable.add(
                    userAPIService.authenticate(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getDisposableObserver(password)));
        } else {
            Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_LONG).show();
        }

    }

    private DisposableSingleObserver<User> getDisposableObserver(String password) {
        return new DisposableSingleObserver<User>() {
            @Override
            public void onSuccess(User user) {
                DataUtils.saveId(getActivity(), user.getId());
                DataUtils.saveUser(getActivity(), user.toString());
                DataUtils.saveEmail(getActivity(), user.getEmail());
                DataUtils.saveName(getActivity(), user.getFullName());
                DataUtils.saveMobile(getActivity(), user.getMobile());
                DataUtils.savePassword(getActivity(), password);
                DataUtils.setActive(getActivity(), false);
                DataUtils.saveCountryCode(getActivity(), user.getCountryCode());
                DataUtils.saveToken(getActivity(), user.getToken());
                startActivity(new Intent(getActivity(), RxMapvsFlatMapSwitchMapActivity.class));
                getActivity().finish();
                Utils.dissmisProgress();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "Failed to signin!", Toast.LENGTH_LONG).show();
                Utils.dissmisProgress();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        compositeDisposable.dispose();
    }
}