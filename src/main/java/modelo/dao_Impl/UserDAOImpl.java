/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao_Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import modelo.domain.User;
import modelo.dao.UserDAO;
import modelo.util.conexion.ConexionBD;

/**
 *
 * @author drola
 */
public class UserDAOImpl implements UserDAO{

    @Override
    public boolean insert(Object objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(Object objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Object> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User login(String username, String password)  {
        User user = null;
        
        // La consulta SQL sigue usando los nombres de tu tabla en Neon
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = true";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    
                    // rs.get("columna_db") -> setVariableIngles()
                    user.setId(rs.getInt("id"));
                    user.setFullName(rs.getString("fullname"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("rol"));
                    user.setActive(rs.getBoolean("status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in UserDAO login: " + e.getMessage());
        }
        
        return user;
    }
    
}
