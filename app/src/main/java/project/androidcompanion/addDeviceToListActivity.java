package project.androidcompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sakina on 04/04/2017.
 */

public class addDeviceToListActivity extends AppCompatActivity {

    Button btn_createDevice;
    EditText deviceId, deviceName;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.add_device_form);
        //Intent i = getIntent();

        btn_createDevice = (Button)findViewById(R.id.btn_createDevice);
        deviceId = (EditText)findViewById(R.id.deviceId);
        deviceName = (EditText)findViewById(R.id.deviceName);

        /*deviceAdapter=new ArrayAdapter<DeviceInformationActivity>(this,
                android.R.layout.simple_list_item_1,
                listDevice);
        setListAdapter(deviceAdapter);*/

        btn_createDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getId();
                /*if(deviceId.getText().toString().equals(" ") &&
                        deviceName.getText().toString().equals(" ")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "not empty",Toast.LENGTH_SHORT).show();
                }*/

                Intent i = new Intent();
                i.putExtra(DeviceListingActivity.DEVICEID,deviceId.getText().toString() );
                i.putExtra(DeviceListingActivity.DEVICENAME, deviceName.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });


    }


}
