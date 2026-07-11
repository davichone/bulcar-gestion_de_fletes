/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

import java.util.List;

/**
 *
 * @author drola
 */
public class Load {
    private int id;    
    private double amount;
    private Flete flete;
    private Customer customer;
    private List<LoadDetail> listProducts;
    
    //constructor

    public Load(int id, double amount, Flete flete, Customer customer,List<LoadDetail> listProducts) {
        this.id = id;
        this.amount = amount;
        this.flete = flete;
        this.customer = customer;
        this.listProducts=listProducts;
    }

    public Load() {
        
    }
    
    //get and set

    public List<LoadDetail> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<LoadDetail> listProducts) {
        this.listProducts = listProducts;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Flete getFlete() {
        return flete;
    }

    public void setFlete(Flete flete) {
        this.flete = flete;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
    
}
