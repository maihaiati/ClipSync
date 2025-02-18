package com.mtechdwork.clipsync;

import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

import java.util.Objects;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private final boolean debug = true;

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void onClipboardChanged() {
        Intent intent = new Intent(this, PopupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void writeClipboard(String text) {
//        if (!text.isEmpty()) {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("Copied Text", selectedText);
//                clipboard.setPrimaryClip(clip);
//
//                Log.d("Accessibility", "Đã ghi vào Clipboard: " + selectedText);
//            }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                    CharSequence className = accessibilityEvent.getClassName();
                    String viewText = getEventText(accessibilityEvent);

                    if (debug) {
                        if (className != null)
                            Log.i("[Accessibility Service]", "Classname of view: " + className);
                        Log.i("[Accessibility Service]", "Clicked view text: " + viewText);
                    }

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
                if (debug) Log.e("[Accessibility Service]", "Exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void onInterrupt() {
        if (debug) Log.w("[Accessibility Service]" , "Service interrupt!");
    }
}
