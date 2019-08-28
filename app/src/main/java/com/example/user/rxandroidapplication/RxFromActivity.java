package com.example.user.rxandroidapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxFromActivity extends AppCompatActivity {
    private static final String TAG = "RxFromActivity";
    private Disposable subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
//        Observable<Integer> observable = Observable.from(list);

        Integer[] array = new Integer[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 10;
        }

        Observable<Integer> observable = Observable.fromArray(array);
        subscribe = observable.subscribe(
                item -> Log.d(TAG, "onCreate: " + item),
                error -> error.printStackTrace(),
                () -> Log.d(TAG, "completed: ")
        );


        /*
        Observable.fromCallable() allows us to delay the creation of a value to be emitted by an
        Observable.This is handy when the value you want to emit from your Observable needs to be
        created off of the UI thread.*/

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
//                return mRestClient.getFavoriteTvShows();
                return "Hello World!!";
            }
        };

        Observable<String> observable1 = Observable.fromCallable(callable);
        observable1.subscribe(
                item -> Log.d(TAG, "onCreate: " + item),
                error -> Log.e(TAG, "onCreate: ", error),
                () -> Log.d(TAG, "completed: ")
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed())
            subscribe.dispose();
    }
}