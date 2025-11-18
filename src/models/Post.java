package models;

public class Post {
    private int id;
    private int userId;
    private String content;
    private int likes;

    public Post(int userId, String content) {
        this.userId = userId;
        this.content = content;
        this.likes = 0;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getContent() { return content; }
    public int getLikes() { return likes; }
    public void like() { likes++; }
}
