/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author drola
 */
// Este es tu objeto Maestro
//entidad para las tardejas de loads con sus detalles
public class LoadDTO {

    private int idFlete;
    private int idLoad;
    private double amount;
    private double advance;
    private double balance;
    private String paymentStatus;
    private int idCustomer;
    private String nameCustomer;
    private String nameEESS;
    private String addressEESS;
    private LocalDate date;
    private int fletesDistincs;
    private String num_doc;
    
    // Lista inicializada para evitar NullPointerException
    private List<LoadDetailDTO> details = new ArrayList<>();

    
    
    public LoadDTO() {
    }
    
    
    
    //quantity especifica
    public double getVolumeof(String nameProduct) {
        for (LoadDetailDTO data : this.details){
            if(nameProduct.equalsIgnoreCase(data.getProductName()))
                return data.getVolume();
        }
        return 00;
    }
    
    //total quntity
    
    public double getTotalVolume(){
        double d =0;
         for (LoadDetailDTO data : this.details){
             
             
            d+=data.getVolume();
        }
        return d;
    }
    
    // get balance 
    public double getImportBalance(){
        return this.amount-this.advance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getFletesDistincs() {
        return fletesDistincs;
    }

    // Getters y Setters

    public String getNum_doc() {
        return num_doc;
    }

    public void setNum_doc(String num_doc) {
        this.num_doc = num_doc;
    }
    
    
    
    
    public void setFletesDistincs(int fletesDistincs) {
        this.fletesDistincs = fletesDistincs;
    }

    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    
    
    
    public int getIdFlete() {
        return idFlete;
    }

    public void setIdFlete(int idFlete) {
        this.idFlete = idFlete;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getNameEESS() {
        return nameEESS;
    }

    public void setNameEESS(String nameEESS) {
        this.nameEESS = nameEESS;
    }

    public String getAddressEESS() {
        return addressEESS;
    }

    public void setAddressEESS(String addressEESS) {
        this.addressEESS = addressEESS;
    }

    public int getIdLoad() {
        return idLoad;
    }

    public void setIdLoad(int idLoad) {
        this.idLoad = idLoad;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<LoadDetailDTO> getDetails() {
        return details;
    }

    // Método clave: Agrega el detalle a la lista interna
    public void addDetail(LoadDetailDTO detail) {
        if (this.details.size() < 3) {
            this.details.add(detail);
        }
    }

    @Override
    public String toString() {
        return "Load #" + idLoad + " [Detalles: " + details.size() + "]";
    }
}
