package by.troyan.config.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * This configure created only by test purpose. It holds embedded database.
 * Others beans are similar to main configure.
 */
@Configuration
@ComponentScan(basePackages = {
        "by.troyan.service",
        "by.troyan.resource",
        "by.troyan.dao",
        "by.troyan.aspect",})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class TestJDBC {

    /**
     * This is bean for embedded database. It linked to "initdb.postgresql/schema.sql"
     * script that initialize database with necessary scheme.
     *
     * @return the data source
     */
    @Bean
    public DataSource driverManagerDataSource(){
        return  new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("initdb.postgresql/schema.sql")
                .build();
    }

    /**
     * Named parameter jdbc template bean.
     *
     * @return the named parameter jdbc template
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(driverManagerDataSource());
    }

    /**
     * Bean to rule handle transactions.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(driverManagerDataSource());
    }
}
