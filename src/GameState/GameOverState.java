/*Austin Van Braeckel
1/2019
This class represents a state of the game where the player has died, and it is game over,
giving them the option of retrying the level or going back to level select.
 */
package GameState;

import Audio.AudioPlayer;
import Handlers.*;
// import Entity.Animation;
import TileMap.Background;
import Main.GamePanel;
import Save.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
/* import java.awt.image.BufferedImage;         -       Not used in this state
import java.util.ArrayList;
import javax.imageio.ImageIO;
*/

/**
 * 
 * @author Austin Van Braeckel
 */
public class GameOverState extends GameState {

    //attributes
    private Background bg;

    private int currentChoice; //keeps track of the selected option
    private String[] options = { //List of options that are given to the user
        "RETRY LEVEL",
        "GO BACK TO LEVEL SELECT"
    };
    
    private int level;
    
    private int highScore;
    private int tempScore;
    private boolean newHighScore;

    //Sets-up the font style and colour of the text
    private Color titleColor;
    private Font titleFont;
    private Font font;
    
    
    //Allow the user to use key input to select the options
    //(since it runs at 60fps, there needs to be a delay or else it will update too fast)
    private boolean selection = false;
    private long selectionTime;

    public GameOverState(GameStateManager gsm) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm; 

        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.3, 0); //sets it to move to the left

            //set-up font style of the text
            titleColor = Color.RED;
            titleFont = GameStateManager.centuryGothicBold.deriveFont(Font.BOLD, 50);
            font = GameStateManager.arialBold.deriveFont(Font.BOLD, 14);
            
            
            //determines which level was being played before game over screen was shown
            if (gsm.previousState == GameStateManager.LEVEL1STATE){ //level 1
                level = 0;
            } else if (gsm.previousState == GameStateManager.LEVEL2STATE){
                level = 1;
            } else if (gsm.previousState == GameStateManager.LEVEL3STATE){
                level = 2;
            } else if (gsm.previousState == GameStateManager.BONUSLEVELSTATE){
                level = 3;
            }
            
            //retrieve highscore for comparison to the retrieved temporary score for the level
            //retrieve highscore for comparison to the retrieved temporary score for the level
            User me = UserManager.getLoadedUser();
            Response current = me.getScoreForLevel(level);
            if (current instanceof SuccessResponse){
                ((SuccessResponse)current).getResponse();
                highScore = (int)((SuccessResponse) current).getResponse();
            } else { //error response
                //set default score (0)
                highScore = 0;
                me.setScoreForLevel(0, level);
            }
            
            //Retrieves player's score
         tempScore = GameStateManager.tempScore;
         
         if (tempScore > highScore){ //new high score
            newHighScore = true;
            UserManager.getLoadedUser().setScoreForLevel(tempScore, level);
        } else {
             newHighScore = false;
         }
        } catch (Exception e) {
            e.printStackTrace(); //error message
        }
    }

    /**
     * Initializes necessary variables and objects for the class
     */
    public void init() {
    }

    /**
     * Updates necessary variables and objects for the class
     */
    public void update() {
        bg.update(); //updates background
        handleInput(); //updates the user input detection
    }

    /**
     * Draws everything that is needs to be displayed
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
      //  bg.draw(g); //draws background
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
       
        //draw score info
        g.setColor(Color.WHITE);
        g.setFont(GameStateManager.centuryGothicBold.deriveFont(Font.BOLD, 16));
        g.drawString("HIGHSCORE FOR THIS LEVEL: " + highScore, 10, 215);
        g.drawString("YOUR FINAL SCORE: " + tempScore, 10, 230);
        
        //display message indicating new high score if applicable
        if (newHighScore){
            g.setColor(Color.YELLOW);
            g.drawString("NEW HIGHSCORE!", 35, 195);
        }
        
         //draw title
        g.setFont(titleFont);
         g.setColor(Color.WHITE);
        g.drawString("GAME OVER", 10, 70);
        g.drawString("GAME OVER", 10, 72);
        g.setColor(titleColor);
        g.drawString("GAME OVER", 12, 70);

        //Draw menu options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.WHITE);
                g.fillOval(15, 128 + i * 20, 10, 10);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 30, 138 + i * 22);
        }
        
    }

    /**
     * Selects the user's current option choice
     */
    private void select() {
        //stops sound
        AudioPlayer.stop("game over");
        AudioPlayer.reset("game over");
        if (currentChoice == 0) {
            //Start level select
            gsm.setState(gsm.getPreviousState());
        }
        if (currentChoice == 1) {
            //Go back to level select
            gsm.setState(GameStateManager.LEVELSELECTSTATE);
        }
    }

    /**
     * Detects user input and does the necessary actions
     */
    public void handleInput() {
        //ensures that only one option is navigated at a time (game updates 60 times in a second, which is too fast)
        if (selection) {
            long elapsed = (System.nanoTime() - selectionTime) / 1000000;
            if (elapsed > 150) {
                selection = false;
            }
        }
        //selects current choice
        if (Keys.isPressed(Keys.ENTER)) {
            select();
        }
        
        //Navigate options
        if (Keys.isPressed(Keys.UP)) {
                if(!selection){
                    currentChoice--;
                    if (currentChoice < 0){
                        currentChoice = options.length - 1;
                    }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
        }

        if (Keys.isPressed(Keys.DOWN)) {
                if(!selection){
                    currentChoice++;
                    if (currentChoice >= options.length){
                currentChoice = 0;
            }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
            }
    }
}
