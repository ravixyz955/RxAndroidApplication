package com.example.user.rxandroidapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxConcatVsMergeActivity extends AppCompatActivity {
    private static final String TAG = "RxConcatVsMergeActivity";
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable<String> observable1 = Observable.just("A1", "A2", "A3");
//        Observable<String> observable1 = Observable.just("B1", "B2", "B3");
        Observable<Integer> observable2 = Observable.just(1, 2, 3);

        disposable = Observable.concat(observable1, observable2)
                .subscribe(
                        val -> Log.d(TAG, "onCreate: " + val),
                        error -> Log.e(TAG, "onCreate: ", error),
                        () -> Log.d(TAG, "onComplete: ")
                );

        disposable = Observable.merge(observable1, observable2)
                .subscribe(
                        val -> Log.d(TAG, "onCreate: " + val),
                        error -> Log.e(TAG, "onCreate: ", error),
                        () -> Log.d(TAG, "onComplete: "));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}