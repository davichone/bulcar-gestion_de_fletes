/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enums;

/**
 *
 * @author drola
 */
public enum ParamFlete {
    
    EN_PROCESO("En proceso"),
    FINALIZADO("Cerrado"),
    CANCELADO("Cancelado"),
    IDENTIFIER("F-"),
    PAGADO("Pagado"),
    PENDIENTE("Pendiente");
    
    
    private final String texto;

    ParamFlete(String texto) {
        this.texto = texto;
    }

    public String getText() {
        return texto;
    }
    
}
