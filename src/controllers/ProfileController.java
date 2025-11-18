package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.User;
import java.sql.*;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label bioLabel;
    @FXML private ImageView avatarView;
    @FXML private Label followersLabel;
    @FXML private Label followingLabel;
    @FXML private TextArea editBio;
    @FXML private Button saveBioButton;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        loadProfile();
    }

    private void loadProfile() {
        usernameLabel.setText(currentUser.getUsername());
        bioLabel.setText(currentUser.getBio() != null ? currentUser.getBio() : "No bio yet");
        followersLabel.setText("Followers: " + getFollowersCount());
        followingLabel.setText("Following: " + getFollowingCount());

        if(currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
            avatarView.setImage(new Image(currentUser.getAvatar()));
        }
    }

    private int getFollowersCount() {
        try {
            Connection conn = database.Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM users WHERE ? IN (SELECT following FROM users WHERE id = users.id)");
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt("count");
        } catch(SQLException e) { return 0; }
    }

    private int getFollowingCount() {
        try {
            Connection conn = database.Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM users WHERE id = ?");
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            return rs.getInt("count");
        } catch(SQLException e) { return 0; }
    }

    @FXML
    private void saveBio() {
        String newBio = editBio.getText().trim();
        if(newBio.isEmpty()) return;

        try {
            Connection conn = database.Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE users SET bio = ? WHERE id = ?");
            stmt.setString(1, newBio);
            stmt.setInt(2, currentUser.getId());
            stmt.executeUpdate();
            currentUser.setBio(newBio);
            bioLabel.setText(newBio);
            editBio.clear();
        } catch(SQLException e) {
            System.out.println("Error updating bio: " + e.getMessage());
        }
    }
}
