package androidcompanion.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import project.androidcompanion.R;

/**
 * Created by Sakina on 04/04/2017.
 */

// TODO améliorer l'échange peut etre utiliser des parcel ou bundle
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


        btn_createDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getId();

                Intent i = new Intent();
                i.putExtra(DeviceListingActivity.DEVICEID,deviceId.getText().toString() );
                i.putExtra(DeviceListingActivity.DEVICENAME, deviceName.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });


    }


}
