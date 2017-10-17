package androidcompanion.notifications;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;

/**
 * Created by dmarck on 13/10/2017.
 */

public class BatteryStateJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = MyApp.getContext().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        //int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        //float batteryPct = level / (float)scale;
        float batteryPct = level;
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;;
        for (LocalClient localClient : SystemManager.getInstance().getClientManager().getClients()) {
            SystemManager.getInstance().getNotifyFactory().notifyBattery(localClient,batteryPct, isCharging);
        }
        jobFinished(params, false);
        // Here we return false because the job was short and considered as finished.
        // Otherwise (longer job, job still running in thread, etc...), return true
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
