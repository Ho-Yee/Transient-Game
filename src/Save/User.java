/**
 * Sasha Seufert
 * Dec 19th, 2018
 * This is a class that represents a user that plays a game. This class stores the progress of the user, as well as basic information, such as the name, id, etc.
 */
package Save;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Sasha Seufert
 */
public class User implements Serializable {
    
    //Attributes
    private static int HIGHEST_LEVEL = 5;
    public static ArrayList<User> allUsers = new ArrayList<User>();
    private String name;
    private int ID,
                currentLevel;
    private ArrayList<Integer> scoreForLevel = new ArrayList<Integer>();
    
    //Constructors
    /**
     * Constructor that creates a new user, only given the name.
     * @param name 
     */
    public User(String name) {
        this(name, 0, new ArrayList<Integer>());
    }
    
    /**
     * Constructor that creates a new user.
     * @param name - The name of the new user
     * @param currentLevel - The level the new user should start at
     * @param scoreForLevels - The score for each level
     */
    public User(String name, int currentLevel, ArrayList<Integer> scoreForLevels) {
        this(name, currentLevel, scoreForLevels, getNextID(), true);
    }
    
    /**
     * Constructor for a user. An existing ID overwrites the saved user, whereas a new ID writes a new user.
     * @param name Name of the user
     * @param currentLevel Current level of the user (unlocked)
     * @param scoreForLevels - Scores for each level
     * @param ID - User ID
     */
    public User(String name, int currentLevel, ArrayList<Integer> scoreForLevels, int ID, boolean setLoaded) {
        //Assign id
        this.ID = ID;
        
        //Scores
        for(int i = 0; i < 20; i++)
            scoreForLevel.add(i, 0);
        
        //Set attributes
        this.name = name;
        this.currentLevel = currentLevel;
        
        //Add this user to array of all users for this runtime
        allUsers.add(this);
        
        if(setLoaded)
            UserManager.setLoadedUser(this);
        
        //Update
        UserManager.update();
    }
    
    public User(User u){
        //Assign id
        this.ID = u.ID;
        
        //Scores
        for(int i = 0; i < 20; i++)
            scoreForLevel.add(i, 0);
        
        //Set attributes
        this.name = u.name;
        this.currentLevel = u.currentLevel;
        
        //Add this user to array of all users for this runtime
        allUsers.add(this);
        
        //Update
        UserManager.update();
    }
    
    //Methods
    
    /**
     * Gets the next ID after the saved user with the greatest ID
     * @return the id of the new user
     */  
    public static int getNextID() {
        User[] users = ((User[])((SuccessResponse)UserManager.getUsers()).getResponse());
        if(users.length < 1)
            return 0;
        int lastID = (int)(users[users.length - 1].getID().getResponse());
        return ++lastID;
    }
    
    /**
     * Sets the highest level of this game that a user can reach.
     * @param level The level to set
     * @return the response object
     */
    
    
    public static Response setHighestLevel(int level) {
        //Check conditions
        if(level < 1) {
            return new ErrorResponse("Cannot set highest level lower than one.");
        }
        
        //Set the level
        HIGHEST_LEVEL = level;
        
        //Return 
        return new SuccessResponse(HIGHEST_LEVEL);
    }
    
    /**
     * Returns all of the users stored during runtime.
     * @return the users as an array.
     */
    public static User[] getAll() {
        //Convert all users to array of Users
        User users[] = new User[allUsers.size()];
        for(int i = 0; i < allUsers.size(); i++)
            users[i] = allUsers.get(i);
                
        //Return
        return users;
    }
    
    /**
     * Returns the highest possible level that the user can reach.
     * @return a response object.
     */
    public static SuccessResponse getHighestLevel() {
        return new SuccessResponse(HIGHEST_LEVEL);
    }
    
    /**
     * Automatically assigns a new unique identifier to the user.
     */
    private Response assignNewID() {
        //Declare variables
        Response currentResponse;
        User users[];
        int lastID;

        //Get all users, sorted.
        currentResponse = UserManager.getUsers();
        if(currentResponse instanceof ErrorResponse)
            return currentResponse;
        users = (User[])((SuccessResponse)currentResponse).getResponse();
        
        //Get the last ID
        lastID = (Integer)users[users.length - 1].getID().getResponse();
        
        //Increment the greatest id
        this.ID = lastID + 1;
        
        //Return a response
        return new SuccessResponse(this.ID);
    }
    
    /**
     * Returns the name of the current user
     * @return The name 
     */
    public SuccessResponse getName() {
        return new SuccessResponse(this.name);
    }
    
    public SuccessResponse setName(String name) {
        //Declare variables
        boolean nameWasBlank = false;

        //Check if the name is blank
        if(name.length() == 0) {
            nameWasBlank = true;
            this.name = "[Unnamed User]";
        }
        else this.name = name;
         
        //Update
        UserManager.update();
        
        //Formulate response
        if(nameWasBlank) {
            return new SuccessResponse(true, "You did not choose a name! You have been assigned a default name.");
        }
        else {
            return new SuccessResponse(true);
        }
    }
    
    /**
     * Returns the ID of the current user
     * @return The ID as an int 
     */
    public SuccessResponse getID() {
        return new SuccessResponse(this.ID);
    }
    
    
    /**
     * Returns the score of a level for the current user
     * @param levelIndex Level index
     * @return The score as an int 
     */
    public Response getScoreForLevel(int levelIndex) {
        //Check if that score exists
        if(levelIndex >= scoreForLevel.size()) {
            return new ErrorResponse("Score for level index " + levelIndex + " has not been set for the current user.");
        }
        
        //Return
        return new SuccessResponse((int)this.scoreForLevel.get(levelIndex));
    }
    
    /**
     * Sets the score of the user at a given level.
     * @param score The new score to set
     * @param levelIndex Level to set the score of
     * @return A response that contains the new score
     */
    public Response setScoreForLevel(int score, int levelIndex) {
        //Check if conditions are met
        if(score < 0) {
            return new ErrorResponse("The score cannot be set lower than zero.");
        }
        if(levelIndex < 0) {
            return new ErrorResponse("There is no level with an index lower than zero.");
        }
        
        //Set the score
        System.out.println("level" + levelIndex + ": " + score);
        this.scoreForLevel.set(levelIndex, score);
        
        //Update
        System.out.println(UserManager.update() instanceof SuccessResponse);
        
        //Return
        return new SuccessResponse(score);
    }
    
    /**
     * Returns the current level of the current user
     * @return The current level as an int 
     */
    public SuccessResponse getCurrentLevel() {
        return new SuccessResponse(this.currentLevel);
    }
    
    /**
     * Sets the current level of the user
     * @param level The level to set
     * @return a response object with corresponding data
     */
    public Response setCurrentLevel(int level) {
        //Check conditions
        if(level < 1 || level > HIGHEST_LEVEL) {
            return new ErrorResponse("Invalid level of " + level + " set.");
        }
        
        //Set the level
        this.currentLevel = level;
        
        //Update
        UserManager.update();
        
        //Return
        return new SuccessResponse(currentLevel);
    }
    
    /**
     * Level up the user by one level
     * @return a response object
     */
    public Response levelUp() {
        //Set the level
        return setCurrentLevel((int)getCurrentLevel().getResponse() + 1);
    }
    
    /**
     * Creates a clone of the current user
     * @return the current user
     */
    @Override public User clone() {
        return new User(this.name, this.currentLevel, this.scoreForLevel, this.ID, false);
    }
}
