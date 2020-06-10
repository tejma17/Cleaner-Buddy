package com.example.garbagecleaner.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPreference {

    SharedPreferences sharedPreferences;
    Context context;
    private String filename;

    public String getFilename() {
        filename = sharedPreferences.getString("userData","");
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        sharedPreferences.edit().putString("userData", filename).commit();
    }

    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }


    public UserSharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

}
