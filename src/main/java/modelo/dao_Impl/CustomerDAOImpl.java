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
import modelo.dao.CustomerDAO;
import modelo.domain.Customer;
import modelo.domain.Operator;

/**
 *
 * @author drola
 */
public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public Customer searchForName(String placa) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean insert(Customer customer) {

        String sql = "INSERT INTO customers (dni, fullname, phone_num, name_eess, ruc_eess, address_eess) VALUES (?,?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, customer.getDni());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getNameES());
            ps.setString(5, customer.getRucES());
            ps.setString(6, customer.getAddressEs());
            System.err.println("successfully inserted"); 
            return ps.executeUpdate()>0;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new customer");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }

    }

    @Override
    public boolean modificar(Customer objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    
    // selected the customeres that not selected in the form
    @Override
    public List<Customer> listar() {
        List<Customer>list= new ArrayList<>();
        String sql = """
                     
                     SELECT id_customer, dni ,fullname, phone_num, name_eess, ruc_eess, address_eess FROM customers
                     
                     EXCEPT
                     
                     -- Clientes con fletes en proceso
                     SELECT c.id_customer, c.dni ,c.fullname, c.phone_num, c.name_eess, c.ruc_eess, c.address_eess 
                     FROM customers AS c
                     INNER JOIN loads AS l ON c.id_customer = l.id_customer
                     INNER JOIN fletes AS f ON l.id_flete = f.id_flete
                     WHERE f.status = 'En proceso';
                     
                     """;

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()){
                Customer cus = new Customer();
                cus.setId(rs.getInt("id_customer"));
                cus.setDni(rs.getString("dni"));
                cus.setName(rs.getString("fullname"));
                cus.setPhoneNumber(rs.getString("phone_num"));
                cus.setNameES(rs.getString("name_eess"));
                cus.setRucES(rs.getString("ruc_eess"));
                cus.setAddressEs(rs.getString("address_eess"));
                System.err.println(cus.getName());
                
                list.add(cus);
            }
            
            
            System.err.println("successfully select -> list of Customers"); 
            return list;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error select customerr");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    @Override
    public Customer buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Customer> list() throws SQLException {
         List<Customer>list= new ArrayList<>();
        String sql ="SELECT id_customer, dni ,fullname, phone_num, name_eess, ruc_eess, address_eess FROM customers";
                    

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()){
                Customer cus = new Customer();
                cus.setId(rs.getInt("id_customer"));
                cus.setDni(rs.getString("dni"));
                cus.setName(rs.getString("fullname"));
                cus.setPhoneNumber(rs.getString("phone_num"));
                cus.setNameES(rs.getString("name_eess"));
                cus.setRucES(rs.getString("ruc_eess"));
                cus.setAddressEs(rs.getString("address_eess"));
                list.add(cus);
            }
            
            
            System.err.println("successfully select -> list of Customers"); 
            return list;
            
               
        } 
    }

}
