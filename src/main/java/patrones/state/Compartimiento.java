/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones.state;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import vista.forms.minDialogs.TankCompartments;
import vista.paintCode.PanelRedondeado;

/**
 *
 * @author drola
 */
//el contexto

public class Compartimiento {

    private int id;
    private EstadoCompartimiento estadoActual;

    private JRadioButton botonUI;
    private PanelRedondeado panelUI;
    private JLabel labelNombre;
    private JLabel labelVolumen;
    private TankCompartments ventanaPadre;

    public Compartimiento(int id, JRadioButton boton, PanelRedondeado panel, JLabel nombre, JLabel volumen, TankCompartments padre) {
        this.id = id;
        this.botonUI = boton;
        this.panelUI = panel;
        this.labelNombre = nombre;
        this.labelVolumen = volumen;
        this.ventanaPadre = padre;

        // Todo compartimiento nace libre por defecto
        this.estadoActual = new EstadoLibre();

        // Delegar el evento clic al patrón State (Reemplaza los action verbosos de Swing)
        this.botonUI.addActionListener(e -> hacerClick());
    }

    
    public void setEstado(EstadoCompartimiento nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.estadoActual.actualizarUI(this);
    }

    public void hacerClick() {
        estadoActual.procesarSeleccion(this);
    }

    public void marcarComoSeleccionado(int id) {
        ventanaPadre.setCompartmentSelected(id); // Envía el dato a la ventana padre
    }

    public int getId() {
        return id;
    }

    public JRadioButton getBotonUI() {
        return botonUI;
    }

    public PanelRedondeado getPanelUI() {
        return panelUI;
    }

    public JLabel getLabelNombre() {
        return labelNombre;
    }

    public JLabel getLabelVolumen() {
        return labelVolumen;
    }
}
