package bhanu.applock.Model;

public class AppModel {
    String appName,packageName,versionName,targetVersion,path,firstInstall,lastModified,features,permissions;

    public AppModel(String appName, String packageName, String versionName, String targetVersion, String path, String firstInstall, String lastModified, String feature, String permission) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionName = versionName;
        this.targetVersion = targetVersion;
        this.path = path;
        this.firstInstall = firstInstall;
        this.lastModified = lastModified;
        features = feature;
        permissions = permission;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public String getPath() {
        return path;
    }

    public String getFirstInstall() {
        return firstInstall;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getFeature() {
        return features;
    }

    public String getPermission() {
        return permissions;
    }
}
