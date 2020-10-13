#!/usr/bin/env bash

set -e

if ! command -v npm &> /dev/null; then
    exit 1
fi

cd "${BASH_SOURCE%/*}/../src/main/client" || exit

[[ ! -d ./node_modules || package-lock.json -nt ./node_modules ]] && npm ci

npm run "$1"
