package cl.tutorial.desglose.config;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class ConnectionFactory {
    private static HikariDataSource dataSource;

    private ConnectionFactory() {
    }


    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:xe");
            dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
            dataSource.setUsername("YOUR_DB_USER");
            dataSource.setPassword("YOUR_DB_PASSWORD");
            dataSource.setAutoCommit(false);
        }
        return dataSource.getConnection();
    }
}
