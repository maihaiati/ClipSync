package com.mtechdwork.clipsync;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Communication {

    private final int TCP_PORT = 7071;
    private final Context context;
    private final SettingManager settingManager;
    private DatagramSocket socket = null;

    Communication(Context context) {
        this.context = context.getApplicationContext();
        settingManager = new SettingManager(context);
    }

    private void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = false;
        if (!debug) return;
        String className = "[Communication]";
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

    public void sendBroadcast() {
        final String messageStr = "CS_BC_" + settingManager.getUsername(); // ClipSync_Broadcast Flag
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] sendData = messageStr.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(), 7070);

            for (int i = 0; i < 3; i++) { // Send broadcast 3 times
                socket.send(sendPacket);
                log("Broadcast packet " + (i + 1) + " sent to: " + getBroadcastAddress().getHostAddress(), 0);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log(e.getMessage(), 2);
        }
        if (socket != null && !socket.isClosed()) socket.close();
    }

    public void sendSyncRequest(InetAddress ipAddress) {
        Authenticator authenticator = new Authenticator(context);
        new Thread(() -> {
            try (Socket socket = new Socket(ipAddress, TCP_PORT);
                 OutputStream outputStream = socket.getOutputStream();
                 PrintWriter writer = new PrintWriter(outputStream, true)) {

                String otp = JsonGen.otpAuthJson(authenticator.genOTP());

                if (otp != null && !otp.isEmpty()) {
                    // Encrypt with XChaCha20-Poly1305
                    byte[] associatedData = "metadata".getBytes(StandardCharsets.UTF_8);
                    String encryptedOTP = XChaChaCrypto.encrypt(otp, associatedData);

                    // Gửi dữ liệu
                    writer.println(encryptedOTP);
                    log("Sent OTP: " + otp, 0);
                    log("Sent encrypted OTP: " + encryptedOTP, 0);
                } else {
                    writer.println("");
                    log("Sent sync request", 0);
                }
            } catch (Exception e) {
                log(e.getMessage(), 2);
            }
        }).start();
    }

    public void sendClipboardData(InetAddress ipAddress) {
        new Thread(() -> {
            try (Socket socket = new Socket(ipAddress, TCP_PORT);
                 OutputStream outputStream = socket.getOutputStream();
                 PrintWriter writer = new PrintWriter(outputStream, true)) {

                String clipboardData = JsonGen.dataJson(ClipboardData.getLatestData());

                if (clipboardData != null) {
                    // Encrypt with XChaCha20-Poly1305
                    byte[] associatedData = "metadata".getBytes(StandardCharsets.UTF_8);
                    String encryptData = XChaChaCrypto.encrypt(clipboardData, associatedData);

                    writer.println(encryptData);
                    log("Sent data: " + clipboardData, 0);
                    log("Sent encrypted data: " + encryptData, 0);
                } else log("Clipboard null. Ignore", 0);
            } catch (Exception e) {
                log(e.getMessage(), 2);
            }
        }).start();
    }

    private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        // (&: AND; |: OR; ~: NOT)
        // Example: IP = 192.168.1.10; Netmask = 255.255.255.0
        // IP in base 2: 11000000.10101000.00000001.00001010
        // Netmask in base 2: 11111111.11111111.11111111.00000000
        // => Broadcast IP = (11000000.10101000.00000001.00001010 & 11111111.11111111.11111111.00000000) | ~(11111111.11111111.11111111.00000000)
        // => Broadcast IP = 192.168.1.255

        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}
