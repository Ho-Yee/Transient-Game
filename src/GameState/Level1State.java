/*Austin Van Braeckel
12/14/2018
This class represents level one (underground stage), having the handling for player controls,
creating entities, loading the background, map, and tileset.  Loads all elements
necessary for level one specifically.
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
public class Level1State extends GameState {

    //attributes
    private TileMap tileMap;
    private TileMap collisions;
    private TileMap secret;
    
    private Time time;
    public int score;
    
    private Background backWalls;
    private Background background;

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<Gem> gems;
    private ArrayList<Healthpack> healthpacks;
    private Portal portal;
    private boolean inPortalRange = false;
    
    private boolean flashstepEnabled = true;
    private long flashstepTimer;
    private boolean checkForAttacks = true;
    private long attackTimer;

    private HUD hud; 
    

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    /**
     * Initializes the level, setting any variables that need to be given defaults,
     * setting-up the tilemap and displays, and handling the volume for sounds
     */
    public void init() {
        
         //initialize tile map(s)
        tileMap = new TileMap(32, true); //used solely for display (level design)
        tileMap.loadMap("/Maps/Level1FINAL_Display.csv");
        tileMap.loadTiles("/Tilesets/underground.png");
        tileMap.setPosition(0,0);
        tileMap.setTween(1);
        collisions = new TileMap(32, false); //determines collision with the map
        collisions.loadMap("/Maps/Level1FINAL_Collision.csv");
        collisions.loadTiles("/Tilesets/32x32Collision.png");
        collisions.setPosition(0,0);
        collisions.setTween(1);
        secret = new TileMap(32, true); //Drawn on-top of player for secret areas
        secret.loadMap("/Maps/Level1FINAL_Secret.csv");
         secret.loadTiles("/Tilesets/underground.png");
        secret.setPosition(0,0);
        secret.setTween(1);

        //Set-up background(s)
        backWalls = new Background("/Backgrounds/Level1BackWalls.png", 1);
        backWalls.setPosition(0, 0);
        background = new Background("/Backgrounds/Level1Bg2.png", 0.5);
        background.setPosition(0,0);
        
        //loops 50 times, decreased by 15 decibels
        AudioPlayer.loopFor("bgMusic1", -15f, 50);
        AudioPlayer.adjustVolume("potionDrink", -10.0f);
        AudioPlayer.adjustVolume("Gem Sound", -7f);

        //set default score to 0
        score = 0;
        
        //creates new player at the proper starting position
        player = new Player(collisions);
        player.setPosition(114, 401);
        //player.setPosition(5813, 337); near portal

        //creates a timer for the level
        time = new Time(60 * 5, gsm); //5 minutes
        
        //creates a HUD for displaying the player's stamina/energy and health 
       hud = new HUD(player);
 
        populateEnemies(); //adds enemies to the level
        loadGems(); //adds gems to the level
        
        //Creates portal for finishing the level and places it at the proper position
        portal = new Portal(collisions);
        portal.setPosition(5862, 337);

        //Initialize an ArrayList to contain all explosions
       explosions = new ArrayList<Explosion>(); 
       
       //Initiailze ArrayList for containing all healthpacks in the level
        healthpacks = new ArrayList<Healthpack>();
        
        //populate healthpacks in the level
        Point[] points = new Point[]{
            new Point(2251,205)
        };
        //loops through all points and places a healthpack at the position
        for (int i = 0; i < points.length; i++) {
            Healthpack health = new Healthpack(collisions);
            health.setPosition(points[i].x, points[i].y);
            healthpacks.add(health);
        }
    }

    /**
     * Loads-in all enemies for the level
     */
    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        //slimes
        Slime s;
        Point[] points = new Point[]{
            new Point(312,400),
            new Point(370,400),
            new Point(575,400),
            new Point(1762,430),
            new Point(2315,430),
            new Point(2743,205),
            
            new Point(3072,205),
            new Point(3274,205),
            new Point(4882,330),
            new Point(4885,330),
        };
