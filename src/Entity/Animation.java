/* Austin Van Braeckel
 * 12/2018
 * An animation class that handles all frame operations related to animated
 * visuals involved with sprites.
 */

package Entity;

import java.awt.image.BufferedImage;

/**
 *
 * @author Austin Van Braeckel
 */
public class Animation {
    
    private BufferedImage[] frames;
    private int currentFrame;
    
    private long startTime;
    private long delay;
    
    private boolean playedOnce;
	
	// Constructor
    public Animation(){
        playedOnce = false;
    }
    
	/**
	 * Sets the array of frame images to the given BufferedImage array
	 * @param frames - An array of BufferedImages
	 */
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }    
    
	/**
	 * Sets the frame delay to the given number
	 * @param d - the number of in-game frames to delay the cycle of frames as a long
	 */
    public void setDelay(long d){
        delay = d;
    }
    
	/**
	 * Sets the current frame of the animation to the element in the frame array of the given index
	 * @param i - the index of the BufferedImage array element as an integer
	 */
    public void setFrame(int i){
        currentFrame = i;
    }
    
	/**
	 * Updates the animation, going to the next frame if enough time has elapsed
	 */
    public void update(){
        
        if (delay == -1){ //no animation
            return;
        }
        
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > delay){
            currentFrame++;
            startTime = System.nanoTime();
        }
        
        if (currentFrame >= frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }
    
	/**
	 * Gives the current index of the image that is being shown in frame array
	 * @return an integer of the frame number
	 */
    public int getFrame(){
        return currentFrame;
    }
    
	/**
	 * Gives the image of the current frame
	 * @return a BufferedImage of the frame image
	 */
    public BufferedImage getImage(){
        return frames[currentFrame];
    }
    
	/**
	 * Indicates whether the animation has fully completed once or not
	 * @return true if the animation has fully played, and false if not
	 */
    public boolean hasPlayedOnce(){
        return playedOnce;
    }
    
    
    
}
