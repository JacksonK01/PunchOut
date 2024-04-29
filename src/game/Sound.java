package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Sound {
    Clip clip;
    FloatControl volumeControl;

    public Sound(String fileLocation) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(fileLocation));
            this.clip = AudioSystem.getClip();
            this.clip.open(ais);

            this.volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void play() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        clip.stop();
    }

    public void changeVolume(float volume) {
        volumeControl.setValue(volume);
    }
    public boolean isPlaying() {
        return clip.isRunning();
    }

}
