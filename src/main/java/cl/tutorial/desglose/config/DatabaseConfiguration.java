package cl.tutorial.desglose.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

import static cl.tutorial.desglose.config.Constants.DATABASE_ORACLE;
import static cl.tutorial.desglose.service.util.Utils.getHibernateConfiguracion;

@Configuration
@EnableJpaRepositories(transactionManagerRef = "transactionManager", entityManagerFactoryRef = "entityManagerFactory",
    basePackages = "cl.tutorial.desglose.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

    private static final Logger logger = LogManager.getLogger(PartnerDbConfig1.class);
    // configuracion de desarrollo
    @Primary
    @Bean
    @ConfigurationProperties(prefix ="spring.datasource")
    public DataSourceProperties defaultDglDataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        return properties;
    }

    @Primary
    @Bean
    public HikariDataSource dglDataSource() {
        return defaultDglDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dglEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        HikariDataSource dataSource =  dglDataSource();
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(2);
        dataSource.setMaxLifetime(1800000);
        dataSource.setIdleTimeout(30000);
        return builder.dataSource(dataSource).packages("cl.tutorial.desglose.domain").persistenceUnit("default").build();
    }
    @Primary
    @Bean(name = "transactionManager")
    public JpaTransactionManager dglTransactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    // configuracion de produccion
/*
   @Primary
    @Bean
    @ConfigurationProperties(prefix ="spring.datasource")
    public DataSourceProperties defaultDglDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Primary
    @Bean(name = "datasourcedgl", destroyMethod = "")
    public DataSource dglDataSource() throws NamingException {
        Context ctx = new InitialContext();
        String jndiName = defaultDglDataSourceProperties().getJndiName();
        DataSource dataSourceJNDINAME = (DataSource) ctx.lookup(jndiName);
        return dataSourceJNDINAME;
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dglEntityManagerFactory(
        @Qualifier("datasourcedgl") final DataSource dglDataSource) throws ClassNotFoundException {
        LocalContainerEntityManagerFactoryBean builder = new LocalContainerEntityManagerFactoryBean();
        try{
        builder.setDataSource(dglDataSource);
        } catch (Exception e) {
            logger.error("Error setting datasource for datasourcedgl", e);
            logger.error(e.getMessage());
        }
        builder.setPackagesToScan("cl.tutorial.desglose.domain");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        builder.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = getHibernateConfiguracion(DATABASE_ORACLE);
        builder.setJpaPropertyMap(properties);
        return builder;
    }

    @Primary
    @Bean(name = "transactionManager")
    public JpaTransactionManager dglTransactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory emf)throws ClassNotFoundException{
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }*/
}
