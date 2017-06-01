package assignment.com.rxjavalearn;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GitHubRepoAdapter adapter = new GitHubRepoAdapter();
    private Subscription subscription;
    private File mFilesDir;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.fragment_first_example_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        findViewById(R.id.from).setOnClickListener(view -> {
            final ApplicationsAdapter applicationsAdapter = new ApplicationsAdapter();

            Observable<AppInfo> appInfoObservable = Observable.from(Applicationlist.getInstance().getList())
//                    .map(appInfo -> {
//                         appInfo.mName = appInfo.mName.toUpperCase();
//
//                        return appInfo;
//                    });
                    .scan((appInfo, appInfo2) -> {
                        if (appInfo.mName.length() > appInfo2.mName.length()) {
                            return appInfo;
                        }
                        return appInfo2;
                    }).distinct();
            //.filter(appInfo -> appInfo.mName.startsWith("B"))
//                    .takeLast(3);
            appInfoObservable.subscribe(new Subscriber<AppInfo>() {

                @Override
                public void onCompleted() {

                    recyclerView.setAdapter(applicationsAdapter);

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(AppInfo appInfo) {

                    applicationsAdapter.addItem(appInfo);
                }
            });


        });

        findViewById(R.id.merge).setOnClickListener(view -> {
            final ApplicationsAdapter applicationsAdapter = new ApplicationsAdapter();
            List<AppInfo> list = Applicationlist.getInstance().getList();
            List<AppInfo> reversedInfoList = Lists.reverse(list);

            Observable<AppInfo> from = Observable.from(list);
            Observable<AppInfo> reverseList = Observable.from(reversedInfoList);
            Observable<AppInfo> merge = Observable.merge(from, reverseList);
            merge.subscribe(new Observer<AppInfo>() {
                @Override
                public void onCompleted() {
                    recyclerView.setAdapter(applicationsAdapter);

                }

                @Override
                public void onError(Throwable e) {
                                                     e.printStackTrace();
                }

                @Override
                public void onNext(AppInfo appInfo) {
                    applicationsAdapter.addItem(appInfo);

                }
            });
        });


        findViewById(R.id.just).setOnClickListener(view -> {
            final ApplicationsAdapter applicationsAdapter = new ApplicationsAdapter();

            List<AppInfo> list = Applicationlist.getInstance().getList();
            AppInfo appInfoOne = list.get(0);
            AppInfo appInfoTwo = list.get(1);
            AppInfo appInfoThree = list.get(2);

            Observable<AppInfo> justAppInfoObservable = Observable.just(appInfoOne, appInfoTwo, appInfoThree).repeat(3).distinct();

            justAppInfoObservable.subscribe(new Subscriber<AppInfo>() {
                @Override
                public void onCompleted() {
                    recyclerView.setAdapter(applicationsAdapter);

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(AppInfo appInfo) {
                    applicationsAdapter.addItem(appInfo);

                }
            });
            Observable<Integer> defferedObservable = Observable.defer(this::getInteger);
            Subscription subscribe = defferedObservable.subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {

                }
            });

        });
        findViewById(R.id.range).setOnClickListener(view -> {
            Observable.range(3, 5).subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {

                    Log.d(TAG, String.valueOf(integer));
                }
            });

            Observable.interval(3, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long aLong) {

                    Log.d(TAG, String.valueOf(aLong));
                }
            });
        });


//        final ListView listView = (ListView) findViewById(R.id.list_view_repos);
//        listView.setAdapter(adapter);
//
//        final EditText editTextUsername = (EditText) findViewById(R.id.edit_text_username);
//        final Button buttonSearch = (Button) findViewById(R.id.button_search);
//        buttonSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String username = editTextUsername.getText().toString();
//                if (!TextUtils.isEmpty(username)) {
//                    getStarredRepos(username);
//                }
//            }
//        });

        recyclerView.setAdapter(new ApplicationsAdapter());
        getFileDir()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    mFilesDir = file;
                    refreshTheList();
                });

    }

    private void refreshTheList() {
        getApps().toSortedList().subscribe(new Observer<List<AppInfo>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "On complete in refreshList");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<AppInfo> appInfos) {
                recyclerView.setVisibility(View.VISIBLE);

                ApplicationsAdapter adapter = (ApplicationsAdapter) recyclerView.getAdapter();
                adapter.setAppInfos(appInfos);
                adapter.notifyDataSetChanged();
                Log.d(TAG, String.valueOf(appInfos.size()));
                Applicationlist.getInstance().setList(appInfos);

            }
        });
    }


    private Observable<Integer> getInteger() {
        return Observable.create(subscriber -> {

            if (subscriber.isUnsubscribed()) {
                return;
            }
            Log.d(TAG, "inside getInteger subscribe");
        });
    }


    private Observable<File> getFileDir() {
        return Observable.create(subscriber -> {
            subscriber.onNext(App.instance.getFilesDir());
            subscriber.onCompleted();
        });
    }


    private Observable<AppInfo> getApps() {

        Observable<AppInfo> appInfoObservable = Observable.create(subscriber -> {

            List<AppInfoRich> apps = new ArrayList<>();
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> infos = getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : infos) {
                apps.add(new AppInfoRich(MainActivity.this, info));
            }

            for (AppInfoRich appInfoRich : apps) {
                Bitmap icon = Utils.drawableToBitmap(appInfoRich.getIcon());
                String name = appInfoRich.getName();
                String iconPath = mFilesDir + "/" + name;
                Utils.storeBitmap(App.instance, icon, name);
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onNext(new AppInfo(name, iconPath, appInfoRich.getLastUpdateTime()));
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        });

        return appInfoObservable;
    }


    public String returnString() {
        return "Hello World";
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }


        super.onDestroy();
    }

    private void getStarredRepos(String username) {
        subscription = GitHubClient.getInstance()
                .getStarredRepos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GitHubRepo>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override
                    public void onNext(List<GitHubRepo> gitHubRepos) {
                        Log.d(TAG, "In onNext()");
                        adapter.setGitHubRepos(gitHubRepos);
                    }
                });

        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });

    }


}
