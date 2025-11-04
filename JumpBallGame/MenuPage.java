package JumpBallGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MenuPage extends JPanel {

    private final JumpBallAdventure parent;

    public MenuPage(JumpBallAdventure parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(900, 600));
        setBackground(new Color(0x7ec0ee)); // Sky blue
        setLayout(null); // We'll manually position buttons and title

        // Title
        JLabel title = new JLabel("Jump Ball Adventure");
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(0xd32f2f));
        title.setBounds(150, 50, 600, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        // Start button
        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        startBtn.setBounds(325, 150, 250, 60);
        startBtn.addActionListener((ActionEvent e) -> parent.startGame());
        add(startBtn);

        // Instructions label
        JLabel instructions = new JLabel("<html>Use A/D or ←/→ to move, W/↑/Space to jump</html>");
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
        instructions.setForeground(Color.BLACK);
        instructions.setBounds(250, 220, 400, 30);
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructions);

        // Leaderboard section
        JLabel leaderboardTitle = new JLabel("🏆 LEADERBOARD - TOP 10 🏆");
        leaderboardTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        leaderboardTitle.setForeground(new Color(0xFF8C00));
        leaderboardTitle.setBounds(250, 270, 400, 30);
        leaderboardTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(leaderboardTitle);

        // Leaderboard panel
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setBounds(200, 310, 500, 260);
        leaderboardPanel.setBackground(new Color(255, 255, 255, 200));
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Fetch and display leaderboard
        List<DatabaseManager.LeaderboardEntry> leaderboard = DatabaseManager.getInstance().getLeaderboard(10);
        
        if (leaderboard.isEmpty()) {
            JLabel noData = new JLabel("No records yet. Be the first!");
            noData.setFont(new Font("SansSerif", Font.ITALIC, 16));
            noData.setAlignmentX(Component.CENTER_ALIGNMENT);
            noData.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
            leaderboardPanel.add(noData);
        } else {
            for (DatabaseManager.LeaderboardEntry entry : leaderboard) {
                JLabel entryLabel = new JLabel(String.format("%d. %s - %s", 
                    entry.rank, entry.name, entry.formattedTime));
                entryLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
                entryLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                entryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                // Highlight top 3
                if (entry.rank == 1) entryLabel.setForeground(new Color(0xFFD700)); // Gold
                else if (entry.rank == 2) entryLabel.setForeground(new Color(0xC0C0C0)); // Silver
                else if (entry.rank == 3) entryLabel.setForeground(new Color(0xCD7F32)); // Bronze
                
                leaderboardPanel.add(entryLabel);
            }
        }

        add(leaderboardPanel);
    }
}
