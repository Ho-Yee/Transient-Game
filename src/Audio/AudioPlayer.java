/*Austin Van Braeckel
12/13/2018
A class that enables audio through the use of a few libraries.  Uses java's sound
package.  Does not have a constructor, as it serves a sole purpose of setting-up
the audio files to be loaded and played throughout the program, and an AudioPlayer
Object should not be created every time an audio file is to be loaded in, like other
classes.
 */
package Audio;

import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;

public class AudioPlayer {

    //it is made static so it can be used throughout program
    private static HashMap<String, Clip> clips; //Clip is a data structure that holds audio
    private static boolean muted = false;

    /**
     * Initializes by instantiating the String/Clip HashMap
     */
    public static void init() {
        
        //initialize HashMap to contain the clips of sound which will be played throughout the program
        clips = new HashMap<String, Clip>();
    //Load necessary Audio in advance
    //save select/manage user music
    load("/Music/GameMenu.mp3", "save select");
    reset("save select");
        //Menu music
        load("/Music/NeonRunner.mp3", "menu");
        reset("menu");
        //level select music
        load("/Music/Escape.mp3", "level select");
        reset("level select");
        //background music for levels
        load("/Music/Bog-Creatures-On-the-Move_Looping.mp3", "bgMusic1");
        reset("bgMusic1");
        load("/Music/LostJungle.mp3", "bgMusic2");
        reset("bgMusic2");
        load("/Music/Surreal-Chase_Looping.mp3", "bgMusic3");
        reset("bgMusic3");
        load("/Music/Megaman.mp3", "bgMusicBonus");
        reset("bgMusicBonus");
        //Game over sound
        load("/Sound FX/Down-Draft.mp3", "game over");
        reset("game over");
        //level complete sound
        load("/Music/LevelComplete.mp3", "level complete");
        reset("level complete");
        //jump
        load("/Sound FX/BounceMP3.mp3", "jump");
        reset("jump");
        //quick shot
        //strong shot
        load("/Sound FX/FlameArrowMP3.mp3", "strong");
        reset("strong");
        //light melee
        load("/Sound FX/swordMP3.mp3", "light");
        reset("light");
        //medium melee
        load("/Sound FX/Sword3MP3.mp3", "medium");
        reset("medium");
        //heavy melee
        load("/Sound FX/HeavySword.mp3", "heavy");
        reset("heavy");
        //player damaged
        load("/Sound FX/oofMP3.mp3", "hurt");
        reset("hurt");
        //Gem collection sound
        load("/Sound FX/gem1.mp3", "Gem Sound");
        reset("Gem Sound");
        //potion sound
        load("/Sound FX/potionMP3.mp3", "potionDrink");
        reset("potionDrink");
        //explosion sound
        load("/Sound FX/Explosion1.mp3", "explosion");
        reset("explosion");
        //portal sound
        load("/Sound FX/PortalMP3.mp3", "portal");
        reset("portal");
    }

