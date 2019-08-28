package com.example.user.rxandroidapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class RxScanvsReduceActivity extends AppCompatActivity {
    private static final String TAG = RxScanvsReduceActivity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = Observable.range(1, 5)
                .scan((integer, integer2) -> integer * 2)
                .subscribeWith(getObserver());

        Observable.range(1, 5)
                .reduce(((integer, integer2) -> integer * 2))
                .subscribe(getSingleObserver());
    }

    private MaybeObserver getSingleObserver() {
        return new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "onSuccess: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    private DisposableObserver getObserver() {
        return new DisposableObserver() {
            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext: " + o);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}