package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import models.User;
import java.sql.*;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            Connection conn = database.Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                messageLabel.setText("Login successful! Welcome, " + rs.getString("username"));
                // Open feed window here
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        } catch(SQLException e) {
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}
