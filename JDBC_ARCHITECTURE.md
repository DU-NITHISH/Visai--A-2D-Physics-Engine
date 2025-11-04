# JDBC Architecture Diagram

## 🏗️ Overall JDBC Connection Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Application Layer                        │
│                                                                   │
│  ┌──────────────┐              ┌──────────────────────┐         │
│  │  Main.java   │              │  JumpBallAdventure   │         │
│  │  (Entry)     │──launches──> │  (Game Controller)   │         │
│  └──────┬───────┘              └──────────┬───────────┘         │
│         │                                  │                      │
│         │ verifies JDBC                    │ game events          │
│         │ tests connection                 │                      │
│         │ initializes DB                   │                      │
│         │                                  │                      │
│         └──────────────┬───────────────────┘                      │
│                        │                                          │
└────────────────────────┼──────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Data Access Layer                           │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │           DatabaseManager (Singleton)                     │  │
│  │                                                             │  │
│  │  getInstance()                                             │  │
│  │  initializeDatabase()                                      │  │
│  │  savePlayerRecord(name, time)                             │  │
│  │  getLeaderboard(limit)                                     │  │
│  │  formatTime(ms)                                            │  │
│  └───────────────────────┬───────────────────────────────────┘  │
│                          │                                        │
└──────────────────────────┼────────────────────────────────────────┘
                           │
                           │ uses
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                      JDBC Layer                                  │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  java.sql.DriverManager                                  │   │
│  │  • getConnection(url)                                    │   │
│  │  • getDriver(url)                                        │   │
│  └────────────────┬─────────────────────────────────────────┘   │
│                   │                                              │
│  ┌────────────────▼──────────────────────────────────────────┐ │
│  │  java.sql.Connection                                      │ │
│  │  • createStatement()                                      │ │
│  │  • prepareStatement(sql)                                  │ │
│  │  • commit() / rollback()                                  │ │
│  └────────────────┬──────────────────────────────────────────┘ │
│                   │                                              │
│  ┌────────────────┼──────────────────────────────────────────┐ │
│  │  PreparedStatement / Statement                            │ │
│  │  • executeUpdate()                                        │ │
│  │  • executeQuery()                                         │ │
│  └────────────────┬──────────────────────────────────────────┘ │
│                   │                                              │
│  ┌────────────────▼──────────────────────────────────────────┐ │
│  │  java.sql.ResultSet                                       │ │
│  │  • next()                                                 │ │
│  │  • getString() / getInt() / getLong()                     │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                   │
└───────────────────────────┬───────────────────────────────────────┘
                            │
                            │ uses
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      JDBC Driver Layer                           │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  org.sqlite.JDBC (SQLite JDBC Driver)                     │  │
│  │  File: sqlite-jdbc.jar (v3.47.1.0)                        │  │
│  │  • Implements java.sql.Driver interface                   │  │
│  │  • Handles SQLite-specific operations                     │  │
│  └───────────────────────┬───────────────────────────────────┘  │
│                          │                                        │
└──────────────────────────┼────────────────────────────────────────┘
                           │
                           │ connects to
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Database Layer                              │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  SQLite Database File: jumpball.db                        │  │
│  │                                                             │  │
│  │  Table: players                                            │  │
│  │  ├─ id (INTEGER, PRIMARY KEY, AUTOINCREMENT)              │  │
│  │  ├─ name (TEXT, NOT NULL)                                 │  │
│  │  ├─ completion_time_ms (BIGINT, NOT NULL)                 │  │
│  │  └─ completed_date (TEXT, NOT NULL)                       │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 JDBC Connection Flow

### Initialization Flow (Main.java)

