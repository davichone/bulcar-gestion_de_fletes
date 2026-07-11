/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

/**
 *
 * @author drola
 */
public class LoadDetail {
    private int id;
    private int idProduct;
    private double quantity;
    private int compartment;
    private String nameProduct;
    private double capacityBox;
    
    
    //constructor generic
    
     public LoadDetail(int id, int idProduct,double quantity) {
        this.id = id;
        this.idProduct = idProduct;
        this.quantity=quantity;
    }

    public LoadDetail(int idproduct, double quantity) {
        this.idProduct = idproduct;
        this.quantity = quantity;
    }

    public LoadDetail() {
      
    }
    
    public String getNameProduct() {
        return nameProduct;
    }    
    
    public double getCapacityBox() {
        return capacityBox;
    }

    //get and set
    public void setCapacityBox(double capacityBox) {    
        this.capacityBox = capacityBox;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getidProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getCompartment() {
        return compartment;
    }

    public void setCompartment(int compartment) {
        this.compartment = compartment;
    }

   
    
    
    
    
    
    

   
}
