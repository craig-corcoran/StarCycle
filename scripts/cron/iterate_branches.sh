# check out each branch, compile, and send to s3

cd /home/blake/starcycle

BRANCHES=$(git branch | grep -oh "\w*")
echo "found branches: "
echo $BRANCHES
for branch in $BRANCHES; do
    echo $branch
done


