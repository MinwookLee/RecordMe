package com.seclab.recordme;

import static android.telephony.TelephonyManager.EXTRA_INCOMING_NUMBER;
import static android.telephony.TelephonyManager.EXTRA_STATE;
import static android.telephony.TelephonyManager.EXTRA_STATE_IDLE;
import static android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CallObserver extends BroadcastReceiver {
    private final CallRecorder callRecorder;
    private boolean canRecord;
    private String lastState;

    public CallObserver() {
        callRecorder = new CallRecorder();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && !action.equals("android.intent.action.PHONE_STATE")) {
            return;
        }

        Bundle extras = intent.getExtras();
        canRecord = canRecord && extras.getBoolean("record", false);
        if (!canRecord) {
            return;
        }

        String nowState = extras.getString(EXTRA_STATE);
        if (nowState == null || nowState.equals(lastState)) {
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