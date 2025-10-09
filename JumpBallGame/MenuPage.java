package JumpBallGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        title.setBounds(150, 150, 600, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        // Start button
        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        startBtn.setBounds(325, 300, 250, 60);
        startBtn.addActionListener((ActionEvent e) -> parent.startLevel1());
        add(startBtn);

        // Instructions label
        JLabel instructions = new JLabel("<html>Use A/D or ←/→ to move, W/↑/Space to jump</html>");
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 18));
        instructions.setForeground(Color.BLACK);
        instructions.setBounds(200, 400, 500, 50);
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructions);
    }
}
