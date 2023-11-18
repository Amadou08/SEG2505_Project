package com.example.novigradservice.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Constant {

    public static String getUserId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("id","");
    }
    public static void setUserId(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("id", s).commit();
    }
    public static boolean getCustomerLoginStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("Customer", false);
    }
    public static void setCustomerLoginStatus(Context context , boolean s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("Customer", s).commit();
    }
    public static boolean getServiceLoginStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("Service", false);
    }
    public static void setServiceLoginStatus(Context context , boolean s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("Service", s).commit();
    }
    public static String getUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("user", "");
    }
    public static void setUsername(Context context , String s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("user", s).commit();
    }

    public static boolean getAdminLoginStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("Admin", false);
    }
    public static void setAdminLoginStatus(Context context , boolean s){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("Admin", s).commit();
    }
}
