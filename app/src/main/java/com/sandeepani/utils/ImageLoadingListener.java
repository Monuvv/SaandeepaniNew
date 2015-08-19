package com.sandeepani.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by gopi on 6/2/15.
 */
public class ImageLoadingListener extends SimpleImageLoadingListener {

    private ProgressBar pBar;

    public ImageLoadingListener(ProgressBar pBar) {
        this.pBar = pBar;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        String message = null;
        switch (failReason.getType()) {
            case IO_ERROR:
                message = "Input/Output error";
                break;
            case DECODING_ERROR:
                message = "Image can't be decoded";
                break;
            case NETWORK_DENIED:
                message = "Downloads are denied";
                break;
            case OUT_OF_MEMORY:
                message = "Out Of Memory error";
                break;
            case UNKNOWN:
                message = "Unknown error";
                break;
        }
        pBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        pBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        pBar.setVisibility(View.GONE);
    }
}