package androidcompanion.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import androidcompanion.device.DeviceListingActivity;
import androidcompanion.device.ReadQRCodeActivity;

/**
 * Class to handle permission right and asking it to user
 * @author David
 * Created by dmarck on 14/05/2017.
 */
// TODO change permission handling way. (add bool methods? See how to add description on a one time multiple permission request)
public class PermissionManager {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int PERMISSION_REQUEST_SMS_SEND = 1;
    private final int PERMISSION_REQUEST_CALL = 2;
    private final int PERMISSION_REQUEST_CONTACT = 3;

    /**
     * Request to get use of sending sms permission from user
     * @param activity
     */
    public void requestSMSToSendPermission(Activity activity)
    {
        // check for permission (use of sms sending)
        int smsPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.SEND_SMS);

        // Check if permission granted
        if (smsPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            // explanation needed?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.SEND_SMS))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_REQUEST_SMS_SEND);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    /**
     * Request to get use of sending sms permission from user
     * @param activity
     */
    public void requestCallPermission(Activity activity)
    {
        // check for permission (call)
        int callPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);

        // Check if permission granted
        if (callPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            // explanation needed?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CALL_PHONE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CALL);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    /**
     * Request to get use of camera permission from user
     * @param activity
     */
    public void requestCameraPermission(Activity activity)
    {
        // check for permission (use of camera)
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);

        // Check if permission granted
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            // explanation needed?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    /**
     * Request to get use of sending sms permission from user
     * @param activity
     */
    public void requestContactPermission(Activity activity)
    {
        // check for permission (use of sms sending)
        int smsPermissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS);

        // Check if permission granted
        if (smsPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            // explanation needed?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSION_REQUEST_CONTACT);

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
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

            case PERMISSION_REQUEST_SMS_SEND:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
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
            case PERMISSION_REQUEST_CALL:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
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
            case PERMISSION_REQUEST_CONTACT:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
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

}
