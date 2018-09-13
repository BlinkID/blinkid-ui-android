package com.microblink;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.microblink.documentscanflow.recognition.resultentry.ResultEntry;
import com.microblink.documentscanflow.recognition.util.ImageStorage;
import com.microblink.image.Image;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_ENTRIES = "com.microblink.resultEntries";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setupResultFields();
        setupImages();
    }

    private void setupResultFields() {
        StringBuilder textBuilder = new StringBuilder();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        ArrayList<Parcelable> entries = extras.getParcelableArrayList(EXTRA_RESULT_ENTRIES);
        if (entries == null) {
            return;
        }

        for (Parcelable entry : entries) {
            if (entry instanceof ResultEntry) {
                ResultEntry stringEntry = (ResultEntry) entry;
                textBuilder.append(stringEntry.getKey());
                textBuilder.append(" : ");
                textBuilder.append(stringEntry.getValue().toString());
                textBuilder.append("\n");
            }
        }

        TextView resultEntriesTv = findViewById(R.id.results);
        resultEntriesTv.setText(textBuilder.toString());
    }

    private void setupImages() {
        setupImage(R.id.card_front_image, ImageStorage.getFrontSideDocumentImage());
        setupImage(R.id.card_back_image, ImageStorage.getBackSideDocumentImage());
        setupImage(R.id.face_image, ImageStorage.getFaceImage());
        setupImage(R.id.signature_image, ImageStorage.getSignatureImage());
    }

    private void setupImage(@IdRes int imageViewId, Image image) {
        ImageView imageView = findViewById(imageViewId);
        if (image != null) {
            imageView.setImageBitmap(image.convertToBitmap());
        }
    }

    @Override
    protected void onDestroy() {
        ImageStorage.clearImages();
        super.onDestroy();
    }

}
