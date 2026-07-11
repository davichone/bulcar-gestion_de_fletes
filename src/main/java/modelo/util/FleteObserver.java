/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util;

/**
 *
 * @author drola
 */
public interface FleteObserver {
    /**
     * Método invocado cuando ocurre un cambio en los fletes (creación, edición, cierre).
     */
    void onFleteChanged();
}
