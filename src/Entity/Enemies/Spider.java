/* Alec Godfrey
 * 12/2018
 * A spider-type enemy that continuously oscillates vertically on a visible wed, and
 * falls-off if hit.
 */
package Entity.Enemies;

/**
 *
 * @author algod5628
 */
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.*;

import TileMap.TileMap;
import Entity.*;
import Entity.Animation;
import Handlers.Content;

public class Spider extends Enemy {

    BufferedImage[] sprites;
    int stringX;
    int stringY;

    private boolean drawWeb;
    

    public Spider(TileMap tm) {
        super(tm);
        moveSpeed = 0.1;
        maxSpeed = 1.0;
        stopSpeed = 0.1;
        fallSpeed = 0.1;
        maxFallSpeed = 4.0;
        
        isSlime = false;
        width = 30;
        height = 25;
        cwidth = 15;
        cheight = 20;

        drawWeb = true;
        stringX = (int) x;
        stringY = (int) gety();

        maxHealth = 5;
        health = maxHealth;
        damage = 8;

        down = true;
        up = false;

        sprites = Content.Spider[0];

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(100);

    }

    private void getNextPosition() {
        if (up) {
            dy -= moveSpeed;
            if (dy < -maxSpeed) {
                dy = -maxSpeed;
            }
        } else if (down) {
            dy += fallSpeed;
            if (dy > maxFallSpeed) {
                dy = maxFallSpeed;
            }
        }

    }

    @Override
    public boolean isSlime() {
        return isSlime;
    }

    public void update(Player p) {
        super.update(p);
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if (down && dy == 0) {
            if (drawWeb){
            dy = -1;
            down = false;
            up = true;
            } else {
                //friction slows it down
                    if (dx > 0){
                    dx -= stopSpeed;
                    }
                    if (dx < 0){
                        dx += stopSpeed;
                    }
                
            }
        }

        if (up && dy == 0) {
            stringY = (int) y;
            dy = 1;
            up = false;
            down = true;

        }
        
        if(!drawWeb){
            up = false;
            down = true;
        }
        animation.update();
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
        drawWeb = false;
        up = true;
        dy = 0;
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();

        if (!notOnScreen()) {
            if (drawWeb) {
                g.setColor(Color.LIGHT_GRAY); //draw web
                g.fillRect((int) (x + xmap - 0.5), (int) (ymap + stringY - 20), 1, (int) (y - stringY + 7));
            }
            g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null); //draw spider
            //draw health bar
            g.setColor(Color.WHITE);
            g.fillRect((int) (x + xmap - 13), (int) (y + ymap - 20), (int) HEALTHBARMAX, 4);
            g.fillRect((int) (x + xmap - 13), (int) (y + ymap - 21), (int) HEALTHBARMAX, 4);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect((int) (x + xmap - 11), (int) (y + ymap - 20), (int) HEALTHBARMAX, 4);
            g.fillRect((int) (x + xmap - 11), (int) (y + ymap - 19), (int) HEALTHBARMAX, 4);
            g.setColor(healthColor);
            g.fillRect((int) (x + xmap - 12), (int) (y + ymap - 20), healthBar, 4);
        }

    }
}
