package com.microblink;

import android.app.Application;

import com.microblink.MicroblinkSDK;
import com.microblink.intent.IntentDataTransferMode;

public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MicroblinkSDK.setLicenseFile("com.microblink.blinkidui.sample.mblic", this);
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);
    }

}
