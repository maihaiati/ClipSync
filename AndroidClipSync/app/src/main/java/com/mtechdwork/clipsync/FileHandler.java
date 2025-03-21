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
import java.util.Objects;

public class FileHandler {
    private final boolean debug = true;
    File dataFile;

    FileHandler(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);

        String filePath = "ClipSync";
        File directory = contextWrapper.getDir(filePath, Context.MODE_PRIVATE);
        String fileName = "clipSyncData.txt";
        dataFile = new File(directory, fileName);
    }

    public void writeData(String data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);

            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            if (debug) Log.e("[File Handler]", Objects.requireNonNull(e.getMessage()));
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
            if (debug) Log.e("[File Handler]", Objects.requireNonNull(e.getMessage()));
        }
        return "";
    }
}
