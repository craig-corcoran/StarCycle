package com.autonomousgames.starcycle.core.log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import java.util.HashMap;

public class UserProgress {
    private HashMap<String, String> uniqueSettingsMap;
    private Json json;
    private String uniqueSettingsJSON;
    private FileHandle handle;
    public UserProgress() {
        handle = Gdx.files.local("user_progress.json");
        uniqueSettingsJSON = "";
        uniqueSettingsMap = new HashMap<String, String>();
        json = new Json();
		if (!handle.exists()) {
			Gdx.app.log("UserProgress", "rewriting settings file");
            uniqueSettingsMap.put("LEVEL1", "false");
            uniqueSettingsMap.put("LEVEL2", "false");
            uniqueSettingsMap.put("LEVEL3", "false");
            uniqueSettingsMap.put("LEVEL4", "false");
            flush();
		}
        else {
            uniqueSettingsJSON = handle.readString();
            uniqueSettingsMap = json.fromJson(HashMap.class, uniqueSettingsJSON);
        }
    }

	public String getLevelComplete(String istring) {
        return uniqueSettingsMap.get(istring);
	}

	public void setLevelComplete(String ikey, String ivalue){
        uniqueSettingsMap.put(ikey, ivalue);
        flush();
	}

    private void flush() {
        uniqueSettingsJSON = json.toJson(uniqueSettingsMap);
        handle.writeString(uniqueSettingsJSON, false);
    }
}
