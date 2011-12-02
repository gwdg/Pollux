#!/bin/bash

# This script requires following environment variables:  POLLUX_HOME  &  VIRGO_HOME

POLLUX_BIN=$POLLUX_HOME/cloud/target
VIRGO_REPO=$VIRGO_HOME/repository/usr
cp $POLLUX_BIN/par-provided/*.* $VIRGO_REPO
cp $POLLUX_BIN/cloud.par-1.0.par $VIRGO_HOME/pickup