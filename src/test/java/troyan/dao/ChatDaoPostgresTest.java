package troyan.dao;

import by.troyan.config.test.TestJDBC;
import by.troyan.dao.ChatDao;
import by.troyan.entity.Message;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import javax.sql.DataSource;
import java.time.LocalDateTime;

public class ChatDaoPostgresTest {

    private ChatDao chatDao;
    private EmbeddedDatabase db;
    private Message message;

    @Before
    public void setUp() throws Exception {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(TestJDBC.class);
        db = (EmbeddedDatabase) applicationContext.getBean(DataSource.class);
        chatDao = applicationContext.getBean(ChatDao.class);

        message = Message.builder()
                .date(LocalDateTime.now())
                .sender("Anton")
                .content("Hello world")
                .build();

        chatDao.saveMessageInDB(message);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    public void saveMessageInDB() {
        Assert.assertTrue(chatDao.saveMessageInDB(message));
    }

    @Test
    public void getListOfLastMessagesEmptyCheck() {
        Assert.assertFalse(chatDao.getListOfLastMessages(10).isEmpty());
    }

    @Test
    public void getListOfLastMessagesMessageCheck() {
        message.setId(1L);
        Assert.assertEquals(message ,chatDao.getListOfLastMessages(10).get(0));
    }
}