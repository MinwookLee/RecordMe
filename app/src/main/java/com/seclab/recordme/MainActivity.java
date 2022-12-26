package com.seclab.recordme;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_func1).setOnClickListener(this);
        findViewById(R.id.btn_func2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_func1) {
            requestPermissions();
            minimizeThisApp();
        }
        if (viewId == R.id.btn_func2) {
            terminateThisApp();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        minimizeThisApp();
    }

    private void requestPermissions() {
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void minimizeThisApp() {
        if (hasPermissions(this, permissions)) {
            setBoolInPref("can_record", true);
            finishAffinity();
        }
    }

    private void terminateThisApp() {
        setBoolInPref("can_record", false);
        finishAffinity();
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    private void setBoolInPref(String key, boolean flag) {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, flag);
        editor.apply();
    }
}