/* Austin Van Braeckel
 * 1/2019
 * This class represents a state of the game where loading is happening. It makes it look
 * more visually appealing when audio files are loading into the game, by showing a static image indicating that
 * the game is loading, and not frozen/malfunctioning.
 */
package GameState;

import Audio.AudioPlayer;
import TileMap.Background;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * 
 * @author Austin Van Braeckel
 */
public class LoadingState extends GameState {

    private Background bg;

    //Sets-up the font style and colour of the text
    private Color titleColor;
    private Font titleFont;
    private Font font;

    private long elapsed;
    private long start;
    boolean loading;

    public LoadingState(GameStateManager gsm) {
        //sets the game state manager object to the protected variable instantiated in the GamePanel class
        this.gsm = gsm;

        try { //try to read-in image file
            bg = new Background("/Backgrounds/menubg8.png", 1);
            font = GameStateManager.centuryGothicBold.deriveFont(Font.BOLD, 9);
            titleFont = GameStateManager.bauhaus.deriveFont(Font.PLAIN, 30);
            titleColor = new Color(60,100,220);
            start = System.nanoTime();
            loading = false;

            // loadSprite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes necessary variables and objects for the class
     */
    public void init() {
        /*Necessary for inheritance, but no initialization needed*/
    }

    /**
     * Updates necessary variables and objects for the class
     */
    public void update() {
        bg.update(); //updates background
        elapsed = (System.nanoTime() - start) / 1000000;
        if (elapsed > 1000) {
            loading = true;
        }
    }

    /**
     * Draws everything that is needs to be displayed
     *
     * @param g Graphics2D object that is instantiated in the GamePanel class
     */
    public void draw(Graphics2D g) {
        bg.draw(g); //draws background
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("Austin Van Braeckel, Alec Godfrey, Sasha Seufert - 2019", 60, 235);
        g.setFont(titleFont);
         g.drawString("Loading Game...", 48, 130);
          g.drawString("Loading Game...", 48, 129);
          g.setColor(titleColor);
        g.drawString("Loading Game...", 50, 130);
        if (loading) {
            AudioPlayer.init(); //initialize audio
            gsm.setWarp(GameStateManager.SAVESELECTSTATE);
        }
    }
    
}
