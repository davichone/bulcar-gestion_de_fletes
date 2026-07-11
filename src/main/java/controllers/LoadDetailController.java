/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import modelo.dao.LoadDetailsDAO;
import modelo.dao_Impl.LoadDetailDAOImpl;
import modelo.domain.LoadDetail;
import modelo.dto.LoadDetailDTO;

/**
 *
 * @author drola
 */
public class LoadDetailController {
    
    private final LoadDetailsDAO dao = new LoadDetailDAOImpl();
    
    public int insertLoadDetail(int idLoad, LoadDetail ld) {
        int exito = -1;
        
        System.err.println(idLoad);
        System.err.println(ld.getidProduct());
        System.err.println(ld.getQuantity());
        if(idLoad>0 && ld.getidProduct()>0 && ld.getQuantity()>0){
            
            
            exito = dao.insertLoadDetail(idLoad, ld);
           // System.err.println("controller ok");
        }else {
            //System.err.println("error controller");
        }
        
        return exito;
    }
    
    public boolean update(int idLoad, LoadDetail ld) throws SQLException{
        if(idLoad>0 && ld!=null){
            System.err.println("llego aca controller");
            return dao.update(idLoad, ld);
            
        }
        return false;
    }
    
    public boolean delete(LoadDetail ld)throws SQLException{
        if(ld.getId()>0){
            return dao.eliminar(ld);
        }
        return false;
    }
    
}
