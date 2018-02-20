package br.com.screenresolution;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

/**
 * Created by douglimar moraes on 19/02/18.
 * Utility class
 */

public class Utils extends AppCompatActivity {

    // Storage Permissions
    public static final int REQUEST_READ_PHONE_STATE = 1;
    public static final String[] PERMISSIONS_READ_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE
    };



    public String getIMEINumber()  {
        String result = "";

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_READ_PHONE_STATE,
                    REQUEST_READ_PHONE_STATE
            );
        }
        //result = telephonyManager.getDeviceId().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = telephonyManager.getImei(1).toString() + "\n" + telephonyManager.getImei(2).toString();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = telephonyManager.getDeviceId(1) + "\n" +
                    telephonyManager.getDeviceId(2) +"\n" +
                    telephonyManager.getDeviceSoftwareVersion()+ "\n" +
                    telephonyManager.getSimCountryIso() + "\n" +
                    telephonyManager.getSimOperator() + "\n" +
                    telephonyManager.getSimOperatorName() + "\n" +
                    telephonyManager.getSimSerialNumber() + "\n" +
                    telephonyManager.getSimSerialNumber();

        } else
            result = telephonyManager.getDeviceId();


        return result;
    }


    public String getHardwareInfo() {

        String text ="SERIAL: " + Build.SERIAL + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "DEVICE: " + Build.DEVICE + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "User: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK:  " + Build.VERSION.SDK + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "BRAND: " + Build.BRAND + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE + "\n" +
                "Security Path: " + Build.VERSION.SECURITY_PATCH + "\n" +
                "Codename: " + Build.VERSION.CODENAME + "\n" +
                //"BASE OS: " + Build.VERSION.BASE_OS + "\n*********************************\n" +
//                "Configurations: " + getResources().getConfiguration() + "\n" +
//                "Configurations: Density DPI: " + getResources().getConfiguration().densityDpi + "\n" +
//                "Configurations: Describe Contents: " + getResources().getConfiguration().describeContents() + "\n" +
                "Screen Brightness: " + Settings.System.SCREEN_BRIGHTNESS + "\n" +
                "Ringtone: " + Settings.System.RINGTONE + "\n" +
                "Display Service: " + DISPLAY_SERVICE + "\n";

        return text;
    }


    private void getBatteryStatus(Context context) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
    }


    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive)
            return true;
        else
            return false;
    }



}
