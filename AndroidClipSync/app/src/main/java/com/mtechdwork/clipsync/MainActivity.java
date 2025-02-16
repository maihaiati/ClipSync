package com.mtechdwork.clipsync;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    boolean debug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For Android API < 29
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(() -> {
            if (debug) Log.i("[Clipboard Manager]", "CLIPBOARD CHANGED");
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null) {
                String clipText = String.valueOf(clipData.getItemAt(0).getText());
                if (debug) Log.i("[Clipboard Manager]", "Clipboard changed: " + clipText);
            }
        });

        // Kiểm tra xem ứng dụng có quyền truy cập Accessibility chưa
        if (isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "Accessibility service is enabled.", Toast.LENGTH_SHORT).show();
        } else {
            // Kích hoạt dịch vụ Accessbility
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        // Kiểm tra xem Accessibility Service đã được kích hoạt chưa
        int enabled = android.provider.Settings.Secure.getInt(getContentResolver(),
                "accessibility_enabled", 0);
        return enabled == 1;
    }
}