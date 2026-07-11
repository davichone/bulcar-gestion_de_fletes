/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import java.util.List;
import modelo.dao.PlantDAO;
import modelo.dao_Impl.PlantDAOImpl;
import modelo.domain.Plant;

/**
 *
 * @author drola
 */
public class PlantController {
    
    PlantDAO dao = new PlantDAOImpl();
    
    public List<Plant> getListForNewFlete(){
        List<Plant>list = dao.listar();
        List<Plant>listReady = new ArrayList<>();
        for (Plant plant : list) {
            if (!plant.getName().isEmpty()) {
                listReady.add(plant);                
            }
            
        }
        return listReady;
    }
    
}