```
Main.main()
    │
    ├─1─> verifyJdbcDriver()
    │       │
    │       ├─> Class.forName("org.sqlite.JDBC")
    │       └─> DriverManager.getDriver(DB_URL)
    │           └─> Returns: org.sqlite.JDBC driver
    │
    ├─2─> testDatabaseConnection()
    │       │
    │       ├─> DriverManager.getConnection("jdbc:sqlite:jumpball.db")
    │       └─> conn.getMetaData()
    │           └─> Returns: SQLite version info
    │
    ├─3─> initializeDatabase()
    │       │
    │       ├─> DriverManager.getConnection(DB_URL)
    │       ├─> conn.createStatement()
    │       ├─> stmt.execute("CREATE TABLE IF NOT EXISTS...")
    │       └─> stmt.executeQuery("SELECT COUNT(*)...")
    │           └─> Returns: Record count
    │
    ├─4─> displayLeaderboard()
    │       │
    │       ├─> DatabaseManager.getInstance()
    │       └─> dbManager.getLeaderboard(10)
    │           └─> Returns: List<LeaderboardEntry>
    │
    └─5─> launchGame()
            │
            └─> SwingUtilities.invokeLater(() -> new JumpBallAdventure())
```

## 💾 Database Operations Flow

### Save Player Record

```
Game Complete
    │
    ├─> JumpBallAdventure.gameCompleted()
    │     │
    │     ├─> Calculate: completionTime = now - startTime
    │     │
    │     └─> DatabaseManager.savePlayerRecord(name, time)
    │           │
    │           ├─> DriverManager.getConnection(DB_URL)
    │           │
    │           ├─> PreparedStatement pstmt = conn.prepareStatement(
    │           │     "INSERT INTO players (name, time, date) VALUES (?, ?, ?)"
    │           │   )
    │           │
    │           ├─> pstmt.setString(1, playerName)
    │           ├─> pstmt.setLong(2, completionTimeMs)
    │           ├─> pstmt.setString(3, currentDateTime)
    │           │
    │           ├─> pstmt.executeUpdate()
    │           │     └─> Writes to jumpball.db
    │           │
    │           └─> Close resources (pstmt, conn)
    │
    └─> Show Congrats Dialog
```

### Query Leaderboard

```
MenuPage/CongratsDialog displays
    │
    ├─> DatabaseManager.getLeaderboard(10)
    │     │
    │     ├─> DriverManager.getConnection(DB_URL)
    │     │
    │     ├─> PreparedStatement pstmt = conn.prepareStatement(
    │     │     "SELECT name, time, date FROM players 
    │     │      ORDER BY completion_time_ms ASC LIMIT ?"
    │     │   )
    │     │
    │     ├─> pstmt.setInt(1, 10)
    │     │
    │     ├─> ResultSet rs = pstmt.executeQuery()
    │     │
    │     ├─> while (rs.next()) {
    │     │     entries.add(new LeaderboardEntry(
    │     │       rs.getString("name"),
    │     │       rs.getLong("completion_time_ms"),
    │     │       rs.getString("completed_date")
    │     │     ))
    │     │   }
    │     │
    │     └─> Close resources (rs, pstmt, conn)
    │           └─> Returns: List<LeaderboardEntry>
    │
    └─> Display in UI (formatted)
```

## 🔐 Connection Management

### Singleton Pattern (DatabaseManager)

```
┌────────────────────────────────────────────┐
│       DatabaseManager (Singleton)          │
│                                            │
│  private static instance = null            │
│                                            │
│  public static getInstance() {             │
│    if (instance == null) {                 │
│      instance = new DatabaseManager()      │
│    }                                       │
│    return instance                         │
│  }                                         │
│                                            │
│  private DatabaseManager() {               │
│    initializeDatabase()                    │
│  }                                         │
└────────────────────────────────────────────┘

Usage:
  DatabaseManager db = DatabaseManager.getInstance();  // First call
  DatabaseManager db2 = DatabaseManager.getInstance(); // Returns same instance
```

### Connection Lifecycle

```
Connection Creation:
  DriverManager.getConnection(url)
      ↓
  [Connection Object Created]
      ↓
  Operations (Insert/Query)
      ↓
  conn.close()
      ↓
  [Connection Released]
```

## 📊 Data Flow Examples

### Example 1: Player Completes Game

```
Player reaches Level 3 goal
    ↓
Level3.gameCompleted() triggered
    ↓
JumpBallAdventure.gameCompleted()
    ↓
Calculate time: 245,678 ms
    ↓
DatabaseManager.savePlayerRecord("Alice", 245678)
    ↓
JDBC Connection established
    ↓
SQL: INSERT INTO players VALUES (?, ?, ?)
     Parameters: ["Alice", 245678, "2024-11-04 17:00:00"]
    ↓
Database writes record
    ↓
Connection closed
    ↓
CongratsDialog shows time and leaderboard
```

