#!/bin/bash

echo "Compiling Jump Ball Adventure..."

# Create output directory for compiled classes
mkdir -p /app/build

# Compile all Java files with SQLite JDBC in classpath
javac -d /app/build -cp "/app/sqlite-jdbc.jar" \
  /app/Physics2D/*.java \
  /app/JumpBallGame/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running Jump Ball Adventure..."
    
    # Run the game with both the build directory and sqlite jar in classpath
    java -cp "/app/build:/app/sqlite-jdbc.jar" JumpBallGame.JumpBallAdventure
else
    echo "Compilation failed!"
    exit 1
fi
