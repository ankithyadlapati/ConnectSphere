package database;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:connectsphere.db";
    private static Connection conn = null;

    public static void connect() {
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Database connected!");
            createTables();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTables() throws SQLException {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "bio TEXT," +
                "avatar TEXT" +
                ");";

        String postsTable = "CREATE TABLE IF NOT EXISTS posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "content TEXT," +
                "likes INTEGER DEFAULT 0," +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");";

        Statement stmt = conn.createStatement();
        stmt.execute(usersTable);
        stmt.execute(postsTable);
    }

    public static Connection getConnection() {
        return conn;
    }
}
