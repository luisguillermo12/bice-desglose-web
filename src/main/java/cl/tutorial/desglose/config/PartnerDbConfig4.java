package cl.tutorial.desglose.config;

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
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

import static cl.tutorial.desglose.config.Constants.DATABASE_SQL;
import static cl.tutorial.desglose.service.util.Utils.*;

@Configuration
@EnableJpaRepositories(transactionManagerRef = "partnersqldesgloseTransactionManager", entityManagerFactoryRef = "partnersqldesgloseEntityManagerFactory",
    basePackages = "cl.tutorial.desglose.conexiones.sqldesglose.repository")
@EnableTransactionManagement
public class PartnerDbConfig4 {

    private static final Logger logger = LogManager.getLogger(PartnerDbConfig4.class);
    // configuracion desarrollo
  @Bean
    @ConfigurationProperties(prefix ="partnersqldesglose.datasource")
    public DataSourceProperties partnerSqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "datasourcesql")
    public DataSource sqlDataSource() {
        return partnerSqlDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "partnersqldesgloseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqlEntityManagerFactory(
        @Qualifier("datasourcesql") final DataSource sqlDataSource) throws ClassNotFoundException{
        LocalContainerEntityManagerFactoryBean builder = new LocalContainerEntityManagerFactoryBean();
        try {
            builder.setDataSource(sqlDataSource);
        } catch (Exception e) {
            logger.error("Error setting datasource for datasourcesql", e);
            logger.error(e.getMessage());
        }
        builder.setPackagesToScan("cl.tutorial.desglose.conexiones.sqldesglose.domain");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.SQL_SERVER);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServer2012Dialect");
        vendorAdapter.setShowSql(true);
        builder.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = getHibernateConfiguracion(DATABASE_SQL);
        builder.setJpaPropertyMap(properties);
        return builder;
    }

    @Bean(name = "partnersqldesgloseTransactionManager")
    public JpaTransactionManager sqlTransactionManager(@Qualifier("partnersqldesgloseEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
    // configuracion de produccion
    /* @Bean
    @ConfigurationProperties(prefix ="partnersqldesglose.datasource")
    public DataSourceProperties partnerSqlDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean(name = "datasourcesql", destroyMethod = "")
    public DataSource sqlDataSource()  throws NamingException {
        Context ctx = new InitialContext();
        String jndiName = partnerSqlDataSourceProperties().getJndiName();
        DataSource dataSourceJNDINAME = (DataSource) ctx.lookup(jndiName);
        return dataSourceJNDINAME;
    }

    @Bean(name = "partnersqldesgloseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqlEntityManagerFactory(
        @Qualifier("datasourcesql") final DataSource sqlDataSource) throws ClassNotFoundException{
        LocalContainerEntityManagerFactoryBean builder = new LocalContainerEntityManagerFactoryBean();
        try {
            builder.setDataSource(sqlDataSource);
        } catch (Exception e) {
            logger.error("Error setting datasource for datasourcesql", e);
            logger.error(e.getMessage());
        }
        builder.setPackagesToScan("cl.tutorial.desglose.conexiones.sqldesglose.domain");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        builder.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = getHibernateConfiguracion(DATABASE_SQL);
        builder.setJpaPropertyMap(properties);
        return builder;
    }

    @Bean(name = "partnersqldesgloseTransactionManager")
    public JpaTransactionManager sqlTransactionManager(@Qualifier("partnersqldesgloseEntityManagerFactory") final EntityManagerFactory emf) throws ClassNotFoundException{
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }*/
}
