package androidcompanion.main;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidcompanion.device.DeviceListingActivity;
import androidcompanion.netcode.LocalClient;

/**
 * Created by dmarck on 12/05/2017.
 */

/**
 * Own Application class
 * Use of the singleton design pattern
 */
// TODO disconnect devices if app killed?
public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // We copy the assets to the external storage
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        boolean isFirstLaunch = settings.getBoolean("FIRST_RUN", false);
        if (!isFirstLaunch) {
            // do the thing for the first time
            // here we copy the assets to the external storage
            SystemManager.getInstance().getSaveManager().copyAssets();
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time the app loads
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
                    newClient.connect();
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
