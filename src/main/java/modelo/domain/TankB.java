/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

/**
 *
 * @author drola
 */
public class TankB {
    private int id;
    private Vehicle vehicle;
    private Customer customer;
    private Load load;
    private LoadDetail loadDetail;
    private String status;

    public TankB(int id, Vehicle vehicle, Customer customer, Load load, LoadDetail loadDetail, String status) {
        this.id = id;
        this.vehicle = vehicle;
        this.customer = customer;
        this.load = load;
        this.loadDetail = loadDetail;
        this.status = status;
    }

    public TankB() {
    }
   


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load = load;
    }

    public LoadDetail getLoadDetail() {
        return this.loadDetail;
    }

    public void setLoadDetail(LoadDetail loadDetail) {
        this.loadDetail = loadDetail;
    }
    
    
}
