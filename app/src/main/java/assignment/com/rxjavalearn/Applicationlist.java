package assignment.com.rxjavalearn;

import java.util.List;

/**
 * Created by anudeep on 30/05/17.
 */

class Applicationlist {
    private static final Applicationlist ourInstance = new Applicationlist();

    static Applicationlist getInstance() {
        return ourInstance;
    }

    private Applicationlist() {
    }
    List<AppInfo> appInfos;

    public List<AppInfo> getList() {
        return appInfos;
    }

    public void setList(List<AppInfo> list) {
        appInfos  =list;
    }

}
