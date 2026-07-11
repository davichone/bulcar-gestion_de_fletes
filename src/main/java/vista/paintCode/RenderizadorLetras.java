/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.paintCode;

/**
 *
 * @author drola
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorLetras extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int modelColumn = table.convertColumnIndexToModel(column);

        // 1. FUENTES Y NEGRITAS
        if (modelColumn == 0 || modelColumn == 5) {
            c.setFont(new Font("SansSerif", Font.BOLD, 12)); 
        } else {
            c.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        // --- 2. ALINEACIÓN (¡Aquí está el cambio!) ---
        if (modelColumn >= 5 && modelColumn <= 7) {
            // Índices 5, 6 y 7 (Cargas, Volumen, Km) al CENTRO
            setHorizontalAlignment(SwingConstants.CENTER);
        } else if (modelColumn >= 8 && modelColumn <= 10) {
            // Índices 8, 9 y 10 (Dinero) a la DERECHA
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            // El resto (ID, Nombres, Fechas) a la IZQUIERDA
            setHorizontalAlignment(SwingConstants.LEFT);
        }
        // ----------------------------------------------

        // 3. COLOR POR DEFECTO
        c.setForeground(Color.BLACK);

        // 4. LÓGICA DE TEXTOS Y COLORES (S/, Km y Semáforo)
        if (value != null) {
            try {
                String textoNum = value.toString().replace("S/", "").replace("Km", "").replace(" ", "").replace(",", "").trim();
                double monto = Double.parseDouble(textoNum);

                if (modelColumn == 7) {
                    setText(textoNum + " Km");
                } else if (modelColumn >= 8 && modelColumn <= 10) {
                    setText("S/ " + String.format("%.2f", monto)); 
                }

                if (modelColumn == 8) { 
                    c.setForeground(new Color(0, 150, 0)); 
                } 
                else if (modelColumn == 10) { 
                    if (monto < 200) {
                        c.setForeground(new Color(200, 0, 0)); 
                    } else if (monto >= 200 && monto <= 600) {
                        c.setForeground(new Color(139, 69, 19)); 
                    } else {
                        c.setForeground(new Color(0, 150, 0)); 
                    }
                }
            } catch (Exception e) {
                // Si no es número, se queda normal
            }
        }

        // 5. SELECCIÓN
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(Color.WHITE);
        } else {
            c.setBackground(table.getBackground());
        }

        return c;
    }
}