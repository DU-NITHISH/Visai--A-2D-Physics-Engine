# JDBC Connection Setup Guide

## Overview

This project uses **SQLite JDBC** for database connectivity. The `Main.java` file provides a comprehensive entry point with proper JDBC initialization, connection testing, and database setup.

## 📁 Key Files

### 1. Main.java (NEW)
- **Purpose**: Entry point with proper JDBC connection setup
- **Location**: `/app/Main.java`
- **Features**:
  - JDBC driver verification
  - Database connection testing
  - Schema initialization
  - Leaderboard display
  - Game launcher

### 2. DatabaseManager.java
- **Purpose**: Singleton database manager
- **Location**: `/app/JumpBallGame/DatabaseManager.java`
- **Features**:
  - Database operations (CRUD)
  - Leaderboard queries
  - Time formatting utilities

## 🔧 JDBC Configuration

### Database Details
```java
Driver Class:     org.sqlite.JDBC
Database Type:    SQLite 3
JDBC URL:         jdbc:sqlite:jumpball.db
Driver File:      sqlite-jdbc.jar (v3.47.1.0)
Database File:    jumpball.db (auto-created)
```

### Connection String
```java
String DB_URL = "jdbc:sqlite:jumpball.db";
Connection conn = DriverManager.getConnection(DB_URL);
```

## 🚀 Running with Proper JDBC Setup

### Method 1: Using Main.java (Recommended)
```bash
cd /app
./compile_and_run.sh
```

This will:
1. ✅ Verify JDBC driver is loaded
2. ✅ Test database connection
3. ✅ Initialize database schema
4. ✅ Display current leaderboard
5. ✅ Launch the game

### Method 2: Direct Java Command
```bash
java -cp "/app/build:/app/sqlite-jdbc.jar" Main
```

### Method 3: Skip JDBC Checks (Direct Launch)
```bash
java -cp "/app/build:/app/sqlite-jdbc.jar" JumpBallGame.JumpBallAdventure
```

## 📊 What Main.java Does

### Step 1: JDBC Driver Verification
```
🔍 Verifying JDBC Driver... ✅ SQLite JDBC Driver loaded successfully
   Driver: org.sqlite.JDBC
   Version: 3.47
```

### Step 2: Connection Test
```
🔌 Testing database connection... ✅ Connected successfully
   Database: SQLite
   Version: 3.47.1.0
   URL: jdbc:sqlite:jumpball.db
```

### Step 3: Schema Initialization
```
📊 Initializing database schema... ✅ Database schema ready
   Records in database: 0
```

### Step 4: Leaderboard Display
```
🏆 Current Leaderboard (Top 10)
─────────────────────────────────────────
   No records yet. Be the first to play!
─────────────────────────────────────────
```

### Step 5: Game Launch
```
🎮 Launching Jump Ball Adventure...
✅ Game window launched successfully
```

## 🔍 JDBC Connection Code Examples

### Basic Connection
```java
// Load driver
Class.forName("org.sqlite.JDBC");

// Connect to database
Connection conn = DriverManager.getConnection("jdbc:sqlite:jumpball.db");

// Use connection
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM players");

// Close resources
rs.close();
stmt.close();
conn.close();
```

### Prepared Statement (Safe from SQL Injection)
```java
String sql = "INSERT INTO players (name, completion_time_ms, completed_date) VALUES (?, ?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, "PlayerName");
pstmt.setLong(2, 180000);
pstmt.setString(3, "2024-11-04 12:00:00");
pstmt.executeUpdate();
pstmt.close();
```

### Query with Results
```java
String sql = "SELECT * FROM players ORDER BY completion_time_ms LIMIT 10";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);

while (rs.next()) {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    long time = rs.getLong("completion_time_ms");
    String date = rs.getString("completed_date");
    
    System.out.println(name + " - " + time + "ms");
}

rs.close();
stmt.close();
```

