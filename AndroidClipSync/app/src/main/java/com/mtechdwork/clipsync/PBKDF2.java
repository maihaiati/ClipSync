package com.mtechdwork.clipsync;

import android.content.Context;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2 extends Thread {

    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 256; // 256-bit key
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static byte[] deriveKey(Context context) throws Exception { // PBKDF2 chỉ dùng để dẫn xuất khoá ở đây (không dùng để chống tấn công)
        SettingManager settingManager = new SettingManager(context);
        String password = settingManager.getPassword();
        String username = settingManager.getUsername();

        KeySpec spec = new PBEKeySpec(password.toCharArray(), username.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }
}
