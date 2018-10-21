#!/usr/bin/env bash

files=("player89" "Individual" "Population")

function generate_files {
    for file in "${files[@]}"; do
        echo "${file}.${1} "
    done
}

java_files=$(generate_files "java")
javac -cp contest.jar ${java_files}
class_files=$(generate_files "class")
jar cmf MainClass.txt submission.jar ${class_files}