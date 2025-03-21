package com.mtechdwork.clipsync;

import android.util.Log;

import org.json.JSONObject;

public class JsonGen {
    /** @noinspection SameParameterValue*/
    private static void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = false;
        if (!debug) return;
        String className = "[JsonGen]";
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
    public static String otpAuthJson(String otp) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "otp_auth");
            jsonObject.put("value", otp);

            return jsonObject.toString();
        } catch (Exception e) {
            log(e.getMessage(), 2);
        }
        return null;
    }

    public static String dataJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "data");
            jsonObject.put("value", data);

            return jsonObject.toString();
        } catch (Exception e) {
            log(e.getMessage(), 2);
        }
        return null;
    }
}
