/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

/**
 *
 * @author drola
 */
public class LoadDetailDTO {
    
    private int id;
    private int idProduct;
    private String productName;
    private double volume;

    public LoadDetailDTO(int id, int idProduct, String productName, double volume) {
        this.id = id;
        this.idProduct = idProduct;
        this.productName = productName;
        this.volume = volume;
    }

    public LoadDetailDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double quantity) {
        this.volume = quantity;
    }
    
    

    @Override
    public String toString() {
        return productName + " (" + volume + ")";
    }
}
