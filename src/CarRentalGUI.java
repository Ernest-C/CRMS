import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import org.json.JSONObject;
import java.io.*;

public class CarRentalGUI {
    private CarRentalDatabase database;
    
    public CarRentalGUI(CarRentalDatabase database) {
        this.database = database;
    }

    public void showMainMenu() {
        JFrame frame = new JFrame("Car Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));  // Increased rows by 1 for the button

        JButton viewCarsButton = new JButton("View All Cars");
        JButton addCarButton = new JButton("Add New Car");
        JButton registerCustomerButton = new JButton("Register Customer");
        JButton rentCarButton = new JButton("Rent a Car");
        JButton returnCarButton = new JButton("Return a Car");
        JButton exitButton = new JButton("Exit");
        JButton generateReportButton = new JButton("Generate Report");

        viewCarsButton.addActionListener(e -> viewAllCars());
        addCarButton.addActionListener(e -> addNewCar());
        registerCustomerButton.addActionListener(e -> registerCustomer());
        rentCarButton.addActionListener(e -> rentCar());
        returnCarButton.addActionListener(e -> returnCar());
        exitButton.addActionListener(e -> System.exit(0));

        // ActionListener for the Generate Report Button
        generateReportButton.addActionListener(e -> generateReport());

        panel.add(viewCarsButton);
        panel.add(addCarButton);
        panel.add(registerCustomerButton);
        panel.add(rentCarButton);
        panel.add(returnCarButton);
        panel.add(generateReportButton);  // Add the report button here
        panel.add(exitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void generateReport() {
        // Prompt the user for the report type and output file
        JTextField reportTypeField = new JTextField(10);
        JTextField outputFileField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Report Type (text/pdf):"));
        panel.add(reportTypeField);
        panel.add(new JLabel("Output File Name:"));
        panel.add(outputFileField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Generate Report", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String reportType = reportTypeField.getText().trim();
            String outputFile = outputFileField.getText().trim();

            // Validate the inputs
            if (reportType.isEmpty() || outputFile.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both report type and output file name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Make the POST request to the Python API
            try {
                String apiUrl = "http://localhost:5000/generate_report";  // Python API URL

                // Prepare the request data
                JSONObject jsonData = new JSONObject();
                jsonData.put("report_type", reportType);
                jsonData.put("output_file", outputFile);

                // Make the POST request
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonData.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String responseLine;
                    StringBuilder response = new StringBuilder();
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JOptionPane.showMessageDialog(null, "Report generated successfully: " + outputFile);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error generating report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Other methods like viewAllCars, addNewCar, registerCustomer, etc. remain unchanged


     

    private void viewAllCars() {
        try {
            String query = "SELECT * FROM cars";
            Statement stmt = database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder carsList = new StringBuilder();
            while (rs.next()) {
                carsList.append(String.format("Car ID: %d, Make: %s, Model: %s, Year: %d, Daily Rate: %.2f, Available: %b\n",
                        rs.getInt("car_id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("daily_rate"), rs.getBoolean("is_available")));
            }
            JOptionPane.showMessageDialog(null, carsList.toString(), "All Cars", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving cars: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewCar() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField dailyRateField = new JTextField();

        panel.add(new JLabel("Make:"));
        panel.add(makeField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Daily Rate:"));
        panel.add(dailyRateField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String query = "INSERT INTO cars (make, model, year, daily_rate, is_available) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = database.getConnection().prepareStatement(query);
                pstmt.setString(1, makeField.getText());
                pstmt.setString(2, modelField.getText());
                pstmt.setInt(3, Integer.parseInt(yearField.getText()));
                pstmt.setDouble(4, Double.parseDouble(dailyRateField.getText()));
                pstmt.setBoolean(5, true);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Car added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error adding car: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerCustomer() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Register Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String query = "INSERT INTO customers (first_name, last_name, phone_number, email) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = database.getConnection().prepareStatement(query);
                pstmt.setString(1, firstNameField.getText());
                pstmt.setString(2, lastNameField.getText());
                pstmt.setString(3, phoneField.getText());
                pstmt.setString(4, emailField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Customer registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error registering customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rentCar() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField customerIdField = new JTextField();
        JTextField carIdField = new JTextField();
        JTextField rentalDateField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);
        panel.add(new JLabel("Car ID:"));
        panel.add(carIdField);
        panel.add(new JLabel("Rental Date (YYYY-MM-DD):"));
        panel.add(rentalDateField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Rent a Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection conn = database.getConnection();
                conn.setAutoCommit(false);

                String rentQuery = "INSERT INTO rentals (customer_id, car_id, rental_date) VALUES (?, ?, ?)";
                String updateCarQuery = "UPDATE cars SET is_available = ? WHERE car_id = ?";

                PreparedStatement rentPstmt = conn.prepareStatement(rentQuery);
                rentPstmt.setInt(1, Integer.parseInt(customerIdField.getText()));
                rentPstmt.setInt(2, Integer.parseInt(carIdField.getText()));
                rentPstmt.setString(3, rentalDateField.getText());
                rentPstmt.executeUpdate();

                PreparedStatement updateCarPstmt = conn.prepareStatement(updateCarQuery);
                updateCarPstmt.setBoolean(1, false);
                updateCarPstmt.setInt(2, Integer.parseInt(carIdField.getText()));
                updateCarPstmt.executeUpdate();

                conn.commit(); //COmmit
                JOptionPane.showMessageDialog(null, "Car rented successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | NumberFormatException e) {
                try {
                    database.getConnection().rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Error renting car: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void returnCar() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField rentalIdField = new JTextField();
        JTextField returnDateField = new JTextField();

                panel.add(new JLabel("Rental ID:"));
        panel.add(rentalIdField);
        panel.add(new JLabel("Return Date (YYYY-MM-DD):"));
        panel.add(returnDateField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Return a Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection conn = database.getConnection();
                conn.setAutoCommit(false);

                // Update the rental record
                String returnQuery = "UPDATE rentals SET return_date = ? WHERE rental_id = ?";
                PreparedStatement returnPstmt = conn.prepareStatement(returnQuery);
                returnPstmt.setString(1, returnDateField.getText());
                returnPstmt.setInt(2, Integer.parseInt(rentalIdField.getText()));
                returnPstmt.executeUpdate();

                // Mark the car as available
                String getCarIdQuery = "SELECT car_id FROM rentals WHERE rental_id = ?";
                PreparedStatement getCarIdPstmt = conn.prepareStatement(getCarIdQuery);
                getCarIdPstmt.setInt(1, Integer.parseInt(rentalIdField.getText()));
                ResultSet rs = getCarIdPstmt.executeQuery();

                if (rs.next()) {
                    int carId = rs.getInt("car_id");
                    String updateCarQuery = "UPDATE cars SET is_available = ? WHERE car_id = ?";
                    PreparedStatement updateCarPstmt = conn.prepareStatement(updateCarQuery);
                    updateCarPstmt.setBoolean(1, true);
                    updateCarPstmt.setInt(2, carId);
                    updateCarPstmt.executeUpdate();
                }

                conn.commit();
                JOptionPane.showMessageDialog(null, "Car returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | NumberFormatException e) {
                try {
                    database.getConnection().rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Error returning car: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}