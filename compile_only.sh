#!/bin/bash

echo "Compiling Jump Ball Adventure..."

# Create output directory for compiled classes
mkdir -p /app/build

# Compile all Java files with SQLite JDBC in classpath
javac -d /app/build -cp "/app/sqlite-jdbc.jar" \
  /app/Physics2D/*.java \
  /app/JumpBallGame/*.java \
  /app/Main.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "To run the game with proper JDBC initialization:"
    echo "  java -cp \"/app/build:/app/sqlite-jdbc.jar\" Main"
    echo ""
    echo "Or simply run: ./compile_and_run.sh"
    echo ""
    echo "Alternative (direct launch without JDBC checks):"
    echo "  java -cp \"/app/build:/app/sqlite-jdbc.jar\" JumpBallGame.JumpBallAdventure"
else
    echo "❌ Compilation failed!"
    exit 1
fi
