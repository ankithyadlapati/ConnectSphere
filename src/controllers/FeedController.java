package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Post;
import models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedController {

    @FXML private VBox postsContainer;
    @FXML private TextArea postContent;
    @FXML private Button postButton;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        loadPosts();
    }

    // Load posts from database
    private void loadPosts() {
        postsContainer.getChildren().clear();
        try {
            Connection conn = database.Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT posts.id, posts.content, posts.likes, users.username FROM posts JOIN users ON posts.user_id = users.id ORDER BY posts.id DESC");

            while(rs.next()) {
                Label content = new Label(rs.getString("username") + ": " + rs.getString("content") + " (Likes: " + rs.getInt("likes") + ")");
                Button likeButton = new Button("Like");
                int postId = rs.getInt("id");

                likeButton.setOnAction(e -> likePost(postId));
                VBox postBox = new VBox(content, likeButton);
                postBox.setSpacing(5);
                postBox.setStyle("-fx-border-color: gray; -fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-border-radius: 5px;");
                postsContainer.getChildren().add(postBox);
            }

        } catch (SQLException e) {
            System.out.println("Error loading posts: " + e.getMessage());
        }
    }

    // Like post in database
    private void likePost(int postId) {
        try {
            Connection conn = database.Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE posts SET likes = likes + 1 WHERE id = ?");
            stmt.setInt(1, postId);
            stmt.executeUpdate();
            loadPosts(); // Refresh posts
        } catch(SQLException e) {
            System.out.println("Error liking post: " + e.getMessage());
        }
    }

    // Create new post
    @FXML
    private void createPost() {
        String content = postContent.getText().trim();
        if(content.isEmpty()) return;

        try {
            Connection conn = database.Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO posts(user_id, content) VALUES(?, ?)");
            stmt.setInt(1, currentUser.getId());
            stmt.setString(2, content);
            stmt.executeUpdate();
            postContent.clear();
            loadPosts();
        } catch(SQLException e) {
            System.out.println("Error creating post: " + e.getMessage());
        }
    }
}
