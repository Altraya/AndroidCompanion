package androidcompanion.data;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import androidcompanion.data.json.Device;
import androidcompanion.data.json.DeviceList;
import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.netcode.ClientManager;
import androidcompanion.netcode.LocalClient;


/**
 * Created by dmarck on 12/05/2017.
 */

public class SaveManager {

    private Gson gson;

    public SaveManager(){

        gson = new Gson();

    }

    /**
     * Fetch data from json file (asset)
     * @param asset_name
     * @return string containing the json data
     */
    public String loadJSONFromAsset(String asset_name) {
        String json = null;
        try {
            File jsonFile = new File(MyApp.getInstance().getFilesDir(), asset_name);
            InputStream is = new FileInputStream(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void loadDevices(){

        String jsonData = loadJSONFromAsset(SystemManager.getInstance().getDeviceFileName());

        if(jsonData==null){
            return;
        }
        if(jsonData.equals("")){
            return;
        }

        DeviceList deviceList = gson.fromJson(jsonData, DeviceList.class);
        if(deviceList != null){
            if(deviceList.getDeviceList()!=null){
                for(int i = 0; i < deviceList.getDeviceList().size(); i++){
                    interpretDevice(deviceList.getDeviceList().get(i));
                }
            }
        }

    }

    private void interpretDevice(Device device){
        LocalClient localClient = SystemManager.getInstance().getClientManager().addClient(device.getIp(),Integer.parseInt(device.getPort()),Integer.parseInt(device.getPort()));
        localClient.getClientSettings().setNotificationAllowed(device.isNotificationAllowed());
        localClient.getClientSettings().setBannedPackages(device.getBannedPackages());
        localClient.connect();
    }

    public void saveDevices(){

        ClientManager clientManager = SystemManager.getInstance().getClientManager();
        ArrayList<Device> devices = new ArrayList<>();
        for(int i = 0; i < clientManager.getClients().size(); i++){
            LocalClient localClient = clientManager.getClients().get(i);
            devices.add(new Device(localClient.getClientSettings().getBannedPackages(),localClient.getClientSettings().isNotificationAllowed(),localClient.getClient().getAddress(),""+localClient.getClient().getPort(),""+localClient.getPairingKey()));
        }
        DeviceList deviceList = new DeviceList(devices);
        String jsonData = gson.toJson(deviceList);

        try {
            File JSONFile = new File(MyApp.getInstance().getFilesDir(),SystemManager.getInstance().getDeviceFileName());
            OutputStream out = new FileOutputStream(JSONFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            outputStreamWriter.write(jsonData);
            outputStreamWriter.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Copy files from Assets folder to Internal Storage
     */
    public void copyAssets() {
        AssetManager assetManager = MyApp.getInstance().getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            System.out.println("Failed to get asset file list.");
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(MyApp.getInstance().getFilesDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (Exception e) {
                System.out.println("Failed to copy asset file: " + filename);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        System.out.println("Failed to close asset file.");
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        System.out.println("Failed to close FileOuputStream.");
                    }
                }
            }
        }
    }

    /**
     * Copy a file from InputStream to OutputStream
     * @param in
     * @param out
     * @throws IOException
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        int size = in.available();
        byte[] buffer = new byte[size];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
