package xezz.org.dbunit.export.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: Xezz
 * Date: 22.06.13
 * Time: 18:00
 */
public interface DatabaseConnector {
    Connection buildConnection() throws ClassNotFoundException, SQLException;

    void setCustomProperties(Properties customProperties);

}
