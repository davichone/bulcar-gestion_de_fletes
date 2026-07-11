/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.dao_Impl.ExpenseDAOImpl;
import modelo.domain.transaction.Expense;
import modelo.dto.FinanceOperatorDTO;

/**
 *
 * @author drola
 */
public class ExpenseController {

    private ExpenseDAOImpl dao = new ExpenseDAOImpl();

    public boolean insertExpenseToDB(Expense e) {
        if (e.getIdFlete() > 0) {
            return dao.insert(e);
        }
        return false;
    }
    
    //with id_operator
    
    public boolean insertWithOperator(FinanceOperatorDTO f) {
        if (f.getOperator().getId()> 0) {
            return dao.InsertFinanceOpe(f);
        }
        return false;
    }
    
    public boolean updateWithOperator(FinanceOperatorDTO f) {
        if (f.getOperator().getId()> 0) {
            return dao.UpdateWithOpe(f);
        }
        return false;
    }

    //by id FLETE CURRENT
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

    //total global
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

    public List<Expense> getlist() {
        List<Expense> l = new ArrayList<>();
        //  Llamamos al DAO y retornamos el valor
        l = dao.listar();
        return l;
    }

    public List<Expense> getlist(int id) {
        List<Expense> l = new ArrayList<>();
        // 1. Validamos la entrada antes de ir a la BD
        System.err.println(id);
        if (id <= 0) {
            return l;
        }

        //  Llamamos al DAO y retornamos el valor
        l = dao.listar(id);
        return l;
    }
    
    public List<FinanceOperatorDTO> getlistForOperator(int id) {
        List<FinanceOperatorDTO> l = new ArrayList<>();
        //  Llamamos al DAO y retornamos el valor
        l = dao.listForOperatorManager(id);
        return l;
    }
    
}
