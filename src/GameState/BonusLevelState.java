/*Austin Van Braeckel
1/2019
This class represents the bonus level (target minigame), having the handling for player controls,
creating target entities, loading the background, map, and tileset.  Loads all elements
necessary for the bonus level specifically.
 */
package GameState;

import java.awt.*;
import TileMap.*;
import Entity.*;
import java.util.ArrayList;
import Handlers.Keys;
import Main.GamePanel;
import Entity.Enemies.*;
import Audio.AudioPlayer;

/**
 * 
 * @author Austin Van Braeckel
 */
public class BonusLevelState extends GameState {

    //attributes
    private TileMap tileMap;
    private TileMap collisions;
    
    private Time time;
    public int score;
    
    private Background foreground;
    private Background bg;
    private Background bg2;
    
    private boolean levelComplete = false;
    private int lastHeight;

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    // private ArrayList<Gem> gems;                 No gems used in bonus level
    private Portal portal;
    private boolean inPortalRange = false;
    
    private boolean flashstepEnabled = true;
    private long flashstepTimer;
    private boolean checkForAttacks = true;
    private long attackTimer;
    
    private static int maxDistance;

    private HUD hud; 
    
    //constructor
    public BonusLevelState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    /**
     * Initializes the level through giving components default values, loading
     * certain files, and populating the entities
     */
    public void init() {
         //initialize tile map
        tileMap = new TileMap(32, true);
        tileMap.loadMap("/Maps/BonusLevel_Display.csv");
        tileMap.loadTiles("/Tilesets/BonusLevelTiles.png");
        tileMap.setPosition(0,0);
        tileMap.setTween(1);
        collisions = new TileMap(32, false);
        collisions.loadMap("/Maps/BonusLevel_Collision.csv");
        collisions.loadTiles("/Tilesets/32x32Collision.png");
        collisions.setPosition(0,0);
        collisions.setTween(1);

        //set-up background(s)
        foreground = new Background("/Backgrounds/BonusLevelFg.png", 1);
        foreground.setPosition(0, -100);
        bg = new Background("/Backgrounds/BonusLevelBg1.png", 0.5);
        bg.setPosition(0,-100);
        bg2 = new Background("/Backgrounds/BonusLevelBg2.png", 0.2);
        bg2.setPosition(0, -100);
        
        //loops 50 times, decreased by 10 decibels
        AudioPlayer.loopFor("bgMusicBonus", -10f, 50);

        //default values
        score = 0;
        maxDistance = 275;
        lastHeight = 0;
        
        //create player and place at the proper position
        player = new Player(collisions);
        player.setPosition(83, 273);

        //set-up a timer
        time = new Time((int)(60 * 1.5), gsm); //1.5 mins
        
        //create a HUD for the player's health and stamina/energy
       hud = new HUD(player);
 
        populateTargets(); //adds enemies to the level
        
        //creates and portal to finish the level by entering and places at the proper position
        portal = new Portal(collisions);
        portal.setPosition(50, 273);

        //set-up an arrayList to contain the explosions
       explosions = new ArrayList<Explosion>(); 
    }

    /**
     * Loads-in all the targets that will be falling
     */
    private void populateTargets() {
        enemies = new ArrayList<Enemy>();
        Target t;
        
        //round 1
        int[] distances = new int[]{
           maxDistance,
           maxDistance - 30,
           maxDistance - 60,
            maxDistance - 60,
            maxDistance - 30,
            maxDistance,
            maxDistance,
            maxDistance - 30,
           maxDistance - 60
        };
//loops through the points and adds enemy at specified position
        
        for (int i = 0; i < distances.length; i++) { 
            t = new Target(collisions);
            t.setPosition(distances[i], 75 - i * 50);
            enemies.add(t);
        }
        lastHeight = -1 * distances.length * 50;
        
        //round 2
//loops through the points and adds target at random distances
        for (int i = 0; i < 15; i++) { 
            t = new Target(collisions);
            
            int rnum = (int)(Math.random() * 3);
            int distance = maxDistance;
            
            if(rnum == 1){
                distance = maxDistance;
            } else if (rnum == 2) {
                distance = maxDistance - 30;
            } else {
                distance = maxDistance - 60;
            }
            
            t.setPosition(distance,(lastHeight - 300) - i * 60);
            enemies.add(t);
        }
        lastHeight = ((lastHeight - 300) - (15 * 60));
        
        //round 2
//loops through the points and adds target at random distances
        for (int i = 0; i < 20; i++) { 
            t = new Target(collisions);
            
            int rnum = (int)(Math.random() * 3);
            int distance = maxDistance;
            
            if(rnum == 1){
                distance = maxDistance;
            } else if (rnum == 2) {
                distance = maxDistance - 30;
            } else {
                distance = maxDistance - 60;
            }
            
            t.setPosition(distance,(lastHeight - 300) - i * 60);
            enemies.add(t);
        }
        
    }

