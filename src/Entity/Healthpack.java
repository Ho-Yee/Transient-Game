/* Alec Godfrey
 * 12/2018
 * A "healthpack"/"potion" that restores the player's health when they move into it.
 */
package Entity;

/**
 *
 * @author alecg
 */
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import TileMap.TileMap;
import Handlers.Content;


public class Healthpack extends MapObject{
    private BufferedImage[] sprites;
    
    public Healthpack(TileMap tm){
        super(tm);
        width = 32;
        height = 32;
        cwidth = 20;
        cheight = 20;
        
        fallSpeed = 10;
        maxFallSpeed = 0.2;
        down = true;
        up = false;
        
        sprites = Content.Healthpack[0];
            
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(120);
    }
    
    private void getNextPosition() {
        if (down) {
            dy += fallSpeed;
            if (dy > maxFallSpeed) {
                dy = maxFallSpeed;

            }
        }
    }
    
    public void setPosition(double x, double y){
       // y -= 6; //corrects its position
        super.setPosition(x, y);
    }
    
    public void update(){
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        animation.update();
        
    }
    
    public void draw(Graphics2D g){
        setMapPosition();
        
        if(!notOnScreen()){
            super.draw(g);
        }
    }
}
