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
import java.util.Properties;

import static cl.tutorial.desglose.config.Constants.DATABASE_ORACLE;
import static cl.tutorial.desglose.service.util.Utils.getHibernateConfiguracion;

@Configuration
@EnableJpaRepositories(transactionManagerRef = "partnersfdesgloseTransactionManager", entityManagerFactoryRef = "partnersfdesgloseEntityManagerFactory",
    basePackages = "cl.tutorial.desglose.conexiones.sfdesglose.repository")
@EnableTransactionManagement
public class PartnerDbConfig3 {

    private static final Logger logger = LogManager.getLogger(PartnerDbConfig3.class);
    // configuracion desarrollo
  @Bean
    @ConfigurationProperties(prefix ="partnersfdesglose.datasource")
    public DataSourceProperties partnerSfDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource sfDataSource() {
        return partnerSfDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

     @Bean(name = "partnersfdesgloseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sfEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
         HikariDataSource dataSource =  sfDataSource();
         dataSource.setMinimumIdle(1);
         dataSource.setMaximumPoolSize(2);
         dataSource.setMaxLifetime(1800000);
         dataSource.setIdleTimeout(30000);
        LocalContainerEntityManagerFactoryBean emf = builder
            .dataSource(dataSource)
            .packages("cl.tutorial.desglose.conexiones.sfdesglose.domain")
            .persistenceUnit("partnersfdesglose")
            .build();
        emf.setJpaProperties(properties);
        return emf;
    }

    @Bean(name = "partnersfdesgloseTransactionManager")
    public JpaTransactionManager sfTransactionManager(@Qualifier("partnersfdesgloseEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }


    // configuracion de produccion
    /*@Bean
    @ConfigurationProperties(prefix ="partnersfdesglose.datasource")
    public DataSourceProperties partnerSfDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean(name = "datasourcesf", destroyMethod = "")
    public DataSource sfDataSource()  throws NamingException {
        Context ctx = new InitialContext();
        String jndiName = partnerSfDataSourceProperties().getJndiName();
        DataSource dataSourceJNDINAME = (DataSource) ctx.lookup(jndiName);
        return dataSourceJNDINAME;
    }

    @Bean(name = "partnersfdesgloseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sfEntityManagerFactory(
        @Qualifier("datasourcesf") final DataSource sfDataSource) throws ClassNotFoundException{
        LocalContainerEntityManagerFactoryBean builder = new LocalContainerEntityManagerFactoryBean();
        try {
            builder.setDataSource(sfDataSource);
        } catch (Exception e) {
            logger.error("Error setting datasource for datasourcesf", e);
            logger.error(e.getMessage());
        }
        builder.setPackagesToScan("cl.tutorial.desglose.conexiones.sfdesglose.domain");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        builder.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = getHibernateConfiguracion(DATABASE_ORACLE);
        builder.setJpaPropertyMap(properties);
        return builder;
    }

    @Bean(name = "partnersfdesgloseTransactionManager")
    public JpaTransactionManager sfTransactionManager(@Qualifier("partnersfdesgloseEntityManagerFactory") final EntityManagerFactory emf) throws ClassNotFoundException{
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }*/
}
