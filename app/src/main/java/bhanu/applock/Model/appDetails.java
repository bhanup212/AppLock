package bhanu.applock.Model;

import android.graphics.drawable.Drawable;

public class appDetails {
    String packageName,appTitle;
    Drawable appIcon;

    public appDetails(String packageName, String appTitle, Drawable appIcon) {
        this.packageName = packageName;
        this.appTitle = appTitle;
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
