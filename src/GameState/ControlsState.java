/* Alec Godfrey
 * 12/2018
 * A game state that shows and describes the player controls for the game
 */
package GameState;

/**
 *
 * @author algod5628
 */
import java.awt.*;
import TileMap.Background;
import Handlers.Keys;

public class ControlsState extends GameState {

    private Background bg;
    private Background detail;
    
    private String[] controls = {
        "LEFT:                      ◄",
        "RIGHT:                    ►",
        "JUMP:                     SPACEBAR",
        "LIGHT MELEE:       A",
        "MEDIUM MELEE:   S",
        "HEAVY MELEE:     D",
        "QUICKSHOT:         Q",
        "STRONGSHOT:     W",
        "FLASH STEP:        SHIFT",
        "GLIDE:                    CTRL"
    };
    private Color titleColor;
    private Font titleFont;
    private Font font;

    public ControlsState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/menubg6.png", 1);
            detail = new Background("/Backgrounds/menuparticles2.png", 1);
            detail.setPosition(0, 0);
            detail.setVector(7,0);
            titleColor = new Color(230, 10, 180);
            titleFont = GameStateManager.harlow.deriveFont(Font.BOLD, 60);
            font = GameStateManager.arialBold.deriveFont(Font.BOLD, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
}

    public void init() {

    }

    public void update() {
        bg.update();
        detail.update();
        handleInput();
    }

    public void draw(Graphics2D g) {
        bg.draw(g);
        detail.draw(g);

        //draw title
        
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString("Controls", 43, 70);
        g.drawString("Controls", 43, 68);
        g.setColor(titleColor);
        g.drawString("Controls", 45, 70);

        //draw 
        g.setFont(font);
        g.setColor(new Color(100,50,250));
        
        
        for (int i = 0; i < controls.length; i++) {
            if (i < 5) {
                g.drawString(controls[i], 10, 138 + i * 22);
            } else {
                g.drawString(controls[i], 188, 28 + i * 22);
            }
        }
        g.setColor(Color.WHITE); 
        g.setFont(GameStateManager.gillMTBold.deriveFont(Font.BOLD, 12));
        g.drawString("BACK", 275, 20);
    }
    
    /**
     * Detects user input (pressing backspace to go back to main menu)
     */
    public void handleInput(){
        if (Keys.isPressed(Keys.BACKSPACE)) {
            //Go back to main menu
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

}
