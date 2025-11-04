package JumpBallGame;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:jumpball.db";
    private static DatabaseManager instance;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    completion_time_ms BIGINT NOT NULL,
                    completed_date TEXT NOT NULL
                )
            """;
            stmt.execute(createTableSQL);
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void savePlayerRecord(String playerName, long completionTimeMs) {
        String sql = "INSERT INTO players (name, completion_time_ms, completed_date) VALUES (?, ?, ?)";
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, playerName);
            pstmt.setLong(2, completionTimeMs);
            pstmt.setString(3, currentDate);
            pstmt.executeUpdate();
            
            System.out.println("Player record saved: " + playerName + " - " + formatTime(completionTimeMs));
        } catch (SQLException e) {
            System.err.println("Error saving player record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<LeaderboardEntry> getLeaderboard(int limit) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        String sql = "SELECT name, completion_time_ms, completed_date FROM players ORDER BY completion_time_ms ASC LIMIT ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                long timeMs = rs.getLong("completion_time_ms");
                String date = rs.getString("completed_date");
                leaderboard.add(new LeaderboardEntry(rank++, name, timeMs, date));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
        
        return leaderboard;
    }

    public static String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long millis = milliseconds % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    public static class LeaderboardEntry {
        public final int rank;
        public final String name;
        public final long timeMs;
        public final String date;
        public final String formattedTime;

        public LeaderboardEntry(int rank, String name, long timeMs, String date) {
            this.rank = rank;
            this.name = name;
            this.timeMs = timeMs;
            this.date = date;
            this.formattedTime = formatTime(timeMs);
        }
    }
}
