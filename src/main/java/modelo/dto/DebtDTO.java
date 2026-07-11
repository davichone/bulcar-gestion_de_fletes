/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.time.LocalDate;

/**
 *
 * @author drola
 */
public class DebtDTO {
    private int idLoad;
    private String nameCustomer;
    private double priceFlete;
    private double abono;
    private double balance;
    private double volume;
    private LocalDate date;

    public DebtDTO() {
    }

    public DebtDTO(int idLoad, String nameCustomer, double priceFlete, double abono, double balance, double volume, LocalDate date) {
        this.idLoad = idLoad;
        this.nameCustomer = nameCustomer;
        this.priceFlete = priceFlete;
        this.abono = abono;
        this.balance = balance;
        this.volume = volume;
        this.date = date;
    }

    

    public double getVolumen() {
        return volume;
    }

    public void setVolumen(double volumen) {
        this.volume = volumen;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    
    
    
    
    public int getIdLoad() {
        return idLoad;
    }

    public void setIdLoad(int id) {
        this.idLoad = id;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public double getPriceFlete() {
        return priceFlete;
    }

    public void setPriceFlete(double priceFlete) {
        this.priceFlete = priceFlete;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    
    
}
