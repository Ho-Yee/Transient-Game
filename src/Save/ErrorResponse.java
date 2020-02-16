/**
 * Sasha Seufert
 * Dec 19th, 2018
 * A class that formulates a detailed error response. Extends the abstract Response class.
 */

package Save;

/**
 * 
 * @author Sashe Seufert
 */
public class ErrorResponse extends Response {

    //Constructor
    /**
     * Primary constructor for a SuccessResponse.
     * @param message Error message
     */
    public ErrorResponse(String message) {
        super(message);
    }
    
}
