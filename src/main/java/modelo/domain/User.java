/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

/**
 *
 * @author drola
 */
public class User {
    private int id;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private boolean active; // En booleanos es común usar adjetivos

    // Constructor vacío
    public User() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    // Para los booleanos, Java suele usar "is" en lugar de "get"
    public boolean isActive() { return active; } 
    public void setActive(boolean active) { this.active = active; }
}
