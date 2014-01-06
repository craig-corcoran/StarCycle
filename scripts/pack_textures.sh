#!/bin/bash
pack_textures() {
    echo "ASSUMING YOUR CWD IS STARCYCLE PROJECT ROOT"
    export SC_DIR=.

    echo "REMOVING OLD TEXTURES"
    if [ -d $SC_DIR/assets ]; then
        rm -r $SC_DIR/assets
    fi

    echo "COPYING ASSETS FROM DROPBOX (ASSUMING ~/Dropbox/assets)"
    cp -r ~/Dropbox/assets $SC_DIR/assets
    export TPCLASSDIR=$(pwd)/$line/tools/src/main/java/com/autonomousgames/starcycle/tools

    export CLASSPATH="$HOME/.m2/repository/com/badlogicgames/gdx/gdx-tools/0.9.9/gdx-tools-0.9.9.jar:$TPCLASSDIR"

    echo "USING CLASSPATH: $CLASSPATH"

    echo "COMPILING"
    /opt/java/bin/javac -g -d $SC_DIR/tools/src/main/java/com/autonomousgames/starcycle/tools $SC_DIR/tools/src/main/java/com/autonomousgames/starcycle/tools/StarCycleTexturePacker.java

    echo "RUNNING"
    /opt/java/bin/java com.autonomousgames.starcycle.tools.StarCycleTexturePacker

    echo "CLEANING UP"
    rm -r $SC_DIR/assets/images
    rm $SC_DIR/tools/src/main/java/com/autonomousgames/starcycle/tools/StarCycleTexturePacker.class
    
    echo "DONE"
}

pack_textures
