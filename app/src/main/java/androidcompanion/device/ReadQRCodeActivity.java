package androidcompanion.device;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Matcher;

import androidcompanion.main.SystemManager;
import project.androidcompanion.R;

// TODO improve interface?
public class ReadQRCodeActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    private SurfaceView cameraView;
    private TextView barcodeInfo;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_qrcode);

        //SystemManager.getInstance().getPermissionManager().requestCameraPermission(this);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        barcodeInfo = (TextView)findViewById(R.id.code_info);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();
        if (!barcodeDetector.isOperational()) {
            barcodeInfo.setText("Could not set up the detector!");
            return;
        }

        // This instance will hold the stream of fetched images from device's camera
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        // Adding callback to the SurfaceHolder of the SurfaceView
        // Allows to know when to start drawing the preview frames
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                try
                {
                    if((ContextCompat.checkSelfPermission(ReadQRCodeActivity.this,
                            Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(cameraView.getHolder());
                    }
                }
                catch (IOException ie)
                {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                cameraSource.stop();
            }
        });

        // barcodeDetector actions if it detects a QR Code
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
        {
            @Override
            public void release()
            {
            }

            // /!\ DOES NOT RUN ON THE UI THREAD
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0)
                {
                    barcodeDetector.release();
                    cameraView.post(new Runnable()
                    {    // Use the post method of the TextView
                        public void run()
                        {
                            // Update the TextView
                            barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                            //Toast.makeText(getApplicationContext(),barcodes.valueAt(0).displayValue,Toast.LENGTH_SHORT).show();
                            if(containsDeviceInfo(barcodes.valueAt(0).displayValue))
                            {
                                String[] split = barcodes.valueAt(0).displayValue.split(":");
                                new AlertDialog.Builder(ReadQRCodeActivity.this)
                                        .setTitle("Device Infos Detected")
                                        .setMessage("Following device infos have been detected : "
                                                + "\n\n\t\t\t- " + split[0] + ":" + split[1]
                                                + "\n\nDo you want to connect to the device related?")
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                String[] split = barcodes.valueAt(0).displayValue.split(":");
                                                String deviceIPAdress = split[0];
                                                String devicePort = split[1];
                                                String pairingKey = split[2];
                                                Intent i = new Intent();
                                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_IP_ADRESS,deviceIPAdress);
                                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_PORT,devicePort);
                                                i.putExtra(DeviceListingActivity.EXTRA_PAIRING_KEY,pairingKey);
                                                setResult(RESULT_OK, i);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                    // only for honeycomb and newer versions
                                                    ReadQRCodeActivity.this.recreate();
                                                } else {
                                                    finish();
                                                    startActivity(ReadQRCodeActivity.this.getIntent());
                                                }*/
                                                Intent i = new Intent();
                                                // TODO try : setResult(RESULT_CANCELED,i)
                                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_IP_ADRESS,"cancelled");
                                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_PORT,"cancelled");
                                                setResult(RESULT_OK, i);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                            else
                            {
                                Intent i = new Intent();
                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_IP_ADRESS,"none");
                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_PORT,"none");
                                setResult(RESULT_OK, i);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // Releasing resources
        cameraSource.release();
        barcodeDetector.release();
    }

    /**
     * Check of presence of IP adress and port in a string
     * @param str
     * @return
     */
    private boolean containsDeviceInfo(String str)
    {
        if(str.contains(":"))
        {
            String[] split = str.split(":");
            if(split.length == 3)
            {
                try {
                    Integer.parseInt(split[1]);
                    Integer.parseInt(split[2]);
                }
                catch (NumberFormatException e) {
                    return false;
                }

                Matcher ipMatcher = Patterns.IP_ADDRESS.matcher(split[0]);
                int port = Integer.parseInt(split[1]);
                if(ipMatcher.matches()) {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
