package xezz.org.dbunit.export;


import org.apache.commons.cli.*;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import xezz.org.dbunit.export.connection.ConnectionFactory;
import xezz.org.dbunit.export.options.PresetOption;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author bkoch
 * @since 14.06.2013
 */
public class App {

    public static void main(String[] args) {
        // Apache commons CLI
        // Create the available options that can be given to the command line
        Options options = createOptions();

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
             cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("db-export", options, true);
            System.exit(-15);
        }

        final Option[] cmdOptions = cmd.getOptions();
        final String optionValue = cmd.getOptionValue(PresetOption.DATABASE.getValue());
        for (Option o : cmdOptions) {
            System.out.println(o.getOpt() + " : " + o.getValue());
        }
        if (cmd.hasOption(PresetOption.DATABASE.getValue())) {
            System.out.println("option db has been set: " + cmd.getOptionValue(PresetOption.DATABASE.getValue()));

        } else {
            System.out.println("No db option was given");
        }
        //System.exit(0);


        // database connection
        try {
            // This would be the driver to use with hsqldb
            //Class driverClass = Class.forName("org.hsqldb.jdbcDriver");
            Class driverClass = Class.forName("org.postgresql.Driver");

            // this would be the connection for hsql
            //Connection jdbcConnection = DriverManager.getConnection("jdbc:hsqldb:sample", "sa", "");
            final String hostName = "localhost";
            final String schemaName = "timeregdb";
            final String url = "jdbc:postgresql://" + hostName + "/" + schemaName;

            final String userName = "timereg";
            final String password = "ec109w";

            final Properties props = new Properties();
            props.setProperty("user", userName);
            props.setProperty("password", password);

            //Connection jdbcConnection = DriverManager.getConnection(url, props);
            IDatabaseConnection connection = new DatabaseConnection(ConnectionFactory.getConnection(cmd));
            // full database export
            IDataSet fullDataSet = connection.createDataSet();
            FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

            // partial database export
            /*
            QueryDataSet partialDataSet = new QueryDataSet(connection);
            partialDataSet.addTable("FOO", "SELECT * FROM TABLE WHERE COL='VALUE'");
            partialDataSet.addTable("BAR");
            FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));
            */



            // dependent tables database export: export table X and all tables that
            // have a PK which is a FK on X, in the right order for insertion
            /*String[] depTableNames = TablesDependencyHelper.getAllDependentTables(connection, "X");
            IDataSet depDataSet = connection.createDataSet( depTableNames );
            FlatXmlDataSet.write(depDataSet, new FileOutputStream("dependents.xml"));
            */
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-2);
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
            System.exit(-3);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-4);
        }

    }

    private static Options createOptions() {
        Options options = new Options();
        //Options that will be able to be parsed
        Option helpOption = new Option(PresetOption.HELP.getValue(), "show this help");
        Option dbOption = OptionBuilder.isRequired().withArgName(PresetOption.DATABASE.getValue()).hasArg().withDescription("Database type").create(PresetOption.DATABASE.getValue());
        Option userOption = OptionBuilder.isRequired().withArgName(PresetOption.USERNAME.getValue()).hasArg().withDescription("Username to authenticate with").create(PresetOption.USERNAME.getValue());
        Option passwordOption = OptionBuilder.isRequired().withArgName(PresetOption.PASSWORD.getValue()).hasArg().withDescription("Password to authenticate with").create(PresetOption.PASSWORD.getValue());
        Option hostnameOption = OptionBuilder.isRequired().withArgName(PresetOption.HOSTNAME.getValue()).hasArg().withDescription("Hostname/IP-Address of the database").create(PresetOption.HOSTNAME.getValue());
        Option schemaOption = OptionBuilder.isRequired().withArgName(PresetOption.SCHEMA.getValue()).hasArg().withDescription("Schema to use").create(PresetOption.SCHEMA.getValue());

        // add those options to the options collection
        options.addOption(helpOption);
        options.addOption(dbOption);
        options.addOption(userOption);
        options.addOption(passwordOption);
        options.addOption(hostnameOption);
        options.addOption(schemaOption);
        return options;
    }
}
