package com.doubleclick.x_course.FolatingLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.doubleclick.x_course.FolatingLayout.callback.FloatingListener;
import com.doubleclick.x_course.FolatingLayout.component.FloatingComponent;
import com.doubleclick.x_course.FolatingLayout.service.FloatingService;

/**
 * Author: Hamed Taherpour
 * *
 * Created: 7/30/2020
 * *
 * Address: https://github.com/HamedTaherpour
 */
public class FloatingResult extends ResultReceiver {

    private FloatingListener floatingListener;

    public FloatingResult(Handler handler, FloatingListener floatingListener) {
        super(handler);
        this.floatingListener = floatingListener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (floatingListener != null) {
            if (resultCode == FloatingComponent.ACTION_ON_CREATE) {
                if (FloatingService.view != null)
                    floatingListener.onCreateListener(FloatingService.view);
            }
            if (resultCode == FloatingComponent.ACTION_ON_CLOSE) {
                floatingListener.onCloseListener();
            }
        }
        super.onReceiveResult(resultCode, resultData);
    }
}
