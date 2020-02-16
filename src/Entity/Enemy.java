/*Austin Van Braeckel
12/14/2018
Represents an entity that can be placed on the map and have functionality and/or
be displayed
 */
package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.Graphics2D;

/**
 *
 * @author Austin Van Braeckel
 */
public class Enemy extends MapObject{
    
    //protected so all subclasses can access
    protected double health;
    protected double maxHealth;
    protected boolean dead;
    protected int damage;
    
    protected boolean givePoints;
    
    protected final double HEALTHBARMAX;
    protected int healthBar;
    protected Color healthColor;
    
    public boolean toRightOfPlayer;
    
    protected boolean flinching;
    protected long flinchTimer;
    
    protected boolean isSlime;
    
    //Constructor
    public Enemy(TileMap tm){
        super(tm);
        HEALTHBARMAX = 25.0;
        healthBar = (int)HEALTHBARMAX;
        healthColor = Color.GREEN;
        toRightOfPlayer = true;
        givePoints = true;
    }
    
    /**
     * Determines whether the enemy is dead or not
     * @return true if dead, and false if not
     */
    public boolean isDead(){
        return dead;
    }
    
    /**
     * Retrieves the damage that the enemy gives to the player
     * @return damage as an integer
     */
    public int getDamage(){
        return damage;
    }
    
    public boolean givePoints(){
        return givePoints;
    }
    
    public void setGivePoints(boolean b){
        givePoints = b;
    }
    
    public boolean isSlime(){
        return isSlime;
    }
    
    /**
     * Hits the enemy with the specified amount of damage
     * @param damage damage to the enemy as an integer
     */
    public void hit(int damage){
        if(dead){ //can't be hit if dead
            return;
        }
        health -= damage;
        
        if(health < 0){ //ensures their health is 0 or greater
            health = 0;
        }
        if(health == 0){ //dead if health reaches 0
            dead = true;
        }
    }
    
    /**
     * Updates the enemy characteristics, animations, attributes
     */
    public void update(Player p){
        //Updates health bar length and color
       double percentHealth = health / maxHealth;
        if (percentHealth > 0.25 && percentHealth <= 0.5){
            healthColor = Color.YELLOW; //yellow if under 50% health
        } else if (percentHealth <= 0.25){
            healthColor = Color.RED; //red if under 25% health
        }
        healthBar = (int)(percentHealth * HEALTHBARMAX);
        
        //Determines if it is to the right or left of the player
        if (x + xmap < p.getx() + xmap){
            //on the left of the player
            toRightOfPlayer = false;
        } else {
            toRightOfPlayer = true;
        }
    }
        
    /**
     * Draws the enemy with its current characteristics, animations, attributes
     */
    public void draw(Graphics2D g){
        super.draw(g);
        
        g.setColor(Color.WHITE);
        g.fillRect((int)(x + xmap - 14), (int)(y + ymap - 9), (int)HEALTHBARMAX, 4);
        g.fillRect((int)(x + xmap - 14), (int)(y + ymap - 10), (int)HEALTHBARMAX, 4);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int)(x + xmap - 12), (int)(y + ymap - 8), (int)HEALTHBARMAX, 4);
        g.fillRect((int)(x + xmap - 12), (int)(y + ymap - 9), (int)HEALTHBARMAX, 4);
           g.setColor(healthColor);
           g.fillRect((int)(x + xmap - 13), (int)(y + ymap - 9), healthBar, 4);
    }
}
