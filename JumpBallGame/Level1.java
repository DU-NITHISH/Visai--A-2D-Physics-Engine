package JumpBallGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Level1 extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    private final Timer timer;
    private final int FPS = 60;

    // Player
    private double px = 100, py = 400;
    private double vx = 0, vy = 0;
    private final int RADIUS = 18;
    private final double MOVE_ACC = 0.7;
    private final double MAX_VX = 6.0;
    private final double JUMP_IMPULSE = -12.0;
    private final double GRAVITY = 0.6;
    private final double FRICTION = 0.85;
    private boolean leftPressed = false, rightPressed = false, jumpPressed = false;
    private boolean onGround = false;

    // World
    private final ArrayList<Rectangle> platforms = new ArrayList<>();
    private final ArrayList<Rectangle> spikes = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private Rectangle goal;
    private Rectangle coin;
    private boolean coinCollected = false;
    private int coins = 0;
    private int coinMsgTimer = 0;

    // State
    private boolean levelWon = false;
    private boolean levelLost = false;
    private boolean nextLevelTriggered=false;
    private final Random rand = new Random();
    private final JumpBallAdventure parent;
    public Level1(JumpBallAdventure parent) {
        this.parent=parent;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0x7ec0ee));
        setFocusable(true);
        addKeyListener(this);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        buildLevel();
        timer = new Timer(10, this);
        timer.start();
    }

    private void buildLevel() {
        platforms.clear();
        spikes.clear();
        enemies.clear();
        levelWon = false;
        levelLost = false;
        coinCollected = false;

        // Ground
        platforms.add(new Rectangle(0, HEIGHT - 40, WIDTH, 40));

        // Platforms
        platforms.add(new Rectangle(50, 480, 140, 16));
        platforms.add(new Rectangle(260, 430, 120, 16));
        platforms.add(new Rectangle(420, 350, 200, 16));
        platforms.add(new Rectangle(660, 300, 180, 16));
        platforms.add(new Rectangle(420, 520, 120, 16));
        platforms.add(new Rectangle(150, 320, 100, 16));

        // Spikes
        spikes.add(new Rectangle(200, HEIGHT - 56, 40, 16));
        spikes.add(new Rectangle(340, HEIGHT - 56, 40, 16));
        spikes.add(new Rectangle(580, HEIGHT - 56, 40, 16));
        spikes.add(new Rectangle(700,284,40,16));
        // Goal
        goal = new Rectangle(WIDTH - 80, HEIGHT - 100, 56, 60);

        // Coin
        coin = new Rectangle(460, 300 - 30, 18, 18); // above platform

        // Enemies
        enemies.add(new Enemy(300, HEIGHT - 80, 40, 40, 250, 350));
        enemies.add(new Enemy(500, 310, 40, 40, 480, 500));
        enemies.add(new Enemy(50,460,20,20,60,80));
        px = 80;
        py = HEIGHT - 40 - RADIUS * 2 - 2;
        vx = vy = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawHills(g2);

        // Platforms
        g2.setColor(new Color(0x8b5a2b));
        for (Rectangle p : platforms) g2.fill(p);

        // Spikes
        g2.setColor(Color.DARK_GRAY);
        for (Rectangle s : spikes) drawSpikes(g2, s);

        // Goal
        g2.setColor(new Color(0x57c84d));
        g2.fill(goal);
        g2.setColor(Color.BLACK);
        g2.drawString("GOAL", goal.x + 10, goal.y + goal.height / 2 + 5);

        // Coin
        if (!coinCollected) {
            g2.setColor(Color.YELLOW);
            g2.fillOval(coin.x, coin.y, coin.width, coin.height);
            g2.setColor(Color.ORANGE.darker());
            g2.drawOval(coin.x, coin.y, coin.width, coin.height);
        }

        // Enemies
        for (Enemy e : enemies) {
            g2.setColor(new Color(0x4b0082)); // purple box
            g2.fill(e.rect);
            g2.setColor(Color.BLACK);
            g2.draw(e.rect);
        }

        // Player
        Ellipse2D.Double ball = new Ellipse2D.Double(px, py, RADIUS * 2, RADIUS * 2);
        g2.setColor(new Color(0xd32f2f));
        g2.fill(ball);
        g2.setColor(new Color(0x8b0000));
        g2.draw(ball);

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval((int) (px + 6), (int) (py + RADIUS * 2 - 6), RADIUS * 2 - 8, 8);

        // HUD
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("Coins: " + coins, WIDTH - 110, 25);
        g2.drawString("Use A/D or ←/→ to move, W/↑/Space to jump, R to restart", 12, 20);

        if (coinMsgTimer > 0) {
            g2.setColor(Color.ORANGE.darker());
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            g2.drawString("+1 Coin!", (int) px, (int) py - 10);
        }

        if (levelWon) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(new Color(0, 120, 0));
            g2.drawString("Level Complete! Press R to play again", 150, HEIGHT / 2);
        } else if (levelLost) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(Color.RED.darker());
            g2.drawString("You Died! Press R to retry", 260, HEIGHT / 2);
        }

        g2.dispose();
    }

    private void drawHills(Graphics2D g2) {
        g2.setColor(new Color(0x5aa469));
        g2.fillOval(-100, HEIGHT - 220, 400, 240);
        g2.fillOval(200, HEIGHT - 260, 520, 300);
        g2.fillOval(480, HEIGHT - 220, 380, 220);
    }

    private void drawSpikes(Graphics2D g2, Rectangle s) {
        int spikeCount = s.width / 10;
        for (int i = 0; i < spikeCount; i++) {
            int x1 = s.x + i * 10;
            int[] xs = {x1, x1 + 5, x1 + 10};
            int[] ys = {s.y + s.height, s.y, s.y + s.height};
            g2.fillPolygon(xs, ys, 3);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!levelWon && !levelLost) {
            updatePhysics();
            updateEnemies();
            updateCoin();
        }
        if (coinMsgTimer > 0) coinMsgTimer--;
        repaint();
    }

    private void updatePhysics() {
        if (leftPressed && !rightPressed) vx -= MOVE_ACC;
        if (rightPressed && !leftPressed) vx += MOVE_ACC;

        if (vx > MAX_VX) vx = MAX_VX;
        if (vx < -MAX_VX) vx = -MAX_VX;

        if (!leftPressed && !rightPressed) vx *= FRICTION;
        if (Math.abs(vx) < 0.05) vx = 0;

        vy += GRAVITY;
        if (jumpPressed && onGround) {
            vy = JUMP_IMPULSE;
            onGround = false;
        }

        px += vx;
        py += vy;

        if (px < 0) { px = 0; vx = 0; }
        if (px + RADIUS * 2 > WIDTH) { px = WIDTH - RADIUS * 2; vx = 0; }

        onGround = false;
        Rectangle playerRect = new Rectangle((int) px, (int) py, RADIUS * 2, RADIUS * 2);
        for (Rectangle p : platforms) {
            if (playerRect.intersects(p)) {
                Rectangle inter = playerRect.intersection(p);
                if (inter.height < inter.width) {
                    if (py < p.y) {
                        py -= inter.height;
                        vy = 0;
                        onGround = true;
                    } else {
                        py += inter.height;
                        vy = 0.5;
                    }
                } else {
                    if (px < p.x) px -= inter.width; else px += inter.width;
                    vx = 0;
                }
                playerRect.setLocation((int) px, (int) py);
            }
        }

        // Spikes death
        for (Rectangle s : spikes)
            if (playerRect.intersects(s)) { levelLost = true; return; }

        // Enemies death
        for (Enemy e : enemies)
            if (playerRect.intersects(e.rect)) { levelLost = true; return; }

        if (playerRect.intersects(goal) && !nextLevelTriggered) {
            levelWon = true;
            vx = vy = 0;
            nextLevelTriggered = true; // ✅ prevent multiple triggers

            // After a short delay, load next level
            new javax.swing.Timer(1500, ev -> {
                parent.loadBossLevel();
            }) {{
                setRepeats(false); // ✅ run only once
                start();
            }};
        }


        if (py > HEIGHT) levelLost = true;
    }

    private void updateEnemies() {
        for (Enemy e : enemies) e.update();
    }

    private void updateCoin() {
        if (!coinCollected) {
            Rectangle playerRect = new Rectangle((int) px, (int) py, RADIUS * 2, RADIUS * 2);
            if (playerRect.intersects(coin)) {
                coinCollected = true;
                coins++;
                coinMsgTimer = 60; // show for 1 second
            }
        }
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) leftPressed = true;
        if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) rightPressed = true;
        if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W || k == KeyEvent.VK_SPACE) jumpPressed = true;
        if (k == KeyEvent.VK_R) buildLevel();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) leftPressed = false;
        if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) rightPressed = false;
        if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W || k == KeyEvent.VK_SPACE) jumpPressed = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

    // Simple enemy class
    private static class Enemy {
        Rectangle rect;
        double vx;
        int leftLimit, rightLimit;
        Random rand = new Random();

        public Enemy(int x, int y, int w, int h, int left, int right) {
            rect = new Rectangle(x, y, w, h);
            leftLimit = left;
            rightLimit = right;
            vx = (rand.nextBoolean() ? 1 : -1) * (2 + rand.nextDouble() * 1.5);
        }

        void update() {
            rect.x += vx;
            if (rect.x < leftLimit) { rect.x = leftLimit; vx *= -1; }
            if (rect.x + rect.width > rightLimit) { rect.x = rightLimit - rect.width; vx *= -1; }
            if (rand.nextInt(100) == 0) vx *= -1;
        }
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Jump Ball Concept");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JumpBallGame.Level1 game = new JumpBallGame.Level1();
            frame.setContentPane(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }*/
}
