package xezz.org.dbunit.export.connection;

import org.apache.commons.cli.CommandLine;
import xezz.org.dbunit.export.options.PresetOption;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Xezz
 * Date: 22.06.13
 * Time: 18:13
 */
public class ConnectionFactory {

    private static final List<String> POSTGRESQL_NAMES = new ArrayList<String>();
    private ConnectionFactory() {}

    {
        POSTGRESQL_NAMES.add("postgresql");
        POSTGRESQL_NAMES.add("postgres");
        POSTGRESQL_NAMES.add("postgre");
    }

    public static Connection getConnection(CommandLine cmd) throws SQLException, ClassNotFoundException {
        final String databaseOption = cmd.getOptionValue(PresetOption.DATABASE.getValue());

        if (databaseOption == null) {
            throw new IllegalArgumentException("databaseOption null is not allowed");
        }

        return buildConnector(databaseOption, cmd).buildConnection();
    }

    private static DatabaseConnector buildConnector(final String database, final CommandLine cmd) {
        if (POSTGRESQL_NAMES.contains(database.toLowerCase())) {
            return buildPostgresConnector(cmd);
        }
        throw new UnsupportedOperationException("UnsupportedDatabase: " + database);
    }

    private static DatabaseConnector buildPostgresConnector(final CommandLine cmd) {
        final String hostname = cmd.getOptionValue(PresetOption.HOSTNAME.getValue());
        final String password = cmd.getOptionValue(PresetOption.PASSWORD.getValue());
        final String schema = cmd.getOptionValue(PresetOption.SCHEMA.getValue());
        final String username = cmd.getOptionValue(PresetOption.USERNAME.getValue());
        DatabaseConnector postgresConnector = new PostgresConnector(hostname, password, schema, username);

        return postgresConnector;
    }

}
