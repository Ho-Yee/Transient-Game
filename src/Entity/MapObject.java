/**
 * Austin Van Braeckel
 * 12/9/2018
 * An abstract superclass for all entities to have access to the necessary attributes
 * for drawing, animations, collision detection, and movement.
 */

package Entity;

import TileMap.Tile;
import TileMap.TileMap;
import java.awt.Rectangle;
import java.awt.Graphics2D;

import Main.GamePanel;

/**
 * 
 * @author Austin Van Braeckel 
 */
public abstract class MapObject {

//Tile-related
    //Protected so subclasses can access 
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    //position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    //dimensions
    protected int width;
    protected int height;

    //collision box dimensions
    protected int cwidth;
    protected int cheight;

    //collision
    protected int currRow;
    protected int currCol;
    protected double ydest;
    protected double xdest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    //animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight; //determines whether the sprite needs to be fliped (which way entity is facing)

    //movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed; //used for varying jump heights depending on how long the jump key is held

    //Constructor
    public MapObject(TileMap tm) {
        //sets tilemap and gets tile size
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    /**
     * Determines whether the given MapObject intersects
     *
     * @param o entity on the map that is to be tested for intersection
     * @return true if they intersect, and false if not
     */
    public boolean intersects(MapObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    /**
     *Retrieves the collision box rectangle of the map object
     * @return Rectangle object of the collision box
     */
    public Rectangle getRectangle() {
        return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
    }

    /**
     * Detects the map object collision with the tile map by checking the corners
     * @param x double x-value
     * @param y double y-value
     */
    public void calculateCorners(double x, double y) {

        //determines the tiles surrounding the map object
        int leftTile = (int) (x - cwidth / 2) / tileSize;
        int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
        int topTile = (int) (y - cheight / 2) / tileSize;
        int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
        if (topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
            //sets all to false/default
            topRight = false;
            topLeft = false;
            bottomLeft = false;
            bottomRight = false;
            return;
        }
        //gets the type (blocked or not blocked) of the surrounding tiles
        int topL = tileMap.getType(topTile, leftTile);
        int topR = tileMap.getType(topTile, rightTile);
        int botL = tileMap.getType(bottomTile, leftTile);
        int botR = tileMap.getType(bottomTile, rightTile);
        //Assigns boolean values depending on if the tile is Blocked
    //sets te surrounding tiles ot be bloked or not blocked
        topLeft = (topL == Tile.BLOCKED);
        topRight = (topR == Tile.BLOCKED);
        bottomLeft = (botL == Tile.BLOCKED);
        bottomRight = (botR == Tile.BLOCKED);
    }

    /**
     * Determines if a blocked tile has been reached
     */
    public void checkTileMapCollision() {

        //calculate current column and row
        currCol = (int) x / tileSize;
        currRow = (int) y / tileSize;

        //x and y coordinate values 
        xdest = x + dx;
        ydest = y + dy;

        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest);
        
        if (dy < 0) {
            if (topLeft || topRight) {
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            } else {
                ytemp += dy;
            }
        }
        if (dy > 0) {
            if (bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            } else {
                ytemp += dy;
            }
        }

        calculateCorners(xdest, y);
        
        if (dx < 0) { //left
            if (topLeft || bottomLeft) {
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2;
            } else {
                xtemp += dx;
            }
        }
        if (dx > 0) { //right
            if (topRight || bottomRight) {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            } else {
                xtemp += dx;
            }
        }
        

        if (!falling) { //checks if falling
            calculateCorners(x, ydest + 1);
            if (!bottomLeft && !bottomRight) {
                falling = true;
            }
        }
    }

    public int getx() {
        return (int) x;
    }

    public int gety() {
        return (int) y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCWidth() {
        return cwidth;
    }

    public int getCHeight() {
        return cheight;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy) { //speed
        this.dx = dx;
        this.dy = dy;
    }

    //determines where to draw the character
    public void setMapPosition() { //x and y position plus tilemap position
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setJumping(boolean b) {
        jumping = b;
    }

    //determines whether the object needs to be draw (if it is on the screen)
    public boolean notOnScreen() {
        return x + xmap + width < 0
                || //beyond left side of screen
                x + xmap - width > GamePanel.WIDTH
                || //beyond right side
                y + ymap + height < 0
                || //beyond bottom
                y + ymap - height > GamePanel.HEIGHT; //beyond top
    }
    
    //Changes the direction that the entity is facing
    public void draw(Graphics2D g){
        if (facingRight) {
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
        } else { //facing left - flips the image because it is only facing right (-width)
            g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
        }
    }

}
