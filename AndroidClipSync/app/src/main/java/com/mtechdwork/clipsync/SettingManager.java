package com.mtechdwork.clipsync;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Objects;

public class SettingManager extends FileHandler {

    SettingManager(Context context) {
        super(context);
        try {
            if (readData().isEmpty() || readData().equals("{}")) { // Init default data if file data is empty or equal "{}"
                JSONObject jsonObject = new JSONObject();

                // Init json data
                jsonObject.put("username", "");
                jsonObject.put("enable_sync", false);
                jsonObject.put("password", "");

                String jsonString = jsonObject.toString(4);
                writeData(jsonString);
            }
        } catch (Exception e) {
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
        }
    }

    private void debugInfo(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = true;
        if (!debug) return;
        String className = "[Setting Manager]";
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

    public void setUsername(String username) {
        try {
            JSONObject jsonObject = new JSONObject(readData());

            if (!username.equals("FAILED_TO_GET_USERNAME"))
                jsonObject.put("username", username);
            else {
                jsonObject.put("username", "Username");
            }

            String jsonString = jsonObject.toString(4);
            writeData(jsonString);
        } catch (Exception e) {
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
        }
    }

    public String getUsername() {
        try {
            JSONObject jsonObject = new JSONObject(readData());
            return jsonObject.getString("username");
        } catch (Exception e) {
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
        }
        return "FAILED_TO_GET_USERNAME";
    }

    public void setPassword(String password) {
        try {
            JSONObject jsonObject = new JSONObject(readData());

            jsonObject.put("password", password);

            String jsonString = jsonObject.toString(4);
            writeData(jsonString);
        } catch (Exception e) {
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
        }
    }

    public String getPassword() {
        try {
            JSONObject jsonObject = new JSONObject(readData());
            return jsonObject.getString("password");
        } catch (Exception e) {
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
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
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
        }
    }

    public boolean isEnable() {
        try {
            JSONObject jsonObject = new JSONObject(readData());
            return jsonObject.getBoolean("enable_sync");
        } catch (Exception e) {
            debugInfo(Objects.requireNonNull(e.getMessage()), 2);
        }
        return false;
    }
}
