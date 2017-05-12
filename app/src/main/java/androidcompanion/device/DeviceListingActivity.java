package androidcompanion.device;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
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
import java.util.ArrayList;

import androidcompanion.main.SystemManager;
import androidcompanion.netcode.Client;
import androidcompanion.netcode.ClientEvent;
import androidcompanion.netcode.LocalClient;
import androidcompanion.notifications.NotifyFactory;
import project.androidcompanion.R;

//TODO porquoi c'est lent
// TODO lock screen orientation to portrait?
//public class DeviceListingActivity extends AppCompatActivity {
public class DeviceListingActivity extends AppCompatActivity{

    // Request code(s)
    static final int DEVICE_INFO_REQUEST = 1;

    //intent static variables
    // used to retrieve text values
    public final static String EXTRA_DEVICE_IP_ADRESS = "androidcompanion.DEVICE_IP_ADRESS";
    public final static String EXTRA_DEVICE_PORT = "androidcompanion.DEVICE_PORT";

    ArrayList<DeviceInformationActivity> listDevice = new ArrayList<DeviceInformationActivity>();
    DeviceListingAdaptater deviceAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_listing);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(R.string.title_activity_managment);

        // We copy the assets to the external storage(in order to be able to write in them)
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        boolean isFirstLaunch = settings.getBoolean("FIRST_RUN", false);
        if (!isFirstLaunch) {
            // do the thing for the first time
            copyAssets();
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time your app loads
        }

        deviceAdapter = new DeviceListingAdaptater(this, listDevice);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(deviceAdapter);

        //click on a deviceList item
        /**/listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DeviceListingActivity.this, DisplayDeviceInformationActivity.class);
                //DeviceInformationActivity clickedDevice = (DeviceInformationActivity) parent.getItemAtPosition(position);
                //intent.putExtra(DEVICENAME, clickedDevice.getDeviceName());
                //intent.putExtra(DEVICEID, clickedDevice.getDeviceId());
                startActivity(intent);
            }
        });

        loadConnectedDevices();

        //click on add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(DeviceListingActivity.this, ReadQRCodeActivity.class);
                startActivityForResult(intent,DEVICE_INFO_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DEVICE_INFO_REQUEST) {
            // Make sure the request was successful
            if(resultCode == RESULT_OK) {
                // Scanned a QRCode and got device infos
                String deviceIPAdress = data.getStringExtra(EXTRA_DEVICE_IP_ADRESS);
                String devicePort = data.getStringExtra(EXTRA_DEVICE_PORT);
                // If the data is approved, we add it to our JSON file
                if(deviceIPAdress.equals("none") && devicePort.equals("none"))
                {
                    //Toast.makeText(getApplicationContext(),"The QR code didn't hold any device informations.",Toast.LENGTH_SHORT).show();
                }
                else if(deviceIPAdress.equals("cancelled") && devicePort.equals("cancelled"))
                {
                    //Toast.makeText(getApplicationContext(),"Adding device cancelled.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

                    // Connection to the device using the infos previously provided
                    LocalClient newClient = SystemManager.getInstance().getClientManager().addClient(deviceIPAdress,Integer.parseInt(devicePort));
                    // effective connection to the client (socket)
                    newClient.connect();
                    //Toast.makeText(getApplicationContext(),"Device successfully connected!",Toast.LENGTH_SHORT).show();
                    addEntryToJsonFile("device_list.json",deviceIPAdress,devicePort);
                    loadConnectedDevices();
                }
                //deviceAdapter.add(new DeviceInformationActivity(deviceIPAdress,devicePort));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetch data from json file (asset)
     * @param asset_name
     * @return JSONObject instance containing the data
     */
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

    /**
     * Retrieve current data from JSON file and add entry to it
     * @param asset_name
     */
    public void addEntryToJsonFile(String asset_name, String ipAdress, String port) {
        if(isNewDevice(ipAdress,port))
        {
            try {
                // Already existing JSON object
                JSONObject prevJSONObj = new JSONObject(loadJSONFromAsset(asset_name));
                // New JSON object
                JSONObject newJSONobj = new JSONObject();
                JSONArray  devices;
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
            Toast.makeText(getApplicationContext(),"This device is already connected.",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load the connected devices list
     */
    private void loadConnectedDevices() {
        deviceAdapter.clear();
        try
        {
            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("device_list.json"));
            if(!jsonObj.isNull("devices"))
            {
                JSONArray devices = jsonObj.getJSONArray("devices");
                for(int i = 0; i < devices.length(); i++)
                {
                    JSONObject device = devices.getJSONObject(i);
                    String deviceIPAdress = device.getString("ip_adress");
                    String devicePort = device.getString("port");
                    deviceAdapter.add(new DeviceInformationActivity(deviceIPAdress,devicePort));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Copy files from Assets folder to External Storage
     */
    private void copyAssets() {
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

    /**
     * Check if device with given IP adress and port is new
     * @param ipAdress
     * @param port
     * @return true if new device, false otherwise
     */
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

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            /*TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml(pack +"<br><b>" + title + " : </b>" + text));
            tr.addView(textview);
            tab.addView(tr);*/

            SystemManager.getInstance().getClientManager().notifyAll(pack, title, text);
        }
    };
}


