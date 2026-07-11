/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

/**
 *
 * @author drola
 */
public class Product {
    
    private int id;
    private String name;
    
    //cconstructor

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    //set and get

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
    
    
}
