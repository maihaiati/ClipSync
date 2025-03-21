package com.mtechdwork.clipsync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.aead.XChaCha20Poly1305KeyManager;
import com.google.crypto.tink.integration.android.AndroidKeysetManager;
import com.google.crypto.tink.proto.KeyData;
import com.google.crypto.tink.proto.KeyStatusType;
import com.google.crypto.tink.proto.Keyset;
import com.google.crypto.tink.proto.OutputPrefixType;
import com.google.crypto.tink.proto.XChaCha20Poly1305KeyFormat;
import com.google.crypto.tink.shaded.protobuf.ByteString;
import com.google.crypto.tink.subtle.XChaCha20Poly1305;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class XChaChaCrypto {
    private static KeysetHandle keysetHandle;
    private static Aead aead;

    public static void init(Context context) {
        if (!isTinkInitialized()) {
            try {
                AeadConfig.register();  // Đăng ký cấu hình Tink

                loadKey(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isTinkInitialized() {
        try {
            KeysetHandle testKeyset = KeysetHandle.generateNew(XChaCha20Poly1305KeyManager.xChaCha20Poly1305Template());
            return true;
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

    public static void loadKey(Context context) {
        new Thread(() -> {
            byte[] derivedKey = null;
            try {
                derivedKey = PBKDF2.deriveKey(context);
                aead = new XChaCha20Poly1305(derivedKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String encrypt(String plaintext, byte[] associatedData) {
        try {
            return Base64.encodeToString(aead.encrypt(plaintext.getBytes(StandardCharsets.UTF_8), null), Base64.NO_WRAP);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String ciphertext, byte[] associatedData) {
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = aead.decrypt(Base64.decode(ciphertext, Base64.NO_WRAP), null);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
