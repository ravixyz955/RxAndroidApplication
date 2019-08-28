package com.example.user.rxandroidapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxJustActivity extends AppCompatActivity {
    private static final String TAG = "RxJustActivity";
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Observable<String> observable = Observable.just("Hello!!");
        Observable<Object> observable = Observable.just("1", "A", "3.2", "def");

        subscribe = observable.subscribe(item -> Log.d(TAG, "onCreate: " + item),
                error -> error.printStackTrace(),
                () -> Log.d(TAG, "completed! "));

        /*Observer observer = new Observer() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext: " + o);
            }
        };

        observable.subscribe(observer);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed())
            subscribe.dispose();
    }
}
