package androidcompanion.device.settings;

import android.util.JsonWriter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;

public class DeviceSettingsManager {
    // keys are device Ids
    private HashMap<String, DeviceSetting> allDevicesSettings;
    private String deviceSettingsFileName = "device_settings.json";

    public DeviceSettingsManager() {
        this.allDevicesSettings = loadDeviceSettings();
    }

    public DeviceSetting getDefaultDeviceSettings() {
        return allDevicesSettings.get("default");
    }

    public DeviceSetting getDeviceSettings(String deviceId) {
        return allDevicesSettings.get(deviceId);
    }

    /**
     * Loads all device settings from JSON file stored on phone.
     */
    private HashMap<String, DeviceSetting> loadDeviceSettings(){
        String allDevicesSettings = SystemManager.getInstance().getSaveManager().loadJSONFromAsset(deviceSettingsFileName);
        HashMap<String, DeviceSetting> ds = new HashMap<String, DeviceSetting>();

        try {
            JSONObject json = new JSONObject(allDevicesSettings);
            for(Iterator<String> it = json.keys(); it.hasNext();){
                String key = it.next();
                ds.put(key, new DeviceSetting(json.get(key).toString(), key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Persists all devices settings to a JSON file.
     * @return true if the file has been persisted, false otherwise
     */
    public Boolean persistAllSettings(){
        try {
            File JSONFile = new File(MyApp.getInstance().getFilesDir(), deviceSettingsFileName);
            OutputStream out = new FileOutputStream(JSONFile);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            for(String key : allDevicesSettings.keySet()){
                writer.name(key).value(allDevicesSettings.get(key).toJSON().toString());
            }
            writer.endObject();
            writer.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Adds a new device setting with default settings and persist it
     */
    public void createDeviceSetting(String deviceId){
        allDevicesSettings.put(deviceId, new DeviceSetting(getDefaultDeviceSettings()));
        persistAllSettings();
    }

    public void removeDeviceSetting(String deviceId){
        allDevicesSettings.remove(deviceId);
        persistAllSettings();
    }
}
