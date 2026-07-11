/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author drola
 */

public class FleteSubject {
    // Singleton
    private static final FleteSubject instance = new FleteSubject();
    
    // Lista de observadores suscritos
    private final List<FleteObserver> observers = new ArrayList<>();
    private FleteSubject() {}
    public static FleteSubject getInstance() {
        return instance;
    }
    // Suscribirse
    public void addObserver(FleteObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    // Cancelar suscripción
    public void removeObserver(FleteObserver observer) {
        observers.remove(observer);
    }
    // Notificar a todos
    public void notifyObservers() {
        for (FleteObserver observer : observers) {
            observer.onFleteChanged();
        }
    }
}
