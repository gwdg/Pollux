#!/bin/bash

function export_cdmi2one
{
	for var in $*
	do
	  export $var
	  echo $var
	done
}

#------------------------------------------------------------------------------
IMAGE_URI=$1

CDMI2ONE=`ruby CDMI2one.rb --image=$IMAGE_URI | grep CDMI2ONE* | tr ";" "\n"`
export_cdmi2one $CDMI2ONE

#FIXME:  remove comment from ONE
#WUBI_ID=`generate_image_path`
WUBI_ID="CODE937164ABEDABC"

ONE_EXTERNAL=/srv/cloud/mount/$WUBI_ID
mkdir -p $ONE_EXTERNAL
mount $CDMI2ONE_EXPORT $ONE_EXTERNAL
DST=$ONE_EXTERNAL'/'$CDMI2ONE_EXPORT'/'$CDMI2ONE_IMAGE_ID
