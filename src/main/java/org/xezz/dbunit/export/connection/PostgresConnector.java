package org.xezz.dbunit.export.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: Xezz
 * Date: 22.06.13
 * Time: 13:05
 */
class PostgresConnector implements DatabaseConnector {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostgresConnector.class);
    private final static String driverClassName = "org.postgresql.Driver";
    private final String urlPrefix = "jdbc:postgresql://";
    private final String USERNAME_PROPERTY = "user";
    private final String PASSWORD_PROPERTY = "password";
    private String host = "localhost";
    private String schemaName;
    private String userName;
    private String password;
    private Properties properties = new Properties();

    PostgresConnector(String host, String password, String schemaName, String userName) {
        if (host != null) {
            this.host = host;
        }
        this.password = password;
        this.schemaName = schemaName;
        this.userName = userName;
    }

    PostgresConnector() {
    }

    @Override
    public Connection buildConnection() throws ClassNotFoundException, SQLException {
        LOGGER.info("Building connection for Postgres");
        Class.forName("org.postgresql.Driver");
        StringBuilder sb = new StringBuilder(urlPrefix);
        // StringBuilder appends null if a given string is null, so only add it if its not null!
        if (host != null) {
            sb.append(host);
            sb.append("/");
        }
        if (schemaName != null) {
            sb.append(schemaName);
        }
        if (userName != null) {
            properties.setProperty(USERNAME_PROPERTY, userName);
        }
        if (password != null) {
            properties.setProperty(PASSWORD_PROPERTY, password);
        }
        LOGGER.info("Connection string building done: " + sb.toString());
        return DriverManager.getConnection(sb.toString(), properties);
    }

    @Override
    public void setCustomProperties(Properties customProperties) {
        this.properties = customProperties;
    }

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getSchemaName() {
        return schemaName;
    }

    void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    String getHost() {
        return host;
    }

    void setHost(String host) {
        this.host = host;
    }

    Properties getProperties() {
        return properties;
    }

    void setProperties(Properties properties) {
        this.properties = properties;
    }
}
