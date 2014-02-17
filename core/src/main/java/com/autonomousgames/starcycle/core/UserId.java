package com.autonomousgames.starcycle.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.UUID;

public class UserId {
    private FileHandle handle;
    private String uid;
    public UserId() {
        handle = Gdx.files.local("user_id.txt");
		if (!handle.exists()) {
			Gdx.app.log("UserId", "rewriting settings file");
            handle.writeString(UUID.randomUUID().toString(), false);
		}
        else {
            uid = handle.readString();
            Gdx.app.log("user id", uid);
        }
    }

	public String getId() {
        return uid;
	}
}
