/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.List;
import modelo.dao_Impl.TankBDAOImpl;
import modelo.domain.TankB;


/**
 *
 * @author drola
 */
public class TankBController {
               
    private final TankBDAOImpl dao = new TankBDAOImpl();

    
    
    public boolean insertTank_behavior(TankB tankB) {
        System.err.println("---validacion ");
        System.err.println(tankB.getVehicle().getId());
        System.err.println(tankB.getCustomer().getId());
        System.err.println(tankB.getLoadDetail().getQuantity());
        System.err.println(tankB.getLoadDetail().getidProduct());
        System.err.println(tankB.getLoad().getId());
        System.err.println(tankB.getLoadDetail().getId());
        System.err.println();
        
        if (tankB.getVehicle().getId() == 0
                || tankB.getCustomer().getId() == 0
                || tankB.getLoadDetail().getQuantity() == 0
                || tankB.getLoadDetail().getidProduct() == 0
                || tankB.getLoad().getId() == 0
                || tankB.getLoadDetail().getId() == 0) {
             System.err.println("aca llego");
            return false;
           
        }
        System.err.println("aca llego 2");
        return dao.insert(tankB);

    }
    
    public boolean reset() {
       return dao.reset();

    }
    
    public List<Integer> getFreeCompartments(){
        
        return dao.getFreeCompartments();
    }
    
    public List<TankB> getBusyList(){
         List<TankB> l = dao.listar();
//         for(TankB data : l){
//             if(data.getId()<0 || !data.getStatus().equalsIgnoreCase("LIBRE")) return null;
//         }
        return l;
    }
}
