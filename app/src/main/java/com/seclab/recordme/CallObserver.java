package com.seclab.recordme;

import static android.content.Context.MODE_PRIVATE;
import static android.telephony.TelephonyManager.EXTRA_INCOMING_NUMBER;
import static android.telephony.TelephonyManager.EXTRA_STATE;
import static android.telephony.TelephonyManager.EXTRA_STATE_IDLE;
import static android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class CallObserver extends BroadcastReceiver {
    private String lastState;
    private final CallRecorder callRecorder;

    public CallObserver() {
        callRecorder = new CallRecorder();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.equals("android.intent.action.PHONE_STATE")) {
            return;
        }

        SharedPreferences preferences = context.getSharedPreferences("pref", MODE_PRIVATE);
        boolean canRecord = preferences.getBoolean("can_record", false);
        if (!canRecord) {
            return;
        }

        Bundle extras = intent.getExtras();
        String nowState = extras.getString(EXTRA_STATE);
        if (nowState.equals(lastState)) {
            return;
        } else {
            lastState = nowState;
        }

        if (nowState.equals(EXTRA_STATE_OFFHOOK)) {
            String phoneNumber = extras.getString(EXTRA_INCOMING_NUMBER);
            callRecorder.startRecording(context, phoneNumber);
        } else if (nowState.equals(EXTRA_STATE_IDLE)) {
            callRecorder.stopRecording();
        }
    }
}
