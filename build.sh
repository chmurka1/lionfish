#!/bin/sh
pacman -S jre-openjdk
pacman -S jdk-openjdk
pacman -S gradle
pacman -S ttf-dejavu
sh ./gradlew build
