package JumpBallGame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CongratsDialog extends JDialog {
    
    public CongratsDialog(JFrame parent, String playerName, long completionTime) {
        super(parent, "🎉 CONGRATULATIONS! 🎉", true);
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0xf0f8ff));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Congrats message
        JLabel congratsLabel = new JLabel("🎊 ALL LEVELS COMPLETED! 🎊");
        congratsLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        congratsLabel.setForeground(new Color(0x00aa00));
        congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(congratsLabel);
        
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Player name
        JLabel nameLabel = new JLabel("Player: " + playerName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        nameLabel.setForeground(new Color(0x0066cc));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nameLabel);
        
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Completion time
        String timeStr = DatabaseManager.formatTime(completionTime);
        JLabel timeLabel = new JLabel("Your Time: " + timeStr);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        timeLabel.setForeground(new Color(0xff6600));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(timeLabel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Leaderboard title
        JLabel leaderboardTitle = new JLabel("🏆 TOP 10 LEADERBOARD 🏆");
        leaderboardTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        leaderboardTitle.setForeground(new Color(0xFF8C00));
        leaderboardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(leaderboardTitle);
        
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Leaderboard panel with scroll
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(Color.WHITE);
        
        List<DatabaseManager.LeaderboardEntry> leaderboard = DatabaseManager.getInstance().getLeaderboard(10);
        
        for (DatabaseManager.LeaderboardEntry entry : leaderboard) {
            JLabel entryLabel = new JLabel(String.format("  %d. %-20s %s", 
                entry.rank, entry.name, entry.formattedTime));
            entryLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
            entryLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // Highlight current player
            if (entry.name.equals(playerName) && 
                Math.abs(entry.timeMs - completionTime) < 100) {
                entryLabel.setOpaque(true);
                entryLabel.setBackground(new Color(0xffff99));
                entryLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
            }
            
            // Color top 3
            if (entry.rank == 1) entryLabel.setForeground(new Color(0xFFD700)); // Gold
            else if (entry.rank == 2) entryLabel.setForeground(new Color(0xC0C0C0)); // Silver
            else if (entry.rank == 3) entryLabel.setForeground(new Color(0xCD7F32)); // Bronze
            
            leaderboardPanel.add(entryLabel);
        }
        
        JScrollPane scrollPane = new JScrollPane(leaderboardPanel);
        scrollPane.setPreferredSize(new Dimension(550, 200));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scrollPane);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Close button
        JButton closeButton = new JButton("Back to Menu");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setPreferredSize(new Dimension(200, 40));
        closeButton.setMaximumSize(new Dimension(200, 40));
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton);
        
        add(mainPanel);
    }
}
