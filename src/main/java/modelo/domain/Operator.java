/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

import java.time.LocalDate;

/**
 *
 * @author drola
 */
public class Operator {
    private int id;
    private String fullname;
    private String phonenumber;
    private String dni;
    private LocalDate entryDate;
    private LocalDate birthday;
    private String category;

    
    //constructor 

    public Operator(int id, String fullname, String phonenumber, String dni, LocalDate entryDate, LocalDate birthday, String category) {
        this.id = id;
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.dni = dni;
        this.entryDate = entryDate;
        this.birthday = birthday;
        this.category = category;
    }

   

    public Operator() {
    }
    
    
    
    public String getCategory() {
        return category;
    }

    //get and set
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    //override for new flete
    
    @Override
    public String toString() {
        // Esto es lo que aparecerá escrito en el combo
        return this.fullname;
    }
    
}
