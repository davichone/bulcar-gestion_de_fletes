    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author drola
 */
public class Flete {
    
    private int id;
    private LocalDate starDate;
    private LocalDate endDate;
    private int kmEnd;
    private Plant plant;
    private Vehicle vehicle;
    private Operator operator;
    private String status;
    private String numDoc;
    private List<Load> listLoads;
    
    //constructor generic

    public Flete(int id, LocalDate starDate, LocalDate endDate, int kmEnd, Plant plant, Vehicle vehicle, Operator drive) {
        this.id = id;
        this.starDate = starDate;
        this.endDate = endDate;
        this.kmEnd = kmEnd;
        this.plant = plant;
        this.vehicle = vehicle;
        this.operator = drive;
    }
    
    //constructor for new flete for insert in dataBase

    public Flete(LocalDate starDate, Plant plantFuel, Vehicle Truck, Operator drive, String status, String numDoc) {
        this.starDate = starDate;
        this.plant = plantFuel;
        this.vehicle = Truck;
        this.operator = drive;
        this.status=status;
        this.numDoc=numDoc;
    }

    public Flete() {
        
    }
    
    
    
    //get ids (delegacion)
    
    
    //operator
    public int getIdOperator(){
       if(this.operator!= null){
        return operator.getId();
       }else{
           return -1;
       }
       
    }
    public void setIdOperator(int id){
        operator.setId(id);
       
    }
    
    public int getKmstart(){
        if(this.vehicle!= null){
        return vehicle.getKm();
       }else{
           return -1;
       }
    }
    
//     public void setKmstart(int km){  oculted for not delegation
//         this.vehicle.setKm(km);
//       
//    }
    
     //plant
    public int getIdPlant(){
        if(this.plant!= null){
        return plant.getId();
       }else{
           return -1;
       }
    }
    
     public void setIdPlant(int id){
        plant.setId(id);
       
    }
    
     
     //vehicle
    public int getIdVehicle(){
        if(this.vehicle!= null){
        return vehicle.getId();
       }else{
           return -1;
       }
    }
    
     public void setIdVehicle(int id){
        vehicle.setId(id);
       
    }
    
    //get and set

    public List<Load> getListLoads() {
        return listLoads;
    }

    public void setListLoads(List<Load> listLoads) {
        this.listLoads = listLoads;
    }
     
     
    

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
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

    public LocalDate getStarDate() {
        return starDate;
    }

    public void setStarDate(LocalDate starDate) {
        this.starDate = starDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getKmEnd() {
        return kmEnd;
    }

    public void setKmEnd(int kmEnd) {
        this.kmEnd = kmEnd;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plantFuel) {
        this.plant = plantFuel;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle Truck) {
        this.vehicle = Truck;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator drive) {
        this.operator = drive;
    }
    @Override
public String toString() {
    // Esto es lo que verá el usuario en el ComboBox
    return "Flete #" + this.id + " - " + this.vehicle.getManufacturer()+this.vehicle.getModel()+ " - " +this.vehicle.getPlate(); // Ajusta los nombres a tus variables
}
    
    
}
