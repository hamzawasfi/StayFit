package com.example.myapplication.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_DATE_CHANGED.equals(intent.getAction())){
            Log.d("hi", "i am working");
        }
    }
}
