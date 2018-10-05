package by.troyan.resource;

import by.troyan.exception.UserDataNotFoundException;
import by.troyan.service.ChatService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static by.troyan.support.Constants.*;

/**
 * Main resource class.
 */
@Component
@Path("/chat")
public class Chat {
    private static final Logger LOG = LogManager.getLogger(Chat.class);

    private ChatService chatService;

    /**
     * Need ChatService to be instantiated.
     *
     * @param chatService the chat service
     */
    @Autowired
    public Chat(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * User make GET request and subscribe to receive new Message from another
     * chat member. After user receives message he make new async request to receive next.
     * Nick of user checked by method isNickValid(nick). If it will fail, user will enter in
     * the chat but could not be able to post anything. If he will try - error message will appear.
     *
     * @param asyncResp the async resp
     * @param nick      the nick of user
     */
    @Path("/{nick}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void hangUp(@Suspended AsyncResponse asyncResp,
                       @PathParam("nick") String nick) {
        if (chatService.isNickValid(nick)) {
            chatService.putUserInChat(nick, asyncResp);
            LOG.info("User: [" + nick + "] " + "registered in the chat.");
        }
    }

    /**
     * Send message method. It checks nick of user and message content. And run method
     * sendMessageToAllUsers(nick, content) which made all work.
     *
     * @param nick    the nick
     * @param content the content
     * @return the response
     */
    @Path("/{nick}")
    @POST
    @Consumes("text/plain")
    public Response sendMessage(@PathParam("nick") final String nick,
                                final String content) {
        String checkResult = handleSavingMessageProcess(nick, content);
        if (checkResult.equals(CONFIRMATION_PHRASE)) {
            chatService.sendMessageToAllUsers(nick, content);
        }
        LOG.info("Send message success");
        return Response.ok(checkResult).build();
    }

    /**
     * Gets previous messages from DataBase. Amount could be set by User.
     *
     * @param amount the amount of messages to return
     * @return the previous messages
     */
    @GET
    @Path("/oldMessages/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getPreviousMessages(@PathParam("amount") int amount) {
        List result = chatService.getListOfLastMessages(amount);
        if (result == null) {
            LOG.error("No messages found");
            throw new UserDataNotFoundException("Cant download messages from dataBase");
        }
        return result;
    }

    /**
     * Get users in chat list.
     * //http://localhost:8080/rest/chat/getUsers
     * @return the list of users currently received messages. List
     * updates when one of user write message and sent it
     */
    @GET
    @Path("getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List getUsersInChat (){
        List<String> result = chatService.getUsersInChat();
        if(result == null){
            throw new UserDataNotFoundException("Cant download users list from Chat. Try again later");
        }
        return chatService.getUsersInChat();
    }

    /**
     * Method helping to handle errors.
     *
     * @param nick the nick of user
     * @param content content of message
     * @return String phrase describing error
     */
    private String handleSavingMessageProcess(String nick, String content) {
        //check nick
        if (!chatService.isNickValid(nick)) {
            LOG.warn("ERROR nick");
            return ERROR_NICK_PHRASE;
        }
        //check content
        if (!chatService.isMessageContentValid(content)) {
            LOG.warn("ERROR content");
            return ERROR_CONTENT_PHRASE;
        }
        //check is message saved
        if (!chatService.createAndSaveMessage(nick, content)) {
            LOG.warn("ERROR saving phrase");
            return ERROR_SAVING_PHRASE;
        }
        return CONFIRMATION_PHRASE;
    }
}
