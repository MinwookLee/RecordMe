package com.seclab.recordme;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallRecorder {
    private MediaRecorder recorder;

    public void startRecording(Context context, String phoneNumber) {
        String voiceDirStr = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            voiceDirStr = context.getDataDir().getAbsolutePath() + "/voice";
        }

        File voiceDir = new File(voiceDirStr);
        if (!voiceDir.exists()) {
            voiceDir.mkdir();
        }

        String dateStr = getDateStr();
        String fileName = voiceDirStr + "/" + phoneNumber + "_" + dateStr + ".m4a";

        try {
            if (recorder == null) {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // if you got an error about recorder starting, you may try another encoding according to the device
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setAudioEncodingBitRate(16);
                recorder.setAudioSamplingRate(44100);
            }

            recorder.setOutputFile(fileName);

            recorder.prepare();
            recorder.start();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (recorder == null) {
            return;
        }

        recorder.stop();
    }

    private String getDateStr() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd_hhmmss");

        return dateFormat.format(date);
    }

}
