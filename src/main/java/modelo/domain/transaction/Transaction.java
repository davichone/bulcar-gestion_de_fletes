/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain.transaction;

import java.time.LocalDate;

/**
 *
 * @author drola
 */
public abstract class Transaction {

    protected int id;
    protected double amount;
    protected String description;
    protected LocalDate date;
    protected CategoryM category;

    public Transaction(double monto, String descripcion, LocalDate date, CategoryM category, int id) {
        this.amount = monto;
        this.description = descripcion;
        this.date = date;
        this.category = category;
        this.id = id;
    }
    
    public Transaction(){}
    
    // Método abstracto que cada hijo implementará a su manera
    //ublic abstract void mostrarDetalle();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CategoryM getCategory() {
        return category;
    }

    public void setCategory(CategoryM category) {
        this.category = category;
    }
}
