import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Main class to start the application
public class CarRentalSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarRentalDatabase database = new CarRentalDatabase();
            CarRentalGUI gui = new CarRentalGUI(database);
            gui.showMainMenu();
        });
    }
    
    
  

}
