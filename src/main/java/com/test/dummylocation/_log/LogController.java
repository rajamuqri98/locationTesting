package com.test.dummylocation._log;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogController {

    private static final String TAG = "LogController";

    public static void writeLog(String text) {
        text = "\n" + text;

        File logFolder = new File(Environment.getExternalStorageDirectory(), "CentricLog");
        boolean isFolderCreated = logFolder.mkdir();

        if (isFolderCreated || logFolder.exists()) {
            File logFile = new File(logFolder, "loc.txt");

            try {
                FileWriter writer = new FileWriter(logFile, true);
                writer.write(text);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
