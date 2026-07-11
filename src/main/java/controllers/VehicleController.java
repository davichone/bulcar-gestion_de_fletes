/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.dao.VehicleDAO;
import modelo.dao_Impl.VehicleDAOImpl;

import modelo.domain.Vehicle;

/**
 *
 * @author drola
 */
public class VehicleController {
    
    
     private final VehicleDAO dao = new VehicleDAOImpl();
    
    public boolean registerVehicle(Vehicle ve){
        //validacion de datos
        if(ve.getKm()==0 || ve.getPurchas_date()==0 || ve.getMax_capacity()==0){
            JOptionPane.showMessageDialog(null, "Km y Max. capacidad obligatorios");
            return false;
        }
        return dao.insert(ve);
        
        
    }
    
    
    //get list of tanks truck (fuel)
    public List<Vehicle> getlistTank(){
        List<Vehicle>listVe =dao.listar();
        List<Vehicle>tanks = new ArrayList<>();
        
        for (Vehicle vehicle : listVe) {
            if(vehicle.getType().equals("Cisterna"))
                tanks.add(vehicle);
        }
        
        System.err.println("successfully select -> list of vehicles -> tanks");
        return tanks;
    }
    
    
    public boolean updateKm(Vehicle v) throws SQLException{
        if(v.getId()<0 ||v.getKm()<0){
            return false;
        }
        return dao.updateKm(v);
    }
}
