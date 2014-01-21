cd /home/blake/starcycle
echo "CRON RUNNING"

BRANCH="master"
reslog=`git log HEAD..origin/$BRANCH --oneline`
echo $reslog

if [[ "${reslog}" != "" ]] ; then

    echo "CHANGE IN MASTER DETECTED!"
    TIMESTAMP=$(date +%Y%m%d%H%M%S)
    echo "$TIMESTAMP"

    git merge origin/$BRANCH

    echo "prepping assets"
    mkdir -p tmp_assets/images
    cp -r ~/starcycle/assets/* tmp_assets
    cp -r ~/Dropbox/assets/images/* tmp_assets/images
    
    echo "compressing assets"
    tar -zcvf assets.tar.gz tmp_assets
    
    echo "pushing assets"
    s3cmd put -P assets.tar.gz s3://autonomousgames/assets/tarballs/$(date +%Y%m%d)-assets.tar.gz
    
    echo "cleaning up"
    rm assets.tar.gz
    rm -r tmp_assets
    
    echo "building android project"
    mvn clean package -Pandroid
    NEW_APK=android/target/starcycle-android.apk 
    S3_APK_LOC=s3://autonomousgames/nightlies/android/$TIMESTAMP-starcycle-android.apk
    
    echo "sending android project"
    s3cmd put -P $NEW_APK $S3_APK_LOC
    
    echo "building desktop project"
    mvn clean package -Pdesktop
    S3_JAR_LOC=s3://autonomousgames/nightlies/desktop/$TIMESTAMP-$branch-starcycle-desktop.jar
    NEW_JAR=desktop/target/starcycle-desktop-0.06-jar-with-dependencies.jar 
    
    echo "sending desktop project"
    s3cmd put -P $NEW_JAR $S3_JAR_LOC

fi

