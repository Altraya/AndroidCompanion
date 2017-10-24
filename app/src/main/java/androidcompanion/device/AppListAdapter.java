package androidcompanion.device;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Switch;

import java.util.List;

import androidcompanion.netcode.LocalClient;
import project.androidcompanion.R;

/**
 * Created by Jo on 24/10/2017.
 */


public class AppListAdapter extends ArrayAdapter<ApplicationInfo> {
    private List<ApplicationInfo> appsList = null;
    private Context context;
    private PackageManager packageManager;
    private LocalClient client;

    public AppListAdapter(Context context, int textViewResourceId,
                              List<ApplicationInfo> appsList,LocalClient client) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();
        this.client = client;
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_list_row, null);
        }

        final ApplicationInfo applicationInfo = appsList.get(position);
        if (null != applicationInfo) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            Switch enableSwitch = (Switch) view.findViewById(R.id.notification_enable_switch);
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

            if(client != null){
                enableSwitch.setChecked(client.getClientSettings().isAuthorized(applicationInfo.packageName));
                enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            client.getClientSettings().unban(applicationInfo.packageName);
                        }else{
                            client.getClientSettings().ban(applicationInfo.packageName);
                        }
                    }
                });
            }else{
                enableSwitch.setChecked(true);
            }

            appName.setText(applicationInfo.loadLabel(packageManager));
            iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }
        return view;
    }

    public LocalClient getClient() {
        return client;
    }

    public void setClient(LocalClient client) {
        this.client = client;
    }
};