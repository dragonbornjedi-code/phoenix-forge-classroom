#!/usr/bin/env sh
# Monorepo entry: run Gradle inside the Forge Profile Android project.
set -eu
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR/phoenix-forge-classroom-forge-profile"
exec ./gradlew "$@"
