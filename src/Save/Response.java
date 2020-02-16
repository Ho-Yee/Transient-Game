/**
 * Sasha Seufert
 * Dec 19th, 2018
 * A class that formulates a detailed response, which includes a message to go with a response if nessessary (such as an error or warning), suggested solution to the error, as well as the actual response.
 */

package Save;

/**
 * 
 * @author Sasha Seufert
 */
public abstract class Response {
    
    //Attributes
    private String message;
    
    //Constructors
    /**
     * Primary constructor for a response object
     * @param message Message of the response
     */
    public Response(String message) {
        this.setMessage(message);
    }
    
    /**
     * Secondary constructor of a response object.
     */
    public Response() {
        this("");
    }
    
    //Methods
    /**
     * Returns the message of the response.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the message of the Response object
     * @param message 
     */
    public void setMessage(String message) {
        //Check if the message is blank
        if(message.length() == 0) {
            message = "[No message]";
        }
        
        //Set the message
        this.message = message;
    }
}
