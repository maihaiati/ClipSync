package com.mtechdwork.clipsync;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastListener extends Thread {
    private static final int PORT = 7070;
    private boolean running = true;

    public void stopListening() {
        running = false;
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

                Log.i("[Broadcast Listener]", "Received: " + receivedMessage + " from " + senderAddress);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
