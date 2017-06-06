package androidcompanion.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import project.androidcompanion.R;

/**
 * Created by Sakina on 07/04/2017.
 */

public class DisplayDeviceInformationActivity extends AppCompatActivity {
    Button btn_backToDeviceList;
    TextView deviceId, deviceName;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.display_device_information);
        Intent intent = getIntent();

        btn_backToDeviceList = (Button)findViewById(R.id.btn_backToDeviceList);
        deviceId = (TextView)findViewById(R.id.deviceId);
        deviceName = (TextView)findViewById(R.id.deviceName);

        if (intent != null) {
            //deviceId.setText(intent.));

        }






    }
}
