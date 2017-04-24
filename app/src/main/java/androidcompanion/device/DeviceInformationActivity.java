package androidcompanion.device;

import android.support.v7.app.AppCompatActivity;
/**
 * Created by Sakina on 04/04/2017.
 */

public class DeviceInformationActivity extends AppCompatActivity{
    private String deviceId = "";
    private String deviceName = "";

    public DeviceInformationActivity(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String toString(){

        return this.deviceName;
    }
}
