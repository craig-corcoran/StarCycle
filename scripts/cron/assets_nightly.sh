# prep
mkdir -p tmp_assets/images
cp -r ~/starcycle/assets/* tmp_assets
cp -r ~/Dropbox/assets/images/* tmp_assets/images
# compress
tar -zcvf assets.tar.gz tmp_assets

# push up
s3cmd put assets.tar.gz s3://autonomousgames/assets/tarballs/$(date +%Y%m%d)-assets.tar.gz

# clean up
rm assets.tar.gz
rm -r tmp_assets
