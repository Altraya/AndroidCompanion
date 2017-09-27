package project.androidcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidcompanion.device.settings.DeviceSetting;
import androidcompanion.device.settings.DeviceSettingsManager;
import androidcompanion.main.SystemManager;

public class ConfigurationActivity extends AppCompatActivity {
    private DeviceSettingsManager deviceSettingsManager;
    private DeviceSetting editedDeviceSettings;

    ConfigurationActivity(){
        deviceSettingsManager = SystemManager.getInstance().getDeviceSettingsManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getting the device id parameter
        Bundle b = this.getIntent().getExtras();
        String configuredDeviceId = b.getString("deviceId");
        // to access the right device settings object
        editedDeviceSettings = deviceSettingsManager.getDeviceSettings(configuredDeviceId);

        setContentView(R.layout.activity_configuration);

        Switch switchSendCallNotification = (Switch) findViewById(R.id.send_call_notifications_switch);
        switchSendCallNotification.setChecked(editedDeviceSettings.getSendCallNotifications());
        switchSendCallNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editedDeviceSettings.setSendCallNotifications(isChecked);
                deviceSettingsManager.persistAllSettings();
            }
        });

        Switch switchSendSmsNotification = (Switch) findViewById(R.id.send_sms_notifications_switch);
        switchSendSmsNotification.setChecked(editedDeviceSettings.getSendSmsNotifications());
        switchSendSmsNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editedDeviceSettings.setSendSmsNotifications(isChecked);
                deviceSettingsManager.persistAllSettings();
            }
        });

        Switch switchSendOtherNotification = (Switch) findViewById(R.id.send_other_notifications_switch);
        switchSendOtherNotification.setChecked(editedDeviceSettings.getSendOtherNotifications());
        switchSendOtherNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editedDeviceSettings.setSendOtherNotifications(isChecked);
                deviceSettingsManager.persistAllSettings();
            }
        });

        Switch switchReceiveCallNotification = (Switch) findViewById(R.id.receive_call_notifications_switch);
        switchReceiveCallNotification.setChecked(editedDeviceSettings.getReceiveCallNotifications());
        switchReceiveCallNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editedDeviceSettings.setReceiveCallNotifications(isChecked);
                deviceSettingsManager.persistAllSettings();
            }
        });

        Switch switchReceiveSmsNotification = (Switch) findViewById(R.id.receive_sms_notifications_switch);
        switchReceiveSmsNotification.setChecked(editedDeviceSettings.getReceiveSmsNotifications());
        switchReceiveSmsNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editedDeviceSettings.setReceiveSmsNotifications(isChecked);
                deviceSettingsManager.persistAllSettings();
            }
        });
    }
}
