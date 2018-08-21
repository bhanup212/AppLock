package bhanu.applock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import bhanu.applock.Adapter.AppListAdapter;
import bhanu.applock.Helpers.SpacesItemDecoration;
import bhanu.applock.Model.appDetails;
import bhanu.applock.Services.LockService;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    ImageView gifImg;
    PackageManager mPackageManager;
    ArrayList<appDetails> appList = new ArrayList<>();
    AppListAdapter mAdapter;

    TreeSet<String> checkedValue;
    SharedPreferences sharedPref;
    HashSet<String> tempSet,lockedAppList;
    HashSet<String> allApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("bhanupro",0);
        setLayoutBackground();

//        if (!isAccessGranted()) {
//            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//            startActivity(intent);
//        }
        getReferences();
        listAllApps();

        tempSet= new HashSet<String>();
        checkedValue= new TreeSet<String>();
        checkedValue.clear();

        //sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        lockedAppList=(HashSet<String>)sharedPref.getStringSet("LockedApps",tempSet);

        startService(new Intent(this, LockService.class));
        Intent intent = new Intent("com.bhanupro.applock.LOCKSERVICE");
        sendBroadcast(intent);
        //bindService(intent, myConnection, Context.BIND_AUTO_CREATE);


//        Intent intent = new Intent();
//        intent.setAction("com.bhanupro.app.lock.LATEST");
//        sendBroadcast(intent);
    }

    private void setLayoutBackground() {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.color1),
                        ContextCompat.getColor(this, R.color.color2),
                        ContextCompat.getColor(this, R.color.color3),
                        ContextCompat.getColor(this, R.color.color4)});

        findViewById(R.id.main_activity_layout).setBackground(gradientDrawable);
    }

    private void getReferences() {
        //recyclerview reference
        rv = findViewById(R.id.app_list_rv);
        gifImg = findViewById(R.id.main_activity_git);
    }
    private void listAllApps(){
        appList.clear();
        allApps=new HashSet<String>();

        mPackageManager = getPackageManager();
        ArrayList<PackageInfo> tempList = (ArrayList<PackageInfo>) mPackageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo p: tempList){
            if (!isSystemPackage(p)){
                Drawable icon = mPackageManager
                        .getApplicationIcon(p.applicationInfo);
                String name = mPackageManager.getApplicationLabel(
                        p.applicationInfo).toString();
                icon.setBounds(0, 0, 40, 40);

                appList.add(new appDetails(p.packageName,name,icon));
                allApps.add(p.packageName);
            }
        }
        sharedPref.edit().putStringSet("allApps",allApps).apply();
        Collections.sort(appList, new Comparator<appDetails>() {
            @Override
            public int compare(appDetails p1, appDetails p2) {
                // return p1.age+"".compareTo(p2.age+""); //sort by age
                return p1.getAppTitle().compareTo(p2.getAppTitle()); // if you want to short by name
            }
        });
        mAdapter = new AppListAdapter(MainActivity.this,appList,mPackageManager);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rv.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rv.setAdapter(mAdapter);
    }
    private boolean isSystemPackage(PackageInfo info){
        return ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    public void playGif(String appName){
        gifImg.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        gifImg.setVisibility(View.VISIBLE);
        CountTimer timer = new CountTimer(1500,1500);
        timer.start();
        lockApp(appName);
    }

    private void lockApp(String appPackageName) {
        Set<String> lockedApps = sharedPref.getStringSet("LockedApps",new HashSet<String>());

        if (!isAccessGranted()) {
            showAlertDialog();
        }
        boolean isFound = false;

        Iterator r = lockedApps.iterator();
        while (r.hasNext()) {
            String x  = r.next().toString();

            if (x.equals(appPackageName)) {
                r.remove();
                Toast.makeText(this, "UnLocked",Toast.LENGTH_SHORT).show();
                isFound = true;
            }
        }
        /*for(String s:lockedApps){
            if (s.equals(appPackageName)){
              lockedApps.remove(appPackageName);
                Toast.makeText(this, "UnLocked",Toast.LENGTH_SHORT).show();
                isFound = true;
            }
        }*/
        if (!isFound){
            lockedApps.add(appPackageName);
            Toast.makeText(this, "App Locked",Toast.LENGTH_SHORT).show();
        }
        sharedPref.edit().putStringSet("LockedApps",lockedApps).apply();
        Intent intent = new Intent("com.bhanupro.applock.LOCKSERVICE");
        sendBroadcast(intent);
    }

    public class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            gifImg.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out));
            gifImg.setVisibility(View.GONE);
        }
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            }
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }
    public void showAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();
        alertDialog.setTitle("Permission requested");
        alertDialog.setMessage("Please give the android usage access permission from settings");
        alertDialog.setIcon(R.drawable.ic_lock_open_white);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               // if (!isAccessGranted()) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onDestroy() {
//        Log.e("ServiceConnection","disconnected");
//        Intent intent = new Intent("com.bhanupro.applock.LOCKSERVICE");
//        sendBroadcast(intent);
//    }

    private LockService myServiceBinder;
    boolean isBind;
    public ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            myServiceBinder = ((LockService.MyBinder) binder).getService();
            Log.e("ServiceConnection","connected");
            isBind = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e("ServiceConnection","disconnected");
            myServiceBinder = null;
            isBind = false;
        }
    };
}
