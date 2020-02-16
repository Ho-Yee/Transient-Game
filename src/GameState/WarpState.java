/*Austin Van Braeckel
 *1/2019
 *This is a Game State that represents a transition between other
 *Game States. The visuals simulates a "warp," hence the name.
 */
package GameState;

import TileMap.Background;
// import java.awt.Color;
// import java.awt.Font;
import java.awt.Graphics2D;

/**
 * 
 * @author Austin Van Braeckel
 */
public class WarpState extends GameState {
    
    
    private Background bg;
    private Background detail;
    
//    private String output;
    
    private long startTime;
    private static final long DURATION = 5; //0.5 seconds
    private int gameStateNum;
    
    //Sets-up the font style and colour of the text                 -       Not used in this state
/*    private Color titleColor;
    private Color fontColor;
    private Font titleFont;
    private Font font;
*/    

    public WarpState(GameStateManager gsm, int gameStateNum) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm; 
        this.gameStateNum = gameStateNum; 
        
        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg8.png", 1);
            detail = new Background("/Backgrounds/menuparticles2.png", 1);
            detail.setVector(-4, 0);
            
            startTime  = System.nanoTime();
            //set-up font style of the text
            
            
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
    
    public void setDest(int num){
        gameStateNum = num;
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
            gsm.setState(gameStateNum);
        }
    }

    /**
     * Draws everything that is needs to be displayed
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
      bg.draw(g); //draws background
      detail.draw(g);
    }  

}
