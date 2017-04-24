package androidcompanion.device;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import project.androidcompanion.R;

public class ReadQRCodeActivity extends AppCompatActivity {

    SurfaceView cameraView;
    TextView barcodeInfo;
    //BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_qrcode);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        barcodeInfo = (TextView)findViewById(R.id.code_info);

        //barcodeDetector =
                //new BarcodeDetector.Builder(this)
                        //.setBarcodeFormats(Barcode.QR_CODE)
                        //.build();

        //cameraSource = new CameraSource
                //.Builder(this, barcodeDetector)
                //.setRequestedPreviewSize(640, 480)
                //.build();
    }
}
