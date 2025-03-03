package com.mtechdwork.clipsync;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_popup);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            Log.i("[Popup Activity]", "CLIPBOARD CHANGED");
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null) {
                String clipText = String.valueOf(clipData.getItemAt(0).getText());
                Communication communication = new Communication(this);
                communication.sendBroadcast();
                Log.i("[Popup Activity]", "Clipboard changed: " + clipText);
            } else Log.i("[Popup Activity]", "Clipboard changed: null");
            finish();
            overridePendingTransition(0, 0);
        }
    }
}