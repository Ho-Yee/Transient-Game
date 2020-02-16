/* Alec Godfrey
 * 12/2018
 * A fireball projectile that is used by turrets as an attack that harms the player.
 */
package Entity;

/**
 *
 * @author algod5628
 */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import TileMap.*;
import Handlers.Content;

public class Fireball extends MapObject {

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;

    public Fireball(TileMap tm, boolean right) {
        super(tm);
        facingRight = right;
        moveSpeed = 3;
        width = 10;
        height = 10;
        cwidth = 8;
        cheight = 6;
        if (facingRight) {
            dx = moveSpeed;
        } else {
            dx = -moveSpeed;
        }

        try {

            sprites = new BufferedImage[1];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = Content.Fireball[0][i];
            }

            hitSprites = new BufferedImage[3];
            for (int i = 0; i < hitSprites.length; i++) {
                hitSprites[i] = Content.Arrow[1][i];
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(50);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHit() {
        if (hit) {
            return;
        }
        hit = true;
        animation.setFrames(hitSprites); //sets the animation to the fireball disappearing
        animation.setDelay(70);
        dx = 0;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if (dx == 0 && !hit) {
            setHit();
        }

        if (hit && animation.hasPlayedOnce()) {
            remove = true;
        }

        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        //draw fireball
        super.draw(g);
        
    }
}
