package androidcompanion.data;

import android.content.res.AssetManager;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import androidcompanion.device.DeviceListingActivity;

/**
 * Created by dmarck on 12/05/2017.
 */

public class SaveManager {

    /**
     * Fetch data from json file (asset)
     * @param asset_name
     * @return JSONObject instance containing the data
     *//*
    public String loadJSONFromAsset(String asset_name) {
        String json = null;
        try {
            File jsonFile = new File(getExternalFilesDir(null).getPath(), asset_name);
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

    *//**
     * Retrieve current data from JSON file and add entry to it
     * @param asset_name
     *//*
    public void addEntryToJsonFile(String asset_name, String ipAdress, String port) {
        if(isNewDevice(ipAdress,port))
        {
            try {
                // Already existing JSON object
                JSONObject prevJSONObj = new JSONObject(loadJSONFromAsset(asset_name));
                // New JSON object
                JSONObject newJSONobj = new JSONObject();
                JSONArray devices;
                if(!prevJSONObj.isNull("devices"))
                {
                    // Array in which data is appended
                    devices = prevJSONObj.getJSONArray("devices");
                    // Adding data
                    newJSONobj.put("ip_adress",ipAdress);
                    newJSONobj.put("port",port);
                    // Append
                    devices.put(newJSONobj);
                    // Save new data in file
                    File JSONFile = new File(getExternalFilesDir(null).getPath(),asset_name);
                    OutputStream out = new FileOutputStream(JSONFile);
                    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out,"UTF-8"));
                    writer.setIndent("  ");
                    writer.beginObject();
                    writer.name("devices");
                    writer.beginArray();
                    for (int i = 0; i < devices.length(); i++) {
                        JSONObject device = devices.getJSONObject(i);
                        String deviceIPAdress = device.getString("ip_adress");
                        String devicePort = device.getString("port");
                        writer.beginObject();
                        writer.name("ip_adress").value(deviceIPAdress);
                        writer.name("port").value(devicePort);
                        writer.endObject();
                    }
                    writer.endArray();
                    writer.endObject();
                    //out.write(writer.toString().getBytes());
                    writer.close();
                    out.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(DeviceListingActivity.getApplicationContext(),"This device is already connected.",Toast.LENGTH_SHORT).show();
        }
    }

    *//**
     * Copy files from Assets folder to External Storage
     *//*
    public void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e("tag", "Failed to close asset file.", e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.e("tag", "Failed to close FileOuputStream.", e);
                    }
                }
            }
        }
    }

    *//**
     * Copy a file from InputStream to OutputStream
     * @param in
     * @param out
     * @throws IOException
     *//*
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        int size = in.available();
        byte[] buffer = new byte[size];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    *//**
     * Check if device with given IP adress and port is new
     * @param ipAdress
     * @param port
     * @return true if new device, false otherwise
     *//*
    private boolean isNewDevice(String ipAdress, String port)
    {
        try {
            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("device_list.json"));
            if(!jsonObj.isNull("devices"))
            {
                JSONArray devices = jsonObj.getJSONArray("devices");
                for(int i = 0; i < devices.length(); i++)
                {
                    JSONObject device = devices.getJSONObject(i);
                    String deviceIPAdress = device.getString("ip_adress");
                    String devicePort = device.getString("port");
                    if(deviceIPAdress.equals(ipAdress) && devicePort.equals(port))
                    {
                        return false;
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    */
}
