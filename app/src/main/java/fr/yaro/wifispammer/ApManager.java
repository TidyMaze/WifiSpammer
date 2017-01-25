package fr.yaro.wifispammer;

import android.content.*;
import android.net.wifi.*;
import java.lang.reflect.*;

public class ApManager {

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    // toggle wifi hotspot on or off
    public static boolean setApState(Context context, boolean activated) {
        if(isApOn(context) == activated) return false;

        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if(activated && wifimanager.isWifiEnabled()) {
                wifimanager.setWifiEnabled(false);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, activated);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setHotspotName(String newName, Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);
            wifiConfig.SSID = newName;
            Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifiManager, wifiConfig);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void sleepABit(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitApOn(Context context){
        while(!isApOn(context)){
            sleepABit();
        }
    }

    public static void waitApOff(Context context){
        while(isApOn(context)){
            sleepABit();
        }
    }
} // end of class