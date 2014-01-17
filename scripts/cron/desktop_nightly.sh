git fetch --all
export BRANCHES=$(git branch | grep -oh "\w*")
for line in $BRANCHES; do
    git checkout $line
    mvn clean package -Pdesktop
done


