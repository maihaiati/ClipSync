package com.mtechdwork.clipsync;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communication {

    private Context context;

    Communication(Context context) {
        this.context = context.getApplicationContext();
    }

    public void sendBroadcast() {
        final String messageStr = "CS_BC"; // ClipSync_Broadcast Flag
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] sendData = messageStr.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(), 7070);
            socket.send(sendPacket);
            Log.i("[Communication]", "Broadcast packet sent to: " + getBroadcastAddress().getHostAddress());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
    }

    private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
