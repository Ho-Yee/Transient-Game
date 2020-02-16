/* Austin Van Braeckel
 * 1/6/2019
 * A gem/currency object that is picked-up by the player to increase their score
 */
package Entity;

import Handlers.Content;
import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Austin Van Braeckel
 */
public class Gem extends MapObject{
   private Animation animation;
   private BufferedImage[] sprites;
    
   boolean remove; 
    
	// Constructor
    public Gem(TileMap tm, int x, int y){
        
        super(tm);
        
        this.x = x;
        this.y = y;
        
        width = 4;
        height = 6;
        cwidth = 12;
        cheight = 12;
        
        
        
        //load sprites
       try {
                        
            sprites = new BufferedImage[10];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = Content.Gem[0][i];
            }
            
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(45);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * Indicates whether it should be taken out of the game
	 * @return true if it should be removed, and false if not
	 */
    public boolean shouldRemove(){
        return remove;
    }
    
	/**
	 * Sets the position of the gem with the given x and y coordinates on the tilemap
	 * @param x - x-coordinate as an integer
	 * @param y - y-coordinate as an integer
	 */
     public void setMapPosition(int x, int y){
        xmap = x;
        ymap = y;
    }
    
	/**
	 * Updates the animation
	 */
    public void update(){
       animation.update();
    }
    
	/**
	 * Draws the gem with Graphics2D
	 * @param g - the Graphics2D object used to draw
	 */
    public void draw(Graphics2D g){
        if (notOnScreen()) return; //don't draw when not on screen
		
        //draw gem 
        g.drawImage(animation.getImage(),(int)(x + xmap - width / 2),(int)(y + ymap - height / 2),null);
        
    }
    
    
    
    
}
