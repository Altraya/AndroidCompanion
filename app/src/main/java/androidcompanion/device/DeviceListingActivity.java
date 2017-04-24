package androidcompanion.device;

import android.app.Activity;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import project.androidcompanion.R;

//TODO porquoi c'est lent
// TODO use JSON to store data (devices)
//public class DeviceListingActivity extends AppCompatActivity {
public class DeviceListingActivity extends Activity
{
    //intent static variables
    public final static String DEVICEID = "id";
    public final static String DEVICENAME = "name";

    ArrayList<DeviceInformationActivity> listDevice=new ArrayList<DeviceInformationActivity>();
    DeviceListingAdaptater deviceAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_listing);
        //populateDeviceList();

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

        //click on add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceListingActivity.this, ReadQRCodeActivity.class);
                startActivity(intent);
            }
        });



    }

   /* private void populateDeviceList() {
        // Construct the data source
        //ArrayList<DeviceInformationActivity> arrayOfUsers = DeviceInformationActivity.getUsers();
        // Create the adapter to convert the array to views
        //listDevice.add(new DeviceInformationActivity("deviceid","deviceName"));
        deviceAdapter = new DeviceListingAdaptater(this, listDevice);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(deviceAdapter);
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String deviceid = data.getStringExtra(DEVICEID);
                String deviceName = data.getStringExtra(DEVICENAME);
                /*listItems.add("ID : "+deviceid);
                adapter.notifyDataSetChanged();
                listItems.add("NAME : "+deviceName);
                adapter.notifyDataSetChanged();*/

                //listDevice.add(new DeviceInformationActivity(deviceid,deviceName));
                //deviceAdapter.notifyDataSetChanged();

                deviceAdapter.add(new DeviceInformationActivity(deviceid,deviceName));
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

}


