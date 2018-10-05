
package by.troyan.config;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey app configuration. Realization of Jax-Rs.
 */
@ApplicationPath("/rest")
public class JerseyAppConfiguration extends ResourceConfig {

    /**
     * Sets main package to find classes.
     */
    public JerseyAppConfiguration() {
         packages("by.troyan");
    }
}
