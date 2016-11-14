package com.demo.maat.hello_rxjava;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.maat.hello_rxjava.common.logger.Log;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class PollingFragment extends Fragment {

    static final String TAG = "PollingFragment";
    @BindView(R.id.btn_polling)
    Button mBtnPolling;

    int N = 0;
    private Subscription subscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.polling_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_polling)
    public void onClick() {

        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                subscriber.onNext(" " + (N++));
                subscriber.onCompleted();
            }
        })
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(1, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        printLog("polling" + s);
                    }
                });
    }

    private void printLog(String s) {
        Log.i(TAG, s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "subscribe.unsubscribe()");
        subscription.unsubscribe();
    }

}
