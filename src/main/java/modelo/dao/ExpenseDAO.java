/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import java.util.List;
import modelo.domain.transaction.Expense;
import modelo.dto.FinanceOperatorDTO;

/**
 *
 * @author drola
 */
public interface ExpenseDAO extends CRUD<Expense> {

    double getImportTotal(int id) throws SQLException;

    double getImportTotal() throws SQLException;
    boolean InsertFinanceOpe(FinanceOperatorDTO f)throws SQLException;
    List<Expense> listar(int idFleteCurrent)throws SQLException;
    List<FinanceOperatorDTO> listForOperatorManager(int id)throws SQLException;
    boolean UpdateWithOpe(FinanceOperatorDTO f)throws SQLException;
}
