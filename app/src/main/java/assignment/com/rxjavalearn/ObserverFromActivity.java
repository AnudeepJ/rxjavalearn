package assignment.com.rxjavalearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class ObserverFromActivity extends AppCompatActivity {

    private static final String TAG = ObserverFromActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer_from);

        List<String> alphabets = new ArrayList<>();
        alphabets.add("D");
        alphabets.add("F");
        alphabets.add("C");
        alphabets.add("B");
        alphabets.add("E");
        alphabets.add("A");

        Observable<String> stringObservable = Observable.from(alphabets);


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


        stringObservable.toSortedList().subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                Log.d(TAG, strings.toString());
            }
        });





    }
}



/*

 */