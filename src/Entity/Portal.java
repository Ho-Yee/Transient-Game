/*Alec Godfrey
 * 1/2019
 * A portal entity that is used by the player to end the level, by "entering" it by pressing the "ENTER" key when in range
 */
package Entity;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import TileMap.TileMap;
import Handlers.Content;
import GameState.GameStateManager;
import Audio.AudioPlayer;

public class Portal extends MapObject {

    //Array of BufferedImage objects to contain the sprites
    private BufferedImage[] sprites;

    //constructor
    public Portal(TileMap tm) {
        
        //attributes
        //sets dimensions and default values
        super(tm);
        width = 32;
        height = 45;
        cwidth = 20;
        cheight = 45;

        fallSpeed = 10;
        maxFallSpeed = 0.2;
        down = true;
        up = false;

        //gets the sprites from the Content class
        sprites = Content.Portal[0];
        
        //initializes the animation
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(100);
        
        //sets-up sound effects (decrease by 10dB)
        AudioPlayer.adjustVolume("portal", -10f);
    }
    
//NOT BEING USED
    /**
     * Calculates the next position of the portal
     */
    private void getNextPosition() {
        if (down) {
            dy += fallSpeed;
            if (dy > maxFallSpeed) {
                dy = maxFallSpeed;

            }
        }
    }
    
    /**
     * Sets the position of the portal to the given x and y coordinates
     * @param x double of the x-value
     * @param y double of the y-value
     */
    @Override
    public void setPosition(double x, double y){
        y -= 6; //corrects its position
        super.setPosition(x, y);
    }
    
    /** (Austin)
     * Ends the level through entering the portal, showing the level complete
     * screen, and transferring/saving the player's score
     * @param gsm main Game State Manager object of the program
     */
    public void endLevel(GameStateManager gsm){
        //play sound
        AudioPlayer.reset("portal");
        AudioPlayer.play("portal");
        //shows the level complete screen
        gsm.setWarp(GameStateManager.LEVELCOMPLETESTATE);
    }
    
    /**
     * Updates animation of the portal
     */
    public void update(){
       // getNextPosition();
       // checkTileMapCollision();
      //  setPosition(xtemp, ytemp);
        animation.update();
    }
    
    /**
     * Draws the portal with the given Graphics2D object
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g){
        setMapPosition();
        //draws portal if it is on the screen
        if(!notOnScreen()){
            super.draw(g);
        }
    }
}
