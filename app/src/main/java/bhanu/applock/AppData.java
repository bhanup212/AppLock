package bhanu.applock;

import android.app.Application;
import android.content.pm.PackageInfo;

public class AppData extends Application {
    PackageInfo mPackageInfo;
    public void setPackageInfo(PackageInfo info){
        mPackageInfo = info;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }
}