    /**
     * Updates all aspects of the level
     */
    public void update() {

        //detects and handles user input
        handleInput();
        
        //makes sure score is not below zero
        if (score < 0){
            score = 0;
        }
        
        //update player
        player.update();
        player.setAmmo(player.getMaxAmmo());
        
        //update tilemap(s)
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        collisions.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

        //set background
        foreground.setPosition(tileMap.getx(), tileMap.gety() + 32);
        bg.setPosition(tileMap.getx(), tileMap.gety() + 32);
        bg2.setPosition(tileMap.getx(), tileMap.gety() + 32);

        //Only update the portal if all rounds of targets have passed
        if (levelComplete){
            portal.update();
        }
        
        //update all enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update(player);
            if (e.isDead()) { //Checks if the target is "dead"
                if (!e.givePoints()){ //only awards points if player hits it
                    e.setGivePoints(true); //resets
                } else { //add to score
                    //gives different score depending on its distance
                    if (e.getx() > maxDistance - 40){ //furthest
                        score += 25;
                    } else if ((int)e.getx() > maxDistance - 70){ //medium
                        score += 15;
                    } else { //closest
                        score += 5;
                    }
                }
                //removes from array list and reduces index by one to ensure no enemies are skipped in the update process
                enemies.remove(i);
                i--; //compensate removal
               explosions.add(new Explosion(e.getx(), e.gety())); //create explosion at the enemy's position
            }
            
        //attack enemies
       player.checkAttack(enemies);
       checkForAttacks = false;
       attackTimer = System.nanoTime();
        
        }

        //update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i); //removes the explosion from the array list
                i--; //reduces index by one to compensate removal
            }
        }
        
        //Checks if all enemies are dead
        if (enemies.size() < 1){
            levelComplete = true;
        }
        
        //update player's proximity to the levelEnd portal
        if (portal.intersects(player)){
            inPortalRange = true;
        } else {
            inPortalRange = false;
        }
        
        //update timer
        time.update(score);
    }

    /**
     * Draws all aspects of the level that are meant to be visible
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g) {
        //draw background
        bg2.draw(g);
        bg.draw(g);
        foreground.draw(g);
        //draw tilemap
        tileMap.draw(g);
        //draw portal for level completion
        if (levelComplete){
         portal.draw(g);
        }
        //draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        //draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }
        //draw player
        player.draw(g);
        //Draw indication message that player can finish the level (is in range)
        if (inPortalRange && levelComplete){
           Font font = GameStateManager.centuryGothicBold.deriveFont(Font.BOLD, 12);
            g.setFont(font);
            g.setColor(Color.CYAN);
            g.drawString("Press ENTER to Finish", (int) (GamePanel.WIDTH / 2) - 55, (int) (GamePanel.HEIGHT / 2) - 40);
            g.drawString("Press ENTER to Finish", (int) (GamePanel.WIDTH / 2) - 55, (int) (GamePanel.HEIGHT / 2) - 41);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Press ENTER to Finish", (int) (GamePanel.WIDTH / 2) - 54 , (int) (GamePanel.HEIGHT / 2) - 40);
        }
        
        //draw HUD and timer
        hud.draw(g);
        time.draw(g);
    }
    
    
    /**
     * Detects user input and acts accordingly
     */
    public void handleInput() {
         //all keys to move, jump, and glide
        player.setLeft(Keys.keyState[Keys.LEFT]);
        player.setRight(Keys.keyState[Keys.RIGHT]);
        player.setDown(Keys.keyState[Keys.DOWN]);
        player.setJumping(Keys.keyState[Keys.SPACE] || Keys.keyState[Keys.UP]); //jump is also UP arrow key
        player.setGliding(Keys.keyState[Keys.CONTROL]);
        //Flashstep/dash
       if (Keys.isPressed(Keys.SHIFT)){
           if (flashstepEnabled){
               player.setFlashstepping();
               flashstepTimer = System.nanoTime();
               flashstepEnabled = false; //disables it
           } else {
               long elapsed = (System.nanoTime() - flashstepTimer) / 1000000;
               if (elapsed > 1000){ //1 second cool-down
                   flashstepEnabled = true; //enables it again
               }
           }
       }
       //either respawn or end level by entering portal
       if (Keys.isPressed(Keys.ENTER)){
           if (player.isDead()){
               //respawn
               player.respawn(83, 273);
               //reduce score because player died (-100)
               score -= 100;
           } else if (inPortalRange && levelComplete){
               //stops music
               AudioPlayer.stop("bgMusicBonus");
               AudioPlayer.reset("bgMusicBonus");
               score += time.finish(); //finishes timer and adds time bonus
              portal.endLevel(gsm); //level 1
              GameStateManager.tempScore = score; //sets score
              levelComplete = false; //resets
           }
       }
       //determines the attack that the user will use
        if(!player.getIsAttacking()){ 
            if(Keys.isPressed(Keys.W)) player.setRangedAttackingS(); //strong shot
            if(Keys.isPressed(Keys.Q)) player.setRangedAttackingQ(); //quick shot
            if(Keys.isPressed(Keys.A)) player.setAttackingL(); //light melee
            if(Keys.isPressed(Keys.S)) player.setAttackingM(); //medium melee
            if(Keys.isPressed(Keys.D)) player.setAttackingH(); //heavy melee
        }
    }
}
