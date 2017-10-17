package androidcompanion.main;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidcompanion.netcode.LocalClient;
import androidcompanion.notifications.BatteryStateJobService;

/**
 * Created by dmarck on 12/05/2017.
 * Class to get context of the application exerywhere
 *
 * Own Application class
 * Use of the singleton design pattern
 */
// TODO disconnect devices if app killed?
public class MyApp extends Application {
    private static MyApp instance = null;

    private JobScheduler jobScheduler;

    public static MyApp getInstance() {

        if(instance == null){
            instance = new MyApp();
        }

        return instance;
    }

    public static Context getContext(){
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // We copy the assets to the external storage
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        boolean isFirstLaunch = settings.getBoolean("FIRST_RUN",true);
        if (isFirstLaunch) {
            // do the thing for the first time
            // here we copy the assets to the external storage
            SystemManager.getInstance().getSaveManager().copyAssets();
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", false);
            editor.commit();
        } else {
            // other time the app loads
        }

        // JobScheduler object that will be used to schedule our job(s)
        jobScheduler = (JobScheduler) getSystemService(getContext().JOB_SCHEDULER_SERVICE);
        // We create job info for a BatteryStateJobService
        ComponentName serviceComponent = new ComponentName(getContext(), BatteryStateJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setPeriodic(10000);
        //builder.setMinimumLatency(10000); // wait at least before start
        //builder.setOverrideDeadline(60000); // maximum delay before start
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require any network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setPersisted(true); // we want the service to still exist after the device has been rebooted
        JobInfo jobInfo = builder.build();
        // We schedule the job
        if(jobScheduler.schedule(jobInfo) <= 0)
        {
            // if something goes wrong
            jobScheduler.cancelAll();
        }

        try
        {
            // reloading of the LocalClients list && connection
            JSONObject jsonObj = new JSONObject(SystemManager.getInstance().getSaveManager().loadJSONFromAsset("device_list.json"));

            if(!jsonObj.isNull("devices"))
            {
                JSONArray devices = jsonObj.getJSONArray("devices");
                for(int i = 0; i < devices.length(); i++)
                {
                    JSONObject device = devices.getJSONObject(i);
                    String deviceIPAdress = device.getString("ip_adress");
                    String devicePort = device.getString("port");
                    String devicePairingKey = device.getString("pairing_key");
                    // Connection to the device using the infos previously provided
                    LocalClient newClient = SystemManager.getInstance().getClientManager().addClient(deviceIPAdress,Integer.parseInt(devicePort),Integer.parseInt(devicePairingKey));
                    // effective connection to the client (socket)
                    if(newClient != null){
                        newClient.connect();
                    }
                    //Toast.makeText(getApplicationContext(),"Device successfully connected!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
