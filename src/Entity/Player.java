/**
 * Austin Van Braeckel
 * 12/2018
 * This class represents a player object, which is controlled by the user to play
 * the levels, eliminating enemies, collecting pick-ups, and doing it in the fastest time possible.
 * These factors contribute to the overall score.
 * It extends the MapObject class, but also adds more attributes such as health, ammo, and more.
 */
package Entity;

import TileMap.*;
import Audio.AudioPlayer;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 
 * @author Austin Van Braeckel
 */
public class Player extends MapObject {

    //player attributes
    private double health;
    private double maxHealth;
    private int ammo;
    private int maxAmmo;
    private boolean dead;
    private boolean hasStrongShot;
    private boolean hasQuickShot;
    private long shotDelay;
    private boolean flinching;
    private long flinchTime;
    private long meleeTimer;
    private boolean finishedAttacking;
    private boolean playSound;

    //ranged attack
    private boolean sRangedAttacking;
    private boolean qRangedAttacking;
    private int ammoCost;
    private int rangedDamage;
    private ArrayList<Arrow> arrows;

    //melee/close-ranged attack
    private boolean attackingL;
    private boolean attackingM;
    private boolean attackingH;
    private int meleeDamageL;
    private int meleeRangeL;
    private int meleeDamageM;
    private int meleeRangeM;
    private int meleeDamageH;
    private int meleeRangeH;
    private boolean hasAttacked;

