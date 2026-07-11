/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import modelo.util.ConfigManager;
import vista.forms.Loginform;

/**
 *
 * @author drola
 */
public class Principal {

    public static void main(String[] args) {
        //con esta linea de code inicio todo =) .
        System.out.println("Hello World by David Rolando");

        //iniciar logger
        ConfigManager.iniciarLogger();

        //set color mode
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Error al iniciar FlatLaf");
        }

        // open form "Login"
        java.awt.EventQueue.invokeLater(() -> {
            new Loginform().setVisible(true);
        });
        
        
    }

}
