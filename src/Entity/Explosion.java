/*Austin Van Braeckel
 * 1/2019
 * An Entity that represents the explosion created when an enemy is defeated.
 */
package Entity;


import Audio.AudioPlayer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import Handlers.Content;
/**
 *
 * @author Austin Van Braeckel
 */
public class Explosion {
    
    private int x;
    private int y;
    private int xmap;
    private int ymap;
    
    private int width;
    private int height;
    
    private Animation animation;
    private BufferedImage[] sprites;
    
    private boolean remove;
    
	// Constructor
    public Explosion(int x, int y){
        this.x = x;
        this.y = y;
        
        width = 30;
        height = 30;
            
            sprites = new BufferedImage[6];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = Content.Explosion[0][i];
            }
            
            //play sound
            AudioPlayer.adjustVolume("explosion", -7f);
            AudioPlayer.play("explosion");
            
        //set-up animation
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(70);
    }
    
	/**
	 *  Updates the explosion entitiy to check the status of the animation
	 */
    public void update(){
        animation.update();
        if(animation.hasPlayedOnce()){
            remove = true;
        }
    }
    
	/**
	 * Indicates if the explosion entity should be removed from the level since the animation is over
	 * @return true if it should be removed, and false if not
	 */
    public boolean shouldRemove(){
        return remove;
    }
    
	/**
	 * Sets the position of the explosion to the given x and y coordinates in the tilemap
	 * @param x - x-coordinate as an int
	 * @param y - y-coordinate as an int
	 */
    public void setMapPosition(int x, int y){
        xmap = x;
        ymap = y;
    }
    
	/**
	 * Draws the explosion
	 * @param g - the Graphics2D object used to draw
	 */
    public void draw(Graphics2D g){
        g.drawImage(animation.getImage(),
                x + xmap - width / 2,
                y + ymap - height / 2,
                null);
    }
    
}
