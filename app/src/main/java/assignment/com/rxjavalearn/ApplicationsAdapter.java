package assignment.com.rxjavalearn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by anudeep on 29/05/17.
 */

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private List<AppInfo> appInfos = new ArrayList<>();


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.appName.setText(appInfos.get(position).mName);

        getBitmap(appInfos.get(position).mIcon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(holder.icon::setImageBitmap);

    }


    public void addItem (AppInfo appInfo ) {
        appInfos.add(appInfo);

    }
    private Observable<Bitmap> getBitmap(String icon) {
        return Observable.create(subscriber -> {
            subscriber.onNext(BitmapFactory.decodeFile(icon));
            subscriber.onCompleted();
        });
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos.clear();
        this.appInfos.addAll(appInfos);
    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView appName;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            appName = (TextView) itemView.findViewById(R.id.name);
            icon = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
