/* Alec Godfey
 * 12/2018
 * A gamestate that displays teh credits for the game, with Austin Van Braeckel's name in a bigger font
 * since he did the vast majority of the work.
 */
package GameState;

/**
 *
 * @author algod5628
 */
import Handlers.Keys;
import java.awt.*;
import TileMap.Background;

public class CreditsState extends GameState {

    private Background bg;
    private Background detail;
    
    private String[] credits = {
        "AUSTIN VAN BRAECKEL",
        "ALEC GODFREY",
        "SASHA SEUFERT",};

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public CreditsState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/menubg1.png", 1);
            bg.setVector(-5, 0);
            detail = new Background("/Backgrounds/menuparticles2.png", 1);
            detail.setVector(-1, 0);

            titleColor = new Color(210,170,50);
            titleFont = GameStateManager.harlow.deriveFont(Font.BOLD, 50);
            font = GameStateManager.gillUltraBoldC.deriveFont(Font.PLAIN, 16);
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
        g.drawString("Credits", 49, 80);
        g.drawString("Credits", 49, 78);
        g.setColor(titleColor);
        g.drawString("Credits", 50, 80);

        //draw 
        g.setColor(Color.WHITE);
        for (int i = 0; i < credits.length; i++) {
            if (i == 0){
                g.setFont(GameStateManager.gillUltraBoldC.deriveFont(Font.BOLD, 17));
            } else {
        g.setFont(font);
            }
            g.drawString(credits[i], 50, 138 + i * 22);
        }
        
        //draw credits for sources
        g.setFont(GameStateManager.centuryGothicBold.deriveFont(Font.BOLD, 10));
        g.drawString("Sources: Mike S., Eric Matyas, Rvros", 10, 235);
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
