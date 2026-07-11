/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain.transaction;

/**
 *
 * @author drola
 */

public class ExpenseCreator extends TransactionCreator {
    @Override
    public Transaction createTransaction() {
        return new Expense();
    }
}
