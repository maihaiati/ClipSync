package com.mtechdwork.clipsync;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.crypto.tink.aead.AeadConfig;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final boolean debug = true;
    private SettingManager settingManager;
    private BroadcastHandler broadcastHandler;
    private TCPHandler tcpHandler;

    // VIEWS
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch swEnableSync;
    Button btnPassChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addViews();

        XChaChaCrypto.init();

        btnPassChange.setOnClickListener(v -> {
            Intent intent = new Intent(this, PassChange.class);
            startActivity(intent);
        });

        // Check Accessibility permission
        isAccessibilityServiceEnabled(this, AccessibilityService.class);

        // Read data file & init
        settingManager = new SettingManager(this);

        if (settingManager.isEnable()) {
            swEnableSync.setChecked(true);
            initListenerThreads();
        } else {
            swEnableSync.setChecked(false);
            killThreads("ClipSync_BroadcastHandler");
            killThreads("ClipSync_TCPHandler");
        }

        // EVENTS
        // Clipboard changed listener (For Android API < 29)
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(() -> {
                ClipData clipData = clipboardManager.getPrimaryClip();
                if (clipData != null && settingManager.isEnable()) {
                    String clipText = String.valueOf(clipData.getItemAt(0).getText());

                    if (debug) Log.i("[Clipboard Manager]", "Clipboard changed: " + clipText);
                    ClipboardData.setLatestData(clipText);

                    Communication communication = new Communication(this);
                    communication.sendBroadcast();
                }
        });

        swEnableSync.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingManager.setEnable(isChecked);

            if (isChecked) initListenerThreads();
            else {
                killThreads("ClipSync_BroadcastHandler");
                killThreads("ClipSync_TCPHandler");
            }
        });
    }

    private void initListenerThreads() {
        killThreads("ClipSync_BroadcastHandler");
        killThreads("ClipSync_TCPHandler"); // Kill old threads

        broadcastHandler = new BroadcastHandler(this);
        broadcastHandler.setName("ClipSync_BroadcastHandler");
        Log.i("[Main Activity]", "Starting new BroadcastHandler thread");
        broadcastHandler.start();

        tcpHandler = new TCPHandler(this);
        tcpHandler.setName("ClipSync_TCPHandler");
        Log.i("[Main Activity]", "Starting new TCPHandler thread");
        tcpHandler.start();
    }

    private void killThreads(String threadName) {
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();

        for (Thread thread : allThreads.keySet()) {
            if (thread.getName().contains(threadName)) {  // Kill thread có liên quan
                thread.interrupt();
            }
        }
    }

    private void addViews() {
        swEnableSync = findViewById(R.id.swEnableSync);
        btnPassChange = findViewById(R.id.btnChangeInfo);
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