## 🗄️ Database Schema

```sql
CREATE TABLE IF NOT EXISTS players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    completion_time_ms BIGINT NOT NULL,
    completed_date TEXT NOT NULL
)
```

### Table Structure
| Column | Type | Constraint | Description |
|--------|------|------------|-------------|
| id | INTEGER | PRIMARY KEY, AUTO_INCREMENT | Unique record ID |
| name | TEXT | NOT NULL | Player name |
| completion_time_ms | BIGINT | NOT NULL | Completion time in milliseconds |
| completed_date | TEXT | NOT NULL | Timestamp when completed |

## 📝 Example SQL Operations

### Insert Record
```sql
INSERT INTO players (name, completion_time_ms, completed_date) 
VALUES ('Alice', 240000, '2024-11-04 16:30:00');
```

### Query Leaderboard
```sql
SELECT name, completion_time_ms, completed_date 
FROM players 
ORDER BY completion_time_ms ASC 
LIMIT 10;
```

### Count Records
```sql
SELECT COUNT(*) as total FROM players;
```

### Get Fastest Time
```sql
SELECT name, MIN(completion_time_ms) as best_time 
FROM players;
```

### Get Player's Best Time
```sql
SELECT MIN(completion_time_ms) as best_time 
FROM players 
WHERE name = 'Alice';
```

## 🛠️ JDBC Best Practices Used

### 1. Resource Management
```java
// Always close resources in reverse order
try {
    Connection conn = DriverManager.getConnection(url);
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    // Use rs
} finally {
    if (rs != null) rs.close();
    if (stmt != null) stmt.close();
    if (conn != null) conn.close();
}
```

### 2. Prepared Statements (Prevent SQL Injection)
```java
// DON'T do this (vulnerable to SQL injection):
String sql = "SELECT * FROM players WHERE name = '" + userInput + "'";

// DO this instead:
String sql = "SELECT * FROM players WHERE name = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, userInput);
```

### 3. Connection Pooling (Via Singleton)
```java
// DatabaseManager uses singleton pattern
DatabaseManager.getInstance(); // Reuses connection
```

### 4. Error Handling
```java
try {
    // Database operations
} catch (SQLException e) {
    System.err.println("Database error: " + e.getMessage());
    e.printStackTrace();
}
```

## 🧪 Testing JDBC Connection

### Test 1: Verify Driver
```bash
java -cp "/app/build:/app/sqlite-jdbc.jar" Main
# Look for: ✅ SQLite JDBC Driver loaded successfully
```

### Test 2: Manual Connection Test
```java
// Create test file: TestConnection.java
import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("✅ Connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        }
    }
}

// Compile and run:
// javac -cp "sqlite-jdbc.jar" TestConnection.java
// java -cp ".:/app/sqlite-jdbc.jar" TestConnection
```

### Test 3: Query Database Directly
```bash
# Install sqlite3 command line tool if needed
apt-get install sqlite3

# Query database
sqlite3 jumpball.db "SELECT * FROM players;"

# Show schema
sqlite3 jumpball.db ".schema players"

# Count records
sqlite3 jumpball.db "SELECT COUNT(*) FROM players;"
```

## 📦 Classpath Configuration

### Compile-time Classpath
```bash
javac -d /app/build -cp "/app/sqlite-jdbc.jar" Main.java
```

### Runtime Classpath
```bash
java -cp "/app/build:/app/sqlite-jdbc.jar" Main
```

### Explanation
- `/app/build`: Contains compiled .class files
- `/app/sqlite-jdbc.jar`: SQLite JDBC driver
- `:` separates classpath entries (Linux/Mac)
- `;` on Windows instead of `:`

## 🔐 Connection Properties

### Basic Connection (Used in Project)
```java
Connection conn = DriverManager.getConnection("jdbc:sqlite:jumpball.db");
```