    /**
     * Loads the audio file at the given file path and sets its key to the given
     * string
     *
     * @param s file path String
     * @param n String of the name associated with the audio file in the HashMap
     */
    public static void load(String s, String n) {
        //If the specified index in the HashMap has something there, don't continue
        if (clips.get(n) != null) {
            return;
        }
        Clip clip; //creates a Clip object
        try {
            //Try to read-in audio file
            AudioInputStream ais = AudioSystem.getAudioInputStream(AudioPlayer.class.getResourceAsStream(s));
            //Decodes audio file so it can be used and played
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            //Decoded audio input stream
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip(); //creates a clip and sets it up to be used
            clip.open(dais);
            clips.put(n, clip); //puts it into the HashMap

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decreases or Increases the volume of the given audio file by the given float
     *
     * @param s String name of the audio (HashMap key)
     * @param f float of the decibel decrease
     */
    public static void adjustVolume(String s, float f) {
        if (clips.get(s) == null) {
            return;
        }
        FloatControl control = (FloatControl) clips.get(s).getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(f); // Reduce volume by f decibels.
        //clips.get(s).loop(i);

    }

    /**
     * Decreases or increases the volume of the given audio file by the given float, and loops
     * for the number of times specified by the given integer
     *
     * @param s String name of the audio (HashMap key)
     * @param f float of the decibel increase
     * @param i number of times it will loop for
     */
    public static void adjustVolumeLoop(String s, float f, int i) {
        if (clips.get(s) == null) { //if no such audio exists, don't continue
            return;
        }
        FloatControl gainControl = (FloatControl) clips.get(s).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(f); // Increase volume by f decibels.
        clips.get(s).loop(i);

    }

    public static void mute(String s) {
        if (clips.get(s) == null) { //if no such audio exists, don't continue
            return;
        }
        if (muted) { //don't continue if it is already muted
            return;
        }
        FloatControl gainControl = (FloatControl) clips.get(s).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-999.0f); // decreases the volume for it to be muted
        //clips.get(s).start();
        muted = true; //mutes it
    }

    public static void unmute(String s) {
        if (clips.get(s) == null) { //if no such audio exists, don't continue
            return;
        }
        if (!muted) { //don't continue if it not muted already
            return;
        }
        FloatControl gainControl = (FloatControl) clips.get(s).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(999.0f); // Increases volume for it to be unmuted if it was muted
        // clips.get(s).start();
        muted = false; //unmutes it
    }

    /**
     * Sets the audio to loop for the specified number of times
     *
     * @param s String name of the audio (HashMap key)
     * @param i the number of times it will loop as an integer
     */
    public static void loopFor(String s, float f, int i) {
        if (clips.get(s) == null) {
            return;
        }
        FloatControl control = (FloatControl) clips.get(s).getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(f); // Reduce volume by f decibels.

        clips.get(s).loop(i);
    }

    /**
     * Sets the audio to loop indefinitely
     *
     * @param s String name of the audio (HashMap key)
     */
    public static void loop(String s) {
        loop(s, 0, 0, clips.get(s).getFrameLength() - 5);
    }

    /**
     * Sets the specified Audio to infinitely loop, given the starting frame
     * position, start, and end
     *
     * @param s String name of the audio (HashMap key)
     * @param frame frame position that the audio will start at as an integer
     * @param start beginning point of the loop as an integer
     * @param end end point of the loop as an integer
     */
    public static void loop(String s, int frame, int start, int end) {
        stop(s); //stops the clip
        clips.get(s).setLoopPoints(start, end); //sets the loop points
        clips.get(s).setFramePosition(frame); //sets the frame position
        clips.get(s).loop(Clip.LOOP_CONTINUOUSLY); //loops indefinitely
    }

    /**
     * Plays the audio at the default frame position (0)
     *
     * @param s String name of the audio (HashMap key)
     */
    public static void play(String s) {
        play(s, 0);
    }

    /**
     * Plays the audio file of the given String at the given time in the file
     * (frame position)
     *
     * @param s String name of the audio that is a key in the HashMap
     * @param i integer of the desired frame position that it is to begin at
     */
    public static void play(String s, int i) {
        Clip c = clips.get(s);
        if (c == null) {
            return;
        }
        if (c.isRunning()) {
            c.stop(); //if it is playing already, stop
        }
        c.setFramePosition(i); //set to specified start time
        while (!c.isRunning()) { //starts the clip
            c.start();
        }

    }

    /**
     * Stops the clip if it is running
     *
     * @param s String name of the audio (HashMap key)
     */
    public static void stop(String s) {
        if (clips.get(s) == null) { //if no such clip exists, don't continue
            return;
        }
        if (clips.get(s).isRunning()) { //if it is running, stop it
            clips.get(s).stop();
        }
    }

    /**
     * Resumes playing the audio clip if it has been stopped
     *
     * @param s String name of the audio (HashMap key)
     */
    public static void resume(String s) {
        if (clips.get(s).isRunning()) {
            return;
        }
        clips.get(s).start();
    }

    /**
     * Sets the starting point of the audio playback
     *
     * @param s String name of the audio (HashMap key)
     * @param frame frame at which it will start - as an integer
     */
    public static void setPosition(String s, int frame) {
        clips.get(s).setFramePosition(frame);
    }

    public static void reset(String s) {
        setPosition(s, 0);
    }

    /**
     * Retrieves the number of frames within the audio file
     *
     * @param s String name of the audio (HashMap key)
     * @return integer of the number of frames
     */
    public static int getFrames(String s) {
        return clips.get(s).getFrameLength();
    }

    /**
     * Retrieves the current position of the frames
     *
     * @param s String name of the audio (HashMap key)
     * @return integer of the current frame position
     */
    public static int getPosition(String s) {
        return clips.get(s).getFramePosition();
    }

    /**
     * Closes the audio clip
     *
     * @param s String name of the audio (HashMap key)
     */
    public static void close(String s) {
        stop(s);
        clips.get(s).close();
    }

}
