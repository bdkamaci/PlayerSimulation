#!/bin/bash

cd "$(dirname "$0")"

# Creating output directory if it doesn't exist
mkdir -p target/classes
mkdir -p target/test-classes

# Compiling main classes
echo "Compiling main classes..."
mvn clean compile

# Compiling test classes
echo "Compiling test classes..."
mvn clean test-compile

# Running the main class
echo "Running main class..."
mvn exec:java -Dexec.mainClass="tech.Main"

# Running tests
echo "Running tests..."
mvn test
