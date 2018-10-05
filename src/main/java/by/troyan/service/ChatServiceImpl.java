package by.troyan.service;

import by.troyan.aspect.TimeCounted;
import by.troyan.dao.ChatDao;
import by.troyan.entity.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.container.AsyncResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static by.troyan.support.Constants.*;

/**
 * Realization of the service layer.
 */
@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger LOG = LogManager.getLogger(ChatServiceImpl.class);
    private final Map<String, AsyncResponse> usersInChat = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ChatDao chatDao;

    /**
     * Instantiates a new Chat service.
     *
     * @param chatDao the chat dao
     */
    @Autowired
    public ChatServiceImpl(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    public boolean createAndSaveMessage(String sender, String content) {
        Message message = Message.builder()
                .sender(sender)
                .content(content)
                .date(LocalDateTime.now())
                .build();
        return chatDao.saveMessageInDB(message);
    }

    @TimeCounted
    public void sendMessageToAllUsers(String nick, String content) {
        LOG.info("User: [" + nick + "] " + "posted message [" + content + "] in the chat.");
        Set<String> nicks = usersInChat.keySet();
        Message message = Message.builder()
                .date(LocalDateTime.now())
                .content(content)
                .sender(nick)
                .build();
        // new thread to send new message to everyone
        startNewThreadToSendAll(nicks, message);
    }

    private void startNewThreadToSendAll(Set<String> nicks, Message message) {
        try {
            executorService.submit(() -> {
                for (String nickOfUser : nicks) {
                    AsyncResponse asyncResponse = getUsersResponse(nickOfUser);
                    asyncResponse.resume(message);
                }
                // need for optimisation and to show users in chat
                usersInChat.clear();
            });
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public boolean isNickValid(String nick) {
        if (nick.length() < MIN_NICK_LENGTH || nick.length() > MAX_NICK_LENGTH) {
            LOG.warn("Wrong nick length!");
            return false;
        }
        return true;
    }

    public boolean isMessageContentValid(String content) {
        return content.length() != MIN_CONTENT_LENGTH
                && content.length() <= MAX_CONTENT_LENGTH;
    }

    public List<String> getUsersInChat() {
        return new ArrayList<>(usersInChat.keySet());
    }

    public List<Message> getListOfLastMessages(int amount) {
        return chatDao.getListOfLastMessages(amount);
    }

    public void putUserInChat(String nick, AsyncResponse asyncResp) {
        usersInChat.put(nick, asyncResp);
    }

    public AsyncResponse getUsersResponse(String keyNick) {
        return usersInChat.get(keyNick);
    }
}
