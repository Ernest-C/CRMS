
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


// Handles database operations
class CarRentalDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/carrentaldb"; // Update as per your setup
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "undeuxtrois123"; // Update with your MySQL password

    private Connection connection;

    public CarRentalDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


