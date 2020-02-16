/*Austin Van Braeckel
 * 12/2018
 * This class represents the state of the game where the user selects one of the 
 * following options: level select (to play), controls (to learn how to play), and
 * credits (to see who made the game).
 */
package GameState;

import Audio.AudioPlayer;
import java.awt.*;
import TileMap.Background;
import Handlers.Keys;

/**
 * 
 * @author Austin Van Braeckel
 */
public class MenuState extends GameState {

    private Background bg;
    private Background detail;
    private Background detail2;

    private int currentChoice; //keeps track of the selected option
    private String[] options = { //List of options that are given to the user
        "LEVEL SELECT",
        "CONTROLS",
        "CREDITS",
        "QUIT"
    };

    //Sets-up the font style and colour of the text
    private Color titleColor;
    private Font titleFont;
    private Font font;
    //Allow the user to use key input to select the options
    //(since it runs at 60fps, there needs to be a delay or else it will update too fast)
    private boolean selection = false;
    private long selectionTime;

    public MenuState(GameStateManager gsm) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm; 

        try { //try to read-in image file, and then proceed to initialize certain elements
            
            //set-up backgrounds
            bg = new Background("/Backgrounds/menubg7.png", 1);
            bg.setPosition(0, 11);
            bg.setVector(0, 0); //sets it to move to the left
            detail = new Background("/Backgrounds/menuparticles3.png",1);
            detail.setPosition(0, -60);
            detail.setVector(-10,0);
            detail2 = new Background("/Backgrounds/menuparticles2.png", 1);
            detail2.setVector(-6, 0);
            detail2.setPosition(0, 0);
            
            //set-up font style of the text
            titleColor = new Color(0,100,120);
            titleFont = GameStateManager.harlow.deriveFont(Font.BOLD, 65);
            font = GameStateManager.bauhaus.deriveFont(Font.PLAIN, 18);
            
            //restart music if not coming from the credits or controls screens
            if (gsm.previousState != GameStateManager.CREDITSSTATE && gsm.previousState != GameStateManager.CONTROLSSTATE){
                AudioPlayer.reset("menu");
                AudioPlayer.loopFor("menu", -5f, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes necessary variables and objects for the class
     */
    public void init() {} //included in the GameState class, but isn't defined here

    /**
     * Updates necessary variables and objects for the class
     */
    public void update() {
        bg.update(); //updates background
        detail.update();
        detail2.update();
        handleInput(); //updates the user input detection
    }

    /**
     * Draws everything that is needs to be displayed
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
        //draws background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 320, 240);
        bg.draw(g);
        detail.draw(g);
        detail2.draw(g);

        //draw title
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString("Transient", 8, 70);
        g.drawString("Transient", 8, 68);
        g.setColor(titleColor);
        g.drawString("Transient", 10, 70);

        //Draw menu options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(new Color(60,100,220));
                g.fillOval(12, 129 + i * 22, 13, 7);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options[i], 30, 138 + i * 22);
        }
    }

    /**
     * Selects the user's current option choice
     */
    private void select() {
        if (currentChoice == 0) {
            //stop menu music
            AudioPlayer.stop("menu");
            AudioPlayer.reset("menu");
            //Start level select
            gsm.setState(GameStateManager.LEVELSELECTSTATE);
        }
        if (currentChoice == 1) {
            //Controls
            gsm.setState(GameStateManager.CONTROLSSTATE);
        }

        if (currentChoice == 2) {
            //Credits
            gsm.setState(GameStateManager.CREDITSSTATE);
        }
        if (currentChoice == 3) {
            //Quit
            System.exit(0);
        }
    }

    /**
     * Detects user input and does the necessary actions
     */
    public void handleInput() {
        if (selection) {
            long elapsed = (System.nanoTime() - selectionTime) / 1000000;
            if (elapsed > 150) {
                selection = false;
            }
        }
        
        //select current choice
        if (Keys.isPressed(Keys.ENTER)) {
            select();
        }
        
        //go back to save select
        if (Keys.isPressed(Keys.BACKSPACE)){
            //Back to Save Select
            gsm.setWarp(GameStateManager.SAVESELECTSTATE);
            
            //stop menu music
            AudioPlayer.stop("menu");
            AudioPlayer.reset("menu");
        }
        
    //navigate menu options
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
