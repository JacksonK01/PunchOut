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
    /**
     * Creates a new Sound object with the given file location.
     * @param fileLocation the file location of the sound file.
     */
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
    /**
     * Plays the sound.
     */
    public void play() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }
    /**
     * Loops the sound.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    /**
     * Stops the sound.
     */
    public void stop() {
        clip.stop();
    }
    /**
     * Changes the volume of the sound.
     * @param volume float for the volume to change to.
     */
    public void changeVolume(float volume) {
        volumeControl.setValue(volume);
    }
    /**
     * Determines if the sound is playing.
     * @return true if the sound is playing, false otherwise.
     */
    public boolean isPlaying() {
        return clip.isRunning();
    }

}
