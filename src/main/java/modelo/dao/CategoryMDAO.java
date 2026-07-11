/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import java.util.List;
import modelo.domain.transaction.CategoryM;

/**
 *
 * @author drola
 */
public interface CategoryMDAO extends CRUD<CategoryM>{
    List<CategoryM>listExpense() throws SQLException;
     List<CategoryM>listIncomes() throws SQLException;
}
