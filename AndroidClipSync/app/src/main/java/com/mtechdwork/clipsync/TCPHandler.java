package com.mtechdwork.clipsync;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPHandler extends Thread {

    private final int PORT = 7071;
    private boolean running = true;
    private Context context;
    private ServerSocket serverSocket;

    TCPHandler(Context context) {
        this.context = context;
    }

    public void stopRunning() {
        try {
            running = false;
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            log("EXCEPTION", 2);
            e.printStackTrace();
        }
    }

    private void log(String message, int type) { // Debug method
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = true;
        if (!debug) return;
        String className = "[TCP Handler]";
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

    private void clientHandler(InetAddress ipAddress, String jsonMessage) {
        try {
            JSONObject jsonObject = new JSONObject(jsonMessage);

            switch (jsonObject.getString("type")) {
                case "otp_auth":
                    Authenticator authenticator = new Authenticator(context);
                    String otp = jsonObject.getString("value");
                    if (authenticator.checkOTPMatch(otp)) {
                        Communication communication = new Communication(context);
                        communication.sendClipboardData(ipAddress);
                    }
                    break;

                case "data":
                    ClipboardData.writeClipboard(context, jsonObject.getString("value"));
                    break;
            }
        } catch (Exception e) {
            log("EXCEPTION", 2);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (running) {
                Socket clientSocket = serverSocket.accept(); // Chờ client kết nối
                log("Client connected: " + clientSocket.getInetAddress(), 0);

                // Đọc dữ liệu từ client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = reader.readLine();
                log("Received: " + message, 0);

                // Handler
                InetAddress clientIP = clientSocket.getInetAddress();
                clientHandler(clientIP, message);

                // Close connection
                clientSocket.close();
            }
        } catch (Exception e) {
            log("EXCEPTION", 2);
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        log("Thread interrupt!", 0);
        stopRunning();
    }
}
