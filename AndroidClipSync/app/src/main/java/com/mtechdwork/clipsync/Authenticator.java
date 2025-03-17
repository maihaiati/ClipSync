package com.mtechdwork.clipsync;

import android.annotation.SuppressLint;
import android.content.Context;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Authenticator {

    private SettingManager settingManager;

    Authenticator(Context context) {
        settingManager = new SettingManager(context);
    }

    @SuppressLint("DefaultLocale")
    public String genOTP() {
        long currentTime = System.currentTimeMillis() / 60000; // Mỗi 60 giây đổi OTP
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
            e.printStackTrace();
        }
        return "";
    }

    public boolean checkOTPMatch(String otp) {
        return otp.equals(genOTP());
    }
}
