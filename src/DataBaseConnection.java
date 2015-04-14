import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.Scanner;

/**
 * Connectivity layer between application and database to allow us to easily
 * send data back and forth
 * @author	Jack Evans
 */
public class DataBaseConnection {

	// JDBC driver name and database URL
	static final String DB_URL = "jdbc:mysql://localhost/";
	static final String PIMDB_URL = "jdbc:mysql://localhost/PIMDataBase";

	// Change this to whatever you had on your local machines
	private static final String USER = "root";
	private static final String PASS = "root";
	public Connection MysqlConnection = null;
	public Connection PIMConnection = null;

	/**
	 * Connects Java to the MySql Databsae
	 */
	public void makeConnection() {
		Boolean DBExists = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			MysqlConnection = DriverManager.getConnection(DB_URL, USER, PASS);
			// Connection connection = <your java.sql.Connection>
			ResultSet resultSet = MysqlConnection.getMetaData().getCatalogs();
			// Loop through results set of database names to check if the database already exists
			while (resultSet.next()) {
				// Get the database name, which is at position 1
				String databaseName = resultSet.getString(1);
				if (databaseName.equals("PIMDataBase")) {
					DBExists = true;
				}
			}
			resultSet.close();
			// If the database doesn't exist locally then we need to create it
			if (!DBExists) createDataBase();

            // Connect to the new Database
            PIMConnection = DriverManager.getConnection(PIMDB_URL, USER, PASS);
            System.out.println("Connected to Database");

			// Detect is the Database is empty in order to fill in with test data
			if (!getTableData("CONTACTS").next() && !getTableData("APPOINTMENTS").next() && !getTableData("NOTES").next()) {
				System.out.println("Database is empty");
				// Call method to populate Database with test data
				populateDataBase();
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Creates the DataBase if it doesn't already exist
	 */
	public void createDataBase() {
		try {
			// Create the Database
			Statement stmt = MysqlConnection.createStatement();
			stmt.executeUpdate("CREATE DATABASE PIMDataBase");
			stmt.close();

			// Make a connection to the newly created DataBase
			PIMConnection = DriverManager.getConnection(PIMDB_URL, USER, PASS);

			// Set up ScriptRunner and Reader to read the .sql files
			String currentDir = System.getProperty("user.dir");
			String script = currentDir + "/CreateDBTables.sql";
			ScriptRunner scriptRunner = new ScriptRunner(PIMConnection);
			Reader reader = new BufferedReader(new FileReader(script));

			// Create Database tables by executing the sql script
			scriptRunner.runScript(reader);

			// Close script runner and reader
			scriptRunner.closeConnection();
			reader.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs a populate database script to fill the Database with sample data
	 */
	public void populateDataBase() {
		// Check with the user if they want to populate the Database with sample data
		Scanner input = new Scanner(System.in);
		System.out.println("Run population script [Y/n]?");
		String a = input.next();
		if (a.equalsIgnoreCase("y")) {

			// Create the Database tables by running an sql script
			String currentDir = System.getProperty("user.dir");
			String populationScript = currentDir + "/PopulateDBWithTestData.sql";

			// Initialise object for ScripRunner
			ScriptRunner sr = new ScriptRunner(PIMConnection);

			// Give the input file to Reader
			try (Reader reader = new BufferedReader(new FileReader(populationScript))) {
				// Execute the script to populate the DataBase
				sr.runScript(reader);
			} catch (IOException e) {
				System.out.println("Unable to execute populate script");
			}
		} else System.out.println("Population script was not run");
	}

	/**
	 * Returns a results set of all the rows form a given table
	 */
	public ResultSet getTableData(String tableName) {
		try {
			Statement stmt = PIMConnection.createStatement();
			return stmt.executeQuery("SELECT * FROM " + tableName);
		} catch (SQLException e) {
			System.out.println("Unable to connect to Database");
			return null;
		}
	}

	public void addToDatabase(String[] row) {
		for(String item: row) {
			System.out.println("Adding: " + item);
		}
	}
}