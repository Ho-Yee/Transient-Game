/* Austin Van Braeckel
 * 12/2018
 * A target for the bonus level that is destroyed when hit.
 */
package Entity.Enemies;

import Entity.*;
import Handlers.Content;
import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Main.GamePanel;

/**
 *
 * @author Austin Van Braeckel
 */
public class Target extends Enemy {
    
    
    BufferedImage[] sprites;
    
    public Target(TileMap tm){
        super(tm);
        
        fallSpeed = 0.001; //should only fall
        maxFallSpeed = 0.5;
        falling = true;
        
        isSlime = false;
        
        width = 30;
        height = 30;
        cwidth = 30;
        cheight = 30;
        
        maxHealth = 1;
        health = maxHealth;
        damage = 100;
        
        //Sets-up the sprite array for movement
       sprites = Content.Target[0];
       
        
         //set-up drawing
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(100);
        
        right = true; //always starts moving to the right when created
        facingRight = false; //image is facing left; not right
        
    }
    
    public Target(TileMap tm, int colourNum){
        this(tm);
         //set-up drawing
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(colourNum);
        animation.setDelay(1000000000 * 200000);
    }
    
    
    private void getNextPosition(){
        
         //falling
         if (falling){ //only falls, and can't jump
                dy += fallSpeed;
                if (fallSpeed > maxFallSpeed){ //limits fall speed
                    fallSpeed = maxFallSpeed;
                }
            }
         
         if (y + ymap > GamePanel.HEIGHT){
             //player didn't hit the target
             dead = true;
             givePoints = false;
         }
         
    }
    
    public void update(Player p){
        super.update(p);
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        //update animation
        animation.update();
        
    }
    
    @Override
    public void draw(Graphics2D g){
        
        setMapPosition();
        
        if (!notOnScreen()){ //if it isn't on the screen, don't draw it
            //draws target
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        }
        
        
        
    }
    
}
