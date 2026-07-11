/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones.state;

/**
 *
 * @author drola
 */
public interface EstadoCompartimiento {
    void procesarSeleccion(Compartimiento contexto);
    
    void actualizarUI(Compartimiento contexto);
}
