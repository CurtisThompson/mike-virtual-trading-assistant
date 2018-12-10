/**
 * Interface to use for converting text output to speech
 */
public interface Text2Speech {

	/**
     * Gets text representing the question that the user has asked. (May be via keyboard or voice)
     * @param text output text to be converted to speech. 
     */
	public void textToSpeech(String text);

}