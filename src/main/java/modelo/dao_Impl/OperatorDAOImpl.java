/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao_Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.util.conexion.ConexionBD;
import modelo.dao.OperatorDAO;
import modelo.domain.Operator;
import modelo.domain.Vehicle;

/**
 *
 * @author drola
 */
public class OperatorDAOImpl implements OperatorDAO{

    @Override
    public boolean insert(Operator objeto) {
        String sql = "INSERT INTO operators (fullname, phone_num, birthday, entry_date, dni, category) VALUES (?,?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, objeto.getFullname());
            ps.setString(2, objeto.getPhonenumber());
            ps.setObject(3, objeto.getBirthday());
            ps.setObject(4, objeto.getEntryDate());
            ps.setString(5, objeto.getDni());
            ps.setString(6, objeto.getCategory());
            System.err.println("successfully inserted -> operator"); 
            return ps.executeUpdate()>0;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new operator");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }

    }

    @Override
    public boolean modificar(Operator objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Operator> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public Operator buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    @Override
    public List<Operator> getLowDataList() {
        List<Operator>listOperator= new ArrayList<>();
        String sql = "SELECT id_operator, fullname, category FROM operators";

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()){
                Operator op = new Operator();
                op.setId(rs.getInt("id_operator"));
                op.setFullname(rs.getString("fullname"));
                op.setCategory(rs.getString("category"));
                
                listOperator.add(op);
            }
            
            
            System.err.println("successfully select -> list of Operators for new FLETE"); 
            return listOperator;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error select operator");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }
    
}
