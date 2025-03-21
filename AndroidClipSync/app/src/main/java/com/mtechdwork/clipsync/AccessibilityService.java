package com.mtechdwork.clipsync;

import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = false;
        if (!debug) return;
        String className = "[Accessibility Service]";
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

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void onClipboardChanged() {
        log("Clipboard changed", 0);
        Intent intent = new Intent(this, PopupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                    CharSequence className = accessibilityEvent.getClassName();
                    String viewText = getEventText(accessibilityEvent);


                    if (className != null)
                        log("Classname of view: " + className, 0);
                    log("Clicked view text: " + viewText, 0);

                    if (className != null && !className.toString().equals("android.widget.EditText")
                            && !(accessibilityEvent.getSource() != null
                            && accessibilityEvent.getSource().isEditable())) {

                        if (viewText.toLowerCase().contains("sao chép") ||
                                viewText.toLowerCase().contains("copy") ||
                                viewText.toLowerCase().contains("cắt") ||
                                viewText.toLowerCase().contains("cut")) {
                            onClipboardChanged();
                        }
                    }
                }
            } catch (Exception e) {
                log("Exception: " + e.getMessage(), 2);
            }
        }
    }

    @Override
    public void onInterrupt() {
        log("Service interrupt!", 1);
    }
}
