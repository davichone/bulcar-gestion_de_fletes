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
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BadgeEstadoRenderer extends JPanel implements TableCellRenderer {
    
    private JLabel badge; // Esta será nuestra cajita de color

    public BadgeEstadoRenderer() {
        // Usamos GridBagLayout para que el badge quede perfectamente centrado
        setLayout(new GridBagLayout()); 
        
        badge = new JLabel();
        badge.setOpaque(true); // Necesario para pintar su propio fondo
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setHorizontalAlignment(JLabel.CENTER);
        
        // Le damos padding (margen interno) para que el color no quede pegado a las letras
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8)); 
        
        add(badge); // Metemos el badge dentro de nuestro panel
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // 1. Color de fondo de la celda (el panel contenedor)
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(Color.WHITE);
        }
        
        // 2. Mantenemos las líneas de la cuadrícula
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, table.getGridColor()));

        // 3. Configurar el Badge (La cajita de color)
        if (value != null) {
            String estado = value.toString().toUpperCase();
            badge.setText(estado);
            
            // Pintamos la cajita según el estado
            switch (estado) {
                case "COMPLETADO":
                    badge.setBackground(new Color(42, 122, 42)); // Verde
                    badge.setForeground(Color.WHITE);
                    break;
                case "EN PROCESO":
                    badge.setBackground(new Color(200, 160, 0)); // Dorado
                    badge.setForeground(Color.WHITE);
                    break;
                case "CANCELADO":
                    badge.setBackground(new Color(200, 64, 64)); // Rojo
                    badge.setForeground(Color.WHITE);
                    break;
                default:
                    badge.setBackground(Color.GRAY);
                    badge.setForeground(Color.WHITE);
                    break;
            }
        } else {
            badge.setText("");
            badge.setBackground(getBackground()); // Ocultar si está vacío
        }

        return this; // Devolvemos el panel contenedor, no la etiqueta suelta
    }
}