/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain.transaction;

/**
 *
 * @author drola
 */
public class CategoryM {
    protected int id;
    protected String name;

    public CategoryM(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryM(int id) {
         this.id = id;
    }

    public CategoryM() {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    //for new flete
     @Override
    public String toString() {
        // Esto es lo que aparecerá escrito en el combo
        return this.name;
    }
    
}
