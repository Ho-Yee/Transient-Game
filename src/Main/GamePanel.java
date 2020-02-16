/*Austin Van Braeckel
 * 12/2018
 * A class that utilizes JPanel to act as the container for all game visuals, drawing the game to the screen
 * and listening for key presses by the user.
 */
package Main;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;

import Handlers.Keys;
import GameState.GameStateManager;

/**
 * 
 * @author Austin Van Braeckel
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {
    
    //dimenstions
    public static final int WIDTH = 320, HEIGHT = 240;
    public static final int SCALE = 2;
    
    //game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000 /  FPS;
    
    //image
    private BufferedImage image;
    private Graphics2D g;
    
    //game state manager
    private GameStateManager gsm;
   
    //Constructor
    public GamePanel(){
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }
    
    /**
     * Uses the super class of the component/container, and starts a thread with a 
     * key listener if it has not yet been started
     */
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }
    
    /**
     * Initializes any necessary components
     */
    private void init(){
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D)image.getGraphics();
        
        running = true;
        
        //creates the game state manager
        gsm = new GameStateManager();
    }

    /**
     * Runs the program - contains the game loop, which updates and draws all game elements
     */
    @Override
    public void run() {
        
        init();
        
        long start, elapsed = 0, wait;
        //game loop
        while (running){
            
            start = System.nanoTime();
            
            update();
            
            if(elapsed < 500000) {
            //if(true) {
                draw();
                drawToScreen();
            }
            elapsed = System.nanoTime() - start;
            
            draw();
            drawToScreen();
            
            elapsed = System.nanoTime() - start;
            
            wait = targetTime - elapsed / 1000000;
            if (wait <= 0){ //prevents a negative wait time
                wait = 5;
            }
            
            
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace(); //prints error
            }
        }
    }
    
    /**
     * Updates the game and its elements
     */
    private void update(){
        gsm.update();
        Keys.update();
    }
    
    /**
     * Draws the game to the game state manager
     */
    private void draw(){
        gsm.draw(g);
    }
    
    /**
     * Draws the game to the screen
     */
    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(image,0,0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
    }
    
    /**
     *Actions for when a key is typed
     * @param key KeyEvent object
     */
    public void keyTyped(KeyEvent key){};
    
    /**
     * Actions for when a key is pressed
     * @param key KeyEvent object
     */
    public void keyPressed(KeyEvent key) {
                //sets the given key to being pressed in the Keys class
		Keys.keySet(key.getKeyCode(), true);
	}
    
    /**
     * Actions for when a key is released
     * @param key KeyEvent object
     */
	public void keyReleased(KeyEvent key) {
            //sets the given key to no longer being pressed in the Keys class
		Keys.keySet(key.getKeyCode(), false);
	}
}
