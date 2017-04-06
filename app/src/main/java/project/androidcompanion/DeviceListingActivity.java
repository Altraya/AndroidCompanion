package project.androidcompanion;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
//TODO porquoi c'est lent
//public class DeviceListingActivity extends AppCompatActivity {
public class DeviceListingActivity extends Activity {
    //intent static variables
    public final static String DEVICEID = "id";
    public final static String DEVICENAME = "name";

    ArrayList<DeviceInformationActivity> listDevice=new ArrayList<DeviceInformationActivity>();
    DeviceListingAdaptater deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_listing);
        populateUsersList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeviceListingActivity.this, addDeviceToListActivity.class);
                startActivityForResult(i, 1);
            }
            Intent form = getIntent();

        });



    }

    private void populateUsersList() {
        // Construct the data source
        //ArrayList<DeviceInformationActivity> arrayOfUsers = DeviceInformationActivity.getUsers();
        // Create the adapter to convert the array to views
        //listDevice.add(new DeviceInformationActivity("deviceid","deviceName"));
        deviceAdapter = new DeviceListingAdaptater(this, listDevice);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(deviceAdapter);
    }

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


