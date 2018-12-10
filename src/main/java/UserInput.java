/**
 * Interface to use for getting textual data from the user
 */
public interface UserInput {

    /**
     * Gets text representing the question that the user has asked. (May be via keyboard or voice)
     * @return the string containg the users query
     */
    public String getText();

}
