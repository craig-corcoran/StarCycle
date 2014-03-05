package com.autonomousgames.starcycle.core.log;

import com.autonomousgames.starcycle.core.StarCycle;

import java.util.ArrayList;

public class LogPackage {
    private final String uid;
    private final long startTime;
    private final String platform;
    private final ArrayList<String[]> screenChanges;
    private final ArrayList<String[]> games;
    private final String version;

    public LogPackage(String uid, long startTime, String platform, ArrayList<String[]> screen_changes, ArrayList<String[]> games) {
        this.uid = uid;
        this.startTime = startTime;
        this.platform = platform;
        this.screenChanges = screen_changes;
        this.games = games;
        this.version = StarCycle.version;
    }
}
