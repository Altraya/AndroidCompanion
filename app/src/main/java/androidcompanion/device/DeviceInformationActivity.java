package androidcompanion.device;

import android.support.v7.app.AppCompatActivity;

import androidcompanion.netcode.LocalClient;

/**
 * Created by Sakina on 04/04/2017.
 */

public class DeviceInformationActivity extends AppCompatActivity{
    private String deviceIPAdress = "";
    private String devicePort = "";
    private String devicePairingKey = "";

    public DeviceInformationActivity(String deviceIPAdress, String devicePort, String devicePairingKey) {
        this.deviceIPAdress = deviceIPAdress;
        this.devicePort = devicePort;
        this.devicePairingKey = devicePairingKey;
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

    public String getDevicePairingKey() {
        return devicePairingKey;
    }

    public void setDevicePairingKey(String devicePairingKey) {
        this.devicePairingKey = devicePairingKey;
    }

    public String toString(){

        return this.deviceIPAdress + "\n" + this.devicePort;
    }
}
