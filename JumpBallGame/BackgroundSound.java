package JumpBallGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class BackgroundSound implements Runnable{

    @Override
    public void run() {
        try {
            File file = new File("C:\\College\\2D ENGINE\\2D_PHYSICS_ENGINE\\src\\JumpBallGame\\BackGround\\BackgroundJumpBall.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            while (clip.isActive()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}