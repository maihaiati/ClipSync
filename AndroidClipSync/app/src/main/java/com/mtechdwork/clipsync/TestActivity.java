package com.mtechdwork.clipsync;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            Log.i("[Test Activity]", "CLIPBOARD CHANGED");
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null) {
                String clipText = String.valueOf(clipData.getItemAt(0).getText());
                Log.i("[Test Activity]", "Clipboard changed: " + clipText);
            } else Log.i("[Test Activity]", "Clipboard changed: null");
            finish();
        }
    }
}