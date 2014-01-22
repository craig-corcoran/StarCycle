#!/bin/bash
pack_textures() {
    echo "ASSUMING YOUR CWD IS STARCYCLE PROJECT ROOT"
    export SC_DIR=.  # TODO make this a script parameter
    cd $SC_DIR

    echo "REMOVING OLD TEXTURES"
    if [ -d $SC_DIR/assets ]; then
        rm -r $SC_DIR/assets
    fi
    if [ -d ~/Dropbox/assets ]; then
        echo "COPY ASSETS FROM DROPBOX (ASSUMING ~/Dropbox/assets)"
        cp -r ~/Dropbox/assets $SC_DIR/assets
    else
        echo "Fail: no ~/Dropbox/assets"
        exit 1
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
    
    echo "DONE"
}

pack_textures
