package androidcompanion.device;

import android.support.v7.app.AppCompatActivity;
/**
 * Created by Sakina on 04/04/2017.
 */

public class DeviceInformationActivity extends AppCompatActivity{
    private String deviceIPAdress = "";
    private String devicePort = "";

    public DeviceInformationActivity(String deviceIPAdress, String devicePort) {
        this.deviceIPAdress = deviceIPAdress;
        this.devicePort = devicePort;
    }

    public String getDeviceIPAdress() {
        return deviceIPAdress;
    }

    public void setDeviceIPAdress(String deviceIPAdress) {
        this.deviceIPAdress = deviceIPAdress;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    public String toString(){

        return this.deviceIPAdress + "\n" + this.devicePort;
    }
}
