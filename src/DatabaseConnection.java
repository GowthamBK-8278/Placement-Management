package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Manages the JDBC connection to the MySQL database.
 * Handles configuration loading, saving, and dynamic configuration on failure.
 */
public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "db.properties";
    private static String url;
    private static String user;
    private static String password;
    private static Connection connection = null;

    static {
        loadProperties();
    }

    /**
     * Loads database properties from db.properties.
     */
    private static void loadProperties() {
        Properties props = new Properties();
        File file = new File(PROPERTIES_FILE);
        
        // Fallback checks for different execution environments (VS Code debugger, different working dirs)
        if (!file.exists()) {
            file = new File("d:/Java Mini project/Placement-Management/" + PROPERTIES_FILE);
        }
        if (!file.exists()) {
            file = new File("../" + PROPERTIES_FILE);
        }
        
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
                url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/placement_db");
                user = props.getProperty("db.user", "root");
                password = props.getProperty("db.password", "");
            } catch (IOException e) {
                System.err.println("Warning: Could not read " + PROPERTIES_FILE + ". Using defaults.");
                setDefaultCredentials();
            }
        } else {
            setDefaultCredentials();
        }
    }

    private static void setDefaultCredentials() {
        url = "jdbc:mysql://localhost:3306/placement_db";
        user = "root";
        password = "";
    }

    /**
     * Saves the provided database properties to db.properties.
     */
    private static void saveProperties(String dbUrl, String dbUser, String dbPass) {
        Properties props = new Properties();
        props.setProperty("db.url", dbUrl);
        props.setProperty("db.user", dbUser);
        props.setProperty("db.password", dbPass);

        try (FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE)) {
            props.store(fos, "Database Configuration");
            url = dbUrl;
            user = dbUser;
            password = dbPass;
            System.out.println("Database configuration updated and saved to " + PROPERTIES_FILE);
        } catch (IOException e) {
            System.err.println("Error saving connection properties: " + e.getMessage());
        }
    }

    /**
     * Returns a valid database connection, loading driver if necessary.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Explicitly register the driver (required for some older setups)
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found in classpath. Ensure the JAR is added to lib/.", e);
            }
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    /**
     * Test connection. Returns true if successful, false otherwise.
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Interactively prompts the user for connection credentials if the default connection fails.
     */
    public static void initializeConnectionWithRetry(Scanner scanner) {
        System.out.println("Connecting to database at: " + url + " as user: " + user + "...");
        if (testConnection()) {
            System.out.println("Successfully connected to MySQL database!");
            return;
        }

        System.out.println("\n[!] Connection failed using settings in db.properties.");
        System.out.println("Let's configure your database credentials. Make sure MySQL Server is running.");

        while (true) {
            System.out.print("Enter MySQL host (default: localhost): ");
            String host = scanner.nextLine().trim();
            if (host.isEmpty()) host = "localhost";

            System.out.print("Enter MySQL port (default: 3306): ");
            String port = scanner.nextLine().trim();
            if (port.isEmpty()) port = "3306";

            System.out.print("Enter MySQL username (default: root): ");
            String dbUser = scanner.nextLine().trim();
            if (dbUser.isEmpty()) dbUser = "root";

            System.out.print("Enter MySQL password: ");
            String dbPass = scanner.nextLine(); // Password can be empty

            String testUrl = "jdbc:mysql://" + host + ":" + port + "/placement_db?useSSL=false&allowPublicKeyRetrieval=true";

            System.out.println("Testing connection...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection conn = DriverManager.getConnection(testUrl, dbUser, dbPass)) {
                    if (conn != null) {
                        System.out.println("\nConnection Successful!");
                        saveProperties(testUrl, dbUser, dbPass);
                        return;
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Error: MySQL JDBC Driver not found. Put it in lib/ folder.");
                return;
            } catch (SQLException e) {
                System.err.println("Connection failed: " + e.getMessage());
                System.out.println("Please check your credentials and make sure the database 'placement_db' exists.");
                System.out.print("Would you like to try again? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    System.out.println("Proceeding with unconfigured settings. Database operations may fail.");
                    return;
                }
            }
        }
    }
}
