/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.List;
import modelo.dao_Impl.CategoryMDAOImpl;
import modelo.domain.transaction.CategoryM;

/**
 *
 * @author drola
 */
public class CategoryMController {
    CategoryMDAOImpl dao = new CategoryMDAOImpl();
    
    public List<CategoryM> listExpense () throws SQLException{
        List<CategoryM>list= dao.listExpense();
        
        if (!list.isEmpty()) {
            return list;
        }else{
            return null;
        }
    }
    
     public List<CategoryM> listIncomes () throws SQLException{
        List<CategoryM>list= dao.listIncomes();
        
        if (!list.isEmpty()) {
            return list;
        }else{
            return null;
        }
    }
}
