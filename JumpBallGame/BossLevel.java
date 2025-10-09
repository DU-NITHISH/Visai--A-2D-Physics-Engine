package JumpBallGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BossLevel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 900, HEIGHT = 600;
    private final Timer timer = new Timer(10, this);
    private final JumpBallAdventure parent;

    // Player
    double px = 100, py = 500, vx = 0, vy = 0;
    final int R = 18;
    boolean left, right, jump, onGround;
    final double GRAVITY = 0.6, JUMP = -12, ACC = 0.7, MAX_VX = 6, FRICTION = 0.85;
    ArrayList<Particle> explosionParticles = new ArrayList<>();
    boolean bossExploding = false;
    // Ground
    Rectangle ground = new Rectangle(0, HEIGHT - 40, WIDTH, 40);

    // Boss
    Rectangle boss = new Rectangle(600, HEIGHT - 120, 90, 90);
    int bossHP = 5;
    double bossVX = 0;
    boolean bossJumping = false;
    boolean bossAttacking = false;
    int attackCooldown = 0;
    int flashTimer = 0;
    Color bossColor = new Color(0x8b0000);

    // Projectiles
    static class Projectile {
        double x, y, vx;
        final int r = 10;
        Projectile(double x, double y, double vx) {
            this.x = x; this.y = y; this.vx = vx;
        }
        void move() { x += vx; }
        Rectangle rect() { return new Rectangle((int)x, (int)y, r, r); }
    }
    ArrayList<Projectile> projectiles = new ArrayList<>();
    Random rand = new Random();

    boolean won = false, lost = false;

    public BossLevel(JumpBallAdventure parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0x1a1a1a));
        setFocusable(true);
        addKeyListener(this);
        timer.start();

        // Ensure focus for key input
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
// Draw explosion particles
        for (Particle p : explosionParticles) {
            g2.setColor(p.color);
            g2.fillOval((int)p.x, (int)p.y, p.size, p.size);
        }

        // Ground
        g2.setColor(Color.DARK_GRAY);
        g2.fill(ground);

        // Boss HP Bar
        g2.setColor(Color.GRAY);
        g2.fillRect(200, 30, 500, 20);
        g2.setColor(Color.RED);
        g2.fillRect(200, 30, (int) (500 * (bossHP / 5.0)), 20);
        g2.setColor(Color.WHITE);
        g2.drawString("BOSS HP", 420, 25);

        // Boss
        if (bossHP > 0) {
            g2.setColor(flashTimer > 0 ? Color.WHITE : bossColor);
            g2.fill(boss);
            g2.setColor(Color.BLACK);
            g2.drawRect(boss.x, boss.y, boss.width, boss.height);
        }

        // Projectiles
        g2.setColor(Color.ORANGE);
        for (Projectile p : projectiles)
            g2.fill(new Ellipse2D.Double(p.x, p.y, p.r, p.r));

        // Player
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(px, py, R * 2, R * 2));

        // HUD / Info
        g2.setColor(Color.WHITE);
        g2.drawString("←/→ move, ↑ or Space jump, R restart", 20, HEIGHT - 10);

        // Win/Lose
        if (won) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(new Color(0, 255, 0));
            g2.drawString("Boss Defeated! You Win!", 230, HEIGHT / 2);
        } else if (lost) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(Color.RED);
            g2.drawString("You Died! Press R to retry", 230, HEIGHT / 2);
        }

        g2.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!won && !lost) {
            updatePlayer();
            updateBoss();
            updateProjectiles();
            if (flashTimer > 0) flashTimer--;
            if (attackCooldown > 0) attackCooldown--;
        }
        Iterator<Particle> pit = explosionParticles.iterator();
        while (pit.hasNext()) {
            Particle p = pit.next();
            p.update();
            if (p.isDead()) pit.remove();
        }

        repaint();
    }

    private void updatePlayer() {
        if (left && !right) vx -= ACC;
        if (right && !left) vx += ACC;
        if (!left && !right) vx *= FRICTION;
        if (Math.abs(vx) < 0.05) vx = 0;
        if (vx > MAX_VX) vx = MAX_VX;
        if (vx < -MAX_VX) vx = -MAX_VX;

        vy += GRAVITY;
        if (jump && onGround) { vy = JUMP; onGround = false; }

        px += vx;
        py += vy;

        // ground
        if (py + R * 2 > HEIGHT - 40) { py = HEIGHT - 40 - R * 2; vy = 0; onGround = true; }

        // --- COLLISION WITH BOSS ---
        Rectangle player = new Rectangle((int) px, (int) py, R * 2, R * 2);
        if (bossHP > 0 && player.intersects(boss)) {
            // Check if player was above boss in previous frame
            double prevPy = py - vy; // approximate previous Y before this frame

            if (prevPy + R * 2 <= boss.y) {
                // ✅ Player landed on top of the boss
                bossHP--;
                vy = -10;                 // bounce upward
                flashTimer = 10;          // flash effect
                attackCooldown = 60;      // pause boss attacks briefly
                if (bossHP <= 0 && !bossExploding) {
                    bossExploding = true;
                    won = true; // still mark win
                    // spawn particles
                    for (int i = 0; i < 50; i++) {
                        explosionParticles.add(new Particle(boss.getCenterX(), boss.getCenterY()));
                    }
                }


            } else {
                // 💀 Hit from the side or below
                lost = true;
            }
        }


        for (Projectile p : projectiles) {
            if (player.intersects(p.rect())) lost = true;
        }

        if (py > HEIGHT) lost = true;
    }

    private void updateBoss() {
        if (bossHP <= 0) return;

        // chase player slowly
        if (!bossJumping && !bossAttacking) {
            bossVX = (px < boss.x) ? -2 : 2;
            boss.x += bossVX;
        }

        // boundaries
        if (boss.x < 50) boss.x = 50;
        if (boss.x + boss.width > WIDTH - 50) boss.x = WIDTH - boss.width - 50;

        // attack pattern
        if (!bossAttacking && attackCooldown == 0) {
            int attackType = rand.nextInt(3);

            if (attackType == 0) { // 🧨 Fire projectile
                bossAttacking = true;
                double dir = (px < boss.x) ? -5 : 5;
                projectiles.add(new Projectile(boss.getCenterX(), boss.getCenterY(), dir));
                new javax.swing.Timer(800, ev -> bossAttacking = false).start();

            } else if (attackType == 1) { // ⚡ Jump attack
                bossAttacking = true;
                bossJumping = true;
                if(boss.y>getHeight()-boss.height)boss.y -= boss.height;
                new javax.swing.Timer(1000, ev -> bossAttacking = false).start();
            } else {
                // idle roar / no attack
                attackCooldown = 90;
            }
        }

        // gravity for boss
        if (bossJumping) {
            boss.y += 8;
            if (boss.y + boss.height >= HEIGHT - 40) {
                boss.y = HEIGHT - boss.height - 40;
                bossJumping = false;
            }
        }
    }

    private void updateProjectiles() {
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.move();
            if (p.x < 0 || p.x > WIDTH) it.remove();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_UP, KeyEvent.VK_SPACE, KeyEvent.VK_W -> jump = true;
            case KeyEvent.VK_R -> {
                if (lost || won) {
                    BossLevel newLevel = new BossLevel(parent);
                    parent.setContentPane(newLevel);
                    parent.revalidate();
                    SwingUtilities.invokeLater(newLevel::requestFocusInWindow);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_UP, KeyEvent.VK_SPACE, KeyEvent.VK_W -> jump = false;
        }
    }

    static class Particle {
        double x, y, vx, vy;
        int size;
        Color color;
        int life; // frames remaining

        Particle(double x, double y) {
            this.x = x;
            this.y = y;
            Random rand = new Random();
            vx = rand.nextDouble() * 8 - 4; // -4 to 4
            vy = rand.nextDouble() * -6;    // upward
            size = rand.nextInt(6) + 4;     // 4–9 px
            color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            life = 40 + rand.nextInt(20);   // 40–60 frames
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.3; // gravity
            life--;
        }

        boolean isDead() { return life <= 0; }
    }

}
