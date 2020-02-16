/*Sasha Seufert
 * 1/2019
 * A state of the game in which the user selects a save slot/file
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
 * @author Sasha Seufert
 */
public class SaveSelect extends GameState {

    private Background bg;
    private Background detail;

    private int currentChoice; //keeps track of the selected option
    private String[] options = { //List of options that are given to the user
        "[EMPTY]",
        "[EMPTY]",
        "[EMPTY]",
        "[EMPTY]"
    };
    public User[] users = new User[4];

    //Sets-up the font style and colour of the text
    private Color titleColor;
    private Font titleFont;
    private Font font;
    //Allow the user to use key input to select the options
    //(since it runs at 60fps, there needs to be a delay or else it will update too fast)
    private boolean selection = false;
    private long selectionTime;

    public SaveSelect(GameStateManager gsm) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm; 
        
        //Try and get the saves
        Response currentResponse;
        currentResponse = UserManager.getUsers();
        if(currentResponse instanceof SuccessResponse) {
            User[] tempUsers = (User[])((SuccessResponse) currentResponse).getResponse();
            
            //Populate name fields
            for (int i = 0; i < Math.min(4, tempUsers.length); i++) {
                users[i] = tempUsers[i];
                options[i] = (String)users[i].getName().getResponse();
            }
        }
        else { //If an error occured
            JOptionPane.showMessageDialog(null, "Error loading saves. The game will now quit. Error details:\n\n" + currentResponse.getMessage());
            System.exit(0);
        }

        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg8.png", 1);
            detail = new Background("/Backgrounds/menuparticles2.png", 1);
            detail.setVector(-6, 0);
            //set-up font style of the text
            titleColor = new Color(12, 22, 28);
            titleFont = GameStateManager.harlow.deriveFont(Font.BOLD, 65);
            font = GameStateManager.bauhaus.deriveFont(Font.PLAIN, 18);
            
            //play music if not coming from manage user state
            if (gsm.getPreviousState() != GameStateManager.SAVEMANAGERSTATE){
                //start music
                AudioPlayer.reset("save select");
                AudioPlayer.loopFor("save select", -5f, 1000);
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
        handleInput(); //updates the user input detection
    }

    /**
     * Draws everything that is needs to be displayed
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
        if(currentChoice >= 0 && currentChoice < 4) { //If a user is selected
            gsm.manageUser(users[currentChoice]);
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
        
        if (Keys.isPressed(Keys.ENTER)) {
            select();
        }
        if (Keys.isPressed(Keys.UP)) {
           // if (currentChoice > 0) {
                if(!selection){
                    currentChoice--;
                    if (currentChoice < 0){
                        currentChoice = options.length - 1;
                    }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
                
          //  }
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
