package xezz.org.dbunit.export;


import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

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
        // database connection
        try {
            // This would be the driver to use with hsqldb
            //Class driverClass = Class.forName("org.hsqldb.jdbcDriver");
            Class driverClass = Class.forName("org.postgresql.Driver");

            // this would be the connection for hsql
            //Connection jdbcConnection = DriverManager.getConnection("jdbc:hsqldb:sample", "sa", "");
            final String hostName = "localhost";
            final String schemaName = "schema";
            final String url = "jdbc:postgresql://" + hostName + "/" + schemaName;

            final String userName = "your username here";
            final String password = "your password here";

            final Properties props = new Properties();
            props.setProperty("user", userName);
            props.setProperty("password", password);

            Connection jdbcConnection = DriverManager.getConnection(url, props);
            IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
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
}
