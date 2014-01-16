#!/bin/bash
pack_textures() {
    echo "ASSUMING YOUR CWD IS STARCYCLE PROJECT ROOT"
    export SC_DIR=.

    echo "REMOVING OLD TEXTURES"
    if [ -d $SC_DIR/assets ]; then
        rm -r $SC_DIR/assets
    fi
    if [ -d $SC_DIR/current ]; then
        rm -r $SC_DIR/current
    fi

    if [ -d ~/Dropbox ]; then
        echo "COPYING ASSETS FROM DROPBOX (ASSUMING ~/Dropbox/assets)"
        cp -r ~/Dropbox/assets $SC_DIR/assets
    else
        mkdir $SC_DIR/assets
        s3cmd get --recursive s3://autonomousgames/assets/current $SC_DIR
        mv $SC_DIR/current $SC_DIR/assets
    fi
    
    export TPCLASSDIR=$(pwd)/$line/tools/src/main/java/com/autonomousgames/starcycle/tools
    export CLASSPATH="$HOME/.m2/repository/com/badlogicgames/gdx/gdx-tools/0.9.9/gdx-tools-0.9.9.jar:$TPCLASSDIR"

    echo "USING CLASSPATH: $CLASSPATH"

    echo "COMPILING"
    $JAVA_HOME/bin/javac -g -d $SC_DIR/tools/src/main/java/com/autonomousgames/starcycle/tools $SC_DIR/tools/src/main/java/com/autonomousgames/starcycle/tools/StarCycleTexturePacker.java

    echo "RUNNING"
    $JAVA_HOME/bin/java com.autonomousgames.starcycle.tools.StarCycleTexturePacker

    echo "CLEANING UP"
    rm -r $SC_DIR/assets/images
    rm $SC_DIR/tools/src/main/java/com/autonomousgames/starcycle/tools/StarCycleTexturePacker.class
    
    echo "DONE"
}

pack_textures
