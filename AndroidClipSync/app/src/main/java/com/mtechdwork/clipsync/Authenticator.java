package com.mtechdwork.clipsync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Authenticator {

    private final SettingManager settingManager;

    Authenticator(Context context) {
        settingManager = new SettingManager(context);
    }

    /** @noinspection SameParameterValue*/
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

    @SuppressLint("DefaultLocale")
    public String genOTP() {
        long currentTime = System.currentTimeMillis() / 30000; // Mỗi 30 giây đổi OTP
        String timeHex = Long.toHexString(currentTime).toUpperCase();

        try {
            // HMAC-SHA1
            String passHash = settingManager.getPassword();

            if (passHash.isEmpty()) return "";

            byte[] keyBytes = passHash.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);

            byte[] hash = mac.doFinal(timeHex.getBytes());

            // Dùng thuật toán để trích xuất 6 chữ số OTP
            int offset = hash[hash.length - 1] & 0xF;
            int otp = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);
            otp %= 1000000; // Chỉ lấy 6 số cuối

            return String.format("%06d", otp);
        } catch (Exception e) {
            log(e.getMessage(), 2);
        }
        return "";
    }

    public boolean checkOTPMatch(String otp) {
        return otp.equals(genOTP());
    }
}
