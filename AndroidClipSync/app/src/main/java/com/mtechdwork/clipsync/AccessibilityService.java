package com.mtechdwork.clipsync;

import android.view.accessibility.AccessibilityEvent;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;

import java.util.Objects;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private boolean debug = true;
    private String selectedText = "";

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void onClipboardChanged() {
        if (debug) Log.i("[Accessibility Service]", "Clipboard changed: " + selectedText);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        try {
            if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {

                final int selectBegin = Objects.requireNonNull(accessibilityEvent.getSource()).getTextSelectionStart();
                final int selectEnd = Objects.requireNonNull(accessibilityEvent.getSource()).getTextSelectionEnd();
                if (selectBegin == selectEnd) return;

                selectedText = getEventText(accessibilityEvent).substring(selectBegin, selectEnd);
                if (debug) {
                    Log.i("[Accessibility Service]", "Text: " + selectedText);
                    Log.i("[Accessibility Service]", "Select begin: " + selectBegin);
                    Log.i("[Accessibility Service]", "Select end: " + selectEnd);
                }

//            if (!selectedText.isEmpty()) {
//                // Ghi vào Clipboard
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("Copied Text", selectedText);
//                clipboard.setPrimaryClip(clip);
//
//                Log.d("Accessibility", "Đã ghi vào Clipboard: " + selectedText);
//            }

            } else if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                String viewText = getEventText(accessibilityEvent);

                if (debug) Log.i("[Accessibility Service]", "Clicked view text: " + viewText);

                if (viewText.equalsIgnoreCase("Sao chép") ||
                        viewText.equalsIgnoreCase("Copy")) {
                    onClipboardChanged();
                } else if (viewText.equalsIgnoreCase("Cắt") ||
                        viewText.equalsIgnoreCase("Cut")) {
                }
            }
        } catch (Exception e) {
            if (debug) Log.e("[Accessibility Service]", "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onInterrupt() {
        if (debug) Log.w("[Accessibility Service]" , "Service interrupt!");
    }
}
