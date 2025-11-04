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
    echo "🚀 Running Jump Ball Adventure with proper JDBC initialization..."
    echo ""
    
    # Run the game through Main.java with proper JDBC setup
    java -cp "/app/build:/app/sqlite-jdbc.jar" Main
else
    echo "❌ Compilation failed!"
    exit 1
fi
