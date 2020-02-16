/*Sasha Seufert
 *1/2019
 * This is a Game State in which the user can either play with the selected
 * saved user, or delete.
 */
package GameState;

import Audio.AudioPlayer;
import java.awt.*;
import TileMap.Background;
import Handlers.Keys;
import Save.*;
import javax.swing.JOptionPane;

/**
 * 
 * @author Austin Van Braeckel
 */
public class ManageUserState extends GameState {

    private Background bg;
    private Background detail;

    private int currentChoice; //keeps track of the selected option
    private String[] options = { //List of options that are given to the user
        "PLAY",
        "DELETE",
        "<< Back"
    };

    //Sets-up the font style and colour of the text
    private Color titleColor;
    private Font titleFont;
    private Font font;
    //Allow the user to use key input to select the options
    //(since it runs at 60fps, there needs to be a delay or else it will update too fast)
    private boolean selection = false;
    private long selectionTime;
    private User currentUser;

    public ManageUserState(GameStateManager gsm, User user) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm;

        //Set current user
        this.currentUser = user;

        //Checks if the user is null (meaning the slot is unassigned).
        if (user == null) {
            //Create new user
            //Declare variables
            String name = JOptionPane.showInputDialog("Please choose a name for a new user:");
            if (name != null) { //If the user did not cancel
                User newUser;

                //Create the user
                newUser = new User(name);

                //Set current user
                this.currentUser = newUser;

                //Manage the newly created user
                gsm.manageUser(newUser);
            } else { //If cancelled
                gsm.setState(9);
            }
        }

        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg8.png", 1);
            detail = new Background("/Backgrounds/menuparticles2.png", 1);
            detail.setVector(-6, 0);
            //set-up font style of the text
            titleColor = new Color(12, 22, 28);
            titleFont = GameStateManager.harlow.deriveFont(Font.BOLD, 65);
            font = GameStateManager.bauhaus.deriveFont(Font.PLAIN, 18);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes necessary variables and objects for the class
     */
    public void init() {
    } //included in the GameState class, but isn't defined here

    /**
     * Updates necessary variables and objects for the class
     */
    public void update() {
        bg.update(); //updates background
        detail.update();
        handleInput(); //updates the user input detection
    }

    /**
     * Draws everything that is needs to be displayed
     *
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 320, 240);
        bg.draw(g); //draws background
        detail.draw(g);

        //draw title
        g.setFont(titleFont);
        g.drawString("Transient", 8, 70);
        g.drawString("Transient", 8, 68);
        g.setColor(titleColor);
        g.drawString("Transient", 10, 70);

        //Draw menu options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(new Color(122, 208, 221));
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
            //Play - Select User
            UserManager.setLoadedUser(currentUser);
            gsm.setWarp(GameStateManager.MENUSTATE);
            //stop music
            AudioPlayer.stop("save select");
            AudioPlayer.reset("save select");
        }
        if (currentChoice == 1) {
            //Delete the current user
            UserManager.deleteUser(currentUser);
        }
        /* OMITTED
        if (currentChoice == 2) {
            //Overwrite user
            UserManager.deleteUser(currentUser); //Delete the current user
            
            //Create new user
            //Declare variables
            String name = JOptionPane.showInputDialog("Please choose a name for a new user:");
            if(name != null) { //If the user did not cancel
                User newUser;

                //Create the user
                newUser = new User(name);
                
                
                //Set current user
                this.currentUser = newUser;

                //Manage the newly created user
                User newUser = null;
                gsm.manageUser(newUser);
                
                gsm.setState(GameStateManager.SAVESELECTSTATE);
            }
        }*/

        if (currentChoice == 1 || currentChoice == 2) {
            //Back
            gsm.setState(GameStateManager.SAVESELECTSTATE);
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

        //selects current choice
        if (Keys.isPressed(Keys.ENTER)) {
            select();
        }

        //goes back to save selection screen
        if (Keys.isPressed(Keys.BACKSPACE)) {
            gsm.setState(GameStateManager.SAVESELECTSTATE);
        }

    //navigates menu options
        if (Keys.isPressed(Keys.UP)) {
            if (!selection) {
                currentChoice--;
                if (currentChoice < 0) {
                    currentChoice = options.length - 1;
                }
                selection = true;
                selectionTime = System.nanoTime();
            }

        }

        if (Keys.isPressed(Keys.DOWN)) {
            if (!selection) {
                currentChoice++;
                if (currentChoice >= options.length) {
                    currentChoice = 0;
                }
                selection = true;
                selectionTime = System.nanoTime();
            }
        }

    }

}