    //other actions
    private boolean gliding;
    private boolean flashstepping;
    private boolean isDying = false;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
        4, //idle
        4,//crouching
        6, //running 
        8, //jumping
        2, //falling
        2, //gliding
        9,//hanging (climbing from ledge)
        4,//attackIdle
        7,//mMelee
        6,//hMelee
        6, //lMelee
        10,//dying
        20,//flashstep
        9, //ranged
        6//rangedQ
    };

    // animation actions
    private static final int IDLE = 0;
    private static final int CROUCHING = 1; //NOT USING
    private static final int RUNNING = 2;
    private static final int JUMPING = 3;
    private static final int FALLING = 4;
    private static final int GLIDING = 5;
    private static final int HANGING = 6; //NOT USING
    private static final int ATTACKIDLE = 7; //NOT USING
    private static final int MMELEE = 8;
    private static final int HMELEE = 9;
    private static final int LMELEE = 10;
    private static final int DYING = 11;
    private static final int FLASHSTEP = 12;
    private static final int RANGEDSTRONG = 13;
    private static final int RANGEDQ = 14;

    //Constructor
    public Player(TileMap tm) {
        super(tm);

        //set default values
        width = 50;
        height = 37;
        cwidth = 23;
        cheight = 30;

        moveSpeed = 0.4;
        maxSpeed = 2.3;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;
        hasStrongShot = false;
        hasQuickShot = false;
        hasAttacked = false;

        maxHealth = 30.0;
        health = maxHealth;
        maxAmmo = 5000;
        ammo = maxAmmo;

        ammoCost = 1000;
        rangedDamage = 8;
        arrows = new ArrayList<Arrow>();

        finishedAttacking = true;
        meleeDamageL = 3;
        meleeRangeL = 40;
        meleeDamageM = 5;
        meleeRangeM = 40;
        meleeDamageH = 15;
        meleeRangeH = 50;

        //load sprites and create animation
        loadSprites();
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        //set-up sound effects
        playSound = true;
        AudioPlayer.adjustVolume("strong", -7f);
        AudioPlayer.adjustVolume("jump", -10f);
        AudioPlayer.adjustVolume("hurt", -13f);
        AudioPlayer.adjustVolume("light", 5f);
        AudioPlayer.adjustVolume("medium", -5f);
        AudioPlayer.adjustVolume("heavy", -10f);
    }

    /**
     * Manually sets the stop/move speeds of the player to the given double
     * values for variation in level design
     *
     * @param stop double of the stop speed
     * @param move double of the move speed
     */
    public void setStopAndMoveSpeed(double stop, double move) {
        stopSpeed = stop;
        moveSpeed = move;
    }

    /**
     * Manually sets the health of the player to the given double value
     *
     * @param h double of the health
     */
    public void setHealth(double h) {
        health = h;
    }

    /**
     * Loads all necessary sprites for the player
     */
    public void loadSprites() {
        //load sprites
        try {

            //reads images and stares them in BufferedImage objects
            BufferedImage mainSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/AdventurerAVBFinal.png"));
            BufferedImage bowSpritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/BowFinal.png"));

            //set-up array list to contain all sprites
            sprites = new ArrayList<BufferedImage[]>();

            //MAIN
            //Fill array list with BufferedImage arrays of images, for animation
            //13 rows
            for (int i = 0; i < 13; i++) {
                //20 columns - but only for the needed number of frames for each animation
                //create a 2D array to hold all animation images
                BufferedImage[] playerSprites = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    playerSprites[j] = mainSpritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(playerSprites); //add to array list of player animation
            }
            //BOW //Same as above, but with the seperate bow spritesheet
            for (int i = 0; i < 2; i++) {
                BufferedImage[] bowSprites = new BufferedImage[numFrames[i + RANGEDSTRONG]];
                for (int j = 0; j < numFrames[i + RANGEDSTRONG]; j++) {
                    bowSprites[j] = bowSpritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(bowSprites);
            }

        } catch (Exception e) {
            e.printStackTrace(); //print error message
        }
    }

    /**
     * Retrieve player's current health
     *
     * @return health as a double
     */
    public double getHealth() {
        return health;
    }

    /**
     * Retrieves player's max health
     *
     * @return max health as a double
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Retrieves player's current ammo count
     *
     * @return ammo as an integer
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Sets the player's ammo the given integer
     *
     * @param num integer of ammo amount
     */
    public void setAmmo(int num) {
        ammo = num;
    }

    /**
     * Retrieves the max ammo of the player
     *
     * @return max ammo as an integer
     */
    public int getMaxAmmo() {
        return maxAmmo;
    }

    /**
     * Sets the player to be shooting an arrow (strong)
     */
    public void setRangedAttackingS() {
        sRangedAttacking = true;
    }

    /**
     * Sets the player to be shooting an arrow (quick)
     */
    public void setRangedAttackingQ() {
        qRangedAttacking = true;
    }

    /**
     * Sets the player to be melee attacking (light)
     */
    public void setAttackingL() {
        attackingL = true;
    }

    /**
     * Sets the player to be melee attacking (medium)
     */
    public void setAttackingM() {
        attackingM = true;
    }

    /**
     * Sets the player to be melee attacking (heavy)
     */
    public void setAttackingH() {
        attackingH = true;
    }

    /**
     * Retrieves whether the player is attacking or not
     *
     * @return boolean representing that the player is attacking when true, and
     * not attacking when false
     */
    public boolean getIsAttacking() {
        return attackingL ^ attackingM ^ attackingH ^ qRangedAttacking ^ sRangedAttacking;
    }

    /**
     * Sets the player to be gliding or not gliding through the given boolean
     *
     * @param b boolean that specifies the status of the player gliding, with
     * true being gliding, and false being not gliding
     */
    public void setGliding(boolean b) {
        gliding = b;
    }

    /**
     * Sets the player to be flashstepping
     */
    public void setFlashstepping() {
        flashstepping = true;
    }

    /**
     * Retrieves the status of the player in regards to their death
     *
     * @return boolean that determines if they are dead, with true being dead,
     * and false being still alive
     */
    public boolean isDead() {
        return dead && !isDying;
    }

    /**
     * Respawns the player at the specified x and y position in the level
     *
     * @param x double of an x-value
     * @param y double of a y-value
     */
    public void respawn(double x, double y) {
        //resets health and ammo
        health = maxHealth;
        ammo = maxAmmo;
        //resets animation to idle when the player respawns
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(500);
        //stops their movement and specifies that they are now alive
        dead = false;
        dx = 0;
        dy = 0;
        //sets their position
        setPosition(x, y);
    }

    /**
     * Checks all attacks taking place
     *
     * @param enemies Arraylist of enemies that is to be checked for collision
     * @return integer of the score lost or gained
     */
    public int checkAttack(ArrayList<Enemy> enemies) {

        //temporary variable to add or subtract from player score
        int score = 0;

        //loop through enemies
        for (int i = 0; i < enemies.size(); i++) { //linear search through enemies to check if they are attacked

            Enemy e = enemies.get(i);

            //check light attack
            if (attackingL) {
                if (facingRight) {
                    if (e.getx() > x //Makes sure enemy is in front of the player when facing right
                            && e.getx() < x + meleeRangeL
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(meleeDamageL);
                        //Adds score (2)
                        score += 2;
                        //Knockback
                        e.dx = 2;
                        e.dy = -2.0;
                    }
                } else {
                    if (e.getx() < x //Makes sure enemy is in front of the player when facing left
                            && e.getx() > x - meleeRangeL
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(meleeDamageL);
                        //Adds score (2)
                        score += 2;
                        //Knockback
                        e.dx = -2;
                        e.dy = -2.0;
                    }
                }

            } else if (attackingM) { //meduim melee
                if (!hasAttacked && !finishedAttacking) { //sets timer for hit detection
                    meleeTimer = System.nanoTime();
                    hasAttacked = true;
                }

                //timer purposed for only damaging enemies at the point in the animation where the sword is swung
                if (hasAttacked && !finishedAttacking) {
                    long elapsed = (System.nanoTime() - meleeTimer) / 1000000;
                    if (elapsed > 150) {
                        if (elapsed > 640) {
                            hasAttacked = false;
                            finishedAttacking = true;
                        }
                        if (facingRight) {
                            if (e.getx() > x //Makes sure enemy is in front of the player when facing right
                                    && e.getx() < x + meleeRangeM
                                    && e.gety() > y - height / 1.5
                                    && e.gety() < y + height / 2) {
                                e.hit(meleeDamageM);
                                //Adds score (5)
                                score += 5;
                                //Knockback
                                e.dx = 5;
                                e.dy = -3.0;
                            }
                        }

                    } else {
                        if (e.getx() < x //Makes sure enemy is in front of the player when facing left
                                && e.getx() > x - meleeRangeM
                                && e.gety() > y - height / 1.5
                                && e.gety() < y + height / 2) {
                            e.hit(meleeDamageM);
                            //Adds score (5)
                            score += 5;
                            //Knockback
                            e.dx = -5;
                            e.dy = -3.0;
                        }
                    }

                }

            } else if (attackingH) { //heavy melee
                if (!hasAttacked && !finishedAttacking) { //sets timer for hit detection
                    meleeTimer = System.nanoTime();
                    hasAttacked = true;
                }

                //timer purposed for only damaging enemies at the point in the animation where the sword is swung
                if (hasAttacked && !finishedAttacking) {
                    long elapsed = (System.nanoTime() - meleeTimer) / 1000000;
                    //plays sound at the beginning only
                    if (elapsed > 500 && playSound) {
                        //play sound
                        AudioPlayer.play("heavy");
                        playSound = false;
                    }
                    if (elapsed > 650) {

                        if (elapsed > 1000) {
                            hasAttacked = false;
                            finishedAttacking = true;
                            playSound = true;
                        }
                        if (facingRight) {
                            if (e.getx() > x //Makes sure enemy is in front of the player when facing right
                                    && e.getx() < x + meleeRangeH
                                    && e.gety() > y - height / 1.7
                                    && e.gety() < y + height / 2) {
                                e.hit(meleeDamageH);
                                //Adds score (7)
                                score += 7;
                                //Knockback
                                e.dx = 8;
                                e.dy = -4.0;
                            }
                        } else {
                            if (e.getx() < x //Makes sure enemy is in front of the player when facing left
                                    && e.getx() > x - meleeRangeH
                                    && e.gety() > y - height / 1.7
                                    && e.gety() < y + height / 2) {
                                e.hit(meleeDamageH);
                                //Adds score (7)
                                score += 7;
                                //Knockback
                                e.dx = -8;
                                e.dy = -4.0;
                            }
                        }

                    }

                }
            }

            //Projectiles
            for (int j = 0; j < arrows.size(); j++) { //loop through arrows
                if (arrows.get(j).intersects(e)) { //if it hits the enemy
                    e.hit(rangedDamage); //causes damage to the enemy
                    //Adds score (10)
                    score += 10;
                    //Knockback
                    if (arrows.get(j).facingRight) {
                        e.dx = 1;
                        e.dy = -0.5;
                    } else {
                        e.dx = -1;
                        e.dy = -0.5;
                    }
                    arrows.get(j).setHit(); //specifies that arrow has hit something
                    break; //end whole loop if enemy has been hit
                }
            }

            //check enemy collision with player
            if (intersects(e)) {
                //subtracts 10 score points per hit point lost
                score -= hit(e.getDamage()); //player takes damage from the enemy

                //only gets knocked back when colliding directly with player if they are a slime
                if (e.isSlime()) {
                    //Knockback
                    if (e.toRightOfPlayer) {
                        e.dx = 2;
                        e.dy = -1;
                    } else {
                        e.dx = -2;
                        e.dy = -1;
                    }
                } //(Don't want spiders or turrets to be knocked-back the same way)
            }
        }//End enemy search loop

        return score; //return the score lost or gained through the attack detections
    }

    /**
     * Determines if the player should take damage, and subtracts necessary hit
     * points. Also returns the number of score points that should be subtracted
     *
     * @param damage to the player in hit points
     * @return number of score points that are lost
     */
    public int hit(int damage) {
        if (flinching || flashstepping || dead) { //can't be damaged while flinching or flashstepping
            return 0;
        }

        //play sound
        AudioPlayer.play("hurt");

        //subtract damage from player's current health
        health -= damage;
        if (health <= 0) { //keeps health at, or above, 0
            health = 0;
            dead = true;
        }
        //player is staggered
        dx = 0;

        //sets the flinch timer
        flinching = true;
        flinchTime = System.nanoTime();

        return damage * 2; //lose 2 score points per hit point lost
    }

    /**
     * Determines the next position of the player based on their current
     * movement and actions
     */
    private void getNextPosition() {

        //sets flashstepping speeds
        if (flashstepping) {
            maxSpeed = 6.0;
            moveSpeed = 0.8;
            fallSpeed = 0.05;
            maxFallSpeed = 2.0;
            jumpStart = 0;
        } else if (gliding) { //sets gliding speed
            maxSpeed = 3.0;
        } else { //sets default movement speed
            maxSpeed = 2.3;
            moveSpeed = 0.4;
            fallSpeed = 0.15;
            maxFallSpeed = 4.0;
            jumpStart = -4.8;
        }

        //movement
        if (left && !dead) {
            dx -= moveSpeed; //speeds up
            if (dx < -maxSpeed) { //limits the max speed of the player
                dx = -maxSpeed;
            }

        } else if (right && !dead) {
            dx += moveSpeed; //speeds up
            if (dx > maxSpeed) { //limits the max speed of the player
                dx = maxSpeed;
            }

        } else { //stopped/not moving
            if (flashstepping) {
                if (facingRight) {
                    dx = 4.0;
                } else {
                    dx = -4.0;
                }
            } else {
                if (dx > 0) {
                    dx -= stopSpeed; //slows down
                    if (dx < 0) { //complete stop
                        dx = 0;
                    }
                } else if (dx < 0) {
                    dx += stopSpeed; //slows down
                    if (dx > 0) { //complete stop
                        dx = 0;
                    }
                }
            }
        }

        //cannot attack while moving (but still in the air)
        if ((currentAction == MMELEE
                || currentAction == HMELEE
                || currentAction == RANGEDSTRONG
                || currentAction == DYING) && !(jumping || falling)) {
            dx = 0;
        }

        //jumping
        if (jumping && !falling) {
            if (!flashstepping && !dead) {
                AudioPlayer.play("jump");
                dy = jumpStart;
                falling = true;
            }

        }

        //falling
        if (falling) {
            if (dy > 0 && gliding) {
                dy += fallSpeed * 0.1; //gliding causes to player to fall 90% slower
            } else { //not gliding
                dy += fallSpeed;
            }

            if (dy > 0) {
                jumping = false;
            }

            if (dy < 0 && !jumping) {
                dy += stopJumpSpeed; //Allows a shorter jump when the jump key is released
            }

            if (dy > maxFallSpeed) {
                dy = maxFallSpeed; //limits fall speed
            }

        }

    }

    /**
     * Updates all necessary aspects of the player
     */
    public void update() {

        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if (y > tileMap.getHeight()) { //if the player falls off of the map
            //player dies instantly
            dead = true;
            isDying = false;
            health = maxHealth;
            currentAction = DYING;
            animation.setFrames(sprites.get(DYING));
            animation.setDelay(0);
        }

        //check action has stopped, and resets booleans
        if (currentAction == LMELEE) {
            if (animation.hasPlayedOnce()) {
                attackingL = false;
            }
        } else if (currentAction == MMELEE) {
            if (animation.hasPlayedOnce()) {
                attackingM = false;
            }
        } else if (currentAction == HMELEE) {
            if (animation.hasPlayedOnce()) {
                attackingH = false;
                hasAttacked = false;
            }
        } else if (currentAction == RANGEDQ) {
            if (animation.hasPlayedOnce()) {
                qRangedAttacking = false;
            }
        } else if (currentAction == RANGEDSTRONG) {
            if (animation.hasPlayedOnce()) {
                sRangedAttacking = false;
            }
        } else if (currentAction == FLASHSTEP) {
            if (animation.hasPlayedOnce()) {
                flashstepping = false;
            }
        } else if (currentAction == DYING) {
            if (animation.hasPlayedOnce()) {
                isDying = false;
                dead = true;
                health = maxHealth;
            }
        }

        //shoot an arrow
        ammo += 2; //constantly regenerates the ammo
        if (!dead) {
            health += 0.008; //constantly regenerates health
        }
        if (ammo > maxAmmo) { //limits it to the specified max ammo
            ammo = maxAmmo;
        }
        if (health > maxHealth) { //limits it to maximum health
            health = maxHealth;
        }
        if (sRangedAttacking && currentAction != RANGEDSTRONG && !hasStrongShot) {
            //Creates an arrow entity, and specifies direction based on where player is facing
            //Only if sufficient ammo is present
            if (ammo > ammoCost) {
                ammo -= ammoCost; //subtracts the ammo used to  attack
                hasStrongShot = true;
                shotDelay = System.nanoTime();
            }
        }
        if (hasStrongShot) {
            long elapsed = (System.nanoTime() - shotDelay) / 1000000;
            if (elapsed > 93 * numFrames[RANGEDSTRONG]) {
                Arrow ar = new Arrow(tileMap, facingRight);
                ar.setPosition(x, y + 3); //sets it to the same position as player
                arrows.add(ar);
                hasStrongShot = false;
                rangedDamage = 8;
                //plays sound effect
                AudioPlayer.play("strong");
            }
        }
        if (qRangedAttacking && currentAction != RANGEDQ && !hasQuickShot) {
            //Creates an arrow entity, and specifies direction based on where player is facing
            //Only if sufficient ammo is present, and the player is in the air or flashstepping or gliding
            if (jumping || falling || flashstepping || gliding) {
                if (ammo > ammoCost / 3) {
                    ammo -= ammoCost / 3; //subtracts the ammo used to  attack
                    hasQuickShot = true;
                    rangedDamage = 3;
                    shotDelay = System.nanoTime();
                }
            } else { //if on the ground, do not play the animation
                qRangedAttacking = false;
            }
        }
        if (hasQuickShot) {
            long elapsed = (System.nanoTime() - shotDelay) / 1000000;
            if (elapsed > 50 * numFrames[RANGEDQ]) {
                rangedDamage = 5;
                Arrow ar = new Arrow(tileMap, facingRight);
                ar.setPosition(x, y + 3); //sets it to the same position as player
                arrows.add(ar);
                hasQuickShot = false;
                //plays sound effect
                AudioPlayer.play("strong"); //IMPLEMENT new sound?
            }
        }

        //update arrows
        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).update();
            if (arrows.get(i).shouldRemove()) { //removes from game if it hits
                //remove from array list and reduce the index to make sure all items in the array list are updated
                arrows.remove(i);
                i--;
            }
        }

        //check if done flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTime) / 1000000;
            if (elapsed > 1000) { //stops flinching after one second
                flinching = false;
            }
        }

        //check if player's health has reached 0
        if (dead && health == 0) {
            isDying = true;
            attackingL = false;
            attackingM = false;
            attackingH = false;
            qRangedAttacking = false;
            sRangedAttacking = false;
            flashstepping = false;
            dx = 0;
            dy = 0;
        }

        if (!isDying && dead && health > 0) { //they have finished dying
            animation.setFrame(9);
            return;
        }

        // set animation and movement speeds
        if (attackingL) { //light attack
            sRangedAttacking = false;
            qRangedAttacking = false;
            flashstepping = false;
            attackingM = false;
            attackingH = false;
            dx *= 0.97; //makes them move 3% slower
            if (currentAction != LMELEE) {
                //play sound
                AudioPlayer.play("light");
                currentAction = LMELEE;
                animation.setFrames(sprites.get(LMELEE));
                animation.setDelay(50);
            }
        } else if (attackingM) { //medium attack
            sRangedAttacking = false;
            qRangedAttacking = false;
            flashstepping = false;
            attackingL = false;
            attackingH = false;
            dx *= 0.65; //makes them move 35% slower
            if (currentAction != MMELEE) {
                //play sound
                AudioPlayer.play("medium");
                currentAction = MMELEE;
                animation.setFrames(sprites.get(MMELEE));
                animation.setDelay(100);
                finishedAttacking = false;
            }
        } else if (attackingH) { //heavy attack
            sRangedAttacking = false;
            qRangedAttacking = false;
            flashstepping = false;
            attackingM = false;
            attackingL = false;
            dx *= 0.5; //makes them move 50% slower
            if (currentAction != HMELEE) {
                currentAction = HMELEE;
                animation.setFrames(sprites.get(HMELEE));
                animation.setDelay(200);
                finishedAttacking = false;
            }
        } else if (sRangedAttacking) { //strong shot

            if (currentAction != RANGEDSTRONG) {
                currentAction = RANGEDSTRONG;
                animation.setFrames(sprites.get(RANGEDSTRONG));
                animation.setDelay(100);
            }

        } else if (qRangedAttacking) { //quick shot
            if (currentAction != RANGEDQ) {
                currentAction = RANGEDQ;
                animation.setFrames(sprites.get(RANGEDQ));
                animation.setDelay(50);
            }
        } else if (flashstepping) { //flashstep
            if (currentAction != FLASHSTEP) {
                currentAction = FLASHSTEP;
                animation.setFrames(sprites.get(FLASHSTEP));
                animation.setDelay(15);
            }

        } else if (isDying) { //dying
            if (currentAction != DYING) {
                currentAction = DYING;
                animation.setFrames(sprites.get(DYING));
                animation.setDelay(375);
            }
        } else if (dy > 0) { //if moving downwards, towards the ground/floor

            if (gliding) { //gliding
                if (currentAction != GLIDING) {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                }
            } else if (currentAction != FALLING) { //falling
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(50);
            }
        } else if (dy < 0) { //player is moving upwards
            if (currentAction != JUMPING) { //jumping
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(50);
            }
        } else if (left || right) { //running (left or right)
            if (currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(120);
            }
        } else { //otherwise, the player is idle
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(500);
            }
        } //End if

        //update the animation
        animation.update();

        //set direction
        if (currentAction != LMELEE
                && currentAction != RANGEDSTRONG
                && currentAction != DYING
                && currentAction != RANGEDQ
                && currentAction != MMELEE
                && currentAction != HMELEE) { //makes sure the player isn't turning while attacking
            if (right) {
                facingRight = true;
            }
            if (left) {
                facingRight = false;
            }
        }

        //Print position to console (for level design purposes)
        // System.out.println("X: " + x + ", Y: " + y + ", Xmap: " + xmap + ", Ymap: " + ymap);
    }

    /**
     * Draws the player, as well as the arrows
     *
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g) {

        //set map position
        setMapPosition();

        //draw arrows
        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).draw(g);
        }

        //draw player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTime) / 1000000;
            if (elapsed / 10 % 2 == 0) {
                return; //doesn't draw the player (flashing effect when hit) - every 100 milliseconds
            }
        }

        super.draw(g);

        //If the player is dead, prompt them to respawn by drawing indicator message
        if (dead && !isDying && health == maxHealth) {
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g.setFont(font);
            g.setColor(Color.DARK_GRAY);
            g.drawString("Press ENTER to Respawn", (int) (GamePanel.WIDTH / 2) - 115, (int) (GamePanel.HEIGHT / 2) + 10);
            g.drawString("Press ENTER to Respawn", (int) (GamePanel.WIDTH / 2) - 115, (int) (GamePanel.HEIGHT / 2) + 9);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Press ENTER to Respawn", (int) (GamePanel.WIDTH / 2) - 113, (int) (GamePanel.HEIGHT / 2) + 10);
        }
    }
}
