/*Austin Van Braeckel
12/2019
This class is the main class in which the game runs, creating the JFrame window.
 */
package Main;

import javax.swing.JFrame;

/**
 * 
 * @author Austin Van Braeckel
 */
public class Game {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //set-up JFrame window and set properties/behaviours
        JFrame window = new JFrame("Transient - (2019) - (Austin Van Braeckel, Alec Godfrey, Sasha Seufert)");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null); //starts program at center of screen
        window.setVisible(true);
        
    }
    
}
