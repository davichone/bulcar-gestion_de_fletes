/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import modelo.domain.Vehicle;

/**
 *
 * @author drola
 */
public interface VehicleDAO extends CRUD<Vehicle>{
    
    boolean updateKm(Vehicle v) throws SQLException;
    
    
    
}
