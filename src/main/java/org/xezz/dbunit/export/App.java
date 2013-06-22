package org.xezz.dbunit.export;


import org.apache.commons.cli.*;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xezz.dbunit.export.connection.ConnectionFactory;
import org.xezz.dbunit.export.options.PresetOption;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author bkoch
 * @since 14.06.2013
 */
public class App {
    final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        LOGGER.warn("Started db-export");
        // Apache commons CLI
        // Create the available options that can be given to the command line

        Options options = createOptions();
        try {

            CommandLineParser parser = new BasicParser();
            CommandLine cmd = null;
            LOGGER.info("Done building CLI stuff");
            cmd = parser.parse(options, args);
            LOGGER.info("Done parsing CLI");
            // This would be the driver to use with hsqldb
            // Keeping here for further reference
            //Class driverClass = Class.forName("org.hsqldb.jdbcDriver");


            // this would be the connection for hsql
            //Connection jdbcConnection = DriverManager.getConnection("jdbc:hsqldb:sample", "sa", "");

            //Connection jdbcConnection = DriverManager.getConnection(url, props);
            IDatabaseConnection connection = ConnectionFactory.getConnection(cmd);

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
        } catch (ParseException e) {
            LOGGER.info("Malformed commandline input: " + e.getMessage());
            new HelpFormatter().printHelp("db-export", options, true);
        } catch (ClassNotFoundException cNFE) {
            LOGGER.warn("Database Driver was not found: " + cNFE.getMessage());
        } catch (SQLException sqlE) {
            LOGGER.warn("SQL Failure: ", sqlE);
            sqlE.printStackTrace();
        } catch (DatabaseUnitException dbUE) {
            LOGGER.warn("DBUnit failed: ", dbUE);
        } catch (IOException ioe) {
            LOGGER.warn("IO Operation failed, see log for more detailed error message. Simple cause: " + ioe.getMessage());
            LOGGER.info("IO failure:", ioe);
        } catch (Exception e) {
            LOGGER.warn("MASSIVE FAIL: ", e);
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
