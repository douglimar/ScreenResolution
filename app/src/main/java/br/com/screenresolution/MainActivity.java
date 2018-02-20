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
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
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


        verifyStoragePermissions2(MainActivity.this);

        TextView textView = findViewById(R.id.textView);


        textView.setText(getHardwareInfo()
                + "\n============================\n"
                + getHardwareConfiguration()
                + "\n============================\n"
                + getBatteryStatus2(getBaseContext())
                + "\n============================\n"
                + getScreenResolution()
                + "\n============================\n"
                + getIMEINumber());

        Toast.makeText(getApplicationContext(), "OS: " +  getKernelVersion(), Toast.LENGTH_LONG).show();
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
                    Utils.PERMISSIONS_READ_PHONE_STATE,
                    Utils.REQUEST_READ_PHONE_STATE
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

                IMEI = telephonyManager.getDeviceId();

            } else {
                IMEI = telephonyManager.getDeviceId(1) + "\n" + telephonyManager.getDeviceId(2);

            }
        }


        result = "IMEI: " + IMEI + "\n"+
                "SW Version: " + telephonyManager.getDeviceSoftwareVersion()+ "\n" +
                "SIM Country: " + telephonyManager.getSimCountryIso() + "\n" +
                "SIM Operator: " + telephonyManager.getSimOperator() + "\n" +
                "SIM Operator Name: " + telephonyManager.getSimOperatorName() + "\n" +
                "SIM State: " + telephonyManager.getSimState() + "\n"+
                "SIM Serial No: " + telephonyManager.getSimSerialNumber() +"\n\n\n\n\n\n";



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
                "Manufacturer: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "DEVICE: " + Build.DEVICE + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "User: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK:  " + Build.VERSION.SDK + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE + "\n" +
                "Codename: " + Build.VERSION.CODENAME + "\n";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text = text + "Security Path: " + Build.VERSION.SECURITY_PATCH + "\n" +
                    "BASE OS: " + Build.VERSION.BASE_OS ;
        }

        return text;
    }

    public String getHardwareConfiguration() {

        String text = "Configurations: " + getResources().getConfiguration() + "\n" +
                "Configurations: Density DPI: " + getResources().getConfiguration().densityDpi + "\n" +
                "Configurations: Describe Contents: " + getResources().getConfiguration().describeContents() + "\n" +
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

        return "Battery level: " + level + "\n"
                + "Battery Scale: " + scale
                + " Is Charging: " + isCharging + "\n"
                + " usbCharge: " + usbCharge + "\n"
                + " acCharge: " + acCharge;
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

        if (between(density, 1,120))
            sDensity = sDensity + "Dpi | LDPI (Low Density)";
        else if (between(density, 121,160))
            sDensity = sDensity + "Dpi | MDPI (Medium Density)" ;
        else if (between(density,161,240))
            sDensity = sDensity + "Dpi | HDPI (High Density)" ;
        else if (between(density,241,320))
            sDensity = sDensity + "Dpi | XHDPI (Extra High Density)" ;
        else if (between(density,321,480))
            sDensity = sDensity + "Dpi | XXHDPI (Extra Extra High Density)" ;
        else  if (density > 480)
            sDensity = sDensity + "Dpi | XXXHDPI (Extra Extra Extra High Density)" ;

        // Exibindo o resultado
    	/*
    	tvResolution.setText( "" + sPixels);
    	tvHeight.setText(height+"px / " + width +"px");
    	//tvWidth.setText(width+"px");
        tvDensity.setText(sDensity); */

        //textView.setText(height+"px / " + width +"px\n" + sDensity);
        //textView.setText(sPixels + "\n" + sDensity);

        //Toast.makeText(getApplicationContext(), "width " + fullscreenActivity.getMeasuredWidth()  + " - height " + fullscreenActivity.getMeasuredHeight(), Toast.LENGTH_SHORT).show();
        return  sPixels + "\n" + sDensity;

    }

    public String getKernelVersion() {

        return System.getProperty("os.version");
/*
        try {
            Runtime.getRuntime().exec("uname -r");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }



}
