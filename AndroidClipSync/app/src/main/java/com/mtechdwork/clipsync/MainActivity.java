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

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SettingManager settingManager;

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

        XChaChaCrypto.init(this);

        btnPassChange.setOnClickListener(v -> {
            Intent intent = new Intent(this, PassChange.class);
            startActivity(intent);
        });

        // Check Accessibility permission
        isAccessibilityServiceEnabled(this);

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

                    log("Clipboard changed: " + clipText, 0);
                    if (clipText.equals(ClipboardData.getLatestData())) {
                        log("Ignored self-triggered clipboard change", 0);
                        return;
                    }
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

    /** @noinspection SameParameterValue*/
    private void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = false;
        if (!debug) return;
        String className = "[MainActivity]";
        switch (type) {
            case 0:
                Log.i(className, message);
                break;

            case 1:
                Log.w(className, message);
                break;

            case 2:
                Log.e(className, message);
        }
    }

    private void initListenerThreads() {
        killThreads("ClipSync_BroadcastHandler");
        killThreads("ClipSync_TCPHandler"); // Kill old threads

        BroadcastHandler broadcastHandler = new BroadcastHandler(this);
        broadcastHandler.setName("ClipSync_BroadcastHandler");
        log("Starting new BroadcastHandler thread", 0);
        broadcastHandler.start();

        TCPHandler tcpHandler = new TCPHandler(this);
        tcpHandler.setName("ClipSync_TCPHandler");
        log("Starting new TCPHandler thread", 0);
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

    /** @noinspection UnusedReturnValue*/
    private boolean isAccessibilityServiceEnabled(Context context) {
        String serviceId = context.getPackageName() + "/" + AccessibilityService.class.getName();

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
            log(e.getMessage(), 2);
        }

        showEnableAccessibilityDialog(context);
        return false;
    }
}