
package by.troyan.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Spring web container initializer. Order was set to 1 to initialize Spring context
 * earlier than jersey context.
 */
@Order(1)
public class SpringWebContainerInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        registerContextLoaderListener(servletContext);
        // Set the Jersey used property to it won't load a ContextLoaderListener
        servletContext.setInitParameter("contextConfigLocation", "");
    }
    
    private void registerContextLoaderListener(ServletContext servletContext) {
        WebApplicationContext webContext;
        webContext = createWebApplicationContext(SpringAppConfiguration.class);
        servletContext.addListener(new ContextLoaderListener(webContext));
    }

    /**
     * Create web application context.
     *
     * @param configClasses the config classes
     * @return the web application context
     */
    private WebApplicationContext createWebApplicationContext(Class... configClasses) {
        AnnotationConfigWebApplicationContext context;
        context = new AnnotationConfigWebApplicationContext();
        context.register(configClasses);
        return context;
    }
}
