package com.example.comsol.utils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
    private static final String KEY_PROJECT = "KEY_PROJECT";
    private static final String KEY_SITE = "KEY_SITE";
    private static final String KEY_OPERATOR = "KEY_OPERATOR";
    private static final String KEY_SUBCON = "KEYSUBCON";



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

    public boolean storeProject(String project){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PROJECT, project);
        editor.apply();
        return true;
    }

    public boolean storeSite(String site){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SITE, site);
        editor.apply();
        return true;
    }

    public boolean storeOperator(String operator){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_OPERATOR, operator);
        editor.apply();
        return true;
    }

    public boolean storeSubCon(String subcon){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SUBCON, subcon);
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

    public String getProject(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PROJECT,null);
    }

    public String getSite(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SITE,null);
    }

    public String getSubCon(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SUBCON,null);
    }

    public String getOperator(){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OPERATOR,null);
    }

    public void clearData(){
       removePreference(KEY_PREF,KEY_ADDRESS);
       removePreference(KEY_PREF,KEY_PROJECT);
       removePreference(KEY_PREF,KEY_SITE);
       removePreference(KEY_PREF,KEY_OPERATOR);
       removePreference(KEY_PREF,KEY_SUBCON);
    }


    protected void removePreference(String prefsName,
                                    String key) {
        SharedPreferences preferences = context.getSharedPreferences(prefsName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
