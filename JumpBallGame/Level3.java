package JumpBallGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Level3 extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    private final Timer timer;
    private final JumpBallAdventure parent;

    // Player
    private double px = 50, py = 400;
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
    private final ArrayList<ShootingEnemy> shootingEnemies = new ArrayList<>();
    private final ArrayList<MovingPlatform> movingPlatforms = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private Rectangle goal;

    // State
    private boolean levelWon = false;
    private boolean levelLost = false;
    private boolean nextLevelTriggered = false;
    private final Random rand = new Random();

    public Level3(JumpBallAdventure parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0x2d2d44)); // Dark purple background for difficulty
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
        shootingEnemies.clear();
        movingPlatforms.clear();
        projectiles.clear();
        levelWon = false;
        levelLost = false;

        // Ground (with gaps for extra difficulty)
        platforms.add(new Rectangle(0, HEIGHT - 40, 200, 40));
        platforms.add(new Rectangle(300, HEIGHT - 40, 200, 40));
        platforms.add(new Rectangle(600, HEIGHT - 40, 300, 40));

        // Static platforms
        platforms.add(new Rectangle(50, 500, 100, 16));
        platforms.add(new Rectangle(200, 450, 80, 16));
        platforms.add(new Rectangle(350, 400, 100, 16));
        platforms.add(new Rectangle(500, 350, 80, 16));
        platforms.add(new Rectangle(650, 300, 100, 16));
        platforms.add(new Rectangle(500, 250, 120, 16));
        platforms.add(new Rectangle(300, 200, 100, 16));
        platforms.add(new Rectangle(100, 150, 100, 16));
        platforms.add(new Rectangle(250, 100, 150, 16));
        platforms.add(new Rectangle(500, 100, 100, 16));
        platforms.add(new Rectangle(700, 150, 150, 16));

        // Moving platforms (high difficulty)
        movingPlatforms.add(new MovingPlatform(150, 320, 80, 12, 150, 250, true));
        movingPlatforms.add(new MovingPlatform(600, 200, 80, 12, 600, 700, true));
        movingPlatforms.add(new MovingPlatform(400, 280, 70, 12, 350, 500, true));

        // Spikes (many dangerous areas)
        spikes.add(new Rectangle(210, HEIGHT - 56, 80, 16));
        spikes.add(new Rectangle(510, HEIGHT - 56, 80, 16));
        spikes.add(new Rectangle(160, 434, 30, 16));
        spikes.add(new Rectangle(460, 384, 30, 16));
        spikes.add(new Rectangle(590, 334, 50, 16));
        spikes.add(new Rectangle(290, 184, 40, 16));
        spikes.add(new Rectangle(610, 184, 40, 16));

        // Shooting enemies (multiple threats)
        shootingEnemies.add(new ShootingEnemy(380, 380, 30, 30, 90));
        shootingEnemies.add(new ShootingEnemy(680, 280, 30, 30, 110));
        shootingEnemies.add(new ShootingEnemy(130, 130, 30, 30, 120));
        shootingEnemies.add(new ShootingEnemy(530, 80, 30, 30, 100));
        shootingEnemies.add(new ShootingEnemy(730, 130, 30, 30, 95));

        // Goal at the end
        goal = new Rectangle(WIDTH - 100, HEIGHT - 100, 60, 60);

        // Reset player
        px = 50;
        py = HEIGHT - 40 - RADIUS * 2 - 2;
        vx = vy = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dark atmospheric background
        drawDarkBackground(g2);

        // Static platforms
        g2.setColor(new Color(0x6b4423));
        for (Rectangle p : platforms) g2.fill(p);

        // Moving platforms
        g2.setColor(new Color(0x8b6914));
        for (MovingPlatform mp : movingPlatforms) g2.fill(mp.rect);

        // Spikes
        g2.setColor(new Color(0xff4444));
        for (Rectangle s : spikes) drawSpikes(g2, s);

        // Shooting enemies
        for (ShootingEnemy e : shootingEnemies) {
            g2.setColor(new Color(0x8b0000));
            g2.fill(e.rect);
            g2.setColor(new Color(0xff6666));
            g2.drawRect(e.rect.x, e.rect.y, e.rect.width, e.rect.height);
            // Eye indicator
            g2.setColor(Color.YELLOW);
            g2.fillOval(e.rect.x + 8, e.rect.y + 8, 6, 6);
        }

        // Projectiles
        g2.setColor(new Color(0xff8800));
        for (Projectile p : projectiles) {
            g2.fill(new Ellipse2D.Double(p.x, p.y, p.r * 2, p.r * 2));
            g2.setColor(new Color(0xff4400));
            g2.draw(new Ellipse2D.Double(p.x, p.y, p.r * 2, p.r * 2));
            g2.setColor(new Color(0xff8800));
        }

        // Goal
        g2.setColor(new Color(0x00ff00));
        g2.fill(goal);
        g2.setColor(new Color(0x00aa00));
        g2.drawRect(goal.x, goal.y, goal.width, goal.height);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("FINISH", goal.x + 8, goal.y + goal.height / 2 + 5);

        // Player
        Ellipse2D.Double ball = new Ellipse2D.Double(px, py, RADIUS * 2, RADIUS * 2);
        g2.setColor(new Color(0xff3333));
        g2.fill(ball);
        g2.setColor(new Color(0xaa0000));
        g2.draw(ball);

        // Shadow
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillOval((int) (px + 6), (int) (py + RADIUS * 2 - 6), RADIUS * 2 - 8, 8);

        // HUD
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("LEVEL 3 - FINAL CHALLENGE", WIDTH / 2 - 140, 30);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("A/D or ←/→ move | W/↑/Space jump | R restart", 12, HEIGHT - 10);

        if (levelWon) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(new Color(0, 255, 0));
            g2.drawString("ALL LEVELS COMPLETE!", 200, HEIGHT / 2);
        } else if (levelLost) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(new Color(255, 100, 100));
            g2.drawString("You Died! Press R to retry", 210, HEIGHT / 2);
        }

        g2.dispose();
    }

    private void drawDarkBackground(Graphics2D g2) {
        // Add some dark hills/mountains for atmosphere
        g2.setColor(new Color(0x1a1a2e));
        g2.fillOval(-50, HEIGHT - 200, 350, 220);
        g2.fillOval(250, HEIGHT - 280, 450, 300);
        g2.fillOval(550, HEIGHT - 200, 400, 220);
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
            updateProjectiles();
            updateMovingPlatforms();
        }
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
        
        // Collision with static platforms
        for (Rectangle p : platforms) {
            if (playerRect.intersects(p)) {
                resolveCollision(playerRect, p);
                playerRect.setLocation((int) px, (int) py);
            }
        }

        // Collision with moving platforms
        for (MovingPlatform mp : movingPlatforms) {
            if (playerRect.intersects(mp.rect)) {
                resolveCollision(playerRect, mp.rect);
                playerRect.setLocation((int) px, (int) py);
            }
        }

        // Death conditions
        for (Rectangle s : spikes)
            if (playerRect.intersects(s)) { levelLost = true; return; }

        for (ShootingEnemy e : shootingEnemies)
            if (playerRect.intersects(e.rect)) { levelLost = true; return; }

        for (Projectile p : projectiles)
            if (playerRect.intersects(p.rect())) { levelLost = true; return; }

        // Win condition
        if (playerRect.intersects(goal) && !nextLevelTriggered) {
            levelWon = true;
            vx = vy = 0;
            nextLevelTriggered = true;

            new javax.swing.Timer(1500, ev -> {
                parent.gameCompleted();
            }) {{
                setRepeats(false);
                start();
            }};
        }

        if (py > HEIGHT) levelLost = true;
    }

    private void resolveCollision(Rectangle playerRect, Rectangle platform) {
        Rectangle inter = playerRect.intersection(platform);
        if (inter.height < inter.width) {
            if (py < platform.y) {
                py -= inter.height;
                vy = 0;
                onGround = true;
            } else {
                py += inter.height;
                vy = 0.5;
            }
        } else {
            if (px < platform.x) px -= inter.width; 
            else px += inter.width;
            vx = 0;
        }
    }

    private void updateEnemies() {
        for (ShootingEnemy e : shootingEnemies) {
            e.update();
            if (e.shouldShoot()) {
                // Shoot towards player
                double dx = (px + RADIUS) - (e.rect.x + e.rect.width / 2.0);
                double dy = (py + RADIUS) - (e.rect.y + e.rect.height / 2.0);
                double dist = Math.sqrt(dx * dx + dy * dy);
                double speed = 4.0;
                projectiles.add(new Projectile(
                    e.rect.x + e.rect.width / 2.0,
                    e.rect.y + e.rect.height / 2.0,
                    (dx / dist) * speed,
                    (dy / dist) * speed
                ));
            }
        }
    }

    private void updateProjectiles() {
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.move();
            if (p.x < -20 || p.x > WIDTH + 20 || p.y < -20 || p.y > HEIGHT + 20) {
                it.remove();
            }
        }
    }

    private void updateMovingPlatforms() {
        for (MovingPlatform mp : movingPlatforms) {
            mp.update();
        }
    }

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

    // Shooting enemy class
    private class ShootingEnemy {
        Rectangle rect;
        int shootCooldown;
        int maxCooldown;
        Random rand = new Random();

        public ShootingEnemy(int x, int y, int w, int h, int shootInterval) {
            rect = new Rectangle(x, y, w, h);
            maxCooldown = shootInterval;
            shootCooldown = rand.nextInt(shootInterval);
        }

        void update() {
            shootCooldown--;
        }

        boolean shouldShoot() {
            if (shootCooldown <= 0) {
                shootCooldown = maxCooldown;
                return true;
            }
            return false;
        }
    }

    // Projectile class
    private static class Projectile {
        double x, y, vx, vy;
        final int r = 6;

        Projectile(double x, double y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        void move() {
            x += vx;
            y += vy;
        }

        Rectangle rect() {
            return new Rectangle((int) x, (int) y, r * 2, r * 2);
        }
    }

    // Moving platform class
    private static class MovingPlatform {
        Rectangle rect;
        int startX, endX;
        double vx;
        boolean horizontal;

        MovingPlatform(int x, int y, int w, int h, int start, int end, boolean horizontal) {
            rect = new Rectangle(x, y, w, h);
            this.startX = start;
            this.endX = end;
            this.horizontal = horizontal;
            this.vx = 2.0;
        }

        void update() {
            if (horizontal) {
                rect.x += vx;
                if (rect.x <= startX) {
                    rect.x = startX;
                    vx = Math.abs(vx);
                } else if (rect.x >= endX) {
                    rect.x = endX;
                    vx = -Math.abs(vx);
                }
            }
        }
    }
}
