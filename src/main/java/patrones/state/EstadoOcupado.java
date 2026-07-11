/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones.state;

/**
 *
 * @author drola
 */

public class EstadoOcupado implements EstadoCompartimiento {
    private String nombreCliente;
    private double galones;
    private double capacidadTotal;
    private int idProducto;

    public EstadoOcupado(String cliente, double galones, double capacidadTotal, int idProducto) {
        this.nombreCliente = cliente;
        this.galones = galones;
        this.capacidadTotal = capacidadTotal;
        this.idProducto = idProducto;
    }

    @Override
    public void procesarSeleccion(Compartimiento contexto) {
        // Bloquea cualquier intento de selección
        contexto.getBotonUI().setSelected(false);
        contexto.getBotonUI().setEnabled(false);
    }

    @Override
    public void actualizarUI(Compartimiento contexto) {
        // Apaga el botón y llena los datos visuales
        contexto.getBotonUI().setEnabled(false);
        contexto.getBotonUI().setToolTipText("Ocupado por " + nombreCliente);
        contexto.getLabelNombre().setText(nombreCliente);
        contexto.getLabelVolumen().setText(galones + " gl");
        
        // Pinta el panel con los datos reales
        contexto.getPanelUI().actualizarEstadoCarga(galones, capacidadTotal, idProducto); 
    }
}