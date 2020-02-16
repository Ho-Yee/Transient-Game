/**
 * Sasha Seufert
 * Dec 20th, 2018
 * A class that manages the users stored in the game datafile.
 */

package Save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Sasha Seufert
 */
public abstract class UserManager {
    
    //Declare variables
    private static String GAME_DATA_DIRECTORY = "resources/Save/GameSaves";
    private static User loadedUser;
    
    //Methods
    
    /**
     * If the game data directory specified above exists, this returns the directory of the folder.
     * @return a response, based on whether or not the directory exists.
     */
    public static Response getGameDataDirectory() {
        //Check if the directory exists
        if(!(new File(GAME_DATA_DIRECTORY)).exists()) {
            return new ErrorResponse("The game data directory stored on file (" + GAME_DATA_DIRECTORY + ") does not exist");
        }
        
        //Return the directory
        return new SuccessResponse(GAME_DATA_DIRECTORY);
    }
    
    /**
     * Sets the game data directory of the current application.
     * @param directory - Directory where all the game data is stored.
     * @return A response, based on whether or not the operation was successful. 
     */
    public static Response changeGameDataDirectory(String directory) {
        //Check if the directory exists
        if(!(new File(GAME_DATA_DIRECTORY)).exists()) {
            return new ErrorResponse("The data directory you would like to set (" + GAME_DATA_DIRECTORY + ") does not exist. The directory remains unchanged.");
        }
        
        //Set the directory
        GAME_DATA_DIRECTORY = directory;
        
        //Return the new directory
        return new SuccessResponse(GAME_DATA_DIRECTORY);
    }
    
    /**
     * Returns all of the users in a User array, encapsulated in a successful response.
     * @return A Response object.
     */
    public static Response getUsers() {
        //Declare variables 
        Response loadResponse;
        User users[];
        ArrayList<User> usersAL,
                        usersSortedAL;

        //Refresh the users array with the stored users
        loadResponse = loadStoredUsers();

        //If loading the users is successful
        if(loadResponse instanceof SuccessResponse) {
            
            //Get the users array
            users = (User[])((SuccessResponse)loadResponse).getResponse();
            
            //Convert users to ArrayList
            usersAL = new ArrayList();
            usersAL.addAll(Arrays.asList(users));
            
            //QuikSort the users, ascending by ID.
            usersSortedAL = quikSortUsers(usersAL);
            
            //Convert result into array
            for(int i = 0; i < usersSortedAL.size(); i++) {
                users[i] = (User)usersSortedAL.get(i);
            }
            
            //Return
            return new SuccessResponse(users);
            
        }
        else return loadResponse; //If error loading 

    }
    
    /**
     * Selects a user from all users by ID
     * @param ID - The ID of the user to select
     * @return - The user
     */
    public static Response getUserByID(int ID) {
        //Declare variables
        User users[],
             targetUser;
        Response currentResponse;

        //Get the users
        currentResponse = getUsers();
        if(currentResponse instanceof ErrorResponse) { //If error
            return currentResponse;
        }
        users = (User[])((SuccessResponse)currentResponse).getResponse();
        
        //Search
        targetUser = binarySearch(users, ID);
        if(targetUser == null) {
            return new ErrorResponse("The user with the ID " + ID + " does not exist.");
        }
        
        //Return
        return new SuccessResponse(targetUser);
    }
    