### Connection with Properties
```java
Properties props = new Properties();
props.setProperty("journal_mode", "WAL"); // Write-Ahead Logging
props.setProperty("synchronous", "NORMAL");

Connection conn = DriverManager.getConnection("jdbc:sqlite:jumpball.db", props);
```

### Connection Pool Configuration (Advanced)
```java
SQLiteConfig config = new SQLiteConfig();
config.setJournalMode(SQLiteConfig.JournalMode.WAL);
config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);

Connection conn = DriverManager.getConnection("jdbc:sqlite:jumpball.db", config.toProperties());
```

## 📈 Performance Optimization

### 1. Use Transactions for Multiple Inserts
```java
conn.setAutoCommit(false);
try {
    // Multiple insert operations
    for (int i = 0; i < 1000; i++) {
        pstmt.setString(1, "Player" + i);
        pstmt.setLong(2, 180000);
        pstmt.setString(3, timestamp);
        pstmt.executeUpdate();
    }
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
}
```

### 2. Use Prepared Statements (Already Done)
```java
// Prepared statements are cached and reused
PreparedStatement pstmt = conn.prepareStatement(sql);
```

### 3. Connection Reuse (Already Done via Singleton)
```java
DatabaseManager.getInstance(); // Reuses connection
```

## 🐛 Troubleshooting

### Issue: "No suitable driver found"
**Solution**: Ensure `sqlite-jdbc.jar` is in classpath
```bash
java -cp "/app/build:/app/sqlite-jdbc.jar" Main
```

### Issue: "java.lang.ClassNotFoundException: org.sqlite.JDBC"
**Solution**: JDBC jar not in classpath
```bash
# Check jar exists
ls -lah /app/sqlite-jdbc.jar

# Add to classpath
java -cp "/app/build:/app/sqlite-jdbc.jar" Main
```

### Issue: "Database is locked"
**Solution**: Close all connections properly
```java
// Always close in finally block
finally {
    if (conn != null) conn.close();
}
```

### Issue: "SQL syntax error"
**Solution**: Check SQL statement
```java
// Test SQL in sqlite3 command line first
sqlite3 jumpball.db "SELECT * FROM players;"
```

## 📚 JDBC Resources

### Documentation
- **SQLite JDBC**: https://github.com/xerial/sqlite-jdbc
- **JDBC API**: https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html
- **SQLite SQL**: https://www.sqlite.org/lang.html

### Key Classes
- `java.sql.DriverManager` - Manages JDBC drivers
- `java.sql.Connection` - Database connection
- `java.sql.Statement` - SQL statements
- `java.sql.PreparedStatement` - Parameterized SQL
- `java.sql.ResultSet` - Query results

## ✅ Verification Checklist

- [x] JDBC driver (sqlite-jdbc.jar) downloaded
- [x] Driver in classpath during compilation
- [x] Driver in classpath during runtime
- [x] Main.java created with proper JDBC setup
- [x] DatabaseManager.java uses JDBC properly
- [x] Connection testing implemented
- [x] Schema initialization automated
- [x] Error handling for all DB operations
- [x] Resources closed properly (no leaks)
- [x] SQL injection prevention (PreparedStatement)
- [x] Singleton pattern for connection reuse

## 🎯 Summary

The project now has **proper JDBC connection setup** through:

1. ✅ **Main.java** - Entry point with JDBC verification
2. ✅ **DatabaseManager.java** - Singleton DB manager
3. ✅ **Connection testing** - Automatic on startup
4. ✅ **Schema initialization** - Auto-creates tables
5. ✅ **Error handling** - Comprehensive exception handling
6. ✅ **Resource management** - Proper connection closing
7. ✅ **SQL injection prevention** - PreparedStatement usage
8. ✅ **Leaderboard integration** - Fully functional

**Run the game with proper JDBC setup:**
```bash
cd /app
./compile_and_run.sh
```

---

**Happy coding with proper JDBC! 🚀📊**
