package vista.paintCode;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author drola
 */
    
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;

public class BotonVerRenderer extends JPanel implements TableCellRenderer {
    private JButton button;

    public BotonVerRenderer() {
        // El GridBagLayout centra el contenido automáticamente
        setLayout(new GridBagLayout()); 
        
        button = new JButton("Ver");
        button.setFont(new Font("SansSerif", Font.PLAIN, 10));
        button.setBackground(new Color(232, 228, 224)); 
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        // ¡Aquí le damos el tamaño pequeñito y fijo!
        button.setPreferredSize(new Dimension(45, 20)); 
        button.setFocusPainted(false); // Quita el borde feo al hacer clic
        
        add(button); // Metemos el botón dentro del panel
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // El panel asume el color de la celda (blanco o azul de selección)
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(Color.WHITE); 
        }
        this.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, table.getGridColor()));
        return this; // Devolvemos el panel, no el botón
    }
}