    /**
     * Searches for a user with a given ID using a recursive binary search method.
     * @param users The users to search through, which are sorted ascending by ID.
     * @param ID The ID of the user to look for
     * @return the user with the given ID. Null is returned if the user is not found.
     */
    private static User binarySearch(User users[], int ID) {
        //Base case
        if(users.length == 1) { //If one user left to sort through
            if((int)users[0].getID().getResponse() == ID) { //If the ID matches
                return users[0];
            }
            else return null; //If the user is not found
        }
        else if(users.length == 0) { 
            return null; //User not found
        }
        
        //Declare variables
        int middle,
            middleID;
        ArrayList<User> narrowedArray = new ArrayList<User>();
        User foundUser, narrowedArrayUser[];
        
        //Get middle index
        middle = users.length / 2;
        
        //Compare user ID at middle index to target ID
        middleID = (int)users[middle].getID().getResponse();
        if(ID > middleID) {
            for(int i = middle + 1; i < users.length; i++) { //Get the upper portion of the array
                narrowedArray.add(users[i]);
            }
        }
        else if(ID < middleID) {
            for(int i = 0; i < middle; i++) { //Get the lower portion of the array
                narrowedArray.add(users[i]);
            }
        }
        else { //If match
            return users[middle];
        }
        
        //ArrayList<Object> to User[] array
        Object obj[] = narrowedArray.toArray();
        narrowedArrayUser = new User[obj.length];
        for(int i = 0; i < obj.length; i++) {
            narrowedArrayUser[i] = (User)obj[i];
        }
                
        //Recursive call
        return binarySearch(narrowedArrayUser, ID);
    }
    
    /**
     * Sorts the users in ascending order by ID
     * @return 
     */
    private static ArrayList<User> quikSortUsers(ArrayList<User> users) {
        //Base case
        if(users.size() < 2) {
            return users;
        }

        //Declare variables
        ArrayList<User>           lessThan = new ArrayList(),
                            greaterThan = new ArrayList(),
                            sorted = new ArrayList();
        ArrayList pivot = new ArrayList();
        ArrayList result;
        
        //Set first element as pivot
        pivot.add((Integer)users.get(0).getID().getResponse());
        
        //Get less than and greater than IDs
        for(User i : users) {
            if((Integer)i.getID().getResponse() > (Integer) pivot.get(0))
                greaterThan.add(i);
            else if((Integer)i.getID().getResponse() < (Integer) pivot.get(0))
                lessThan.add(i);
            else pivot.add(i);
        }
        
        //Remove first pivot
        pivot.remove(0);
        
        //Sort both partitions
        lessThan = quikSortUsers(lessThan);
        greaterThan = quikSortUsers(greaterThan);
        
        //Create final sorted array
        sorted.addAll(lessThan);
        sorted.addAll(pivot);
        sorted.addAll(greaterThan);
        
        //Return
        return sorted;
    }
    