### Example 2: Display Leaderboard

```
MenuPage created
    ↓
Fetch leaderboard data
    ↓
DatabaseManager.getLeaderboard(10)
    ↓
JDBC Connection established
    ↓
SQL: SELECT * FROM players ORDER BY time LIMIT 10
    ↓
ResultSet returned with 10 rows
    ↓
Loop through ResultSet:
  Row 1: Alice, 240000 ms
  Row 2: Bob, 255000 ms
  Row 3: Charlie, 270000 ms
  ...
    ↓
Create LeaderboardEntry objects
    ↓
Connection closed
    ↓
Display in UI with rankings and colors
```

## 🔧 JDBC Component Responsibilities

### Main.java
```
Role: Application Entry Point & JDBC Verifier
├─ Load JDBC driver
├─ Test database connectivity
├─ Initialize database schema
├─ Display startup information
└─ Launch game
```

### DatabaseManager.java
```
Role: Data Access Layer
├─ Manage database connections
├─ Execute SQL operations
│  ├─ INSERT (save records)
│  └─ SELECT (query leaderboard)
├─ Format data for display
└─ Handle database errors
```

### JumpBallAdventure.java
```
Role: Game Controller & Timer
├─ Track game start time
├─ Manage player name
├─ Calculate completion time
├─ Trigger database save
└─ Show completion dialog
```

### CongratsDialog.java / MenuPage.java
```
Role: User Interface
├─ Fetch leaderboard data
├─ Display rankings
├─ Format times
└─ Highlight current player
```

## 🛡️ Error Handling Flow

```
Database Operation Attempted
    │
    ├─> Try Block
    │     │
    │     ├─> Open Connection
    │     ├─> Execute SQL
    │     └─> Process Results
    │
    ├─> Catch SQLException
    │     │
    │     ├─> Log error message
    │     ├─> Print stack trace
    │     └─> Return empty/default data
    │
    └─> Finally Block
          │
          ├─> Close ResultSet (if exists)
          ├─> Close Statement (if exists)
          └─> Close Connection (if exists)
```

## 📈 Performance Considerations

### Connection Reuse (Singleton Pattern)
```
First Call:
  getInstance() → Create new instance → Initialize DB → Return

Subsequent Calls:
  getInstance() → Return existing instance (fast!)
```

### Prepared Statement Caching
```
First Execution:
  prepareStatement(sql) → Parse SQL → Create statement → Cache

Subsequent Executions:
  prepareStatement(sql) → Return cached statement (fast!)
```

### Transaction Batching (for multiple inserts)
```
Without Transaction:
  Insert 1 → Commit → Insert 2 → Commit → ... (slow)

With Transaction:
  Begin Transaction
  Insert 1
  Insert 2
  ...
  Commit (once) → Much faster!
```

## 🔍 Classpath Visualization

```
Compilation Classpath:
  /app/JumpBallGame/*.java
  /app/Physics2D/*.java
  /app/Main.java
        +
  /app/sqlite-jdbc.jar
        ↓
  javac -d /app/build -cp "/app/sqlite-jdbc.jar" ...
        ↓
  /app/build/*.class

Runtime Classpath:
  /app/build/*.class
        +
  /app/sqlite-jdbc.jar
        ↓
  java -cp "/app/build:/app/sqlite-jdbc.jar" Main
        ↓
  Application runs with JDBC access
```

## ✅ JDBC Architecture Summary

| Layer | Component | Responsibility |
|-------|-----------|----------------|
| Application | Main.java | Entry point, JDBC verification |
| Application | JumpBallAdventure | Game logic, timer management |
| Data Access | DatabaseManager | Database operations |
| JDBC API | DriverManager | Connection management |
| JDBC API | Connection | Database session |
| JDBC API | PreparedStatement | SQL execution |
| JDBC API | ResultSet | Query results |
| Driver | sqlite-jdbc.jar | SQLite implementation |
| Database | jumpball.db | Data storage |

---

**This architecture ensures proper JDBC connection management, separation of concerns, and maintainable code! 🏗️✨**
