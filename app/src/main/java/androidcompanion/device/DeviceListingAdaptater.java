package androidcompanion.device;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidcompanion.main.MyApp;
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
        ImageButton btnDisconnect;
        ImageButton btnSettings;
        ImageButton btnRemove;
        TextView deviceIPAdress;
        TextView DeviceConnectionState;
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
            viewHolder.btnDisconnect = (ImageButton) convertView.findViewById(R.id.btn_disconnect);
            viewHolder.btnSettings = (ImageButton) convertView.findViewById(R.id.btn_device_settings);
            viewHolder.btnRemove = (ImageButton) convertView.findViewById(R.id.btn_device_remove);
            viewHolder.deviceIPAdress = (TextView) convertView.findViewById(R.id.textView_deviceIP);
            viewHolder.DeviceConnectionState = (TextView) convertView.findViewById(R.id.textView_ConnectionState);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.deviceIPAdress.setText(device.getClient().getAddress());
        switch (device.getConnectionState()) {
            case PENDING:
                viewHolder.DeviceConnectionState.setText("Pending...");
                viewHolder.DeviceConnectionState.setTextColor(ContextCompat.getColor(MyApp.getContext(), R.color.ORANGE));
                break;
            case ACCEPTED:
                viewHolder.DeviceConnectionState.setText("Connected");
                viewHolder.DeviceConnectionState.setTextColor(ContextCompat.getColor(MyApp.getContext(), R.color.GREEN));
                break;
            case REFUSED:
                viewHolder.DeviceConnectionState.setText("Connection Refused");
                viewHolder.DeviceConnectionState.setTextColor(ContextCompat.getColor(MyApp.getContext(), R.color.RED));
                break;
            case DISCONNECTED:
                viewHolder.DeviceConnectionState.setText("Disconnected");
                viewHolder.DeviceConnectionState.setTextColor(ContextCompat.getColor(MyApp.getContext(), R.color.BLUE_GRAY));
                break;
            case CONNECTING:
                viewHolder.DeviceConnectionState.setText("Connecting...");
                viewHolder.DeviceConnectionState.setTextColor(ContextCompat.getColor(MyApp.getContext(), R.color.TEAL));
                break;
        }

        if(device.getClient().isActive()){
            ((ImageButton) viewHolder.btnDisconnect).setImageResource(R.drawable.ic_phonelink_off_black_24dp);
        }else{
            ((ImageButton) viewHolder.btnDisconnect).setImageResource(R.drawable.ic_phonelink_black_24dp);
        }

        viewHolder.btnDisconnect.setTag(position);
        viewHolder.btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // We look for the local client to disconnect
                for(LocalClient localClient : SystemManager.getInstance().getClientManager().getClients())
                {
                    if(localClient.getUid().equals(device.getUid()))
                    {
                        if(localClient.getClient().isActive()){
                            SystemManager.getInstance().getNotifyFactory().disconnect(localClient);
                            DeviceListingActivity.refreshListview();
                            break;
                        }else{
                            localClient.connect();
                        }
                    }
                }
            }
        });

        viewHolder.btnSettings.setTag(position);
        viewHolder.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConfigurationActivity.class);
                // passing parameters to the conf activity
                Bundle b = new Bundle();
                b.putString("UID", device.getUid());
                intent.putExtras(b);
                getContext().startActivity(intent);
            }
        });

        viewHolder.btnRemove.setTag(position);
        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                device.remove();
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}
