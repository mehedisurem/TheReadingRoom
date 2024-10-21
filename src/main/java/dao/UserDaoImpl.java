package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;
public class UserDaoImpl implements UserDao {
    private final String TABLE_NAME = "users";
    public UserDaoImpl() {
    }

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (username VARCHAR(10) NOT NULL,"
                    + "password VARCHAR(8) NOT NULL,"+"fname VARCHAR(10) NOT NULL,"+"lname VARCHAR(10) NOT NULL," + "PRIMARY KEY (username))";
            stmt.executeUpdate(sql);
        }
    }

    @Override
    public User getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFname(rs.getString("fname"));
                    user.setLname(rs.getString("lname"));
                    return user;
                }
                return null;
            }
        }
    }

    @Override
    public User createUser(String username, String password, String fname, String lname) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?,?,?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fname);
            stmt.setString(4, lname);

            stmt.executeUpdate();
            return new User(username, password,fname,lname);
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET password = ?, fname = ?, lname = ? WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getFname());
            stmt.setString(3, user.getLname());
            stmt.setString(4, user.getUsername());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }
    }
}
