
package by.troyan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * This class holds beans and settings for Spring.
 */
@Component
@Configuration
@ComponentScan(basePackages = {
    "by.troyan.service",
    "by.troyan.resource",
    "by.troyan.dao",
    "by.troyan.aspect",
    "by.troyan.exception",})
@PropertySource(value = { "classpath:jdbc.properties" })
@EnableAspectJAutoProxy
public class SpringAppConfiguration {

    private final Environment environment;

    /**
     * To instantiate a spring config we need check property file. If we could not - there
     * will be no sense to run application.
     *
     * @param environment the environment
     */
    @Autowired
    public SpringAppConfiguration(Environment environment) {
        this.environment = environment;
    }

    /**
     * Object mapper to convert java objects to json.
     *
     * @return the object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper;
    }

    /**
     * Data source use commons-dbcp as connection pool.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource(){

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        dataSource.setInitialSize(10);
        return dataSource;
    }

    /**
     * Named parameter jdbc template to simplify work.
     *
     * @return the named parameter jdbc template
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource());
    }

    /**
     * Transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
