package troyan.service;

import by.troyan.dao.ChatDao;
import by.troyan.entity.Message;
import by.troyan.service.ChatService;
import by.troyan.service.ChatServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.container.AsyncResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChatServiceImplTest {
    private static final String TEST_SENDER = "Anton";
    private static final String WRONG_TEST_SENDER = "!";
    private static final String TEST_CONTENT = "Hello world!";
    private static final String WRONG_TEST_CONTENT = "";
    private static final int STANDARD_MSG_AMOUNT = 10;
    private ChatService chatService;
    private ChatDao chatDaoMock;
    private Message message;

    @Before
    public void setUp() {
        chatDaoMock = mock(ChatDao.class);
        message = mock(Message.class);
        chatService = new ChatServiceImpl(chatDaoMock);
    }

    @Test
    public void isNickValidTrue() {
        Assert.assertTrue(chatService.isNickValid(TEST_SENDER));
    }

    @Test
    public void isNickValidFalse() {
        Assert.assertFalse(chatService.isNickValid(WRONG_TEST_SENDER));
    }

    @Test
    public void isMessageContentValidTrue() {
        Assert.assertTrue(chatService.isMessageContentValid(TEST_CONTENT));
    }

    @Test
    public void isMessageContentValidFalse() {
        Assert.assertFalse(chatService.isMessageContentValid(WRONG_TEST_CONTENT));
    }

    @Test
    public void getUsersInChatTrue() {
        AsyncResponse asyncResponse = mock(AsyncResponse.class);
        chatService.putUserInChat(TEST_SENDER, asyncResponse);

        Assert.assertTrue(!chatService.getUsersInChat().isEmpty());
    }

    @Test
    public void getUsersInChatFalse() {
        Assert.assertTrue(chatService.getUsersInChat().isEmpty());
    }

    @Test
    public void getListOfLastMessages() {
        List<Message> messages =  new ArrayList<>();
        messages.add(message);
        when(chatDaoMock.getListOfLastMessages(STANDARD_MSG_AMOUNT)).thenReturn(messages);

        Assert.assertFalse(chatService.getListOfLastMessages(STANDARD_MSG_AMOUNT).isEmpty());
    }

    @Test
    public void putUserInChat() {
        AsyncResponse asyncResponse = mock(AsyncResponse.class);
        chatService.putUserInChat(TEST_SENDER, asyncResponse);

        Assert.assertNotNull(chatService.getUsersResponse(TEST_SENDER));
    }

    @Test
    public void getUsersResponse() {
        AsyncResponse asyncResponse = mock(AsyncResponse.class);
        chatService.putUserInChat(TEST_SENDER, asyncResponse);

        Assert.assertEquals(asyncResponse, chatService.getUsersResponse(TEST_SENDER));
    }
}