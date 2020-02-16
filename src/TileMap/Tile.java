/* Austin Van Braeckel
 * 12/2018
 * A class that represents a single tile in the tilemap used to model the in-game level/world and manage
 * collisions with platforms and walls.
 */
package TileMap;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Austin Van Braeckel
 */
public class Tile {
    
    private BufferedImage image;
    private int type;
    
    //tile types
    public static final int NORMAL = 0;
    public static final int BLOCKED = 1;
    
    // Constructor
    public Tile(BufferedImage image, int type){
        this.image = image;
        this.type = type;
    }
    
    public BufferedImage getImage(){
        return image;
    }
    
    public int getType(){
        return type;
    }
    
}
