package androidcompanion.device;

import android.content.Context;
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
public class DeviceListingAdaptater extends ArrayAdapter<DeviceInformationActivity> {
    public DeviceListingAdaptater(Context context, ArrayList<DeviceInformationActivity> devicesLists) {
        super(context, 0, devicesLists);
    }

/*@Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        DeviceInformationActivity deviceInformationActivity = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_listing_row, parent, false);
        }
        // Lookup view for data population
        TextView deviceRow = (TextView) convertView.findViewById(R.id.txt_deviceNameRow);
        // Populate the data into the template view using the data object
        deviceRow.setText(deviceInformationActivity.getDeviceName());
        // Return the completed view to render on screen

        // Lookup view for data population
        Button btButton = (Button) convertView.findViewById(R.id.btn_delete);
        // Cache row position inside the button using `setTag`
        btButton.setTag(position);
        // Attach the click event handler
        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                DeviceInformationActivity dev = getItem(position);
                // Do what you want here...
                remove(dev);
            }
        });

        return convertView;
        }
    }*/


    // View lookup cache
    private static class ViewHolder {
        Button btnDelete;
        TextView deviceIPAdress;
        TextView devicePort;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DeviceInformationActivity deviceInformationActivity = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.device_listing_row, parent, false);
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btn_disconnect);
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
        viewHolder.deviceIPAdress.setText("IP Adress : " + deviceInformationActivity.getDeviceIPAdress());
        viewHolder.devicePort.setText("Port : " + deviceInformationActivity.getDevicePort());
        viewHolder.btnDelete.setTag(position);
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                DeviceInformationActivity device = getItem(position);
                // Do what you want here...
                //remove(device);
                // We look for the local client to disconnect
                for(LocalClient localClient : SystemManager.getInstance().getClientManager().getClients())
                {
                    if(localClient.getClient().getAddress().equals(device.getDeviceIPAdress())
                            && (new String(localClient.getClient().getPort() + "").equals(device.getDevicePort())))
                    {
                        SystemManager.getInstance().getNotifyFactory().disconnect(localClient);
                        break;
                    }
                }
            }
        });

       /* viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setButtonClicked(1);
                new DownloadTask(currentActivity, position).execute();
            }
        });*/


        // Return the completed view to render on screen
        return convertView;
    }
}


/*
public class DeviceListingAdaptater extends BaseAdapter implements ListAdapter {
    ArrayList<DeviceInformationActivity> list=new ArrayList<DeviceInformationActivity>();
    private Context context;



    public DeviceListingAdaptater(ArrayList<DeviceInformationActivity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }


    //TODO change final position in another thing
    public View getView(final int position, View convertView, ViewGroup parent) {
        //…
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.device_listing_row, null);
        }
        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.txt_deviceName);
        listItemText.setText(list.get(position).getDeviceName());

        //Handle buttons and add onClickListeners
        Button btn_delete = (Button)view.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        return view;
    }
}*/
