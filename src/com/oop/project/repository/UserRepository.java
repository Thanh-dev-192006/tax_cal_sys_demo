package com.oop.project.repository;

import com.oop.project.model.User;
import com.oop.project.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository - Su dung MySQL thay vi file serialization
 */
public class UserRepository {
    
    /**
     * Luu danh sach users vao database
     * @param users Danh sach users can luu
     */
    public void saveUsers(List<User> users) {
        String sql = "INSERT INTO Users (staff_id, username, password, role, full_name, email, phone_number) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "password = VALUES(password), " +
                    "role = VALUES(role), " +
                    "full_name = VALUES(full_name), " +
                    "email = VALUES(email), " +
                    "phone_number = VALUES(phone_number)";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (User user : users) {
                stmt.setString(1, user.getStaffId());
                stmt.setString(2, user.getUsername());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getRole());
                stmt.setString(5, user.getFullName());
                stmt.setString(6, user.getEmail());
                stmt.setString(7, user.getPhoneNumber());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            System.out.println("Saved " + users.size() + " users to database");
            
        } catch (SQLException e) {
            System.err.println("Error saving users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load tat ca users tu database
     * @return Danh sach users
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY staff_id";
        
        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("staff_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone_number")
                );
                users.add(user);
            }
            
            System.out.println("Loaded " + users.size() + " users from database");
            
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
        
        return users;
    }
    
    /**
     * Tim user theo username
     * @param username Username can tim
     * @return User neu tim thay, null neu khong
     */
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("staff_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Them 1 user moi
     * @param user User can them
     */
    public void addUser(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        saveUsers(users);
    }
    
    /**
     * Xoa user theo username
     * @param username Username can xoa
     */
    public void deleteUser(String username) {
        String sql = "DELETE FROM Users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Deleted user: " + username);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}