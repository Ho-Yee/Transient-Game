/*Austin Van Braeckel
12/2018
This class represents level three (ice stage), having the handling for player controls,
creating entities, loading the background, map, and tileset.  Loads all elements
necessary for level three specifically.
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
public class Level3State extends GameState {

    //Attributes
    private TileMap tileMap;
    private TileMap collisions;
    private Background bg;
    private Background snow;

    private int score;
    
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<Gem> gems;
    private Portal portal;
    boolean inPortalRange = false;
    private Time time;
    
    private boolean flashstepEnabled = true;
    private long flashstepTimer;
    private boolean checkForAttacks = true;
    private long attackTimer;
    

    private HUD hud; 
    

    public Level3State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        //initialize tile map(s)
        tileMap = new TileMap(16, true);
        tileMap.loadMap("/Maps/IceLevel_Display.csv");
        tileMap.loadTiles("/Tilesets/rvrosSample.png");
        tileMap.setPosition(0,753);
        tileMap.setTween(1);
        collisions = new TileMap(16, false);
        collisions.loadMap("/Maps/IceLevel_Collision.csv");
        collisions.loadTiles("/Tilesets/16x16Collision.png");
        collisions.setPosition(0,753);
        collisions.setTween(1);

        //set-up background(s)
        bg = new Background("/Backgrounds/Level3Bg.png", 1);
        bg.setPosition(0, 0);
        snow = new Background("/Backgrounds/Level3Snow.png", 1);
        snow.setPosition(0, 0);
        snow.setVector(-10,0);
        
        //loops 50 times, decreased by 15 decibels
       AudioPlayer.loopFor("bgMusic3", -15, 50);
       AudioPlayer.adjustVolume("potionDrink", -10.0f);
        AudioPlayer.adjustVolume("Gem Sound", -7f);

        //Creates a player and places them at the start of the level
        player = new Player(collisions);
        player.setPosition(100, 753);
        player.setStopAndMoveSpeed(0.02, 0.1);
        
        //default score is 0
        score = 0;
        
        //creates a player HUD showing stamina/energy and health
       hud = new HUD(player);
 
       //Set-up timer for the level
       time = new Time((int)(60 * 2.5), gsm); //2.5 minutes
       
        populateEnemies(); //adds enemies to the level
        
        loadGems(); //adds gems to the level
        
        //creates a portal to end the level at the specified position
        portal = new Portal(collisions);
        portal.setPosition(3990, 353);

        //sets-up an array list to hold all explosions
       explosions = new ArrayList<Explosion>(); 
    }

    /**
     * Loads the enemies at the proper positions
     */
    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        Slime s;
        Point[] points = new Point[]{
            new Point(480,753), //starting area
            new Point(440,753),
            new Point(410,753),
            new Point(380,753),
            new Point(350,753),
            new Point(320,753),
            new Point(840,625), //middle area
            new Point(880,625),
            new Point(3180,449), //end area
            new Point(3150,449),
            new Point(3120,449),
            new Point(3090,449),
            new Point(3060,449),
            new Point(3030,449),
            new Point(3000,449),
        };
