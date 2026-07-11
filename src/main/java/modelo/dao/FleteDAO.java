/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import java.util.List;
import modelo.domain.Flete;
import modelo.dto.DocFleteDTO;
import modelo.dto.SummaryFlete;

/**
 *
 * @author drola
 */
public interface FleteDAO extends CRUD<Flete>{
    
     List<Flete> getAllFletesInProcess();
     boolean closeFleteCurrent(Flete f);
     DocFleteDTO getLastDoc()throws SQLException;
     List<SummaryFlete> getList() throws SQLException;
     boolean deleted(int id) throws SQLException;
    
}
