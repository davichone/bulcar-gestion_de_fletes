/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain.transaction;

import java.time.LocalDate;

/**
 *
 * @author drola
 */
public class Expense extends Transaction{
    private int idFlete;

    public Expense( int id, int idFlete, double monto, String descripcion, LocalDate date, CategoryM category) {
        super(monto, descripcion, date, category, id);
        this.idFlete=idFlete;
    }

  
    public Expense(){}

    public int getIdFlete() {
        return idFlete;
    }

    public void setIdFlete(int idLoad) {
        this.idFlete = idLoad;
    }
    
    
    
    
   
    
}
