/**
 * Interface separating GUI module and ChatBot module. 
 */
public interface IChatBot {

	/**
     * Determines intent, then will perform appropriate action (based on inputs handled by GUI), where the action will be a method in IChatBot's implementing class.
     * @param input UserInput object form the GUI, which will be used to discern the intent and any Stock or Sector names that form part of the query. 
     * @return Response object. 
     */
    public Response executeAction(UserInput input);
    
}