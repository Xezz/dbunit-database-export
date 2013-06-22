package xezz.org.dbunit.export.connection;

import org.apache.commons.cli.CommandLine;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import xezz.org.dbunit.export.options.PresetOption;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Xezz
 * Date: 22.06.13
 * Time: 18:13
 */
public class ConnectionFactory {

    /**
     * A lowercase list of possible names of the POSTGRESQL database
     */
    private static final List<String> POSTGRESQL_NAMES = new ArrayList<String>();

    private ConnectionFactory() {
    }

    static {
        POSTGRESQL_NAMES.add("postgresql");
        POSTGRESQL_NAMES.add("postgres");
        POSTGRESQL_NAMES.add("postgre");
    }

    /**
     * @param cmd
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws DatabaseUnitException
     */
    public static IDatabaseConnection getConnection(CommandLine cmd) throws SQLException, ClassNotFoundException, DatabaseUnitException {
        final String databaseOption = cmd.getOptionValue(PresetOption.DATABASE.getValue());

        if (databaseOption == null) {
            throw new IllegalArgumentException("databaseOption null is not allowed");
        }

        return buildIDatabaseConnection(databaseOption, cmd);
    }

    private static IDatabaseConnection buildIDatabaseConnection(final String database, final CommandLine cmd) throws DatabaseUnitException, SQLException, ClassNotFoundException {
        if (POSTGRESQL_NAMES.contains(database.toLowerCase())) {
            return buildPostgresConnector(cmd);
        }
        throw new UnsupportedOperationException("UnsupportedDatabase: " + database);
    }

    private static IDatabaseConnection buildPostgresConnector(final CommandLine cmd) throws SQLException, ClassNotFoundException, DatabaseUnitException {
        final String hostname = cmd.getOptionValue(PresetOption.HOSTNAME.getValue());
        final String password = cmd.getOptionValue(PresetOption.PASSWORD.getValue());
        final String schema = cmd.getOptionValue(PresetOption.SCHEMA.getValue());
        final String username = cmd.getOptionValue(PresetOption.USERNAME.getValue());
        DatabaseConnector postgresConnector = new PostgresConnector(hostname, password, schema, username);
        IDatabaseConnection connection = new DatabaseConnection(postgresConnector.buildConnection());
        connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
        return connection;
    }

}
