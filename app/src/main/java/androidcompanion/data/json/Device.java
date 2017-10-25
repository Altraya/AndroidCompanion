package androidcompanion.data.json;

import java.util.ArrayList;

import androidcompanion.notifications.json.JsonObject;

/**
 * Created by Jo on 25/10/2017.
 */

public class Device extends JsonObject{

    /////

    private ArrayList<String> bannedPackages;

    private boolean notificationAllowed;

    /////

    private String ip;

    private String port;

    private String key;

    public Device(ArrayList<String> bannedPackages, boolean notificationAllowed, String ip, String port, String key) {
        this.bannedPackages = bannedPackages;
        this.notificationAllowed = notificationAllowed;
        this.ip = ip;
        this.port = port;
        this.key = key;
    }

    public ArrayList<String> getBannedPackages() {
        return bannedPackages;
    }

    public void setBannedPackages(ArrayList<String> bannedPackages) {
        this.bannedPackages = bannedPackages;
    }

    public boolean isNotificationAllowed() {
        return notificationAllowed;
    }

    public void setNotificationAllowed(boolean notificationAllowed) {
        this.notificationAllowed = notificationAllowed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
