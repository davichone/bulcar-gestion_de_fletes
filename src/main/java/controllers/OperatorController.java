/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.dao.OperatorDAO;
import modelo.dao_Impl.OperatorDAOImpl;
import modelo.domain.Operator;

/**
 *
 * @author drola
 */
public class OperatorController {
    
    OperatorDAO dao = new OperatorDAOImpl();
    
    
    //new customer to dataBase
    public boolean registerCustomer(Operator op){
        //validacion de datos
        if(op.getDni().isEmpty() || op.getFullname().isEmpty() || op.getPhonenumber().isEmpty()){
            JOptionPane.showMessageDialog(null, "Dni y telefono obligatorios");
            return false;
        }
        
        dao.insert(op);
        JOptionPane.showMessageDialog(null, "Operador registrado exitosamente");
        return true;
    }
    
    //list operator for new FLete
    public List<Operator> getListOpTanks(){
        
        List<Operator>list = dao.getLowDataList();
        List<Operator>listOpTanks = new ArrayList<>();
        for(Operator op : list){
            if(op.getCategory().toLowerCase().contains("cisterna")){
                listOpTanks.add(op);
            }
        }
        return listOpTanks;
        
    }
}
