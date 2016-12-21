#!/usr/bin/env bash

input=$1
output=$2

if [[ -n "$input" ]]; then
    java -jar venmo.jar "$input" "$output"
else
    java -jar venmo.jar
fi