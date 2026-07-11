/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.List;
import modelo.dao.FleteDAO;
import modelo.dao_Impl.FleteDAOImpl;
import modelo.domain.Flete;
import modelo.dto.DocFleteDTO;
import modelo.dto.SummaryFlete;

/**
 *
 * @author drola
 */
public class FleteController {
    
    private final FleteDAO dao = new FleteDAOImpl();
    
    public boolean registerFlete(Flete flete){
        if(flete.getStarDate() == null || 
        flete.getIdVehicle() <= 0 || 
        flete.getIdPlant() <= 0 || 
        flete.getIdOperator() <= 0 ||
        flete.getStatus().isEmpty()      ){
            return false;
        }
        
        
        return dao.insert(flete);
    }
    
    public List<Flete> getAllFletesInProcess() {
        List<Flete> lista = dao.getAllFletesInProcess();
        
        // Si la lista no es nula y tiene al menos 1 elemento, la devolvemos
        if (lista != null && !lista.isEmpty()) {
            return lista;
        }
        
        // Si no hay fletes, devolvemos null (o una lista vacía, ambas son válidas)
        return null;
    }
    
    
    public List<SummaryFlete> getList() throws SQLException{
        List<SummaryFlete> lista = dao.getList();
        
        // Si la lista no es nula y tiene al menos 1 elemento, la devolvemos
        if (lista != null && !lista.isEmpty()) {
            return lista;
        }
        
        // Si no hay fletes, devolvemos null (o una lista vacía, ambas son válidas)
        return null;
    }
    
    public boolean closedStatusFleteCurrent(Flete f){
        if(f.getStatus().equalsIgnoreCase("En proceso")){
            return dao.closeFleteCurrent(f);
        }
        return false;
    }
    
    public DocFleteDTO getLastDoc() throws SQLException{
       return dao.getLastDoc();
    }
    
    public boolean deleted(int id) throws SQLException{
        if(id<0){
            return false;
        }
        return dao.deleted(id);
    }
}
