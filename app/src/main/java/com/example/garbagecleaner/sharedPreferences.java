package com.example.garbagecleaner;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPreferences {

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


    public sharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

}
