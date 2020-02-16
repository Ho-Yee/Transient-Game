/*Austin Van Braeckel
 * 1/2019
 * Acts as a transitional Game State that will proceed to the Game Over State,
 * and it simply plays a sound effect and has a special visual.
 */
package GameState;

import Audio.AudioPlayer;
import TileMap.Background;
import java.awt.Color;
// import java.awt.Font;
import java.awt.Graphics2D;
import Main.GamePanel;

/**
 *
 * @author Austin Van Braeckel
 */
public class DyingState extends GameState {
    
    
    private Background bg;
    private Background detail;
    
//    private String output;
    
    private long startTime;
    private static final long DURATION = 5; //0.5 seconds
    
    //Sets-up the font style and colour of the text     -       Not used in this state
/*    private Color titleColor;
    private Color fontColor;
    private Font titleFont;
    private Font font;
*/    

    public DyingState(GameStateManager gsm) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm; 
        
        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg8.png", 1);
            detail = new Background("/Backgrounds/menuparticles2.png", 1);
            detail.setPosition(0, 220);
            detail.setVector(0, -18);
            
            startTime  = System.nanoTime();
            
            //play game over sound
            AudioPlayer.reset("game over");
            AudioPlayer.adjustVolume("game over", -20.0f);
            AudioPlayer.play("game over");
            
           // loadSprite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes necessary variables and objects for the class
     */
    public void init() {
    }

    public void loadSprite(){//IMPLEMENT
        
    }
    
    /**
     * Updates necessary variables and objects for the class
     */
    public void update() {
        bg.update(); //updates background
        detail.update();
        
        //Updates time elapsed
        long elapsed = (System.nanoTime() - startTime);
        if (elapsed > DURATION * 100000000){
            gsm.setState(GameStateManager.GAMEOVERSTATE);
        }
    }

    /**
     * Draws everything that is needs to be displayed
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
      //bg.draw(g); //draws background
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
      detail.draw(g);
    }  
}
