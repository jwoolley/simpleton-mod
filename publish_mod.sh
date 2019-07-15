#!/bin/bash
source ~/.bashrc
echo "STEAM_STS_PATH: $STEAM_STS_PATH"
java -jar "$STEAM_STS_PATH/mod-uploader.jar" upload -w "$STEAM_STS_PATH/thehayseed"

