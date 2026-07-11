/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;



/**
 *
 * @author drola
 */
public class Vehicle {

    private int id;
    private int km;
    private String manufacturer; //scania
    private String model;
    private String plate;
    private int purchas_date;
    private String fuel;
    private int max_capacity;
    private String type;

    //costructor
    public Vehicle(int id, int km, String manufacturer, String model, String plate, int purchas_date, String fuel, int max_capacity, String type) {
        this.id = id;
        this.km = km;
        this.manufacturer = manufacturer;
        this.model = model;
        this.plate = plate;
        this.purchas_date = purchas_date;
        this.fuel = fuel;
        this.max_capacity = max_capacity;
        this.type = type;
    }

    public Vehicle() {
    }

    //get and set
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }
    
    

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getPurchas_date() {
        return purchas_date;
    }

    public void setPurchas_date(int purchas_date) {
        this.purchas_date = purchas_date;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    //for see only the name
    @Override
    public String toString() {
        // Esto es lo que aparecerá escrito en el combo
        return this.manufacturer + " " + this.model + "";
    }

}
