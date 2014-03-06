package com.autonomousgames.starcycle.core.log;

import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class LogWriter extends Thread {
    public ArrayList<String> logData;
    public FileHandle logFile;
    public LogWriter(ArrayList<String> logData, FileHandle logFile) {
        this.logData = logData;
        this.logFile = logFile;
    }

    @Override
    public void run() {
        if (!logData.isEmpty()) {
            for (int i=0; i<logData.size(); i++) {
                logFile.writeString(logData.get(i) + "\n", true);
            }
            logData.clear();
        }
    }
}
