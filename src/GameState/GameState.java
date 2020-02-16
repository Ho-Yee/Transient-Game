/* Austin Van Braeckel
 * 12/2019
 * An abstract class to represent the state/screen that the game is currently in
 */
package GameState;

import java.awt.Graphics2D;

/**
 *
 * @author Austin Van Braeckel
 */
public abstract class GameState {
    
    protected GameStateManager gsm;
    
    public abstract void init();
    public abstract void update();
    public abstract void draw(Graphics2D g);
    
}
