package troyan.resource;

import by.troyan.config.JerseyAppConfiguration;
import by.troyan.config.SpringAppConfiguration;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class ChatTest extends JerseyTest {

    @Override
    public Application configure() {
        JerseyAppConfiguration config = new JerseyAppConfiguration();
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(SpringAppConfiguration.class);
        config.property("contextConfig", context);

        return config;
    }

    @Test
    public void getPreviousMessages() {
        Response response = target("chat")
                .path("/oldMessages/10")
                .request()
                .get();

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull("Should return list", response.getEntity());
    }

    @Test
    public void getUsersInChat() {
        Response response = target("chat")
                .path("getUsers")
                .request()
                .get();

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull("Should return list", response.getEntity());
    }
}