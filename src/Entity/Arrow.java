/* Austin Van Braeckel
 * 12/10/2018
 * A projectile that is shot by the player that can damage enemies,
 * and has an explosion animation on-collision.
 */
package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import TileMap.*;
import Handlers.Content;

/**
 *
 * @author Austin Van Braeckel
 */
public class Arrow extends MapObject{
    
    private boolean hit; //arrow hits something
    private boolean remove; //whether it should be removed from the game
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    
    public Arrow(TileMap tm, boolean right){
        
        super(tm);
       
        facingRight = right;
        moveSpeed = 7; //speed of arrow
        
        if(right){
            dx = moveSpeed; //shoots to the right
        } else {
            dx = -moveSpeed; //shoots to the left
        }
        
        width = 30;
        height = 30;
        cwidth = 14;
        cheight = 14;
        
        //load sprites
        try {
                        
            sprites = new BufferedImage[4];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = Content.Arrow[0][i];
            }
            
            hitSprites = new BufferedImage[3];
            for (int i = 0; i < hitSprites.length; i++) {
                hitSprites[i] = Content.Arrow[1][i];
            }
            
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(50);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setHit(){ //determines if the fireball has hit something
        if (hit){ //if it is already determined to be hit, don't run the code below
            return;
        }
        hit = true;
        animation.setFrames(hitSprites); //sets the animation to the fireball disappearing
        animation.setDelay(70);
        dx = 0; //make the entity stop moving
    }
    
    //Whether it should be taken out of the game
    public boolean shouldRemove(){
        return remove;
    }
    
    public void update(){
        
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        if(dx == 0 && !hit){ //if the fireball stops moving (horizontally) (collides with the map or entity)
            //Set it as "hit" to play the animation and remove it
            setHit();
        }
        
        animation.update();
        
        //If the arrow has hit something, remove it from the game after the hit animation is over
        if(hit && animation.hasPlayedOnce()){ 
            remove = true;
        }
        
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();
        
        //draw arrow 
        super.draw(g);
        
    }
    
    
    
    
}
