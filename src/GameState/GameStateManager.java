/*Austin Van Braeckel
 * 12/2018
 * A class that manages the game states that are being used/displayed, selecting the one that is to be in-use
 */
package GameState;

import java.awt.Graphics2D;
import Save.*;
import javax.swing.JOptionPane;

import java.io.InputStream;
import java.awt.Font;

/**
 * 
 * @author Austin Van Braeckel
 */
public class GameStateManager {
   
    //attributes
    private GameState[] gameStates;
    private int currentState;
    public int previousState;
    
    public static int tempScore;

    //constants
    public static final int NUMSTATES = 15;
    
    public static final int MENUSTATE = 0;
    public static final int LEVELSELECTSTATE = 1;
    public static final int CREDITSSTATE = 2;
    public static final int CONTROLSSTATE = 3;
    
    public static final int LEVEL1STATE = 4;
    public static final int LEVEL2STATE = 5;
    public static final int LEVEL3STATE = 6;
    public static final int BONUSLEVELSTATE = 11;
    
    public static final int GAMEOVERSTATE = 7;
    public static final int LEVELCOMPLETESTATE = 8;
     
    public static final int SAVESELECTSTATE = 9;
    public static final int SAVEMANAGERSTATE = 10;
    
    public static final int WARPSTATE = 12;
    public static final int DYINGSTATE = 13;
    
    public static final int LOADINGSTATE = 14;
    
    public static Font bauhaus;
    public static Font harlow;
    public static Font arialBold;
    public static Font gillMTBold;
    public static Font gillMTBoldC;
    public static Font gillUltraBoldC;
    public static Font centuryGothicBold;

    public GameStateManager() {
        try {
            //load-in fonts
            InputStream in = getClass().getResourceAsStream("/Fonts/Bauhaus.TTF");
            bauhaus = Font.createFont(Font.TRUETYPE_FONT, in);
            in = getClass().getResourceAsStream("/Fonts/Harlow.TTF");
            harlow = Font.createFont(Font.TRUETYPE_FONT, in);
            in = getClass().getResourceAsStream("/Fonts/ArialBold.ttf");
            arialBold = Font.createFont(Font.TRUETYPE_FONT, in);
            in = getClass().getResourceAsStream("/Fonts/CenturyGothicBold.TTF");
            centuryGothicBold = Font.createFont(Font.TRUETYPE_FONT, in);
            in = getClass().getResourceAsStream("/Fonts/GillMTBold.TTF");
            gillMTBold = Font.createFont(Font.TRUETYPE_FONT, in);
            in = getClass().getResourceAsStream("/Fonts/GillMTBoldC.TTF");
            gillMTBoldC = Font.createFont(Font.TRUETYPE_FONT, in);
            in = getClass().getResourceAsStream("/Fonts/GillUltraBoldC.TTF");
            gillUltraBoldC = Font.createFont(Font.TRUETYPE_FONT, in);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Issue with font-loading.");
        }
        //sets defaults
        tempScore = 0;
        gameStates = new GameState[NUMSTATES];
        //load the save selction screen
        previousState = LOADINGSTATE;
        currentState = LOADINGSTATE;
        loadState(currentState);
        
         
    }

    /**
     * Loads a GameState in an array using the given integer as the index
     * @param state integer of the GameState array index
     */
    private void loadState(int state) {
        //Loads the state corresponding to the constant that matches the given integer
        //creates a new object of the particular state
        if (state == SAVESELECTSTATE) {
            gameStates[state] = new SaveSelect(this);
        }
        if (state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if (state == LEVELSELECTSTATE) {
            gameStates[state] = new LevelSelectState(this);
        }
        if (state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
        }
        if (state == LEVEL2STATE){
            gameStates[state] = new Level2State(this);
        }
        if (state == LEVEL3STATE){
            gameStates[state] = new Level3State(this);
        }
        if (state == BONUSLEVELSTATE){
           gameStates[state] = new BonusLevelState(this);
        }
        if (state == CREDITSSTATE) {
            gameStates[state] = new CreditsState(this);
        }
        if (state == CONTROLSSTATE) {
            gameStates[state] = new ControlsState(this);
        }
        if (state == GAMEOVERSTATE){
            gameStates[state] = new GameOverState(this);
        }
        if (state == LEVELCOMPLETESTATE){
            gameStates[state] = new LevelCompleteState(this);
        }
        if (state == LOADINGSTATE){
            gameStates[state] = new LoadingState(this);
        }
    }

    /**
     * Unloads the state corresponding to the given integer
     * @param state integer of the GameState array index that is to be unloaded
     */
    private void unloadState(int state) {
        //unloads to improve runtime and memory
        gameStates[state] = null; //sets it to null
    }
    
    /**
     * Added method (Sasha). Menu state that prompts the user what he/she would like to do with the user.
     * @param user User in question.
     */
    public void manageUser(User user) {
        unloadState(currentState);
        previousState = currentState;
        currentState = SAVEMANAGERSTATE;
        gameStates[currentState] = new ManageUserState(this, user);
    }

    /**
     * Sets the current state to the the GameState that corresponds to the given integer
     * @param state integer of the GameState array index that will be loaded
     */
    public void setState(int state) {
        unloadState(currentState);
        if (currentState != WARPSTATE && currentState != DYINGSTATE){ //Doesn't record transition states as previous
            previousState = currentState;
        }
        currentState = state;
        loadState(currentState);
    }
    
    /**
     * Sets the state to the GameState corresponding with the given integer, but with a small animated transition
     * @param dest integer of the index of the destination GameState in the array
     */
    public void setWarp(int dest){
        unloadState(currentState);
        previousState = currentState;
        //if the destination is anything other than game over state, normal warp
        if (dest != GAMEOVERSTATE){
            currentState = WARPSTATE;
            gameStates[currentState] = new WarpState(this, dest);
        } else { //different type of loading when game over
            currentState = DYINGSTATE;
            gameStates[currentState] = new DyingState(this);
        }
    }

    /**
     * Retrieves the integer index in the GameState array corresponding to the previous state
     * @return integer of the previous GameState index
     */
    public int getPreviousState(){
        return previousState;
    }
    
    /**
     * Updates the current GameState if not null
     */
    public void update() {
        if (gameStates[currentState] != null){ //if it is not null, update
            gameStates[currentState].update();
        }
    }

    /**
     * Draws the current GameState to the screen using the given Graphics2D object
     * @param g Graphics 2D object that is used to draw
     */
    public void draw(Graphics2D g) {
        if (gameStates[currentState] != null){ //if it is not null, draw
            gameStates[currentState].draw(g);
        }
    }
}
