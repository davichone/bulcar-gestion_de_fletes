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
import modelo.dao.PlantDAO;
import modelo.domain.Operator;
import modelo.domain.Plant;

/**
 *
 * @author drola
 */
public class PlantDAOImpl implements PlantDAO{

    @Override
    public boolean insert(Plant objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(Plant objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Plant> listar() {
        List<Plant>list= new ArrayList<>();
        
        String sql = "SELECT id_plant, name_plant FROM plants";

        try (Connection con = ConexionBD.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()){
                Plant plant = new Plant();
                plant.setId(rs.getInt("id_plant"));
                plant.setName(rs.getString("name_plant"));
               
                
                list.add(plant);
            }
            
            
            System.err.println("successfully select -> list of plants"); 
            return list;
            
               
        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error select plants");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    @Override
    public Plant buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
