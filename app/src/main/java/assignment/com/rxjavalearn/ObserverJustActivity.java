package assignment.com.rxjavalearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;

public class ObserverJustActivity extends AppCompatActivity {

    private static final String TAG = ObserverJustActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer_just);

        Observable<Integer> integerObservable = Observable.just(2, 2, 3);


        integerObservable.subscribe(new Observer<Integer>() {
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

        Observable<String> stringObservable = Observable.just(sayHello(),"Ananth");
        stringObservable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);

            }
        });


    }


    public String sayHello() {
        return "Hello";
    }
}
