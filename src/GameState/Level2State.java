/*Austin Van Braackel
 *1/2019
 *A Game State that represents playing level 2 of Transient.
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
public class Level2State extends GameState {

    private TileMap tileMap;
    private TileMap collisions;
    private TileMap secret;
    
    private Time time;
    public int score;
    
    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private Background bg5;

    private Player player;
private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<Gem> gems;
    private Portal portal;
    private ArrayList<Healthpack> healthpacks;
    private boolean inPortalRange = false;
    
    private boolean flashstepEnabled = true;
    private long flashstepTimer;
    private boolean checkForAttacks = true;
    private long attackTimer;

    private HUD hud; 
    

    public Level2State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
         //initialize tile map
        tileMap = new TileMap(16, true);
        tileMap.loadMap("/Maps/Level2._Display.csv");
        tileMap.loadTiles("/Tilesets/environment-tiles.png");
        tileMap.setPosition(0,0);
        tileMap.setTween(1);
        collisions = new TileMap(16, false);
        collisions.loadMap("/Maps/Level2._Collision.csv");
        collisions.loadTiles("/Tilesets/16x16Collision.png");
        collisions.setPosition(0,0);
        collisions.setTween(1);
        secret = new TileMap(16, true);
        secret.loadMap("/Maps/Level2._Secret.csv");
         secret.loadTiles("/Tilesets/environment-tiles.png");
        secret.setPosition(0,0);
        secret.setTween(1);

        bg1 = new Background("/Backgrounds/plx-1.png", 1);
        bg1.setPosition(0,-500);
        bg2 = new Background("/Backgrounds/plx-2.png", 0.1);
        bg2.setPosition(0,-500);
        bg3 = new Background("/Backgrounds/plx-3.png", 0.3);
        bg3.setPosition(0,-500);
        bg4 = new Background("/Backgrounds/plx-4.png", 0.5);
        bg4.setPosition(70,-500);
        bg5 = new Background("/Backgrounds/plx-5.png", 0.75);
        bg5.setPosition(0,-500);
        
         //loops 50 times, decreased by 15 decibels
        AudioPlayer.loopFor("bgMusic2", -15f, 50);
        AudioPlayer.adjustVolume("potionDrink", -10.0f);
        AudioPlayer.adjustVolume("Gem Sound", -7f);

        score = 0;
        
        player = new Player(collisions);
        player.setPosition(50, 430);

        time = new Time(60 * 5, gsm); //5 minutes
        
       hud = new HUD(player);
 
        populateEnemies(); //adds enemies to the level
        loadGems(); //adds gems to the level
        
        portal = new Portal(collisions);
        portal.setPosition(1040, 177);

        healthpacks = new ArrayList<Healthpack>();
       explosions = new ArrayList<Explosion>(); 
       
       Point[] points = new Point[]{
            new Point(1200, 433),
            new Point(1361, 113),
        };
        Healthpack hp;
        for (int i = 0; i < points.length; i++) {
            hp = new Healthpack(collisions);
            hp.setPosition(points[i].x, points[i].y);
            healthpacks.add(hp);
        }
    }

    private void populateEnemies() {
         enemies = new ArrayList<Enemy>();
        Slime s;
        Point[] points = new Point[]{
            new Point(316, 400),
            new Point(505, 430),
            new Point(590, 430),
            new Point(725, 430),
            new Point(499, 300),
            new Point(655, 300),
            new Point(515, 110),
            new Point(650, 140),};
//loops through the points and adds enemy at specified position
        for (int i = 0; i < points.length; i++) {
            s = new Slime(collisions);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }

        points = new Point[]{
            new Point(765, 50),
            new Point(910, 240),
            new Point(976, 305),
            new Point(1041, 430),
            new Point(1105, 370),
            new Point(685, 369),
            new Point(1642,145),
            
        };

        Spider spider;
        for (int i = 0; i < points.length; i++) {
            spider = new Spider(collisions);
            spider.setPosition(points[i].x, points[i].y);
            enemies.add(spider);
        }

        points = new Point[]{
            new Point(821, 433),
            new Point(437, 81),
            new Point(917, 113),
            new Point(1653,273),
            new Point(1463,113),
        };
           
        boolean[] directions = new boolean[]{false, false, false,false,false};
        Turret t;

        for (int i = 0; i < points.length; i++) {
            t = new Turret(collisions, directions[i]);
            t.setPosition(points[i].x, points[i].y);
            enemies.add(t);
        }

    }

    private void loadGems() {
        gems = new ArrayList<Gem>();
        Gem g;
        Point[] points = new Point[]{
            new Point(146,400),
            new Point(272,369),
            new Point(367,369),
            new Point(49,113),
            new Point(912,177),
            new Point(847,241),
            new Point(975,433),
            new Point(1038,433),
            new Point(1102,433),
            new Point(460,49),
            new Point(591,81),
            new Point(820,113),
            new Point(1648,81),
            new Point(689,273),
        };

        for (int i = 0; i < points.length; i++) {
            g = new Gem(collisions, points[i].x, points[i].y);
            gems.add(g);
        }

        for (int i = 150; i < 300; i += 10) {
            g = new Gem(collisions, i, 140);
            gems.add(g);
        }
        
        for (int i = 1320; i < 1420; i+=10) {
            g = new Gem(collisions, i, 337);
            gems.add(g);
        }
        
        for (int i = 1580; i < 1650; i+=10) {
            g = new Gem(collisions, i, 433);
            gems.add(g);
        }
        
        for (int i = 1140; i < 1270; i+=10) {
            g = new Gem(collisions, i, 113);
            gems.add(g);
        }
        
        for (int i = 975; i < 1110; i+=10) {
            g = new Gem(collisions, i, 241);
            gems.add(g);
        }
    }
    
    public void update() {

        handleInput();
        
        //update player
        player.update();
        
        //update tilemap
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        collisions.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
        secret.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());

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
        bg1.setPosition(tileMap.getx(), 0);
        bg2.setPosition(tileMap.getx(), 0);
        bg3.setPosition(tileMap.getx(), 0);
        bg4.setPosition(tileMap.getx(), 0);
        bg5.setPosition(tileMap.getx(), 0);

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
        
        //update healthpacks
        for (int i = 0; i < healthpacks.size(); i++) {
            healthpacks.get(i).update();
            if (healthpacks.get(i).intersects(player) && player.getHealth() < (int) player.getMaxHealth()) {
                AudioPlayer.play("potionDrink");
                player.setHealth((int) player.getHealth() + 10);
                healthpacks.remove(i);
            }
        }
        
        //update gems
        for (int i = 0; i < gems.size(); i++) {
            gems.get(i).update(); //update animation
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
        g.setColor(new Color(43,87,84));
        g.fillRect(-GamePanel.WIDTH / 2, -GamePanel.HEIGHT / 2, GamePanel.WIDTH * 2, GamePanel.HEIGHT * 2);
        bg1.draw(g);
        bg2.draw(g);
        bg3.draw(g);
        bg4.draw(g);
        bg5.draw(g);
        //draw tilemap
        tileMap.draw(g);
        //draw portal for level completion
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
        //draw healthpacks
        for (int i = 0; i < healthpacks.size(); i++) {
            healthpacks.get(i).draw(g);
        }
        //draw player
        player.draw(g);
        //draw indication that the player is in range of the portal and can finish the level
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
    
    
    
    public void handleInput() {
        player.setLeft(Keys.keyState[Keys.LEFT]);
        player.setRight(Keys.keyState[Keys.RIGHT]);
        //player.setUp(Keys.keyState[Keys.UP]);
        player.setDown(Keys.keyState[Keys.DOWN]);
        player.setJumping(Keys.keyState[Keys.SPACE] || Keys.keyState[Keys.UP]); //jump is also UP arrow key
        player.setGliding(Keys.keyState[Keys.CONTROL]);
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
       if (Keys.isPressed(Keys.ENTER)){
           if (player.isDead()){
               //respawn
               player.respawn(50, 430);
               //reduce score because player died (-100)
               score -= 100;
           } else if (inPortalRange){
               //stops music
               AudioPlayer.stop("bgMusic2");
               AudioPlayer.reset("bgMusic2");
               score += time.finish(); //finishes timer and adds time bonus
              portal.endLevel(gsm);
              GameStateManager.tempScore = score;
           }
       }
       
        if(!player.getIsAttacking()){ //determines the attack that the user will use
            if(Keys.isPressed(Keys.W)) player.setRangedAttackingS(); //strong shot
            if(Keys.isPressed(Keys.Q)) player.setRangedAttackingQ(); //quick shot
            if(Keys.isPressed(Keys.A)) player.setAttackingL(); //light melee
            if(Keys.isPressed(Keys.S)) player.setAttackingM(); //medium melee
            if(Keys.isPressed(Keys.D)) player.setAttackingH(); //heavy melee
        } 
            
        //ADD PAUSE/SOMETHING player.Pause(Keys.isPressed(Keys.ESCAPE));
    }
}
