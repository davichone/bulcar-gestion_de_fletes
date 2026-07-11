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
public class Income extends Transaction{
    private int idLoad;

    public Income( int id, int idLoad, double monto, String descripcion, LocalDate date, CategoryM category) {
        super(monto, descripcion, date, category, id);
        this.idLoad=idLoad;
    }

  
    public Income(){}

    public int getIdLoad() {
        return idLoad;
    }

    public void setIdLoad(int idLoad) {
        this.idLoad = idLoad;
    }
    
    
    
    
   
    
}
