/* Austin Van Braeckel
 * 12/2018
 * A class that represents the in-game background image that is to be displayed, and move as for visual-appeal;
 * for example, when the player traverses the level.
 */
package TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 *
 * @author Austin Van Braeckel
 */
public class Background {
    
    public int height;
    public int width;
    
    private BufferedImage image;
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private double moveScale; //speed
    
    //Constructor
    public Background(String s, double ms){
        try {
            image = ImageIO.read(getClass().getResourceAsStream(s)); //reads in image
            moveScale = ms;
            height = image.getHeight();
            width = image.getWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPosition(double x, double y){
        //Ensures smooth-scrolling - don't want it to scroll off the screen
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.WIDTH;
    }
    
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public void update(){
        x += dx;
        y += dy;
    }
    
    public void draw(Graphics2D g){
        g.drawImage(image, (int)x, (int)y, null);
        //Makes sure the background is always filling the screen
        //Draws another image when the limit is reached
        if(x < 0) { //background shifting to left
            g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
        } else if (x > 0){ //used if the background is shifting to the right
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
        if(x < 0 - GamePanel.WIDTH || x > GamePanel.WIDTH) { //makes animation endless to the left
            x = 0;
        }
    }
    
}
