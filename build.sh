#!/usr/bin/env sh
version=0.3

mkdir -p dist

zip -r dist/nether-islands-${version}.zip \
	data \
	pack.mcmeta \
	pack.png