/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

/**
 *
 * @author drola
 */
public class Plant {
    private int id;
    private String name;
    
    //constructor
    public Plant (int id, String name){
        this.id=id;
        this.name=name;
    }

    public Plant() {
        
    }

    
    //get and set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String  getName() {
        return name;
    }

    public void setName(String  name) {
        this.name = name;
    }
    
    
    //for insert to new flete
    
    @Override
    public String toString(){
        return this.getName();
    }
    
    
}
