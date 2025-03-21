package com.mtechdwork.clipsync;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

public class ClipboardData {
    private static String LATEST_DATA;

    public static String getLatestData() {
        return LATEST_DATA;
    }

    public static void setLatestData(String latestData) {
        LATEST_DATA = latestData;
    }

    /** @noinspection SameParameterValue*/
    private static void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = false;
        if (!debug) return;
        String className = "[Clipboard Data]";
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

    public static void writeClipboard(Context context, String text) {
        if (!text.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            setLatestData(text);

            log("Writed to clipboard: " + text, 0);
        }
    }
}
