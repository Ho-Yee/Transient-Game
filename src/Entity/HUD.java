/* Austin Van Braeckel
 * 12/2018
 * A HUD (Heads-Up Display) for the player, showing current values for health and energy/ammo
 */
package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Austin Van Braeckel
 */
public class HUD {
    
    private Player player;
    private BufferedImage image;
    private Font font;
    
    // Constructor
    public HUD(Player p){
        player = p;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/HUD.png"));
            font = new Font("Century Gothic", Font.PLAIN, 10);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Draws the HUD and the associated values in text
     * @param g - The Graphics2D object used to draw
     */
    public void draw(Graphics2D g){
        g.drawImage(image,0,10,null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString((int)(player.getHealth()) + "/" + (int)(player.getMaxHealth()), 30, 23);
        g.drawString(player.getAmmo() / 100 + "/" + (int)(player.getMaxAmmo() / 100), 30, 43);
    }
    
}
