/**
 * Austin Van Braeckel
 * 12/2018
 * Represents a slime enemy that moves side-to-side, switching directions if it
 * comes into contact with a wall, and damages the player when they collide.
 */
 
/**
 * @author Austin Van Braeckel
 */
package Entity.Enemies;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import TileMap.TileMap;
import Entity.*;
import Entity.Animation;
import Handlers.Content;

/**
 *
 * @author Austin Van Braeckel
 */
public class Slime extends Enemy {

    BufferedImage[] sprites;

    private static long stopTime;
    private static boolean stopped;

    //constructor
    public Slime(TileMap tm) {
        //sets default values
        super(tm);

        moveSpeed = 0.1;
        maxSpeed = 1;
        fallSpeed = 0.2; //shouldn't fall - just move side to side
        maxFallSpeed = 10.0;

        isSlime = true;

        width = 32;
        height = 25;
        cwidth = 25;
        cheight = 20;

        maxHealth = 20;
        health = maxHealth;
        damage = 5;

        stopped = false;

        //Sets-up the sprite array for movement
        sprites = Content.Slime[0];

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(100);

        right = true; //always starts moving to the right when created
        facingRight = false; //image is facing left; not right

    }

    /**
     * Calculates the next position of the slime, depending on its environment
     * and other factors
     */
    private void getNextPosition() {

        //falling
        if (falling) { //only falls, and can't jump
            dy += fallSpeed;
        }

        //periodically stops (like taking steps)
        if (stopped) {
            if (left) {
                dx = -0.1;
            } else { //right
                dx = 0.1;
            }

            long elapsed = (System.nanoTime() - stopTime) / 1000000;
            if (elapsed > 400) {
                stopped = false;
                stopTime = System.nanoTime();
            }
        } else {

            //only moves side-to-side between walls
            if (left) {
                dx -= moveSpeed; //speeds up
                if (dx < -maxSpeed) { //limits the max speed of the slime
                    dx = -maxSpeed;
                }
            } else if (right) {
                dx += moveSpeed; //speeds up
                if (dx > maxSpeed) { //limits the max speed of the slime
                    dx = maxSpeed;
                }
            }
        }
    }

    /**
     * Updates the slime, using the given player to improve functionality
     *
     * @param p Player object that will be used in the updating process
     */
    public void update(Player p) {
        super.update(p);
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if (!stopped) {
            long elapsed = (System.nanoTime() - stopTime) / 1000000;
            if (elapsed > 400) {
                stopped = true;
                stopTime = System.nanoTime();
            }
        }

        //if it hits a wall, go the other direction
        if (right && dx == 0) { //automatically sets movement to 0 when an entity hits a wall
            right = false;
            left = true;
            facingRight = true;
        } else if (left && dx == 0) {
            right = true;
            left = false;
            facingRight = false;
        }

        //update animation
        animation.update();

    }

    /**
     * Draws the slime using the given Graphics2D object
     *
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g) {

        //sets the map position before drawing
        setMapPosition();

        if (!notOnScreen()) { //if it isn't on the screen, don't draw it
            //draws it the proper way it should be facing
            if (flinching) {
                long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
                if (elapsed / 10 % 2 == 0) {
                    return; //doesn't draw the slime (flashing effect when hit) - every 100 milliseconds
                }
                super.draw(g);
            } else {
                super.draw(g);
            }

        }
    }

}
