/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import modelo.domain.Flete;
import modelo.domain.Load;
import modelo.dto.LoadDTO;

/**
 *
 * @author drola
 */
public interface LoadDAO extends CRUD<Load> {

    int insertLoadOfFleteCurrent(Load load, Flete flete, Connection con) throws SQLException;

    public List<LoadDTO> getLoadsWithDetails(int idFlete) throws SQLException;
    public List<LoadDTO> getLoadsWithDetails(String num_doc) throws SQLException;
    public List<LoadDTO> getLoadsWithDetailsCust(int id_customer) throws SQLException;

    double getAmountById (int id) throws SQLException;
    
    boolean update (Load l) throws SQLException;
}
