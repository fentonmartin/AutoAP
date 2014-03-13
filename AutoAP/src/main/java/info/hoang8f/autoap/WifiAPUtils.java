package info.hoang8f.autoap;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tranvu on 3/12/14.
 */
public class WifiAPUtils {

    public static String SECURE_OPEN = "Open";
    public static String SECURE_WPA = "WPA";
    public static String SECURE_WPA2 = "WPA2";
    public static int PASS_MIN_LENGHT = 8;

    WifiManager mWifiManager;
    Context context;
    String ssid = "Auto AP";
    String securityType = SECURE_OPEN;
    String password = "12345678";

    public WifiAPUtils(Context context) {
        this.context = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean setAP(boolean shouldOpen) {
        WifiConfiguration wifi_configuration = new WifiConfiguration();
        wifi_configuration.SSID = ssid;
        if (securityType.equals(SECURE_OPEN)) {
            wifi_configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        } else {
//            wifi_configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifi_configuration.enterpriseConfig.setPassword(password);
        }
        mWifiManager.setWifiEnabled(false);
        try {
            //USE REFLECTION TO GET METHOD "SetWifiAPEnabled"
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, wifi_configuration, shouldOpen);
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void enableWifi() {
        mWifiManager.setWifiEnabled(true);
    }

    public boolean isWifiApEnable() {
        boolean isWifiApEnable = false;
        Method[] mMethods = mWifiManager.getClass().getDeclaredMethods();
        for (Method method : mMethods) {
            if (method.getName().equals("isWifiApEnabled")) {
                try {
                    isWifiApEnable = (Boolean) method.invoke(mWifiManager);
                } catch (IllegalArgumentException e) {

                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
                break;
            }
        }
        return isWifiApEnable;
    }
}
