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
import modelo.dao.VehicleDAO;
import modelo.domain.Vehicle;

/**
 *
 * @author drola
 */
public class VehicleDAOImpl implements VehicleDAO{

    @Override
    public boolean insert(Vehicle objeto) {
        String sql = "INSERT INTO vehicles (km, manufacturer, model, plate, purchase_date, fuel, max_capacity, type_vehicle) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, objeto.getKm());
            ps.setString(2, objeto.getManufacturer());
            ps.setString(3, objeto.getModel());
            ps.setString(4, objeto.getPlate());
            ps.setInt(5, objeto.getPurchas_date());
            ps.setString(6, objeto.getFuel());
            ps.setInt(7, objeto.getMax_capacity());
            ps.setString(8,objeto.getType());
            
            
            System.err.println("successfully inserted -> vehicle"); 
            return ps.executeUpdate()>0;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new vehicle");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar(Vehicle objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Vehicle> listar() {
        
        List<Vehicle>listVehicles= new ArrayList<>();
        String sql = "SELECT id_vehicle, km, manufacturer, model, plate, purchase_date, fuel, max_capacity, type_vehicle FROM vehicles";

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()){
                Vehicle ve = new Vehicle();
                ve.setFuel(rs.getString("fuel"));
                ve.setId(rs.getInt("id_vehicle"));
                ve.setKm(rs.getInt("km"));
                ve.setManufacturer(rs.getString("manufacturer"));
                ve.setMax_capacity(rs.getInt("max_capacity"));
                ve.setModel(rs.getString("model"));
                ve.setPlate(rs.getString("plate"));
                ve.setPurchas_date(rs.getInt("purchase_date"));
                ve.setType(rs.getString("type_vehicle"));
                listVehicles.add(ve);
            }
            
            
            System.err.println("successfully select -> list of vehicles"); 
            return listVehicles;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new vehicle");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    @Override
    public Vehicle buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateKm(Vehicle v) throws SQLException {
        String sql = "UPDATE vehicles SET km = ? WHERE id_vehicle =?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, v.getKm());
            ps.setInt(2, v.getId());
            System.err.println("update successfuly");
            return ps.executeUpdate() >0;
        }
        //return false; la excepcion sube al controller
    }
    
}
