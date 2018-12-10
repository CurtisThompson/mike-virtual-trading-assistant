/**
 * Interface for Natural Language Parser API
 */
public interface NLP {

	/**
	 * Parses the provided UserInput, which is in natural language (i.e. spoken English) and produced a JSON object detailing the intent of the query, and any parameters such as a stock name. 
	 *
	 * @param input UserInput object to be converted to JSON. 
	 * @return JsonObject containing extracted intent and parameters. 
	 */
	public String determineIntent(UserInput input);

}