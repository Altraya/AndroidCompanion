package androidcompanion.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;
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
    public final static String EXTRA_PAIRING_KEY = "androidcompanion.KEY";

    ArrayList<DeviceInformationActivity> listDevice = new ArrayList<DeviceInformationActivity>();
    public static DeviceListingAdaptater deviceAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_listing);

        // We request the camera permission
        SystemManager.getInstance().getPermissionManager().requestCameraPermission(DeviceListingActivity.this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(R.string.title_activity_managment);

        // We copy the assets to the external storage(in order to be able to write in them)
        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        boolean isFirstLaunch = settings.getBoolean("FIRST_RUN", false);
        if (!isFirstLaunch) {
            // do the thing for the first time
            SystemManager.getInstance().getSaveManager().copyAssets();
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

        SystemManager.getInstance().getSaveManager().loadConnectedDevices(deviceAdapter);

        //click on add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if((ContextCompat.checkSelfPermission(DeviceListingActivity.this,
                        Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(DeviceListingActivity.this, ReadQRCodeActivity.class);
                    startActivityForResult(intent,DEVICE_INFO_REQUEST);
                }
                else
                {
                    // TODO ask user if he wants to allow permission (AlertDialog), then request permission and thus launch activity
                    Toast.makeText(MyApp.getInstance().getApplicationContext(),"The use of this device's camera is not allowed.",Toast.LENGTH_SHORT).show();
                }
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
                String pairingKey = data.getStringExtra(EXTRA_PAIRING_KEY);
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
                    LocalClient newClient = SystemManager.getInstance().getClientManager().addClient(deviceIPAdress,Integer.parseInt(devicePort),Integer.parseInt(pairingKey));
                    // effective connection to the client (socket)
                    newClient.connect();
                    //Toast.makeText(getApplicationContext(),"Device successfully connected!",Toast.LENGTH_SHORT).show();
                    SystemManager.getInstance().getSaveManager().addDeviceToJsonFile("device_list.json",deviceIPAdress,devicePort);
                    SystemManager.getInstance().getSaveManager().loadConnectedDevices(deviceAdapter);
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
     * Load the connected devices list
     */
    private void loadConnectedDevices() {
        deviceAdapter.clear();
        try
        {
            JSONObject jsonObj = new JSONObject(SystemManager.getInstance().getSaveManager().loadJSONFromAsset("device_list.json"));
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


