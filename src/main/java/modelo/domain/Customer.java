/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

/**
 *
 * @author drola
 */
public class Customer {
    
    private int id;
    private String dni;
    private String name;
    private String phoneNumber;
    private String nameES;
    private String rucES;
    private String addressEs;
    
    
    //construsctor

    public Customer(int id, String dni, String name, String phoneNumber, String nameES, String rucES, String addressEs) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nameES = nameES;
        this.rucES = rucES;
        this.addressEs = addressEs;
    }

    public Customer() {
        
    }

    
    
    // get and set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNameES() {
        return nameES;
    }

    public void setNameES(String nameES) {
        this.nameES = nameES;
    }

    public String getRucES() {
        return rucES;
    }

    public void setRucES(String rucES) {
        this.rucES = rucES;
    }

    public String getAddressEs() {
        return addressEs;
    }

    public void setAddressEs(String addressEs) {
        this.addressEs = addressEs;
    }
    
    
    //for new flete
     @Override
    public String toString() {
        // Esto es lo que aparecerá escrito en el combo
        return this.name;
    }
    
    
}
