package androidcompanion.device;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;
import project.androidcompanion.R;

// improved adapter using viewholder : https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

/**
 * Created by Sakina on 06/04/2017.
 */
public class DeviceListingAdaptater extends ArrayAdapter<LocalClient> {

    public DeviceListingAdaptater(Context context, ArrayList<LocalClient> devicesLists) {
        super(context, 0, devicesLists);
    }

    // View lookup cache
    private static class ViewHolder {
        Button btnDelete;
        Button btnSettings;
        TextView deviceIPAdress;
        TextView devicePort;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final LocalClient device = SystemManager.getInstance().getClientManager().getClients().get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.device_listing_row, parent, false);
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btn_disconnect);
            viewHolder.btnSettings = (Button) convertView.findViewById(R.id.btn_device_settings);
            viewHolder.deviceIPAdress = (TextView) convertView.findViewById(R.id.textView_deviceIP);
            viewHolder.devicePort = (TextView) convertView.findViewById(R.id.textView_devicePort);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.deviceIPAdress.setText("IP Adress : " + device.getClient().getAddress());
        viewHolder.devicePort.setText("Port : " + device.getClient().getPort());
        viewHolder.btnDelete.setTag(position);
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Do what you want here...
                //remove(device);
                // We look for the local client to disconnect
                for(LocalClient localClient : SystemManager.getInstance().getClientManager().getClients())
                {
                    if(localClient.getUid().equals(device.getUid()))
                    {
                        //TODO REACTIVATE SAVE
                        //SystemManager.getInstance().getNotifyFactory().disconnect(localClient);
                        //SystemManager.getInstance().getSaveManager().removeDeviceFromJsonFile("device_list.json",device.getClient().getAddress(),""+device.getClient().getPort(),device.getUid());
                        //SystemManager.getInstance().getSaveManager().loadConnectedDevices(DeviceListingActivity.deviceAdapter);
                        break;
                    }
                }
            }
        });

        viewHolder.btnSettings.setTag(position);
        viewHolder.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Intent intent = new Intent(getContext(), ConfigurationActivity.class);
                // passing parameters to the conf activity
                Bundle b = new Bundle();
                b.putString("UID", device.getUid());
                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}
