#!/bin/bash

sync_assets() {

    echo "MAKING NEW ASSETS FOLDER IN CURRENT DIR"
    if [ -d assets ]; then
        rm -r assets
    fi
    mkdir assets

    echo "PULLING TEXTURES FROM S3CMD"
    s3cmd get --recursive s3://autonomousgames/assets/current/ assets
}

sync_assets
