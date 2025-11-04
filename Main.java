import JumpBallGame.DatabaseManager;
import JumpBallGame.JumpBallAdventure;

import javax.swing.*;
import java.sql.*;
import java.util.List;

/**
 * Main entry point for Jump Ball Adventure
 * Handles JDBC connection initialization and game startup
 */
public class Main {
    
    // Database configuration
    private static final String DB_URL = "jdbc:sqlite:jumpball.db";
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Jump Ball Adventure - Starting Up");
        System.out.println("===========================================");
        System.out.println();
        
        // Step 1: Verify JDBC Driver
        if (!verifyJdbcDriver()) {
            System.err.println("❌ Failed to load JDBC driver. Exiting.");
            System.exit(1);
        }
        
        // Step 2: Test Database Connection
        if (!testDatabaseConnection()) {
            System.err.println("❌ Failed to connect to database. Exiting.");
            System.exit(1);
        }
        
        // Step 3: Initialize Database Schema
        if (!initializeDatabase()) {
            System.err.println("❌ Failed to initialize database. Exiting.");
            System.exit(1);
        }
        
        // Step 4: Display current leaderboard
        displayLeaderboard();
        
        // Step 5: Launch the game
        System.out.println("\n🎮 Launching Jump Ball Adventure...\n");
        launchGame();
    }
    
    /**
     * Verify that SQLite JDBC driver is available
     */
    private static boolean verifyJdbcDriver() {
        System.out.print("🔍 Verifying JDBC Driver... ");
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("✅ SQLite JDBC Driver loaded successfully");
            
            // Get driver info
            Driver driver = DriverManager.getDriver(DB_URL);
            System.out.println("   Driver: " + driver.getClass().getName());
            System.out.println("   Version: " + driver.getMajorVersion() + "." + driver.getMinorVersion());
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found!");
            System.err.println("   Make sure sqlite-jdbc.jar is in classpath");
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Error getting driver info: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test database connection
     */
    private static boolean testDatabaseConnection() {
        System.out.print("🔌 Testing database connection... ");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("✅ Connected successfully");
                System.out.println("   Database: " + meta.getDatabaseProductName());
                System.out.println("   Version: " + meta.getDatabaseProductVersion());
                System.out.println("   URL: " + DB_URL);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Initialize database schema
     */
    private static boolean initializeDatabase() {
        System.out.print("📊 Initializing database schema... ");
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            
            // Create players table if not exists
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    completion_time_ms BIGINT NOT NULL,
                    completed_date TEXT NOT NULL
                )
            """;
            
            stmt.execute(createTableSQL);
            System.out.println("✅ Database schema ready");
            
            // Check if table exists and get count
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM players");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("   Records in database: " + count);
            }
            rs.close();
            
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Schema initialization failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }
    
    /**
     * Display current leaderboard from database
     */
    private static void displayLeaderboard() {
        System.out.println("\n🏆 Current Leaderboard (Top 10)");
        System.out.println("─────────────────────────────────────────");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            List<DatabaseManager.LeaderboardEntry> leaderboard = dbManager.getLeaderboard(10);
            
            if (leaderboard.isEmpty()) {
                System.out.println("   No records yet. Be the first to play!");
            } else {
                System.out.printf("   %-5s %-20s %-15s%n", "Rank", "Name", "Time");
                System.out.println("   ───────────────────────────────────────");
                
                for (DatabaseManager.LeaderboardEntry entry : leaderboard) {
                    String medal = getMedal(entry.rank);
                    System.out.printf("   %s%-3d  %-20s %s%n", 
                        medal, 
                        entry.rank, 
                        truncate(entry.name, 20), 
                        entry.formattedTime);
                }
            }
        } catch (Exception e) {
            System.err.println("   ❌ Error fetching leaderboard: " + e.getMessage());
        }
        
        System.out.println("─────────────────────────────────────────");
    }
    
    /**
     * Launch the game using Swing
     */
    private static void launchGame() {
        SwingUtilities.invokeLater(() -> {
            try {
                new JumpBallAdventure();
                System.out.println("✅ Game window launched successfully");
                System.out.println("\n📝 Game Instructions:");
                System.out.println("   • Use A/D or Arrow Keys to move");
                System.out.println("   • Use W/Space/Up Arrow to jump");
                System.out.println("   • Press R to restart level");
                System.out.println("   • Complete all 3 levels to save your time!");
                System.out.println("\n🎯 Goal: Get the fastest time on the leaderboard!");
                System.out.println("===========================================\n");
            } catch (Exception e) {
                System.err.println("❌ Failed to launch game: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    /**
     * Get medal emoji for rank
     */
    private static String getMedal(int rank) {
        return switch (rank) {
            case 1 -> "🥇 ";
            case 2 -> "🥈 ";
            case 3 -> "🥉 ";
            default -> "   ";
        };
    }
    
    /**
     * Truncate string to max length
     */
    private static String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Close database connection safely
     */
    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Warning: Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close statement safely
     */
    private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Warning: Error closing statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Utility method to test database operations (for debugging)
     */
    public static void testDatabaseOperations() {
        System.out.println("\n🧪 Running Database Operation Tests...\n");
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            
            // Test 1: Insert test record
            System.out.print("Test 1: Insert test record... ");
            String insertSQL = "INSERT INTO players (name, completion_time_ms, completed_date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, "TestPlayer");
            pstmt.setLong(2, 180000); // 3 minutes
            pstmt.setString(3, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            pstmt.executeUpdate();
            System.out.println("✅ Success");
            pstmt.close();
            
            // Test 2: Query records
            System.out.print("Test 2: Query all records... ");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM players");
            if (rs.next()) {
                System.out.println("✅ Found " + rs.getInt("count") + " records");
            }
            rs.close();
            stmt.close();
            
            // Test 3: Query top record
            System.out.print("Test 3: Query fastest time... ");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name, completion_time_ms FROM players ORDER BY completion_time_ms LIMIT 1");
            if (rs.next()) {
                String name = rs.getString("name");
                long time = rs.getLong("completion_time_ms");
                System.out.println("✅ " + name + " - " + DatabaseManager.formatTime(time));
            }
            rs.close();
            stmt.close();
            
            System.out.println("\n✅ All database tests passed!\n");
            
        } catch (SQLException e) {
            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
}
