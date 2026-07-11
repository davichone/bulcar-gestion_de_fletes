/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import java.util.List;
import modelo.domain.transaction.Expense;
import modelo.domain.transaction.Income;
import modelo.dto.DebtDTO;

/**
 *
 * @author drola
 */
public interface IncomeDAO extends CRUD<Income>{
    
    double getCalculusAmountByID(int idLoad) throws SQLException;
    double getImportTotal(int id) throws SQLException;
    double getImportTotal() throws SQLException;
    double getImportPending(int id) throws SQLException;
    List<Income> listar(int idFleteCurrent)throws SQLException;
    List<DebtDTO> listNotPayed()throws SQLException;
    
}