//loops through the points and adds enemy at specified position
        for (int i = 0; i < points.length; i++) { 
            s = new Slime(collisions);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
        
        points = new Point[]{
          new Point(1600, 740),
            new Point (1900, 740),
            new Point (1970, 740)
        };
        
        Spider spider;
        for (int i = 0; i < points.length; i++) {
            spider = new Spider(collisions);
            spider.setPosition(points[i].x, points[i].y);
            enemies.add(spider);
        }

    }

    /**
     * Loads the collectible  gems at the proper positions
     */
    private void loadGems(){
        gems = new ArrayList<Gem>();
        Gem g;
        Point[] points = new Point[]{
            new Point(565,689),
            new Point(575,689),
            new Point(585,689),
            new Point(595,689),
                new Point (605,689),
                new Point(820,625),
                new Point(830,625),
                new Point(840,625),
                    new Point(980,737),
                    new Point(990, 737),
                    new Point(1000,737),
                        new Point(1190,705),
                        new Point(1200,705),
                        new Point(1205,705),
                            new Point(1440,657),
                            new Point(1450,657),
                            new Point(1620,753),
                            new Point(1940,753),
                                new Point(3420,385),
                                new Point(3430,385),
                                new Point(3440,385),
                                new Point(3445,385),
                                new Point(3450,385),
                                    new Point(3730,353),
                                    new Point(3740,353),
                                    new Point(3750,353),
        };
        
         for (int i = 0; i < points.length; i++) {
            g = new Gem(collisions, points[i].x, points[i].y);
            gems.add(g);
        }
         
         for (int i = 400; i < 505; i += 10){ //starting area
              g = new Gem(collisions, i, 753);
                gems.add(g);
         }
         
         for (int i = 1210; i < 2265; i += 10){ //secret area
                g = new Gem(collisions, i, 209);
                gems.add(g);
         }
         
         for (int i = 3000; i < 3155; i += 10){ //middle area
             g = new Gem(collisions, i, 449);
             gems.add(g);
         }
         
    }
    
    /**
     * Updates all aspects of the level
     */
    public void update() {
        
        //detects and handles input from user
        handleInput();
        
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        collisions.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        //update portal
        portal.update();

        //Makes sure the player's attack only hits the enemy once
        if (checkForAttacks){
        //attack enemies
       score += player.checkAttack(enemies);
       checkForAttacks = false;
       attackTimer = System.nanoTime();
        } else {
            long elapsed = (System.nanoTime() - attackTimer) / 1000000;
            if(elapsed > 100){
                checkForAttacks = true;
            }
        }
        
        //set background
        bg.setPosition(tileMap.getx(), tileMap.gety());
        snow.update();

        //update all enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update(player);
            if (e.isDead()) { //Checks if the enemy is dead
                //removes from array list and reduces index by one to ensure no enemies are skipped in the update process
                enemies.remove(i);
                i--; //compensate removal
                score += 30; //Add to score (30 points)
               explosions.add(new Explosion(e.getx(), e.gety())); //create explosion at the enemy's position
            }
        }

        //update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i); //removes the explosion from the array list
                i--; //reduces index by one to compensate removal
            }
        }
        
        //update gems
        for (int i = 0; i < gems.size(); i++) {
            gems.get(i).update(); //update animation
           //NOT NEEDED? gems.get(i).update(player);
            if (gems.get(i).intersects(player)) { //player collects coin and it is removed from the game
                gems.remove(i); //removes the explosion from the array list
                //plays sound
                AudioPlayer.play("Gem Sound");
                //Adds to the player's score (+15)
                score += 15;
                i--; //reduces index by one to compensate removal
            }
        }
        
        //update player's proximity to the levelEnd portal
        if (portal.intersects(player)){
            inPortalRange = true;
        } else {
            inPortalRange = false;
        }
        
        
        //ensures that score does not go below zero
        if (score < 0){
            score = 0;
        }
        
        //update level timer (converts to seconds)
        time.update(score);
    }

    /**
     * Draws all necessary components of the level to the screen with the given Graphics2D object
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g) {
        //draw background
        bg.draw(g);
       
        //draw tilemap
        tileMap.draw(g);
        //Draw portal
            portal.draw(g);
        //draw gems
        for (int i = 0; i < gems.size(); i++) {
            gems.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            gems.get(i).draw(g);
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
        
        //checks if the player is in range of the end-level portal, and indicates if they can enter
        if (inPortalRange){
           Font font = GameStateManager.centuryGothicBold.deriveFont(Font.BOLD, 12);
            g.setFont(font);
            g.setColor(Color.CYAN);
            g.drawString("Press ENTER to Finish", (int) (GamePanel.WIDTH / 2) - 55, (int) (GamePanel.HEIGHT / 2) - 40);
            g.drawString("Press ENTER to Finish", (int) (GamePanel.WIDTH / 2) - 55, (int) (GamePanel.HEIGHT / 2) - 41);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Press ENTER to Finish", (int) (GamePanel.WIDTH / 2) - 54 , (int) (GamePanel.HEIGHT / 2) - 40);
        }
        
        //draw snow
         snow.draw(g);
        
        //draw HUD
        hud.draw(g);
        //draw timer
        time.draw(g);
    }
    
    
    /**
     * Detects key input from the user
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
               player.respawn(100,753);
           } else if (inPortalRange){
               //stops music
               AudioPlayer.stop("bgMusic3");
               AudioPlayer.reset("bgMusic3");
              score += time.finish(); //finishes timer and adds time bonus
              portal.endLevel(gsm); 
              GameStateManager.tempScore = score; //sets the score
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
