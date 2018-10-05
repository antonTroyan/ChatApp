package by.troyan.service;

import by.troyan.entity.Message;

import javax.ws.rs.container.AsyncResponse;
import java.util.List;

/**
 * The interface Chat service.
 */
public interface ChatService {

    /**
     * Create and save message. Returns boolean.
     *
     * @param sender  the sender
     * @param content the content
     * @return the boolean. True if operation was success, false if not.
     */
    boolean createAndSaveMessage (String sender, String content);

    /**
     * Gets list of last messages. User could specify, how much
     * messages he wants to receive.
     *
     * @param amount the amount of messages to return
     * @return the list of last messages
     */
    List<Message> getListOfLastMessages (int amount);

    /**
     * Put user in chat.
     *
     * @param nick      the nick
     * @param asyncResp the async resp
     */
    void putUserInChat (String nick, AsyncResponse asyncResp);

    /**
     * Gets users response by nick.
     *
     * @param keyNick the key nick
     * @return the users response
     */
    AsyncResponse getUsersResponse(String keyNick);

    /**
     * Send message to all users. In this method new thread starts
     * that will send messages to all users that were added in the
     * chat room.
     *
     * @param nick    the nick
     * @param content the content
     */
    void sendMessageToAllUsers(String nick, String content);

    /**
     * Check is nick valid. Not it simply determine the length of nick,
     * and check is length normal.
     *
     * @param nick the nick
     * @return the boolean
     */
    boolean isNickValid(String nick);

    /**
     * Is message content. Not simply checks message length.
     *
     * @param content the content
     * @return the boolean
     */
    boolean isMessageContentValid(String content);

    /**
     * Gets users in chat. Information refreshed every time some of the users
     * write something in the chat.
     *
     * @return the users in chat at this moment
     */
    List<String> getUsersInChat();
}
