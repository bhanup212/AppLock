package bhanu.applock.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import bhanu.applock.Activities.PinActivity;
import bhanu.applock.MainActivity;
import bhanu.applock.R;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class LockService extends Service {
    PackageManager packageManager;
    SharedPreferences sharedPref;
    Handler handler;
    int delay = 1000;

    String cAppName;
    boolean isUnlocked = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    public  class MyBinder extends Binder{
        public LockService getService(){
            return LockService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();

        runBackground();
    }
    protected void runBackground(){
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                scanApps();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runBackground();

        String CHANNEL_ONE_ID = "com.applock.bhanupro";
        String CHANNEL_ONE_NAME = "Applock Service Running";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("App lock is running")
                    .setContentText("")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true).build();

            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            startForeground(101,
                    notification);
        }



        return START_STICKY;
    }
    protected void scanApps() {

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

                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), PinActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        String last = sharedPref.getString("lastApp","none");

                        if (!isUnlocked){
                                startActivity(i);
                                isUnlocked = true;
                        }

                       /* if (!last.equalsIgnoreCase(appPackage)){
                            Log.e("AppLockerService", "App name is supposed to be locked " + appTitle);
                            i.putExtra("lastApp",appPackage);
                            startActivity(i);
                        }*/


                    }else {
                        if (!appPackage.equals(getPackageName())){
                            isUnlocked = false;
                        }

                    }
                }

                Log.e("AppLockerService", "App name is" + appTitle);
                //Toast.makeText(this,appTitle,Toast.LENGTH_SHORT).show();

    }

    private String printForegroundTask() {
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager)this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
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
        cAppName = currentApp;
        return currentApp;
    }

//    @Override
//    public void onDestroy() {
//        Log.e("AppLockerService", "service destroyed ");
//        Intent intent = new Intent("com.bhanupro.applock.LOCKSERVICE");
//        sendBroadcast(intent);
//    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.e("AppLockerService", "service destroyed ");
        Intent intent = new Intent("com.bhanupro.applock.LOCKSERVICE");
        sendBroadcast(intent);

//        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
//            Intent intent2 = new Intent();
//            intent2.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
//            startActivity(intent2);
//        }
    }

    @Override
    public void onDestroy() {
        Log.e("AppLocker destroy", "service destroyed ");
        Intent intent = new Intent("com.bhanupro.applock.LOCKSERVICE");
        sendBroadcast(intent);
    }
}
