package com.seclab.recordme;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] permissions;
    private String channelId;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_func1).setOnClickListener(this);
        findViewById(R.id.btn_func2).setOnClickListener(this);

        permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        channelId = getString(R.string.app_name);
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

    private void requestPermissions() {
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void minimizeThisApp() {
        if (hasPermissions(this, permissions)) {
            Intent intent = new Intent(this, CallObserver.class);
            intent.putExtra("record", true);
            sendBroadcast(intent);

            displayNotification();

            finishAffinity();
        }
    }

    private void terminateThisApp() {
        Intent intent = new Intent(this, CallObserver.class);
        intent.putExtra("record", false);
        sendBroadcast(intent);

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

    private void displayNotification() {
        if (notification == null) {
            setNotificationChannel();

            NotificationCompat.Builder builder = createNotificationBuilder();
            notification = builder.build();
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(100, notification);
    }

    private void setNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelId, "name", IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }

    private NotificationCompat.Builder createNotificationBuilder() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentText("If you want to restore this app, Click here!");
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.logo);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        return builder;
    }
}