package JumpBallGame;

import javax.swing.*;

public class JumpBallAdventure extends JFrame {
    
    private String playerName;
    private long gameStartTime;
    private boolean gameInProgress = false;

    public JumpBallAdventure() {
        new Thread(new BackgroundSound()).start();
        setTitle("Jump Ball Adventure");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new MenuPage(this)); // Start with menu
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void startGame() {
        // Ask for player name before starting
        String name = JOptionPane.showInputDialog(this, 
            "Enter your name to start:", 
            "Player Name", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Name is required to play!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        playerName = name.trim();
        gameStartTime = System.currentTimeMillis();
        gameInProgress = true;
        
        startLevel1();
    }

    public void startLevel1() {
        setContentPane(new Level1(this)); // Load first level
        revalidate();
        repaint();
    }

    public void loadBossLevel() {
        setContentPane(new BossLevel(this)); // Load boss after level 1
        revalidate();
        repaint();
    }

    public void loadLevel3() {
        setContentPane(new Level3(this)); // Load level 3 after boss
        revalidate();
        repaint();
    }

    public void gameCompleted() {
        if (!gameInProgress) return;
        
        long completionTime = System.currentTimeMillis() - gameStartTime;
        gameInProgress = false;
        
        // Save to database
        DatabaseManager.getInstance().savePlayerRecord(playerName, completionTime);
        
        // Show congratulations dialog
        showCongratsDialog(completionTime);
    }

    private void showCongratsDialog(long completionTime) {
        CongratsDialog dialog = new CongratsDialog(this, playerName, completionTime);
        dialog.setVisible(true);
        
        // Return to menu
        setContentPane(new MenuPage(this));
        revalidate();
        repaint();
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getElapsedTime() {
        if (!gameInProgress) return 0;
        return System.currentTimeMillis() - gameStartTime;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JumpBallAdventure::new);
    }
}
