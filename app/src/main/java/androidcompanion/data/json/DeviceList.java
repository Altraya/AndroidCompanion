package androidcompanion.data.json;

import java.util.ArrayList;

import androidcompanion.notifications.json.JsonObject;

/**
 * Created by Jo on 25/10/2017.
 */

public class DeviceList extends JsonObject{

    ArrayList<Device> deviceList;

    public DeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }
}
