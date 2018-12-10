public class ChatBotDummy implements IChatBot{
  public Response executeAction(UserInput input){
    //TODO implement this
    return new Response("Finding price of " + input.getText());
    }
}
