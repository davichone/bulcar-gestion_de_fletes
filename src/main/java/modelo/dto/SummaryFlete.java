/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.time.LocalDate;
import modelo.domain.Flete;
import modelo.domain.Operator;
import modelo.domain.Plant;
import modelo.domain.Vehicle;

/**
 *
 * @author drola
 */
public class SummaryFlete extends Flete{
   private int KmTraveled;
   private double incomes;
   private double expenses;
   private double profit;
   private double performance;
   private int sizeLoads;
   private double volumeTotal;

    public SummaryFlete(int KmTraveled, double incomes, double expenses, double profit, double performance, LocalDate starDate, Plant plantFuel, Vehicle Truck, Operator drive, String status, String numDoc) {
        super(starDate, plantFuel, Truck, drive, status, numDoc);
        this.KmTraveled = KmTraveled;
        this.incomes = incomes;
        this.expenses = expenses;
        this.profit = profit;
        this.performance = performance;
    }

    public SummaryFlete() {
    }

    public int getSizeLoads() {
        return sizeLoads;
    }

    public void setSizeLoads(int sizeLoads) {
        this.sizeLoads = sizeLoads;
    }

    public double getVolumeTotal() {
        return volumeTotal;
    }

    public void setVolumeTotal(double volumeTotal) {
        this.volumeTotal = volumeTotal;
    }



    public int getKmTraveled() {
        return KmTraveled;
    }

    public void setKmTraveled(int KmTraveled) {
        this.KmTraveled = KmTraveled;
    }

    public double getIncomes() {
        return incomes;
    }

    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }
   
   
}
