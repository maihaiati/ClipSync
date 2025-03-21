package com.mtechdwork.clipsync;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileHandler {
    File dataFile;

    FileHandler(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);

        String filePath = "ClipSync";
        File directory = contextWrapper.getDir(filePath, Context.MODE_PRIVATE);
        String fileName = "clipSyncData.txt";
        dataFile = new File(directory, fileName);
    }

    /** @noinspection SameParameterValue*/
    private void log(String message, int type) {
        // Type: 0 - Info, 1 - Warning, 2 - Error
        boolean debug = true;
        if (!debug) return;
        String className = "[FileHandler]";
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

    public void writeData(String data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);

            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            log(e.getMessage(), 2);
        }
    }

    public String readData() {
        try {
            StringBuilder data = new StringBuilder();
            FileInputStream fileInputStream = new FileInputStream(dataFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            String strLine;

            while ((strLine = bufferedReader.readLine()) != null) {
                data.append(strLine);
            }
            dataInputStream.close();
            return data.toString();
        } catch (Exception e) {
            log(e.getMessage(), 2);
        }
        return "";
    }
}
