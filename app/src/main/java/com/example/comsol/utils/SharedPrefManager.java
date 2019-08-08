package com.example.comsol.utils;
import android.content.Context;

/**
 * By Usman Siddiqui
 */
public class SharedPrefManager {
    private static SharedPrefManager INSTANCE;
    private static Context context;

    private static final String KEY_PREF = "KEY_PREF";
    private static final String KEY_ADDRESS = "KEY_ADDRESS";
    private static final String KEY_LAT = "KEY_LAT";
    private static final String KEY_LONG = "KEY_LONG";

    private SharedPrefManager(Context context){
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
      if(INSTANCE==null){
          INSTANCE = new SharedPrefManager(context);
      }
      return INSTANCE;
    }

    public boolean storeAddress(String address){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ADDRESS,address);
        editor.apply();
        return true;
    }
    public boolean storeLatitude(Double latitude){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LAT, Double.doubleToRawLongBits(latitude));
        editor.apply();
        return true;
    }

    public boolean storeLongitude(Double longitude){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LONG, Double.doubleToRawLongBits(longitude));
        editor.apply();
        return true;
    }

    public String getAddress(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDRESS,null);
    }

    public Double getLat(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong(KEY_LAT, 0));
    }

    public Double getLong(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong(KEY_LONG, 0));
    }
}
