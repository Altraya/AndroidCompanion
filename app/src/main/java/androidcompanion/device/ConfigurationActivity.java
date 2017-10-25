package androidcompanion.device;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;
import project.androidcompanion.R;

public class ConfigurationActivity extends AppCompatActivity {

    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private AppListAdapter listadaptor = null;
    private ListView applicationList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);

        // getting the device id parameter
        Bundle b = this.getIntent().getExtras();
        String uid = b.getString("UID");

        final LocalClient device = SystemManager.getInstance().getClientManager().getClientByUid(uid);

        Switch switchSendCallNotification = (Switch) findViewById(R.id.use_notifications_switch);
        switchSendCallNotification.setChecked(true);

        if(device != null){

            switchSendCallNotification.setChecked(device.getClientSettings().isNotificationAllowed());
            switchSendCallNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    device.getClientSettings().setNotificationAllowed(isChecked);
                    SystemManager.getInstance().getSaveManager().saveDevices();
                }
            });

        }

        packageManager = getPackageManager();
        applicationList = (ListView) findViewById(R.id.app_list);
        applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        listadaptor = new AppListAdapter(ConfigurationActivity.this, R.layout.app_list_row, applist,device);
        applicationList.setAdapter(listadaptor);


    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

}
