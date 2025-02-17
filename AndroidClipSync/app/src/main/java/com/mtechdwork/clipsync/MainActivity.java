package com.mtechdwork.clipsync;

import android.app.AlertDialog;
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
        isAccessibilityServiceEnabled(this, AccessibilityService.class);
    }

    private void showEnableAccessibilityDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Yêu cầu quyền Trợ năng")
                .setMessage("Ứng dụng cần quyền Trợ năng để hoạt động. Hãy bật quyền này trong cài đặt.")
                .setPositiveButton("Đi tới cài đặt", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    context.startActivity(intent);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> serviceClass) {
        String serviceId = context.getPackageName() + "/" + serviceClass.getName();

        try {
            String enabledServices = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            boolean accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;

            if (accessibilityEnabled && enabledServices != null) {
                String[] services = enabledServices.split(":");
                for (String service : services) {
                    if (service.equalsIgnoreCase(serviceId)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        showEnableAccessibilityDialog(context);
        return false;
    }
}