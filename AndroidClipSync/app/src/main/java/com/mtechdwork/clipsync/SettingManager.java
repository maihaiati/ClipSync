package com.mtechdwork.clipsync;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SettingManager extends FileHandler {
    private boolean debug = true;

    SettingManager(Context context) {
        super(context);
        try {
            if (readData().isEmpty() || readData().equals("{}")) { // Init default data if file data is empty or equal "{}"
                JSONObject jsonObject = new JSONObject();

                // Init json data
                jsonObject.put("enable_sync", false);
                jsonObject.put("password", "");

                String jsonString = jsonObject.toString(4);
                writeData(jsonString);
            }
        } catch (Exception e) {
            if (debug) Log.e("[Setting Manager]", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void setPassword(String password) {
        try {
            JSONObject jsonObject = new JSONObject(readData());

            jsonObject.put("password", password);

            String jsonString = jsonObject.toString(4);
            writeData(jsonString);
        } catch (Exception e) {
            if (debug) Log.e("[Setting Manager]", Objects.requireNonNull(e.getMessage()));
        }
    }

    public String getPassword() {
        try {
            JSONObject jsonObject = new JSONObject(readData());
            return jsonObject.getString("password");
        } catch (Exception e) {
            if (debug) Log.e("[Setting Manager]", Objects.requireNonNull(e.getMessage()));
        }
        return "FAILED_TO_GET_PASS";
    }

    public void setEnable(boolean enable) {
        try {
            JSONObject jsonObject = new JSONObject(readData());

            jsonObject.put("enable_sync", enable);

            String jsonString = jsonObject.toString(4);
            writeData(jsonString);
        } catch (Exception e) {
            if (debug) Log.e("[Setting Manager]", Objects.requireNonNull(e.getMessage()));
        }
    }

    public boolean isEnable() {
        try {
            JSONObject jsonObject = new JSONObject(readData());
            return jsonObject.getBoolean("enable_sync");
        } catch (Exception e) {
            if (debug) Log.e("[Setting Manager]", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }
}
