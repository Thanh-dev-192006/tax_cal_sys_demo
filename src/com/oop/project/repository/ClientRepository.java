package com.oop.project.repository;

import com.oop.project.model.Client;
import com.oop.project.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientRepository - Su dung MySQL thay vi file serialization
 */
public class ClientRepository {
    
    /**
     * Luu danh sach clients vao database
     */
    public void saveClients(List<Client> clients) {
        String sql = "INSERT INTO Clients (id, name, income, dependents, marital_status, email, phone_number, city) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "name = VALUES(name), " +
                    "income = VALUES(income), " +
                    "dependents = VALUES(dependents), " +
                    "marital_status = VALUES(marital_status), " +
                    "email = VALUES(email), " +
                    "phone_number = VALUES(phone_number), " +
                    "city = VALUES(city)";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Client client : clients) {
                stmt.setString(1, client.getId());
                stmt.setString(2, client.getName());
                stmt.setDouble(3, client.getIncome());
                stmt.setInt(4, client.getDependents());
                stmt.setString(5, client.getMaritalStatus());
                stmt.setString(6, client.getEmail());
                stmt.setString(7, client.getPhoneNumber());
                stmt.setString(8, client.getCity());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            System.out.println("Saved " + clients.size() + " clients to database");
            
        } catch (SQLException e) {
            System.err.println("Error saving clients: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load tat ca clients
     */
    public List<Client> loadClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Client client = new Client(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getDouble("income"),
                    rs.getInt("dependents"),
                    rs.getString("marital_status"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("city")
                );
                clients.add(client);
            }
            
            System.out.println("Loaded " + clients.size() + " clients from database");
            
        } catch (SQLException e) {
            System.err.println("Error loading clients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
    
    /**
     * Them 1 client moi
     */
    public void addClient(Client client) {
        String sql = "INSERT INTO Clients (id, name, income, dependents, marital_status, email, phone_number, city) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, client.getId());
            stmt.setString(2, client.getName());
            stmt.setDouble(3, client.getIncome());
            stmt.setInt(4, client.getDependents());
            stmt.setString(5, client.getMaritalStatus());
            stmt.setString(6, client.getEmail());
            stmt.setString(7, client.getPhoneNumber());
            stmt.setString(8, client.getCity());
            
            stmt.executeUpdate();
            System.out.println("Added client: " + client.getName());
            
        } catch (SQLException e) {
            System.err.println("Error adding client: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Update client
     */
    public void updateClient(Client client) {
        String sql = "UPDATE Clients SET name = ?, income = ?, dependents = ?, " +
                    "marital_status = ?, email = ?, phone_number = ?, city = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, client.getName());
            stmt.setDouble(2, client.getIncome());
            stmt.setInt(3, client.getDependents());
            stmt.setString(4, client.getMaritalStatus());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getPhoneNumber());
            stmt.setString(7, client.getCity());
            stmt.setString(8, client.getId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Updated client: " + client.getName());
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating client: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Xoa client
     */
    public void deleteClient(String clientId) {
        String sql = "DELETE FROM Clients WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, clientId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Deleted client: " + clientId);
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting client: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tim client theo ID
     */
    public Client findClientById(String id) {
        String sql = "SELECT * FROM Clients WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("income"),
                        rs.getInt("dependents"),
                        rs.getString("marital_status"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("city")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Tim clients theo ten
     */
    public List<Client> findClientsByName(String name) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("income"),
                        rs.getInt("dependents"),
                        rs.getString("marital_status"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("city")
                    );
                    clients.add(client);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching clients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
}