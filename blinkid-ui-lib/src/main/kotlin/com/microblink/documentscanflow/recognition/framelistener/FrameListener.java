package com.microblink.documentscanflow.recognition.framelistener;

import androidx.annotation.Nullable;
import com.microblink.image.Image;

public interface FrameListener {

    void onFrameAvailable(@Nullable Image frame);

    FrameListener EMPTY = new FrameListener() {
        @Override
        public void onFrameAvailable(@Nullable Image frame) {
        }
    };

}
