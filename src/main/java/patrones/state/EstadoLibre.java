/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones.state;

/**
 *
 * @author drola
 */


public class EstadoLibre implements EstadoCompartimiento {

    @Override
    public void procesarSeleccion(Compartimiento contexto) {
        // permite la selección y avisa al padre
        contexto.getBotonUI().setSelected(true);
        contexto.marcarComoSeleccionado(contexto.getId()); 
    }

    @Override
    public void actualizarUI(Compartimiento contexto) {
        // enciende el botón limpia textos y el panel
        contexto.getBotonUI().setEnabled(true);
        contexto.getBotonUI().setToolTipText("Disponible");
        contexto.getLabelNombre().setText("---");
        contexto.getLabelVolumen().setText("0 gl");
        
        // pasa ceros para vaciar el gráfico del panel redondeado
        contexto.getPanelUI().actualizarEstadoCarga(0, 0, 0); 
    }
}
