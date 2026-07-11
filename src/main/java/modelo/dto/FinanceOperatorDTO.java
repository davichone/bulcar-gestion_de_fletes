/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.time.LocalDate;
import modelo.domain.Operator;
import modelo.domain.transaction.CategoryM;
import modelo.domain.transaction.Expense;

/**
 *
 * @author drola
 */
public class FinanceOperatorDTO extends Expense{
    private Operator operator;

    public FinanceOperatorDTO(Operator operator, int id, int idFlete, double monto, String descripcion, LocalDate date, CategoryM category) {
        super(id, idFlete, monto, descripcion, date, category);
        this.operator = operator;
    }

    public FinanceOperatorDTO(Operator operator) {
        this.operator = operator;
    }

    public FinanceOperatorDTO() {
        
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
    
    
    
    
    
}
