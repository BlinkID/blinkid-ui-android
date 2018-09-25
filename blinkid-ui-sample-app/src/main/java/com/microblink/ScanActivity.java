package com.microblink;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.microblink.documentscanflow.BaseDocumentScanActivity;
import com.microblink.documentscanflow.ScanFlowListener;
import com.microblink.documentscanflow.country.Country;
import com.microblink.documentscanflow.country.SupportedCountry;
import com.microblink.documentscanflow.document.Document;
import com.microblink.documentscanflow.document.DocumentType;
import com.microblink.documentscanflow.recognition.RecognitionResult;
import com.microblink.documentscanflow.recognition.framelistener.FrameGrabberMode;
import com.microblink.documentscanflow.recognition.framelistener.FrameListener;
import com.microblink.documentscanflow.recognition.util.ImageStorage;
import com.microblink.documentscanflow.ui.documentchooser.DefaultDocumentChooser;
import com.microblink.documentscanflow.ui.documentchooser.DocumentChooser;
import com.microblink.documentscanflow.ui.scantimeouthandler.DefaultScanTimeoutHandler;
import com.microblink.documentscanflow.ui.scantimeouthandler.ScanTimeoutHandler;
import com.microblink.documentscanflow.ui.splashoverlay.SplashOverlaySettings;
import com.microblink.image.Image;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class ScanActivity extends BaseDocumentScanActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScanSuccessPlayer().setSoundEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @NonNull
    @Override
    protected Document getInitialDocument() {
        Country country = SupportedCountry.CROATIA;
        DocumentType documentType = country.getSupportedDocumentTypes().get(0);
        return new Document(country, documentType);
    }

    @Override
    public boolean shouldScanBothDocumentSides() {
        return false;
    }

    @NonNull
    @Override
    public ScanFlowListener createScanFlowListener() {
        return new ScanFlowListener() {
            @Override
            public void onScanStarted() {
            }

            @Override
            public void onSelectedDocumentChanged(@NonNull Document newDocument) {
            }

            @Override
            public void onEntireDocumentScanned(@NonNull RecognitionResult recognitionResult, Image successFrame) {
                ImageStorage.addImages(recognitionResult);
                Intent intent = new Intent(ScanActivity.this, ResultsActivity.class);
                ArrayList<Parcelable> parcelableArrayList = new ArrayList<>();
                parcelableArrayList.addAll(recognitionResult.getResultEntries());
                intent.putExtra(ResultsActivity.EXTRA_RESULT_ENTRIES, parcelableArrayList);
                startActivity(intent);
            }

            @Override
            public void onFirstSideScanned(@Nullable RecognitionResult recognitionResult, Image successFrame) {
            }
        };
    }

    @NonNull
    @Override
    protected DocumentChooser createDocumentChooser() {
        return new DefaultDocumentChooser(this) {
            @NonNull
            @Override
            public String geChooseCountryLabel() {
                return getString(R.string.change_country_label);
            }
        };
    }

    @NonNull
    @Override
    protected ScanTimeoutHandler createScanTimeoutHandler() {
        // just increase timeout to 5 minutes
        return new DefaultScanTimeoutHandler(this, TimeUnit.MINUTES.toMillis(5), createScanTimeoutListener());
    }

    @NonNull
    @Override
    protected SplashOverlaySettings createSplashOverlaySettings() {
        return new SplashOverlaySettings() {
            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public long getDurationMillis() {
                return TimeUnit.SECONDS.toMillis(2);
            }

            @Override
            public int getBackgroundColorResource() {
                return R.color.bgLight;
            }

            @Override
            public int getLogoDrawableResource() {
                return 0;
            }
        };
    }
}
