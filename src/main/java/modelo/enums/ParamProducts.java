/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enums;

/**
 *
 * @author David Rolando
 */
public enum ParamProducts {
    idDB5(1),
    idGPremium(2),
    idGRegular(3);
    
    private final int id;

    private ParamProducts(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

        



    
    

}