//loops through the points and adds enemy at specified position
        for (int i = 0; i < points.length; i++) { 
            s = new Slime(collisions);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
        
        //spiders
         points = new Point[]{
          new Point(530,350),
          new Point(817,280),
          new Point(1267,210),
          new Point(1537,100),
          new Point(2000,80),
          new Point(2315,115),
          new Point(2451,370),
          new Point(2816,100),
          new Point(3479,100),
          new Point(5613,210),
          new Point(5235,175),
          new Point(4687,240),
          new Point(1596,50),
          new Point(3151,80),
          new Point(3790,50),
          new Point(3920,50),
        };
        
        Spider spider;
        for (int i = 0; i < points.length; i++) {
            spider = new Spider(collisions);
            spider.setPosition(points[i].x, points[i].y);
            enemies.add(spider);
        }
        //turrets
        Turret t;
        points = new Point[]{
            new Point(1397,209),
            new Point(2270,81),
            new Point(757,337),
            new Point(2773,177),
            new Point(4469,177),
            new Point(5013,337),
            new Point(5813,241),
        };
        
        //specifies the direction that the turrets are facing
        boolean directions[] = new boolean[]{false,false,false,false,false,false,false};
        for (int i = 0; i < points.length; i++) {
            t = new Turret(collisions,directions[i]);
            t.setPosition(points[i].x, points[i].y);
            enemies.add(t);
        }
    }

    /**
     * Loads all gems into the level at the proper positions
     */
     private void loadGems(){
         
         //instantiates the ArrayList that contains all gems
        gems = new ArrayList<Gem>();
        
        //create temporary Gem variable to add all gems
        Gem g;
        
        //Specify certain points at which gems are to be located
        Point[] points = new Point[]{
            new Point(2920,175),
            new Point(2545,337),
            new Point(395,335),
            new Point(3415,145),
            new Point(3470,210),
            new Point(4685,305),
            new Point(5040,305),
            new Point(1265,110),
            new Point(1165,177),
        };
        
        //Loops through all points and adds a gem at each position
         for (int i = 0; i < points.length; i++) { 
            g = new Gem(collisions, points[i].x, points[i].y);
            gems.add(g);
        }
         
         //Adds gems in a line within the range specified
         
         for (int i = 120; i < 180; i += 10){
                g = new Gem(collisions, i, 401);
                gems.add(g);
         }
         for (int i = 2730; i < 2800; i+= 10) {
             g = new Gem(collisions,i,401);
             gems.add(g);
         }
         for (int i = 3020; i < 3190; i+=10) {
             g = new Gem(collisions,i,401);
             gems.add(g);
         }
         for (int i = 2800; i < 2830; i+=10) {
             g = new Gem(collisions,i,140);
             gems.add(g);
         }
         for (int i = 1420; i < 1530; i+=10) {
             g = new Gem(collisions,i,430);
             gems.add(g);
         }
         for (int i = 3790; i < 3920; i+=10) {
             g = new Gem(collisions,i,113);
             gems.add(g);
         }
         for (int i = 5645; i < 5745; i+=10) {
             g = new Gem(collisions,i,305);
             gems.add(g);
         }
         for (int i = 790; i < 940; i += 10){
             g = new Gem(collisions,i,81);
             gems.add(g);
         }
         for (int i = 1960; i < 2055; i += 10){
             g = new Gem(collisions,i,273);
             gems.add(g);
         }
         for (int i = 5330; i < 5425; i += 10){
             g = new Gem(collisions,i,401);
             gems.add(g);
         }
    }
    
     /**
      * Updates all aspects of the level
      */
    public void update() {

        //detects and handles input from the user
        handleInput();
        
        //update player
        player.update();
        
        //update tilemap(s)
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        collisions.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        secret.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

        //update portal
        portal.update();

        //Makes sure the player's attack only hits the enemy once through the use of a timer
        if (checkForAttacks){
            
        //attack enemies
       score += player.checkAttack(enemies); //adds score based on the player's success in attacking enemies
       // (or subtracts score if the player takes damage)
       checkForAttacks = false; //resets the boolean for the timer
       attackTimer = System.nanoTime(); //sets a new start time for the timer
        } else {
            long elapsed = (System.nanoTime() - attackTimer) / 1000000;
            if(elapsed > 100){ //tracks elapsed time and only checks for attacks after every 100 milliseconds
                checkForAttacks = true;
            }
        }
        
        //set background
        backWalls.setPosition(tileMap.getx(), tileMap.gety());
        background.setPosition(tileMap.getx(), tileMap.gety());

        //update all enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update(player);
            if (e.isDead()) { //Checks if the enemy is dead
                //removes from array list and reduces index by one to ensure no enemies are skipped in the update process
                enemies.remove(i);
                i--; //compensate removal
                //Add to score (30 points)
                score += 30;
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
            if (gems.get(i).intersects(player)) { //player collects coin and it is removed from the game
                gems.remove(i); //removes the explosion from the array list
                //plays sound IMPLEMENT
                AudioPlayer.play("Gem Sound");
                //Adds to the player's score (+15)
                score += 15;
                i--; //reduces index by one to compensate removal
            }
        }
        
        //update healthpacks
         for (int i = 0; i < healthpacks.size(); i++) {
             healthpacks.get(i).update(); //update animation and collision detection with player
             
             //Player drinks potion/healthpack if they are not at full health, and they intersect/collide with it
            if (healthpacks.get(i).intersects(player) && player.getHealth() < player.getMaxHealth()) {
                AudioPlayer.play("potionDrink"); //play sound effect
                player.setHealth((int) player.getHealth() + 10); //add 10 health points
                healthpacks.remove(i); //remove the healthpack from the level (one-time use)
            }
        }
        
        //update player's proximity to the levelEnd portal
        if (portal.intersects(player)){
            inPortalRange = true;
        } else {
            inPortalRange = false;
        }
        
        //makes sure score is not below zero
        if (score < 0){
            score = 0;
        }
        
        //update timer
        time.update(score);
    }

    /**
     * Draws all necessary components of the level to the screen with the given Graphics2D object
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g) {
        //draw background
        background.draw(g);
        backWalls.draw(g);
        //draw tilemap
        tileMap.draw(g);
        //draw portal for level completion
        portal.draw(g);
        //draw healthpacks
        for (int i = 0; i < healthpacks.size(); i++) {
            healthpacks.get(i).draw(g);
        }
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
        //draws indicator that the player can enter the portal (if they are in range)
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
        
        //draw secret passage over player
        secret.draw(g);
        
        //draw HUD and timer
        hud.draw(g);
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
        //Flashstep
       if (Keys.isPressed(Keys.SHIFT)){
           if (flashstepEnabled){
               player.setFlashstepping();
               flashstepTimer = System.nanoTime();
               flashstepEnabled = false; //disables it
           } else { //cool-down timer
               long elapsed = (System.nanoTime() - flashstepTimer) / 1000000;
               if (elapsed > 1000){ //1 second cool-down
                   flashstepEnabled = true; //enables it again
               }
           }
       }
       //Enter the portal if player is in range, or respawn if player is dead
       if (Keys.isPressed(Keys.ENTER)){
           if (player.isDead()){
               //respawn
               player.respawn(114, 401);
               //reduce score because player died (-100)
               score -= 100;
           } else if (inPortalRange){
               //stops music
               AudioPlayer.stop("bgMusic1");
               AudioPlayer.reset("bgMusic1");
               score += time.finish(); //finishes timer and adds time bonus
              portal.endLevel(gsm); //level 1
              GameStateManager.tempScore = score;
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
