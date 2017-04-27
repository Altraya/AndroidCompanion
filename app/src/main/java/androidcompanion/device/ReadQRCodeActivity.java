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

import project.androidcompanion.R;

// TODO improve interface?
// TODO connection on register (device info)
// TODO handle when not device infos
// TODO check if not already registered
// TODO connection
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

        requestCameraPermission();

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
                            // TODO find way to do things elsewhere than in TextView.post method
                            // TODO why does it loop?
                            Toast.makeText(getApplicationContext(),barcodes.valueAt(0).displayValue,Toast.LENGTH_SHORT).show();
                            if(containsDeviceInfo(barcodes.valueAt(0).displayValue))
                            {
                                new AlertDialog.Builder(ReadQRCodeActivity.this)
                                        .setTitle("Device Infos Detected")
                                        .setMessage("Following device infos have been detected : "
                                                + "\n\n\t\t\t- " + barcodes.valueAt(0).displayValue
                                                + "\n\nDo you want to connect to the device related?")
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                String[] split = barcodes.valueAt(0).displayValue.split(":");
                                                String deviceIPAdress = split[0];
                                                String devicePort = split[1];
                                                Intent i = new Intent();
                                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_IP_ADRESS,deviceIPAdress);
                                                i.putExtra(DeviceListingActivity.EXTRA_DEVICE_PORT,devicePort);
                                                setResult(RESULT_OK, i);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                    // only for honeycomb and newer versions
                                                    ReadQRCodeActivity.this.recreate();
                                                } else {
                                                    finish();
                                                    startActivity(ReadQRCodeActivity.this.getIntent());
                                                }
                                            }
                                        })
                                        .show();
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
     * Request to get use of camera permission from user
     */
    // TODO move the two methods below to static class? (e.g. PermissionHandler)
    private void requestCameraPermission()
    {
        // TODO handle permission request on application start OR before QRCodeActivity is launched
        // check for permission (use of camera)
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(ReadQRCodeActivity.this,
                Manifest.permission.CAMERA);

        // Check if permission granted
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            // explanation needed?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReadQRCodeActivity.this,
                    Manifest.permission.CAMERA))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(ReadQRCodeActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    /**
     * Handles permissions request response
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // TODO get camera permission before starting this activity
                    // permission was granted.
                    // Do the camera task you need to do.

                    // We restart the activity in order to apply permission changed state
                    //Intent intent = getIntent();
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //finish();
                    //startActivity(intent);
                }
                else
                {
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
            if(split.length == 2)
            {
                try {
                    Integer.parseInt(split[1]);
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
