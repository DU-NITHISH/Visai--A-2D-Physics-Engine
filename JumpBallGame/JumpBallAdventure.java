package JumpBallGame;

import javax.swing.*;

public class JumpBallAdventure extends JFrame {

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

    public void startLevel1() {
        setContentPane(new Level1(this)); // Load first level
        revalidate();
        repaint();
    }

    public void loadNextLevel() {
        setContentPane(new BossLevel(this)); // Load boss after level 1
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JumpBallAdventure::new);
    }
}
