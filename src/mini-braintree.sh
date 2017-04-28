#!/usr/bin/env bash

input=$1
output=$2

if [[ -n "$input" ]]; then
    java -jar braintree.jar "$input" "$output"
else
    java -jar braintree.jar
fi