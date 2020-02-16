/* Alec Godfrey
 * 12/2018
 * A turret enemy that remains stationary, but continuously shoots a projectile
 * that will harm the player.
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
import java.util.ArrayList;
import Entity.*;
import Entity.Animation;
import Handlers.Content;

public class Turret extends Enemy {

    BufferedImage[] sprites;
    private ArrayList<Fireball> fireballs;
    private long stopTime;
    public static boolean isSlime = false;

    public Turret(TileMap tm, boolean right) {
        super(tm);
        facingRight = right;
        moveSpeed = 0.0;
        maxSpeed = 0.0;
        fallSpeed = 0.0;
        maxFallSpeed = 0.0;

        width = 30;
        height = 25;
        cwidth = 10;
        cheight = 15;

        maxHealth = 1;
        health = maxHealth;
        damage = 10;

        down = false;
        up = false;

        sprites = Content.Turret[0];

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setFrame(0);
        animation.setDelay(100);

        fireballs = new ArrayList<Fireball>();
    }

    @Override
    public boolean isSlime() {
        return false;
    }

    public void shoot() {
        Fireball f = new Fireball(tileMap, facingRight);
        f.setPosition(x, y);
        fireballs.add(f);

    }

    public void update(Player p) {
        super.update(p);
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        long elapsed = (System.nanoTime() - stopTime) / 1000000;
        if (elapsed > 2000) {
            shoot();
            stopTime = System.nanoTime();
        }

        for (int i = 0; i < fireballs.size(); i++) {
            fireballs.get(i).update();
            if (fireballs.get(i).shouldRemove()) {
                fireballs.remove(i);
                i++;
            }
        }
        for (int i = 0; i < fireballs.size(); i++) {
            if (fireballs.get(i).intersects(p)) {
                hitPlayer(p);
            }
        }
        animation.update();
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
    }

    public void hitPlayer(Player p) {
        p.hit(damage);
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();
        
        for (int i = 0; i < fireballs.size(); i++) {
            fireballs.get(i).draw(g);
        }
        
        if (!notOnScreen()) {

            if (!facingRight) {
                g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
            } else { //facing right - flips the image because it is only facing left (-width)
                g.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height+7, null);
            }

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
