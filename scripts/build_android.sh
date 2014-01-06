### DOES NOT WORK-- ONLY COMMITTING FOR REFERENCE

#!/bin/bash
pack_textures() {
    echo "PACKING TEXTURES"
    if [ -d assets ]; then
        rm -r assets
    fi
    cp -r ../starcycle-desktop/assets assets
    
    if [ -f $SC_DIR/out/production/starcycle/com/ag/starcycle/StarCycleTexturePacker.class ]; then
        rm $SC_DIR/out/production/starcycle/com/ag/starcycle/StarCycleTexturePacker.class
    fi
    
    mkdir -p $SC_DIR/out/production/starcycle/com/ag/starcycle
    
    /opt/java/bin/javac -g \
        -classpath "/opt/java/jre/lib/jsse.jar:/opt/java/jre/lib/jce.jar:/opt/java/jre/lib/charsets.jar:/opt/java/jre/lib/rt.jar:/opt/java/jre/lib/resources.jar:/opt/java/jre/lib/management-agent.jar:/opt/java/jre/lib/rhino.jar:/opt/java/jre/lib/ext/dnsns.jar:/opt/java/jre/lib/ext/sunpkcs11.jar:/opt/java/jre/lib/ext/pulse-java.jar:/opt/java/jre/lib/ext/localedata.jar:/opt/java/jre/lib/ext/sunjce_provider.jar:/opt/java/jre/lib/ext/zipfs.jar:$SC_DIR/out/production/starcycle:$SC_DIR/starcycle/libs/gdx-freetype.jar:$SC_DIR/starcycle/libs/gdx-sources.jar:$SC_DIR/starcycle/libs/gdx-tools.jar:$SC_DIR/starcycle/libs/gdx.jar:$SC_DIR/starcycle-desktop/libs/gdx-freetype-natives.jar:$SC_DIR/starcycle-desktop/libs/gdx-backend-lwjgl.jar:$SC_DIR/starcycle-desktop/libs/gdx-freetype.jar:$SC_DIR/starcycle-desktop/libs/gdx-natives.jar:$SC_DIR/starcycle-desktop/libs/gdx-backend-lwjgl-natives.jar:$SC_DIR/starcycle-desktop/libs/gdx-backend-lwjgl-sources.jar" \
        -d $SC_DIR/out/production/starcycle/com/ag/starcycle \
        $SC_DIR/starcycle/src/com/ag/starcycle/StarCycleTexturePacker.java
    
    /opt/java/bin/java \
        -classpath "/opt/java/jre/lib/jsse.jar:/opt/java/jre/lib/jce.jar:/opt/java/jre/lib/charsets.jar:/opt/java/jre/lib/rt.jar:/opt/java/jre/lib/resources.jar:/opt/java/jre/lib/management-agent.jar:/opt/java/jre/lib/rhino.jar:/opt/java/jre/lib/ext/dnsns.jar:/opt/java/jre/lib/ext/sunpkcs11.jar:/opt/java/jre/lib/ext/pulse-java.jar:/opt/java/jre/lib/ext/localedata.jar:/opt/java/jre/lib/ext/sunjce_provider.jar:/opt/java/jre/lib/ext/zipfs.jar:$SC_DIR/out/production/starcycle:$SC_DIR/starcycle/libs/gdx-freetype.jar:$SC_DIR/starcycle/libs/gdx-sources.jar:$SC_DIR/starcycle/libs/gdx-tools.jar:$SC_DIR/starcycle/libs/gdx.jar:$SC_DIR/starcycle-desktop/libs/gdx-freetype-natives.jar:$SC_DIR/starcycle-desktop/libs/gdx-backend-lwjgl.jar:$SC_DIR/starcycle-desktop/libs/gdx-freetype.jar:$SC_DIR/starcycle-desktop/libs/gdx-natives.jar:$SC_DIR/starcycle-desktop/libs/gdx-backend-lwjgl-natives.jar:$SC_DIR/starcycle-desktop/libs/gdx-backend-lwjgl-sources.jar:$SC_DIR/out/production/starcycle/com/ag/starcycle" com.ag.starcycle.StarCycleTexturePacker 
    rm -r assets/images assets/*.log
}

build_project() {
    
    # Create R.java-- must be included
    echo "CREATING R.JAVA"
    aapt package -v -f -m \
        -A assets \
        -M AndroidManifest.xml \
        -I /opt/adt-bundle/sdk/platforms/android-19/android.jar \
        -I ../starcycle/libs/gdx-freetype.jar \
        -I ../starcycle/libs/gdx.jar \
        -I ../starcycle/libs/gdx-tools/gdx-tools.jar \
        -I libs/gdx-freetype.jar \
        -I libs/gdx-backend-android.jar \
        -I libs/armeabi \
        -I libs/armeabi-v7a \
        -S res \
        -J src/
    
    if [ -d bin ]; then
        rm -r bin 
    fi
    mkdir -p bin/classes

    # Compile source files
    export CLASSPATH="bin:bin/*:bin/armeabi/libgdx.so:bin/armeabi-v7a/libgdx.so:libs/gdx-backend-android.jar:libs/gdx-freetype.jar:../starcycle/libs/gdx.jar:../starcycle/libs/gdx-freetype.jar:../starcycle/libs/gdx-tools/gdx-tools.jar:/opt/adt-bundle/sdk/platforms/android-19/android.jar"

    /opt/java/bin/javac \
        -verbose \
        -d bin/classes \
        -target 1.6 `find ./src -iname "*.java"` `find ../starcycle/src -iname "*.java"`

    echo "GENERATE DALVIK BYTECODE"
    dx --debug \
        --dex \
        --output bin/classes.dex bin/classes libs/gdx-backend-android.jar libs/gdx-freetype.jar ../starcycle/libs/gdx.jar 

    echo "CREATE UNSIGNED APK"
    sleep 1
    aapt package -v -f \
        -A assets \
        -M AndroidManifest.xml \
        -I /opt/adt-bundle/sdk/platforms/android-19/android.jar \
        -I ../starcycle/libs/gdx-freetype.jar \
        -I ../starcycle/libs/gdx.jar \
        -I ../starcycle/libs/gdx-tools/gdx-tools.jar \
        -I libs/gdx-freetype.jar \
        -I libs/gdx-backend-android.jar \
        -I libs/armeabi \
        -I libs/armeabi-v7a \
        -S res \
        -F bin/starcycle.unsigned.apk bin/

    # CREATE CERT IF NECESSARY
    # keytool -genkey -v -keystore debug.keystore -alias androiddebugkey -storepass android -keypass android -keyalg RSA -validity 14000
    
    # Sign APK
    echo "SIGN APK"
    sleep 1
    jarsigner -verbose -keystore debug.keystore -storepass android -keypass android -signedjar bin/starcycle.signed.apk bin/starcycle.unsigned.apk androiddebugkey

    aapt add -j ../starcycle/libs/gdx.jar bin/starcycle.signed.apk
    
    # Compress (?)
    zipalign -v -f 4 bin/starcycle.signed.apk bin/starcycle.apk
    
    #CLEAN UP
    #if [ -d assets ]; then
    #    rm -r assets
    #fi
    if [ -d bin/classes ]; then
        rm -r bin/classes
    fi 
}

install_on_device() {
    #adb shell uninstall pm com.ag.starcycle # DOESNT WORK
    adb -d install bin/starcycle.apk
}

usage() { echo "i-- install to device over USB; b-- build project; p-- pack textures;" 1>&2; exit 1; }

### START HERE
export PATH=$PATH:/opt/android-sdk/build-tools/19.0.1/:/opt/adt-bundle/sdk/tools/
export SC_DIR=$(pwd)/..

while getopts ":ipb" o; do
    case "${o}" in
        p)
            pack_textures
            ;;
        b)
            build_project
            ;;
        i)
            install_on_device
            ;;
        *)
            usage
            ;;
    esac
done
shift $((OPTIND-1))
