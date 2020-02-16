/**
 * Austin Van Braeckel
 * 12/2018
 * This class represents a timer that ultimately results in game over when time runs-out.
 * It also keeps track of score, displaying it along with the number of minutes and
 * seconds left before the timer runs-out.
 */
package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import Audio.*;
import GameState.GameStateManager;

/**
 * 
 * @author Austin Van Braeckel
 */
public class Time {

    //attributes
    private Font font;
    
    public long time; //so other classes can access the time
    public long start;
    public long maxTime;
    
    //set-up formatting for time measurements and score
    DecimalFormat seconds = new DecimalFormat("00");
    DecimalFormat score = new DecimalFormat("###,###,###,###,###,##0");

    private GameStateManager gsm;
    private String output;
    private boolean timeIsUp;
    private Color color;

    //constructor
    public Time(long maxTime, GameStateManager gsm) {
        this.gsm = gsm;
        //set-up font to be used
        font = GameStateManager.centuryGothicBold.deriveFont(Font.PLAIN, 10);
        //sets the start time, maximum time (time limit)
        start = System.nanoTime();
        this.maxTime = maxTime;
        //specifies that time has not run-out yet (default)
        timeIsUp = false;
        color = Color.WHITE; //default colour
        output = "TIME ---\nTIME LEFT ---"; //sets-up output string formatting
    }

    /**
     * Sets the time to the given long value
     * @param time long of the time to which the timer will be set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Retrieves the status of the timer in regards to its completion
     * @return boolean true if time is up, and false if it is still running
     */
    public boolean timeIsUp() {
        return timeIsUp;
    }
    
    /**
     * Finishes the timer and returns the time bonus score
     * @return the score of the time bonus as an integer
     */
    public int finish(){
        timeIsUp = true;
        //Accumulates time score - (amount of seconds left is 5 points each
        int timeScore = ((int)(maxTime - time)) * 5;
        return timeScore;
    }

    /**
     * Updates all necessary aspects of the timer
     * @param playerScore the player's current score as an integer
     */
    public void update(int playerScore) {
        //Only updates if the timer is still running
        if (!timeIsUp) {
            time = (System.nanoTime() - start) / 1000000000; //calculate time elapsed and convert to seconds
            color = Color.WHITE;//default colour
            
            //if the time exceeds time limit, end the game (Game over)
            if (time >= maxTime) {
                //set player's final score (adds time bonus)
                GameStateManager.tempScore = playerScore + finish();
                //stop all level-related music
                AudioPlayer.stop("bgMusic3");
                AudioPlayer.reset("bgMusic3");
                AudioPlayer.stop("bgMusic2");
                AudioPlayer.reset("bgMusic2");
                AudioPlayer.stop("bgMusic1");
                AudioPlayer.reset("bgMusic1");
                AudioPlayer.stop("bgMusicBonus");
                AudioPlayer.reset("bgMusicBonus");
                //show game over screen
                gsm.setWarp(GameStateManager.GAMEOVERSTATE);
                //reset time
                start = System.nanoTime();
                time = 0;
                timeIsUp = true;
            } else if (time >= (maxTime * 0.75)) { //25% of time is left
                color = Color.RED; //make the colour red to show the utmost urgency
            } else if (time >= (maxTime * 0.5)) {//50% of time is left
                color = Color.YELLOW; //make the colour yellow to show more urgency
            }
            
            //sets the output string to the current time and score values
            output = /*"TIME --- " + ((int) (time) / 60) + ":" + seconds.format((int) (time) % 60)
                    +*/ "          TIME LEFT --- " + ((int) (maxTime - time) / 60) + ":" + seconds.format((int) (maxTime - time) % 60)
                    + "     SCORE: " + score.format(playerScore);
        } else { //if time is up, finish the timer
            finish();
        }
    }

    /**
     * Draws the time and score information as a String on screen with the given Graphics2D object
     * @param g Graphics2D object that is used to draw
     */
    public void draw(Graphics2D g) {
        //sets the font and colour, then draws the output string at the top of the screen
        g.setFont(font);
        g.setColor(color);
        g.drawString(output, 100, 23);
    }
}
