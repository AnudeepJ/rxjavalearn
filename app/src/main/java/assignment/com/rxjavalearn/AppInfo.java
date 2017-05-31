package assignment.com.rxjavalearn;

import android.support.annotation.NonNull;

/**
 * Created by anudeep on 24/05/17.
 */

public class AppInfo implements Comparable<Object> {

    public long mLastUpdatedTime;
    public String mName;
    public String mIcon;


    public AppInfo(String name, String icon, long lastUpdateTime) {
        mName = name;
        mIcon = icon;
        mLastUpdatedTime = lastUpdateTime;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        AppInfo appInfo = (AppInfo) o;
        return mName.compareTo(appInfo.mName);
    }
}
