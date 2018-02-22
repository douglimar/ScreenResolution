package br.com.screenresolution;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    // Storage Permissions
    public static final int REQUEST_READ_PHONE_STATE = 1;
    public static final String[] PERMISSIONS_READ_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);

        verifyStoragePermissions2(MainActivity.this);

        TextView tvHWInfo = findViewById(R.id.tvHardwareInfo);
        TextView tvHWConfig = findViewById(R.id.tvHardwareConfig);
        TextView tvBatteryStatus = findViewById(R.id.tvBatteryStatus);
        //TextView tvDataType = findViewById(R.id.tvDataType);
        //TextView tvDeviceInch = findViewById(R.id.tvDeviceInch);
        //TextView tvKernel = findViewById(R.id.tvKernel);
        //TextView tvSDCard = findViewById(R.id.tvSDCard);
        TextView tvIMEIInfo = findViewById(R.id.tvIMEIInfo);
        TextView tvScreenRes = findViewById(R.id.tvScreenRes);

        tvHWInfo.setText(getHardwareInfo());

        tvHWConfig.setText(getHardwareConfiguration());

        tvBatteryStatus.setText(getBatteryStatus2(getBaseContext()));
        tvScreenRes.setText(getScreenResolution());

        //tvSDCard.setText(getSDCardStatus()+ "\n" + getSDCardFreeSpace3());

        //tvKernel.setText(getKernelVersion());

        //tvDataType.setText();

        //tvDeviceInch.setText(getDeviceInch(this));

        tvIMEIInfo.setText(getDataType(this)+ "\n" + getIMEINumber());

        //Toast.makeText(getApplicationContext(), "Toolbar Height: " +  toolbar.getHeight(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "Imei: " +  getIMEINumber(), Toast.LENGTH_LONG).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static void verifyStoragePermissions2(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_READ_PHONE_STATE,
                    REQUEST_READ_PHONE_STATE
            );
        }
    }

    // Utility Methods

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

        String IMEI = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int iSIM_Total = telephonyManager.getPhoneCount();

            if (iSIM_Total == 1) {

                IMEI = "IMEI: " + telephonyManager.getDeviceId();

            } else {
                IMEI = "IMEI 1: " + telephonyManager.getDeviceId(1) + "\n"
                     + "IMEI 2: " + telephonyManager.getDeviceId(2);
            }
        }


        result = IMEI + "\n"+
                "SW VERSION: " + telephonyManager.getDeviceSoftwareVersion()+ "\n" +
                "SIM COUNTRY: " + telephonyManager.getSimCountryIso() + "\n" +
                "SIM OPERATOR: " + telephonyManager.getSimOperator() + "\n" +
                "SIM OPERATOR NAME: " + telephonyManager.getSimOperatorName() + "\n" +
                "SIM STATE: " + telephonyManager.getSimState() + "\n"+
                "SIM SERIAL No: " + telephonyManager.getSimSerialNumber();



        /*
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
*/

        return result;
    }


    public String getHardwareInfo() {

        String text ="SERIAL: " + Build.SERIAL + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "MANUFACTURER: " + Build.MANUFACTURER + "\n" +
                "PRODUCT: " + Build.PRODUCT + "\n"+
                "BRAND: " + Build.BRAND + "\n" +
                "DEVICE: " + Build.DEVICE + "\n" +
                "TYPE: " + Build.TYPE + "\n" +
                "USER: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK:  " + Build.VERSION.SDK + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "VERSION CODE: " + Build.VERSION.RELEASE + "\n" +
                "CODENAME: " + Build.VERSION.CODENAME + "\n";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text += "SECURITY PATCH: " + Build.VERSION.SECURITY_PATCH + "\n" +
                    "BASE OS: " + Build.VERSION.BASE_OS;
        }

        text+= "\n\nKERNEL VERSION: " + getKernelVersion();


        return text;
    }

    public String getHardwareConfiguration() {

        String text = "CONFIGURATIONS: " + getResources().getConfiguration() + "\n" +
                "DENSITY DPI: " + getResources().getConfiguration().densityDpi + "\n" +
                "DESCRIBE CONTENTS: " + getResources().getConfiguration().describeContents() + "\n" +
                "SCREEN BRIGHTNESS: " + Settings.System.SCREEN_BRIGHTNESS + "\n" +
                "SCREEN BRIGHTNESS MODE: " + Settings.System.SCREEN_BRIGHTNESS_MODE + "\n"+
                "RINGTONE: " + Settings.System.RINGTONE + "\n" +
                "DISPLAY SERVICE: " + DISPLAY_SERVICE + "\n";

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


    private String getBatteryStatus2(Context context) {

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

        return "BATTERY LEVEL: " + level + " %\n"
                + "BATTERY SCALE: " + scale + "\n"
                + "IS CHARGING: " + isCharging + "\n"
                + "CHARGING by USB: " + usbCharge + "\n"
                + "CHARGING by AC PLUG: " + acCharge;
    }


    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive)
            return true;
        else
            return false;
    }


    public String getScreenResolution() {

        // Acessnando e manipulando as informacoes da Tela do dispositivo
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int height  = dm.heightPixels;
        final int width   = dm.widthPixels;
        final int density = dm.densityDpi;

        String sPixels = height + "px x " + width + "px";

        String sDensity = Integer.toString(density);
/*
        String sBrightness = "SCREEN BRIGHTNESS: " + Settings.System.SCREEN_BRIGHTNESS + "\n"
                + "SCREEN BRIGHTNESS MODE: " + Settings.System.SCREEN_BRIGHTNESS_MODE;
*/

        if (between(density, 1,120))
            sDensity += "DPI | LDPI (Low Density)";
        else if (between(density, 121,160))
            sDensity += "DPI | MDPI (Medium Density)" ;
        else if (between(density,161,240))
            sDensity += "DPI | HDPI (High Density)" ;
        else if (between(density,241,320))
            sDensity += "DPI | XHDPI (Extra High Density)" ;
        else if (between(density,321,480))
            sDensity += "DPI | XXHDPI (Extra Extra High Density)" ;
        else  if (density > 480)
            sDensity += "DPI | XXXHDPI (Extra Extra Extra High Density)" ;

        return  sPixels + "\n" + sDensity;

    }

    public String getScreenResolution_v2(Activity activity){

        return "screenWidth: " + activity.getWindow().getWindowManager().getDefaultDisplay()
                .getWidth() + "\n screenHeigth: "
                + activity.getWindow().getWindowManager().getDefaultDisplay()
                .getHeight();


    }

    public String getKernelVersion() {

        return "KERNEL VERSION:" + System.getProperty("os.version");

    }

    public String getSDCardStatus() {

        return "\n SD Card state: " + Environment.getExternalStorageState()
                + "\n SD Card Used Space: "+ Environment.getExternalStorageDirectory().getUsableSpace();

    }


    public String getSDCardFreeSpace(){

        // Relevant documentation: http://developer.android.com/reference/android/os/StatFs.html

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdAvailSize = (double)stat.getAvailableBlocks()
                * (double)stat.getBlockSize();
        //One binary gigabyte equals 1,073,741,824 bytes.
        double gigaAvailable = sdAvailSize / 1073741824;

        return  "\n SD Card Free Space: "+ gigaAvailable;
    }



    public String getSDCardFreeSpace2(){

        // Relevant documentation: http://developer.android.com/reference/android/os/StatFs.html

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdAvailSize = (double)stat.getAvailableBytes();
        //One binary gigabyte equals 1,073,741,824 bytes.
        double gigaAvailable = sdAvailSize / 1073741824;

        return  "\n <b>SD Card Available Bytes:</b> "+ stat.getAvailableBytes()/1024L +
                "\n SD Card Free Bytes: " + stat.getFreeBytes() /1024L;
    }


    public String getSDCardFreeSpace3(){

        //StatFs stat = new StatFs(System.getenv("SECONDARY_STORAGE"));
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long bytesAvailable = (long)stat.getBlockSizeLong() * (long)stat.getAvailableBlocksLong();

        long kiloAvailable = bytesAvailable / 1024; // Available space from SD in KB
        long megaAvailable = bytesAvailable / (1024*1024); // Available space from SD in MB

        return "\n NEW SDCARD in KB: " + megaAvailable;
    }

    public static String getDeviceInch(Context activity) {
        try {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

            float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
            float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
            double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            return String.valueOf(diagonalInches);
        } catch (Exception e) {
            return "-1";
        }
    }

    public static String getDataType(Context activity) {
        String type = "MOBILE DATA: ";
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        type += tm.getNetworkType() + " - ";

        switch (tm.getNetworkType()) {

            case TelephonyManager.NETWORK_TYPE_1xRTT:
                type += "Current network is 1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                type += "Current network is CDMA: Either IS95A or IS95B (2G)";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type += "Current network is EDGE (2G)";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                type += "Current network is eHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                type += "Current network is EVDO revision 0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                type += "Current network is EVDO revision A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                type += "Current network is EVDO revision B";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type += "Current network is GPRS";
                break;

            case TelephonyManager.NETWORK_TYPE_GSM:
                type += "Current network is GSM";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type += "Current network is HSDPA (3G)";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                type += "Current network is HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                type += "Current network is HSPA+ (4G)";
                break;

            case TelephonyManager.NETWORK_TYPE_HSUPA:
                type += "Current network is HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                type += "Current network is iDen";
                break;

            case TelephonyManager.NETWORK_TYPE_IWLAN:
                type += "Current network is IWLAN";
                break;

            case TelephonyManager.NETWORK_TYPE_LTE:
                type += "Current network is LTE";
                break;

            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:

                type += "Current network is TD_SCDMA";
                break;

            case TelephonyManager.NETWORK_TYPE_UMTS:
                type += "Current network is UMTS";
                break;

            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                type += "Network type is unknown";
                break;

        }

        return type;
    }

}