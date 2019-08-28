package com.example.user.rxandroidapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxdeferActivity extends AppCompatActivity {
    private static final String TAG = "RxdeferActivity";
    private Disposable subscribe;
    private Observable<String> observable1, observable2;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Car car = new Car();

        Observable<Integer> observable = Observable.defer(() -> Observable.just(1, 2, 3, 4));
        subscribe = observable.subscribe(
                item -> Log.d(TAG, "onCreate: " + item),
                error -> Log.e(TAG, "onCreate: ", error),
                () -> Log.d(TAG, "onComplete: ")
        );

        observable1 = car.getBrandObservable();
        subscribe = observable1.subscribe(
                brand -> Log.d(TAG, "onCreate: " + brand)
        );

        observable2 = car.getBrandDeferObservable();
        subscribe = observable2.subscribe(
                brand -> Log.d(TAG, "onCreate: " + brand)
        );

        car.brand = "BMW";


        observable1 = car.getBrandObservable();
        subscribe = observable1.subscribe(
                brand -> Log.d(TAG, "onCreate: " + brand)
        );

        observable2 = car.getBrandDeferObservable();
        subscribe = observable2.subscribe(
                brand -> Log.d(TAG, "onCreate: " + brand)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed())
            subscribe.dispose();
    }

    class Car {

        String brand = "DEFAULT";

        Observable<String> getBrandObservable() {
            return Observable.just(brand);
        }

        Observable<String> getBrandDeferObservable() {
            return Observable.defer(
                    () -> Observable.just(brand)
            );
        }
    }
}
