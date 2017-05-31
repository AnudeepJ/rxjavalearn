package assignment.com.rxjavalearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

public class ObserverCreateActivity extends AppCompatActivity {

    private static final String TAG = ObserverCreateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer_create);

        Observable<Integer> integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.d(TAG, "inside call");

                for (int i = 0; i < 5; i++) {
                    if (!subscriber.isUnsubscribed())
                        subscriber.onNext(i);
                }
                if (!subscriber.isUnsubscribed())
                    subscriber.onCompleted();

            }
        });

        Subscription subscription = integerObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

                Log.d(TAG, String.valueOf(integer));

            }
        });

        subscription.unsubscribe();


    }
}
