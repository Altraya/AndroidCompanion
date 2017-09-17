package androidcompanion.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.netcode.Client;
import androidcompanion.netcode.LocalClient;
import project.androidcompanion.R;

// improved adapter using viewholder : https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

/**
 * Created by Sakina on 06/04/2017.
 */
public class DeviceListingAdaptater extends ArrayAdapter<DeviceInformationActivity> {
    public DeviceListingAdaptater(Context context, ArrayList<DeviceInformationActivity> devicesLists) {
        super(context, 0, devicesLists);
    }

    // View lookup cache
    private static class ViewHolder {
        Button btnConnectDisconnect;
        TextView deviceIPAdress;
        TextView devicePort;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        DeviceInformationActivity deviceInformation = getItem(position);
        // We get the Client corresponding to the device infos
        // TODO Set client attribute in DeviceInformationActivity? getClient() method? Relevant?
        Client client = null;
        for(LocalClient localClient : SystemManager.getInstance().getClientManager().getClients())
        {
            if(localClient.getClient().getAddress().equals(deviceInformation.getDeviceIPAdress())
                && (new String(localClient.getClient().getPort() + "").equals(deviceInformation.getDevicePort()))
                && (new String(localClient.getPairingKey() + "").equals(deviceInformation.getDevicePairingKey())))
            {
                client = localClient.getClient();
                break;
            }
        }
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.device_listing_row, parent, false);
            viewHolder.btnConnectDisconnect = (Button) convertView.findViewById(R.id.btn_connect_disconnect);
            // Check if device connected or not and set button text accordingly
            if(client == null)
            {
                viewHolder.btnConnectDisconnect.setText(R.string.connect_device);
            }
            else
            {
                // See when the client is considered active or not...
                if(client.isActive())
                {
                    viewHolder.btnConnectDisconnect.setText(R.string.disconnect_device);
                }
                else
                {
                    viewHolder.btnConnectDisconnect.setText(R.string.connect_device);
                }
            }
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
        viewHolder.deviceIPAdress.setText("IP Adress : " + deviceInformation.getDeviceIPAdress());
        viewHolder.devicePort.setText("Port : " + deviceInformation.getDevicePort());
        viewHolder.btnConnectDisconnect.setTag(position);
        viewHolder.btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                DeviceInformationActivity device = getItem(position);
                // Do what you want here...
                //remove(device);
                if(viewHolder.btnConnectDisconnect.getText().toString().equals(MyApp.getInstance().getResources().getString(R.string.disconnect_device)))
                {
                    // We look for the local client to connect/disconnect
                    for (LocalClient localClient : SystemManager.getInstance().getClientManager().getClients())
                    {
                        if (localClient.getClient().getAddress().equals(device.getDeviceIPAdress())
                            && (new String(localClient.getClient().getPort() + "").equals(device.getDevicePort()))
                            && (new String(localClient.getPairingKey() + "").equals(device.getDevicePairingKey())))
                        {
                            SystemManager.getInstance().getNotifyFactory().disconnect(localClient);
                            SystemManager.getInstance().getSaveManager().removeDeviceFromJsonFile("device_list.json", device.getDeviceIPAdress(), device.getDevicePort(), device.getDevicePairingKey());
                            SystemManager.getInstance().getSaveManager().loadConnectedDevices(DeviceListingActivity.deviceAdapter);
                            break;
                        }
                    }
                }
                else if(viewHolder.btnConnectDisconnect.getText().toString().equals(MyApp.getInstance().getResources().getString(R.string.connect_device)))
                {
                    // TODO handle exceptions (UI events?)
                    try
                    {
                        // Connection to the device held by the row
                        LocalClient newClient = SystemManager.getInstance().getClientManager().addClient(getItem(position).getDeviceIPAdress(),Integer.parseInt(getItem(position).getDevicePort()),Integer.parseInt(getItem(position).getDevicePairingKey()));
                        // effective connection to the client (socket)
                        if(newClient!=null)
                        {
                            newClient.connect();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
