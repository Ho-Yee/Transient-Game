/* Sasha Seufert
 * 12/2018
 * A simple mainline program used for testing the functions created for saving the user's data.
 */

package Save;


/**
 * 
 * @author Sasha Seufert
 */
public class TestHarnessForUserSave {

    public static void main(String[] args) {
        //Get user
        System.out.println(((User)((SuccessResponse)UserManager.getUserByID(2)).getResponse()).getName().getResponse());
        
    }
    
}


