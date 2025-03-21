package com.mtechdwork.clipsync;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.XChaCha20Poly1305KeyManager;
import com.google.crypto.tink.subtle.XChaCha20Poly1305;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

public class XChaChaCrypto {
    private static Aead aead;

    public static void init(Context context) {
        if (!isTinkInitialized()) {
            try {
                AeadConfig.register();  // Đăng ký cấu hình Tink

                loadKey(context);
            } catch (Exception e) {
                log(e.getMessage(), 2);
            }
        }
    }

    /** @noinspection SameParameterValue*/
    private static void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = true;
        if (!debug) return;
        String className = "[XChaChaCrypto]";
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

    public static boolean isTinkInitialized() {
        try {
            //noinspection unused
            KeysetHandle testKeyset = KeysetHandle.generateNew(XChaCha20Poly1305KeyManager.xChaCha20Poly1305Template());
            return true;
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

    public static void loadKey(Context context) {
        new Thread(() -> {
            byte[] derivedKey;
            try {
                derivedKey = PBKDF2.deriveKey(context);
                aead = new XChaCha20Poly1305(derivedKey);
            } catch (Exception e) {
                log(e.getMessage(), 2);
            }
        }).start();
    }

    public static String encrypt(String plaintext, byte[] associatedData) {
        try {
            return Base64.encodeToString(aead.encrypt(plaintext.getBytes(StandardCharsets.UTF_8), associatedData), Base64.NO_WRAP);
        } catch (GeneralSecurityException e) {
            log(e.getMessage(), 2);
        }
        return null;
    }

    public static String decrypt(String ciphertext, byte[] associatedData) {
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = aead.decrypt(Base64.decode(ciphertext, Base64.NO_WRAP), associatedData);
        } catch (GeneralSecurityException e) {
            log(e.getMessage(), 2);
        }
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
