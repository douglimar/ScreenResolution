package br.com.screenresolution;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class HwInfoActivity extends AppCompatActivity {

    private int iAvailableRAM;
    private int iTotalRAM;
    private int iUsedRAM;

    // Storage Permissions
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final String[] PERMISSIONS_READ_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hwinfo);


        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        verifyStoragePermissions2(HwInfoActivity.this);

        TextView tvHWInfo = findViewById(R.id.tvHardwareInfo);
        TextView tvHWConfig = findViewById(R.id.tvHardwareConfig);
        TextView tvBatteryStatus = findViewById(R.id.tvBatteryStatus);
        TextView tvRAMMemoryInfo = findViewById(R.id.tvRAMInfo);
        //TextView tvDataType = findViewById(R.id.tvDataType);
        //TextView tvDeviceInch = findViewById(R.id.tvDeviceInch);
        //TextView tvKernel = findViewById(R.id.tvKernel);
        //TextView tvSDCard = findViewById(R.id.tvSDCard);
        TextView tvIMEIInfo = findViewById(R.id.tvIMEIInfo);
        TextView tvScreenRes = findViewById(R.id.tvScreenRes);

        tvScreenRes.setText(getScreenResolution());
        //tvScreenRes.setText(getScreenResolution() + "\n\n" + getRAMInfoNewGraph());


        tvHWInfo.setText(getHardwareInfo().concat("\n" + getStorageInfoFeb25()));

        tvHWConfig.setText(getHardwareConfiguration());

        tvBatteryStatus.setText(getBatteryStatus2(getBaseContext()));

        tvRAMMemoryInfo.setText(getRAMInfo_WithGraph());

        //tvSDCard.setText(getSDCardStatus()+ "\n" + getSDCardFreeSpace3());

        //tvKernel.setText(getKernelVersion());

        //tvDataType.setText();

        //tvDeviceInch.setText(getDeviceInch(this));

        tvIMEIInfo.setText(getDataType(this).concat("\n" + getIMEINumber()));

        //Toast.makeText(getApplicationContext(), "Toolbar Height: " +  toolbar.getHeight(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "Imei: " +  getIMEINumber(), Toast.LENGTH_LONG).show();


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Screen2");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        // Create a AdView
        // Load Advertisement Banner
        AdView adView1 = findViewById(R.id.adViewHwInfo1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);

        // Create a AdView
        // Load Advertisement Banner
        AdView adView2 = findViewById(R.id.adViewHwInfo2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);

        // Create a AdView
        // Load Advertisement Banner
        AdView adView3 = findViewById(R.id.adViewHwInfo3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
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

    @SuppressLint("HardwareIds")
    private String getIMEINumber()  {
        String result;

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
            assert telephonyManager != null;
            int iSIM_Total = telephonyManager.getPhoneCount();

            if (iSIM_Total == 1) {

                IMEI = "IMEI: " + telephonyManager.getDeviceId();

            } else {
                IMEI = "IMEI 1: " + telephonyManager.getDeviceId(1) + "\n"
                     + "IMEI 2: " + telephonyManager.getDeviceId(2);
            }
        }

        assert telephonyManager != null;
        result = IMEI + "\n"+
                getString(R.string.sw_version) + telephonyManager.getDeviceSoftwareVersion()+ "\n" +
                getString(R.string.sim_country) + telephonyManager.getSimCountryIso() + "\n" +
                getString(R.string.sim_operator) + telephonyManager.getSimOperator() + "\n" +
                getString(R.string.sim_operator_name) + telephonyManager.getSimOperatorName() + "\n" +
                getString(R.string.sim_state) + telephonyManager.getSimState() + "\n"+
                getString(R.string.sim_serial) + telephonyManager.getSimSerialNumber();



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


    private String getHardwareInfo() {

        @SuppressLint("HardwareIds") String text =getString(R.string.Serial) + Build.SERIAL + "\n" +
                getString(R.string.model) + Build.MODEL + "\n" +
                getString(R.string.id) + Build.ID + "\n" +
                getString(R.string.Manufacturer) + Build.MANUFACTURER + "\n" +
                getString(R.string.Product) + Build.PRODUCT + "\n"+
                getString(R.string.brand) + Build.BRAND + "\n" +
                getString(R.string.device) + Build.DEVICE + "\n" +
                getString(R.string.type) + Build.TYPE + "\n" +
                getString(R.string.user) + Build.USER + "\n" +
                getString(R.string.base) + Build.VERSION_CODES.BASE + "\n" +
                getString(R.string.incremental) + Build.VERSION.INCREMENTAL + "\n" +
                getString(R.string.sdk) + Build.VERSION.SDK + "\n" +
                getString(R.string.board) + Build.BOARD + "\n" +
                getString(R.string.host) + Build.HOST + "\n" +
                getString(R.string.fingerprint) + Build.FINGERPRINT + "\n" +
                getString(R.string.version_code) + Build.VERSION.RELEASE + "\n" +
                getString(R.string.codename) + Build.VERSION.CODENAME + "\n";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text += getString(R.string.securith_patch) + Build.VERSION.SECURITY_PATCH + "\n" +
                    getString(R.string.base_os) + Build.VERSION.BASE_OS;
        }

        text+= "\n" + getKernelVersion();


        return text;
    }

    private String getHardwareConfiguration() {

        return getString(R.string.configurations) + getResources().getConfiguration() + "\n" +
               getString(R.string.density_dpi) + getResources().getConfiguration().densityDpi + "\n" +
               getString(R.string.describe_contents) + getResources().getConfiguration().describeContents() + "\n" +
               getString(R.string.screen_brightness) + Settings.System.SCREEN_BRIGHTNESS + "\n" +
               getString(R.string.SCREEN_BRIGHTNESS_MODE) + Settings.System.SCREEN_BRIGHTNESS_MODE + "\n"+
               getString(R.string.RINGTONE) + Settings.System.RINGTONE + "\n" +
               getString(R.string.DISPLAY_SERVICE) + DISPLAY_SERVICE + "\n";
    }

    private String getBatteryStatus2(Context context) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        String sCharging, sUSB_Charge, sAC_Charge;

        if (isCharging)
            sCharging = getResources().getString(R.string.strCharging);
        else
            sCharging = getResources().getString(R.string.strDischarging);

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (usbCharge)
            sUSB_Charge = getResources().getString(R.string.yes);
        else
            sUSB_Charge = getResources().getString(R.string.no);

        if (acCharge)
            sAC_Charge = getResources().getString(R.string.yes);
        else
            sAC_Charge = getResources().getString(R.string.no);


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        //float batteryPct = level / (float)scale;

        return getString(R.string.Battery_Level_Label) + level + "%\n"
                //+ getString(R.string.Battery_Scale_Label) + scale + "\n"
                + getString(R.string.IsCharging_Label) + sCharging + "\n"
                + getString(R.string.ChargingByUSB_Label) + sUSB_Charge + "\n"
                + getString(R.string.ChargingByAC_Label) + sAC_Charge;
    }


    private static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        return i >= minValueInclusive && i <= maxValueInclusive;
    }


    private String getScreenResolution() {

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
            sDensity += getString(R.string.dpi_LowDensity);
        else if (between(density, 121,160))
            sDensity += getString(R.string.dpi_mediumDensity) ;
        else if (between(density,161,240))
            sDensity += getString(R.string.dpi_highDensity) ;
        else if (between(density,241,320))
            sDensity += getString(R.string.dpi_extra_high_density) ;
        else if (between(density,321,480))
            sDensity += getString(R.string.dpi_extra_extra_high_density) ;
        else  if (density > 480)
            sDensity += getString(R.string.dpi_extra_extra_extra_high_density) ;

        return  sPixels + "\n" + sDensity;

    }

    private String getKernelVersion() {

        return getString(R.string.kernel_version) + System.getProperty("os.version");

    }

    /*
    public String getRAMInfo() {


        iUsedRAM = getUsedRAM2();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(0, iTotalRAM),
                new DataPoint(1, iAvailableRAM),
                new DataPoint(2, iUsedRAM),
                new DataPoint(3, 0),
        }) ;
        graph.addSeries(series);

        // styling

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                //return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(5);

        series.setTitle("Total of RAM");

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);


        return getTotalRAMInfo() + "\n" + getAvailableRAMInfo() + "\n" + "RAM Used Memory: " + getUsedRAM2();
    }
*/

    private String getRAMInfo_WithGraph() {

        BarChart chart = findViewById(R.id.chart);

        //MP Graph
        BarData data = new BarData(getXAxisMemValues(), getDataSet());
        chart.setData(data);
        chart.setDescription(getString(R.string.RAM_Memory_Info));
        chart.animateXY(2000, 2000);
        chart.invalidate();

        iUsedRAM = getUsedRAM2();

        return getTotalRAMInfo() + "\n" + getAvailableRAMInfo() + "\n" + getUsedRAM();
    }



    private ArrayList<BarDataSet> getDataSet() {

        getTotalRAMInfo();
        getAvailableRAMInfo();
        getUsedRAM2();

        ArrayList<BarDataSet> dataSets;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(iTotalRAM, 0); // total Mem
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(iUsedRAM, 1); // Used Mem
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(iAvailableRAM, 2); // Avail. Mem
        valueSet1.add(v1e3);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.Label));
        //barDataSet1.setColor(Color.rgb(0, 155, 0));

        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);


        //BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisMemValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add(getString(R.string.RAM_Total));
        xAxis.add(getString(R.string.RAM_Use));
        xAxis.add(getString(R.string.RAM_Avl));
        return xAxis;
    }


    private String getUsedRAM(){

        String sReturnValue;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

        iUsedRAM = (iTotalRAM - iAvailableRAM) * 1024;

        double mb = iUsedRAM / 1024.0;
        double gb = iUsedRAM / 1048576.0;
        //double tb = iUsedRAM / 1073741824.0;

        //iUsedRAM = (int) mb;

        if (gb > 1) {
            sReturnValue = twoDecimalForm.format(gb).concat(" GB");
        } else if (mb > 1) {
            sReturnValue = twoDecimalForm.format(mb).concat(" MB");
        } else {
            sReturnValue = twoDecimalForm.format(iUsedRAM).concat(" KB");
        }

        return getString(R.string.RAM_Used_Memory).concat(sReturnValue);

    }



    private int getUsedRAM2(){

        iUsedRAM = iTotalRAM - iAvailableRAM;

        return  iUsedRAM;
    }

    private String getAvailableRAMInfo() {


        String sReturnValue;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        assert activityManager != null;
        activityManager.getMemoryInfo(mi);

        double availableRAM = mi.availMem;

        //double mb = availableRAM / 1024.0;
        double mb = availableRAM / 1048576.0;
        double gb = availableRAM / 1073741824.0;

        iAvailableRAM = (int) mb;

        if (gb > 1) {
            sReturnValue = twoDecimalForm.format(gb).concat(" GB");
        } else if (mb > 1) {
            sReturnValue = twoDecimalForm.format(mb).concat(" MB");
        } else {
            sReturnValue = twoDecimalForm.format(availableRAM).concat(" KB");
        }

        return getString(R.string.RAM_Avl_Memory) + sReturnValue;
    }


    private String getTotalRAMInfo() {

        String sReturnValue;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        assert activityManager != null;
        activityManager.getMemoryInfo(mi);

        double totalRAM = mi.totalMem;

        //totalRAM = Double.parseDouble(value);
        // totRam = totRam / 1024;

        //double kb = totalRAM / 1024.0;
        double mb = totalRAM / 1048576.0;
        double gb = totalRAM / 1073741824.0;

        iTotalRAM = (int) mb;

        if (gb > 1) {
            sReturnValue = twoDecimalForm.format(gb).concat(" GB");
        } else if (mb > 1) {
            sReturnValue = twoDecimalForm.format(mb).concat(" MB");
        } else {
            sReturnValue = twoDecimalForm.format(totalRAM).concat(" KB");
        }

        return getString(R.string.RAM_total_Memory) + sReturnValue;

    }

    /*
    public String getMemInfoFeb25() {

        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");


        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        double availableMegs = mi.availMem / 1048576L;
        double totalMemory = mi.totalMem / 1073741824.0; //1048576.0; //1048576L;

        String totalMemoryFormatted = twoDecimalForm.format(totalMemory).concat(" GB");
        String availableMegsFormatted = twoDecimalForm.format(availableMegs).concat(" MB");


        return "MEM Info (Available): " + availableMegsFormatted + "\n" + "MEM INfo (TOTAL MEM): " + totalMemoryFormatted
                + "\n RAM IN MEM Info" + getTotalRAMInMemInfo();
    }
*/

    /*
    public String getTotalRAMInMemInfo() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }
*/

    private String getStorageInfoFeb25(){

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getBlockCountLong();
        long megAvailable = bytesAvailable / 1048576L;
        double gigaAvailable = bytesAvailable / 1073741824;

        return "Storage Info: " + megAvailable + "MB | " + gigaAvailable + " GB";
    }

    private static String getDataType(Context activity) {
        String type = "MOBILE DATA: ";
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        assert tm != null;
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