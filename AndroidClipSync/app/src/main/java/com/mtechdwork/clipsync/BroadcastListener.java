package com.mtechdwork.clipsync;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;

public class BroadcastListener extends Thread {

    private static final int PORT = 7070;
    private boolean running = true;
    private Context context;
    private SettingManager settingManager;

    BroadcastListener(Context context) {
        this.context = context;
        settingManager = new SettingManager(context);
    }

    private void debugInfo(String message, int type) { // Debug method
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = true;
        if (!debug) return;
        String className = "[Broadcast Listener]";
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
            DatagramSocket socket = new DatagramSocket(PORT);
            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                InetAddress senderAddress = packet.getAddress();

                debugInfo("Received: " + receivedMessage + " from " + senderAddress.getHostAddress(), 0);
                if (checkSenderMatch(receivedMessage) && !Objects.equals(senderAddress.getHostAddress(), getIPAddress())) {

                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            stopListening();
        }

    }

    @Override
    public void interrupt() {
        super.interrupt();
        debugInfo("Thread interrupt!", 0);
        stopListening();
    }
}
