package androidcompanion.device.settings;

import android.util.Log;

import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import androidcompanion.main.MyApp;

/**
 * Created by scrip on 29/09/17.
 */

public class DeviceSetting {
    private String deviceId;
    private boolean sendCallNotifications;
    private boolean sendSmsNotifications;
    private boolean sendOtherNotifications;
    private boolean receiveCallNotifications;
    private boolean receiveSmsNotifications;

    public boolean getSendCallNotifications() {
        return sendCallNotifications;
    }

    public void setSendCallNotifications(boolean sendCallNotifications) {
        this.sendCallNotifications = sendCallNotifications;
    }

    public boolean getSendSmsNotifications() {
        return sendSmsNotifications;
    }

    public void setSendSmsNotifications(boolean sendSmsNotifications) {
        this.sendSmsNotifications = sendSmsNotifications;
    }

    public boolean getSendOtherNotifications() {
        return sendOtherNotifications;
    }

    public void setSendOtherNotifications(boolean sendOtherNotifications) {
        this.sendOtherNotifications = sendOtherNotifications;
    }

    public boolean getReceiveCallNotifications() {
        return receiveCallNotifications;
    }

    public void setReceiveCallNotifications(boolean receiveCallNotifications) {
        this.receiveCallNotifications = receiveCallNotifications;
    }

    public boolean getReceiveSmsNotifications() {
        return receiveSmsNotifications;
    }

    public void setReceiveSmsNotifications(boolean receiveSmsNotifications) {
        this.receiveSmsNotifications = receiveSmsNotifications;
    }

    public DeviceSetting(String jsonData, String deviceId){
        this.deviceId = deviceId;
        try {
            JSONObject data = new JSONObject(jsonData);
            sendCallNotifications = data.getBoolean("sendCallNotifications");
            sendSmsNotifications = data.getBoolean("sendSmsNotifications");
            sendOtherNotifications = data.getBoolean("sendOtherNotifications");
            receiveCallNotifications = data.getBoolean("receiveCallNotifications");
            receiveSmsNotifications = data.getBoolean("receiveSmsNotifications");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public DeviceSetting(DeviceSetting s){
        this.sendCallNotifications = s.getSendCallNotifications();
        this.sendSmsNotifications = s.getSendSmsNotifications();
        this.sendOtherNotifications =  s.getSendOtherNotifications();
        this.receiveCallNotifications = s.getReceiveCallNotifications();
        this.receiveSmsNotifications = s.getReceiveSmsNotifications();
    }

    public JSONObject toJSON(){
        JSONObject json  = new JSONObject();
        try {
            json.put("sendCallNotifications", sendCallNotifications);
            json.put("sendSmsNotifications", sendSmsNotifications);
            json.put("sendOtherNotifications", sendOtherNotifications);
            json.put("receiveCallNotifications", receiveCallNotifications);
            json.put("receiveSmsNotifications", receiveSmsNotifications);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
