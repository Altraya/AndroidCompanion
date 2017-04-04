package project.androidcompanion;

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
import android.widget.Toast;

import java.util.ArrayList;

//public class DeviceListingActivity extends AppCompatActivity {
public class DeviceListingActivity extends ListActivity {

    public final static String DEVICEID = "id";
    public final static String DEVICENAME = "name";

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    //ArrayList<String> listItems=new ArrayList<String>();
    ArrayList<DeviceInformationActivity> listDevice=new ArrayList<DeviceInformationActivity>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    //ArrayAdapter<String> adapter;


    ArrayAdapter<DeviceInformationActivity> deviceAdapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_device_listing);
        /*adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);*/

        deviceAdapter=new ArrayAdapter<DeviceInformationActivity>(this,
                android.R.layout.simple_list_item_1,
                listDevice);
        setListAdapter(deviceAdapter);

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

                listDevice.add(new DeviceInformationActivity(deviceid,deviceName));
                deviceAdapter.notifyDataSetChanged();



            }
        }
    }
    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(View v) {
        //listItems.add("Clicked : "+clickCounter++);
        //adapter.notifyDataSetChanged();



        /*listDevice.add(new DeviceInformationActivity("id1","name id1"));
        deviceAdapter.notifyDataSetChanged();*/



    }

    /*@Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

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
