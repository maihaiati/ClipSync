package com.mtechdwork.clipsync;

import com.google.crypto.tink.aead.AeadConfig;

public class XChaChaCrypto {
    public static void init() {
        try {
            AeadConfig.register();  // Đăng ký cấu hình Tink
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static byte[] encrypt() {
//
//    }
//
//    public static byte[] decrypt() {
//
//    }
}
