package com.autonomousgames.starcycle.tools;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class StarCycleTexturePacker {
    public static void main (String[] args) throws Exception {
        Settings settings = new Settings();
        settings.paddingX = 2;
        settings.paddingY = 2;
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        settings.flattenPaths = true;
        //settings.flattenPaths = true; // does not like
        //settings.incremental = true; TODO, set these in a json file?
        String assetsDir = "assets";
    	TexturePacker2.process(settings, assetsDir + "/images",
    						   assetsDir + "/packed-images", "packed-textures");
    }
}