    /**
     * Returns the users stored in the data files, sorted ascending by ID.
     * @return a response, based on whether or not the loading was successful.
     */
    private static Response loadStoredUsers() {
        //Declare variables
        String gameDataDirPath,
               filePath = "";
        User users[];
        Response currentResponse;
        File    gameDataDir,
                records[];
        FileInputStream fileIn;
        ObjectInputStream objIn;
        
        //Get game data directory response
        currentResponse = getGameDataDirectory();
                
        //Make sure directory exists
        if(currentResponse instanceof ErrorResponse)
            return currentResponse;
        
        //Get the directory
        gameDataDirPath = (String)((SuccessResponse)currentResponse).getResponse();
        
        //Create File object of the directory
        gameDataDir = new File(gameDataDirPath);
        
        //Get all records in the directory
        records = gameDataDir.listFiles();
        
        //Initialise users array
        users = new User[records.length];
        
        //Deserialise all files
        for(int i = 0; i < records.length; i++) {
            try {
                //Open file
                filePath = records[i].getPath();
                fileIn = new FileInputStream(filePath);
                objIn = new ObjectInputStream(fileIn);
                
                //Read file
                users[i] = (User)objIn.readObject();
                
                //Close file
                objIn.close();
                fileIn.close();
            }
            catch(ClassNotFoundException e) {
                return new ErrorResponse("The data file for the user at the path \"" + filePath + "\" is corrupt and cannot the loaded.");
            }
            catch(Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        
        //Return
        return new SuccessResponse(users);
    }
    
    /**
     * Updates the user stored in the game data directory as a serialised byte file.
     * @param user The user to update. If the user does not exist, a new file will be created.
     * @return A Response object. Success: A string, stating whether the user was updated or created. Error: an error response message
     */
    private static Response writeUser(User user) {
        //Declare variables
        String  filePath,
                gameDataDirPath;
        Response currentResponse;
        FileOutputStream fileOut;
        ObjectOutputStream objOut;
        boolean fileExists;
        
        //Get game data directory response
        currentResponse = getGameDataDirectory();
                
        //Make sure directory exists
        if(currentResponse instanceof ErrorResponse)
            return currentResponse;
        
        //Get the directory
        gameDataDirPath = (String)((SuccessResponse)currentResponse).getResponse();
        
        //Generate user filePath
        filePath = gameDataDirPath + "/" + (int)user.getID().getResponse();
        
        //Check whether or not the user already exists
        fileExists = new File(filePath).exists();
        
        //Write out
        try {
            //Open file
            fileOut = new FileOutputStream(filePath);
            objOut = new ObjectOutputStream(fileOut);
            
            //Write object
            objOut.writeObject(user);
            
            //Close file
            objOut.close();
            fileOut.close();
        }
        catch(Exception e) {
            return new ErrorResponse(e.getMessage());
        }
        
        //Return
        if(fileExists)
            return new SuccessResponse("User updated.");
        else
            return new SuccessResponse("New user created.");
    }
    
    /**
     * Same as writeUser(), but for an array of users.
     * @return the response of the write.
     */
    private static Response writeUsers(User users[]) {
        //Declare variables
        Response currentResponse;
        String successResponsesStr = "All users successfuly saved:\n";
        //System.out.println(((SuccessResponse)users[0].getScoreForLevel(1)).getResponse());
        
        //Save the users
        for(User user : users) {
            //Write the current user
            if (user != null){
            currentResponse = writeUser(user);
            
            //Check the status
            if(currentResponse instanceof ErrorResponse){
                System.out.println("ERORR");
                return currentResponse;
            }  else {
                successResponsesStr += "\n\tUserID " + user.getID().getResponse() + ": " + ((SuccessResponse)currentResponse).getMessage();
            }
            }
        }
        
        //Success response
        return new SuccessResponse(successResponsesStr);
    }
    
    /**
     * Deletes a user's saved game data.
     * @param user User to delete
     * @return Whether or not the user was deleted.
     */
    public static Response deleteUser(User user) {
        //Declare variables
        String  filePath,
                gameDataDirPath;
        Response currentResponse;
        File file;
        
        //Get game data directory response
        currentResponse = getGameDataDirectory();
                
        //Make sure directory exists
        if(currentResponse instanceof ErrorResponse)
            return currentResponse;
        
        //Get the directory
        gameDataDirPath = (String)((SuccessResponse)currentResponse).getResponse();
        
        //Generate user filePath
        filePath = gameDataDirPath + "/" + (int)user.getID().getResponse();
        
        //Create file object
        file = new File(filePath);
        
        //try and delete the file
        if(file.delete())
            return new SuccessResponse("User successfuly deleted.");
        else {return new ErrorResponse("The user could not be deleted. Chech whether or not the user exists in the game directory.");}
    }
    
    /**
     * Saves/updates all users that are stored at runtime to the datafile.
     * @return the status of the update.
     */
    public static Response update() {
        //Declare variables
        User users[];
        Response currentResponse;
        
        //Save the user
        currentResponse = UserManager.writeUser(getLoadedUser());
        if(currentResponse instanceof ErrorResponse)
            return currentResponse;
        
        //Generate a success response
        return new SuccessResponse("Successfuly updated all users.");
    }
    
    /**
     * Sets the loaded user for the game
     * @param user - User to set
     */
    public static void setLoadedUser(User user) {
        loadedUser = user;
    }
    
    /**
     * Returns the loaded user of the game
     * @return the loaded user.
     */
    public static User getLoadedUser() {
        return loadedUser;
    }
}
