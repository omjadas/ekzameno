#!/usr/bin/env bash

set -e

if ! command -v npm &> /dev/null; then
    exit
fi

cd "${BASH_SOURCE%/*}/../src/main/client" || exit

[[ ! -d "./node_modules" ]] && npm ci

npm run "$1"
