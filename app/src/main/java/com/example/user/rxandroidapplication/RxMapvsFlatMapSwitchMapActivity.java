package com.example.user.rxandroidapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RxMapvsFlatMapSwitchMapActivity extends AppCompatActivity {
    private static final String TAG = "RxActivity";
    private Disposable disposable;

    private static Observable<List<Integer>> getIds() {
        return Observable.just(Arrays.asList(1, 2, 3));
    }

    private static Observable<Item> getItemObservable(Integer id) {
        Item item = new Item();
        item.id = id;
        return Observable.just(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = Observable.just("A", "B", "C")
                .map(alp -> alp + "1")
                .subscribe(
                        val -> Log.d(TAG, "onCreate: " + val),
                        error -> Log.e(TAG, "onCreate: ", error),
                        () -> Log.d(TAG, "onComplete: ")
                );

        disposable = Observable.just("A", "B", "C")
                .flatMap(
                        S -> {
                            return Observable.just(S + "1", S + "2", S + "3");
                        }
                )
                .subscribe(
                        val -> Log.d(TAG, "onCreate: " + val),
                        error -> Log.e(TAG, "onCreate: ", error),
                        () -> Log.d(TAG, "onComplete: ")
                );

        disposable = getIds()
                .flatMapIterable(ids -> ids) // Converts your list of ids into an Observable which emits every item in the list
                .flatMap(id -> getItemObservable(id)) // Calls the method which returns a new Observable<Item>
                .subscribe(
                        item -> Log.d(TAG, "onCreate: " + item.id),
                        error -> Log.e(TAG, "onCreate: ", error),
                        () -> Log.d(TAG, "onComplete: ")
                );


        // This always emits 6 as it un-subscribes the before observer
        disposable = getIntegerObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Exception {
                        return Observable.just(integer).delay(1, TimeUnit.SECONDS);
                    }
                })
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "All users emitted!");
                    }
                });
    }

    private Observable<Integer> getIntegerObservable() {

        return Observable.fromArray(1, 2, 3, 4, 5, 6);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    static class Item {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}