/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.dao_Impl.IncomeDAOImpl;
import modelo.domain.transaction.Income;
import modelo.dto.DebtDTO;

/**
 *
 * @author drola
 */
public class IncomeController {

    IncomeDAOImpl dao = new IncomeDAOImpl();

    //insertar ingreso del la carga, del flete actual
    public boolean insertIncome(Income i) {
        if (i.getAmount() > 0) {
            return dao.insert(i);
        }

        return false;
    }

    //get total amount of flete by id
    public double getTotalAmount(int id) {
        // 1. Validamos la entrada antes de ir a la BD
        if (id <= 0) {
            return 0.0;
        }

        try {
            // 2. Llamamos al DAO y retornamos el valor directamente
            return dao.getImportTotal(id);
        } catch (SQLException ex) {
            // 3. ¡Nunca dejes un catch vacío! 
            // Imprime el error para poder arreglarlo si algo falla
            System.err.println("Error en Controller [Total Flete]: " + ex.getMessage());
            return 0.0;
        }
    }
    
    
    
    
    //get total amount of total loads
    public double getTotalAmount() {   
        
        try {
            // 2. Llamamos al DAO y retornamos el valor directamente
            return dao.getImportTotal();
        } catch (SQLException ex) {
            // 3. ¡Nunca dejes un catch vacío! 
            // Imprime el error para poder arreglarlo si algo falla
            System.err.println("Error en Controller [Total Flete]: " + ex.getMessage());
            return 0.0;
        }
    }
    
    //get import pending payment 
    public double getImportPending(int id) {
        // 1. Validamos la entrada antes de ir a la BD
        if (id <= 0) {
            return 0.0;
        }

        try {
            // 2. Llamamos al DAO y retornamos el valor directamente
            return dao.getImportPending(id);
        } catch (SQLException ex) {
            // 3. ¡Nunca dejes un catch vacío! 
            // Imprime el error para poder arreglarlo si algo falla
            System.err.println("Error en Controller [Total Flete]: " + ex.getMessage());
            return 0.0;
        }
    }
    
    
    //list incomes by month
    public List<Income> getlist() {   
        List<Income> l= new ArrayList<>();
        //  Llamamos al DAO y retornamos el valor
        l= dao.listar();
        return l;
    }
    
    // list of customers that not payed
    
     public List<DebtDTO> getlistNotPayed() {   
        List<DebtDTO> l= new ArrayList<>();
        //  Llamamos al DAO y retornamos el valor
        l= dao.listNotPayed();
        return l;
    }
    
    public List<Income> getlist(int id) {
        List<Income> l = new ArrayList<>();
        // 1. Validamos la entrada antes de ir a la BD
        System.err.println(id);
        if (id <= 0) {
            return l;
        }

        //  Llamamos al DAO y retornamos el valor
        l = dao.listar(id);
        return l;
    }
    
    //get total abonos of load
    public double getTotalAbonos(int id) {
        // 1. Validamos la entrada antes de ir a la BD
        if (id <= 0) {
            return 0.0;
        }

        try {
            // 2. Llamamos al DAO y retornamos el valor directamente
            return dao.getCalculusAmountByID(id);
        } catch (SQLException ex) {
            // 3. ¡Nunca dejes un catch vacío! 
            // Imprime el error para poder arreglarlo si algo falla
            System.err.println("Error en Controller [Total Flete]: " + ex.getMessage());
            return 0.0;
        }
    }

}
