package bhanu.applock.Services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import bhanu.applock.Activities.PinActivity;
import bhanu.applock.MainActivity;

public class AppLockService extends IntentService {
    PackageManager packageManager;
    SharedPreferences sharedPref;
    HashSet<String> tempSet;
    public AppLockService(String name) {
        super(name);
    }

    public AppLockService() {
        super("AppLockerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("AppLockerService", "onHandleIntent:Service has started.");
        while(true) {
            try {
                Thread.sleep(1000);
                Log.e("AppLockerService", "Polling..");

                //sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
                sharedPref = getSharedPreferences("bhanupro",0);
                HashSet<String> lockedAppList=(HashSet<String>)sharedPref.getStringSet("LockedApps",null);
                HashSet<String>allApps=(HashSet<String>)sharedPref.getStringSet("LockedApps",null);

                packageManager = getPackageManager();
                String appPackage=printForegroundTask();


                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = packageManager.getApplicationInfo(appPackage, 0);
                } catch (final PackageManager.NameNotFoundException e) {}
                final String appTitle = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");


                if(lockedAppList!=null) {
                    if (lockedAppList.contains(appPackage)) {
                        //sharedPref.edit().putString("lastApp", appPackage).apply();
                        Log.e("AppLockerService", "App name is supposed to be locked " + appTitle);
                        Intent i = new Intent();
                        i.setClass(this, PinActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String last = sharedPref.getString("lastApp","none");
                        if (!last.equalsIgnoreCase(appPackage)){

                            i.putExtra("lastApp",appPackage);
                            startActivity(i);
                        }

                    }
                }

                Log.e("AppLockerService", "App name is" + appTitle);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

    }

    private String printForegroundTask() {
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager)this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 500*500, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    Log.e("lollipop", "Current App  is: " + currentApp);

                }
            }
        } else {

            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.e("AppLockerService", "Current App in foreground is: " + currentApp);
        return currentApp;
    }
}
