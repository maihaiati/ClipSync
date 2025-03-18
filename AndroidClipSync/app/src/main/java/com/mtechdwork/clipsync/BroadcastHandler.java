package com.mtechdwork.clipsync;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

public class BroadcastHandler extends Thread {

    private static final int PORT = 7070;
    private boolean running = true;
    private Context context;
    private SettingManager settingManager;

    private DatagramSocket socket;

    BroadcastHandler(Context context) {
        this.context = context;
        settingManager = new SettingManager(context);
    }

    private void log(String message, int type) { // Debug method
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = true;
        if (!debug) return;
        String className = "[Broadcast Handler]";
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

    public void stopListening() {
        running = false;
        if (socket != null && !socket.isClosed()) socket.close();
    }

    private String getIPAddress() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();

            // Chuyển đổi từ int sang địa chỉ IP
            return (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    ((ipAddress >> 24) & 0xFF);
        }
        return null;
    }

    private boolean checkSenderMatch(String receivedMessage) {
        String broadcastContent = "CS_BC_" + settingManager.getUsername();
        return receivedMessage.equals(broadcastContent);
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(PORT);
            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                InetAddress senderAddress = packet.getAddress();

                if (checkSenderMatch(receivedMessage) && !Objects.equals(senderAddress.getHostAddress(), getIPAddress())) {
                    log("Received: " + receivedMessage + " from " + senderAddress.getHostAddress(), 0);
                    Communication communication = new Communication(context);
                    communication.sendSyncRequest(senderAddress);
                }
            }

            if (socket != null) socket.close();
        } catch (Exception e) {
            log("EXCEPTION", 2);
            e.printStackTrace();
            stopListening();
        }
        log("Thread stopped!", 0);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        log("Thread interrupt!", 0);
        stopListening();
    }
}
