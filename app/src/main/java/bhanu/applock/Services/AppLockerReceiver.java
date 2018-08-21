package bhanu.applock.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppLockerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.bhanupro.applock.LOCKSERVICE")){
            Intent service = new Intent(context, LockService.class);
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(service);
            Log.e("AppLockerReceiver", "Broadcast :Receiver has started.");
        }else {
            Log.e("AppLockerReceiver", "Broadcast :failed to start Receiver.");

        }

    }
}
