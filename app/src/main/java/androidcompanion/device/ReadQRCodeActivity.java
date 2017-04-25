package androidcompanion.device;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;

import project.androidcompanion.R;

// TODO improve interface (bigger SurfaceView, centered scan result, ...)
// TODO add for register when ip adress and port are found
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

        // TODO handle permission on application start OR before QRCodeActivity is launched
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
                    barcodeInfo.post(new Runnable()
                    {    // Use the post method of the TextView
                        public void run()
                        {
                            barcodeInfo.setText(    // Update the TextView
                                    barcodes.valueAt(0).displayValue
                            );
                            // TODO find way to do things elsewhere than in TextView.post method
                            Toast.makeText(getApplicationContext(),barcodes.valueAt(0).displayValue,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    // Handles the permissions request response
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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();;
        cameraSource.release();
        barcodeDetector.release();
    }
}
