/*Alec Godfrey
 * 12/2018
 * This is a game state that shows the available levels, allowing the user to select one to play
 */
package GameState;

import Handlers.Keys;
import java.awt.*;
import TileMap.Background;
import Audio.AudioPlayer;
import Save.*;

/**
 * 
 * @author Alec Godfrey
 */
public class LevelSelectState extends GameState {
    
    private Background bg;
    private Background detail;
    
    private int currentChoice;
    private String[] levels = {
        "1",
        "2",
        "3",
        "BONUS"
    };
    
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private String output1;
    private String output2;

    private int levelPoints = 0;
    private String name;
    private User currentUser;
    
    private boolean selection = false;
    private long selectionTime;
    
    /**
     * 
     * @param gsm Game State manager class
     */
    public LevelSelectState(GameStateManager gsm) {
        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/menubg4.png", 1);
            detail = new Background("/Backgrounds/menuparticles2.png",1);
            detail.setVector(-9, 0);
            titleColor = new Color(40, 0, 230);
            titleFont = GameStateManager.harlow.deriveFont(Font.BOLD, 50);
            font = GameStateManager.bauhaus.deriveFont(Font.PLAIN, 20);
            output1 = "";
            output2 = "";
            currentChoice = 0;
            currentUser = UserManager.getLoadedUser();
            name = (String)((currentUser.getName().getResponse()));
            
            //play music
            AudioPlayer.reset("level select");
            AudioPlayer.loopFor("level select", -5f, 1000);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void init(){
        
    }
    
    public void update(){
        bg.update();
        detail.update();
        handleInput();
        
        if (currentChoice == 0){
            //Level 1 (Underground)
            output1 = "   Level 1: Cavern Chaos";
        }
        if (currentChoice == 1){
            //Level 2 (Lava Temple)
            output1 = "Level 2: Lava-Temple Turmoil";
        }
        
        if (currentChoice == 2){
            //Level 3 (Ice)
            output1 = " Level 3: Infuriating Iciness";
        }
        if (currentChoice == 3){
            //BONUS
            output1 = " BONUS Level: Bullseye Bash";
        }
        
         Response score = currentUser.getScoreForLevel(currentChoice);
            if (score instanceof SuccessResponse){
                ((SuccessResponse)score).getResponse();
                levelPoints = (int)((SuccessResponse) score).getResponse();
            } else { //error response
                //set default score (0)
                levelPoints = 0;
            }
        
        output2 = "Highscore: (" + name + ") - " + levelPoints + " Points";
    }
    
    public void draw(Graphics2D g){
        bg.draw(g);
        detail.draw(g);
        
        g.setColor(Color.WHITE);
    //draw level info
        //Draw level name
       // g.setFont(new Font("Gill Sans MT Condensed", Font.BOLD, 20));
        g.setFont(GameStateManager.gillMTBoldC.deriveFont(Font.BOLD, 20));
        g.drawString(output1, 60, 120);
        //draw highscore info
        g.drawString(output2, 10, 230);
        
        
        //draw title
        
        g.setFont(titleFont);
         g.drawString("Level Select", 20, 70);
          g.drawString("Level Select", 20, 69);
        g.setColor(titleColor);
        g.drawString("Level Select", 22, 70);
        
        
        //draw levels
        g.setFont(font);
        for (int i = 0; i < levels.length; i++) {
            if(i == currentChoice){
                g.setColor(Color.WHITE);
            }else {
                g.setColor(new Color(50,80,230));
            }
            
            if(i != levels.length - 1){
                g.fillRect(i * 30 + 85,135,18,18);
            } else {
                g.fillRect(i * 30 + 85, 135, 65, 18);
            }
            
            if(i == currentChoice){
                g.setColor(titleColor);
            }else {
                g.setColor(Color.WHITE);
            }
            g.drawString(levels[i], i*30 + 88, 150);
            
        }
        g.setColor(Color.WHITE);
        g.setFont(GameStateManager.gillMTBold.deriveFont(Font.BOLD, 12));
        g.drawString("BACK", 275, 20);
        
    }
    private void select(){
            //stop music
            AudioPlayer.stop("level select");
            AudioPlayer.reset("level select");
        
        if (currentChoice == 0){
            //Level 1 (Underground)
            gsm.setWarp(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1){
            //Level 2 (Lava Temple)
           gsm.setWarp(GameStateManager.LEVEL2STATE);
        }
        
        if (currentChoice == 2){
            //Level 3 (Ice)
            gsm.setWarp(GameStateManager.LEVEL3STATE);
        }
        if (currentChoice == 3){
            //BONUS
            gsm.setWarp(GameStateManager.BONUSLEVELSTATE);
        }
    }
    
    /**
     * Detects user input (pressing backspace to go back to main menu)
     */
    public void handleInput(){
        if (selection) {
            long elapsed = (System.nanoTime() - selectionTime) / 1000000;
            if (elapsed > 150) {
                selection = false;
            }
        }
        
        if (Keys.isPressed(Keys.BACKSPACE)) {
            //stop music
            AudioPlayer.stop("level select");
            AudioPlayer.reset("level select");
            //Go back to main menu
            gsm.setState(GameStateManager.MENUSTATE);
        }
        
        if (Keys.isPressed(Keys.ENTER)){
            //play selected level
            select();
        }
        
        if (Keys.isPressed(Keys.LEFT)) {
                if(!selection){
                    currentChoice--;
                    if (currentChoice < 0){
                        currentChoice = levels.length - 1;
                    }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
                
        }

        if (Keys.isPressed(Keys.RIGHT)) {
                if(!selection){
                    currentChoice++;
                    if (currentChoice >= levels.length){
                currentChoice = 0;
            }
                    selection = true;
                    selectionTime = System.nanoTime();
                }
            }
    }
    
}


