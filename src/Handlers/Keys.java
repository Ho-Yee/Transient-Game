/*Austin Van Braeckel
 * 12/13/2018
This class contains a boolean array of current and previous key states for
all 15 keys that are used for this game. A key k is down when keyState[k] is true.
 */

package Handlers;

import java.awt.event.KeyEvent;

/**
 * 
 * @author Austin Van Braeckel
 */
public class Keys {
	
    //Number of total keys
	public static final int NUM_KEYS = 15;
	
        //sets-up arrays
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
        //Create constants toe easily distinguish between keys
	public final static int UP = 0;
	public final static int LEFT = 1;
	public final static int DOWN = 2;
	public final static int RIGHT = 3;
	public final static int A = 4;
	public final static int S = 5;
	public final static int D = 6;
	public final static int Q = 7;
        public final static int W = 8;
        public final static int SHIFT = 9;
        public final static int CONTROL = 10;
        public final static int SPACE = 11;
	public final static int ENTER = 12;
	public final static int ESCAPE = 13;
        public final static int BACKSPACE = 14;
	
        /**
         * Sets the specified key to the given state (pressed or not pressed)
         * @param i integer of the key that is to be checked
         * @param b boolean true if pressed, and false if not pressed
         */
	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_UP) keyState[UP] = b;
		else if(i == KeyEvent.VK_LEFT) keyState[LEFT] = b;
		else if(i == KeyEvent.VK_DOWN) keyState[DOWN] = b;
		else if(i == KeyEvent.VK_RIGHT) keyState[RIGHT] = b;
		else if(i == KeyEvent.VK_A) keyState[A] = b;
		else if(i == KeyEvent.VK_S) keyState[S] = b;
		else if(i == KeyEvent.VK_D) keyState[D] = b;
		else if(i == KeyEvent.VK_Q) keyState[Q] = b;
                else if(i == KeyEvent.VK_W) keyState[W] = b;
                else if(i == KeyEvent.VK_SHIFT) keyState[SHIFT] = b;
                else if(i == KeyEvent.VK_CONTROL) keyState[CONTROL] = b;
                else if(i == KeyEvent.VK_SPACE) keyState[SPACE] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
                else if(i == KeyEvent.VK_BACK_SPACE) keyState[BACKSPACE] = b;

	}
	
        /**
         * Updates the 
         */
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}
	
}
