/**
 * Sasha Seufert
 * Dec 19th, 2018
 * A class that formulates a detailed success response. Extends the abstract Response class.
 */

package Save;

/**
 * 
 * @author Sasha Seufert
 */
public class SuccessResponse extends Response {
    
    //Attribute
    private Object response;
    
    //Constructors
    /**
     * Primary constructor for a SuccessResponse.
     * @param response The response object
     */
    public SuccessResponse(Object response) {
        super("Successful Response. [No additional message]");
        
        //Set response object
        setResponse(response);
    }
    
    /**
     * Secondary constructor of a SuccessResponse
     * @param response The response object
     * @param message Message of the response
     */
    public SuccessResponse(Object response, String message) {
        super(message);
        
        //Set response object
        setResponse(response);
    }
    
    //Methods
    /**
     * Returns the response of this successful response
     * @return the response object
     */
    public Object getResponse() {
        return this.response;
    }
    
    /**
     * Sets the response of this successful response
     * @param response Response to set
     */
    public void setResponse(Object response) {
        this.response = response;
    }
}
