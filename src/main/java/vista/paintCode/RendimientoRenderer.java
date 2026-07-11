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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class RendimientoRenderer extends JPanel implements TableCellRenderer {
    
    private int porcentaje = 0;
    private boolean isSelected = false;

    public RendimientoRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        this.isSelected = isSelected;
        
        // Extraemos el número (por si viene con el símbolo % o es nulo)
        if (value != null) {
            try {
                String valStr = value.toString().replace("%", "").trim();
                porcentaje = (int) Math.round(Double.parseDouble(valStr));
            } catch (NumberFormatException e) {
                porcentaje = 0;
            }
        } else {
            porcentaje = 0;
        }

        // Fondo de la fila (azul si está seleccionada, blanco si no)
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(Color.WHITE);
        }
        this.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, table.getGridColor()));
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Suavizado de bordes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int height = getHeight();

        // 1. Configuración de la barra
        int barWidth = 40; // Ancho total de la barrita
        int barHeight = 10; // Grosor
        int barX = 10; // Margen izquierdo
        int barY = (height - barHeight) / 2; // Centrado vertical

        // 2. Dibujar el fondo gris de la barra
        g2.setColor(new Color(204, 204, 204)); // Fondo gris #ccc
        g2.fillRect(barX, barY, barWidth, barHeight);
        g2.setColor(new Color(153, 153, 153)); // Borde gris oscuro
        g2.drawRect(barX, barY, barWidth, barHeight);

        // 3. Decidir el color según el porcentaje (Lógica de Semáforo)
        Color fillColor;
        Color textColor;
        if (porcentaje >= 70) {
            fillColor = new Color(42, 122, 42); // Verde (Alto)
            textColor = new Color(26, 106, 26);
        } else if (porcentaje >= 30) {
            fillColor = new Color(200, 160, 0); // Amarillo/Dorado (Medio)
            textColor = new Color(170, 136, 0);
        } else {
            fillColor = new Color(200, 64, 64); // Rojo (Bajo)
            textColor = new Color(170, 32, 32);
        }

        // 4. Dibujar la porción llena de la barra
        // Regla de 3 simple para calcular los píxeles
        int fillWidth = (int) ((porcentaje / 100.0) * barWidth);
        g2.setColor(fillColor);
        g2.fillRect(barX, barY, fillWidth, barHeight);

        // 5. Dibujar el texto del porcentaje al lado derecho
        g2.setColor(isSelected ? Color.BLACK : textColor);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String text = porcentaje + "%";
        
        int textX = barX + barWidth + 10; // 10px de separación de la barra
        int textY = (height + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
        g2.drawString(text, textX, textY);
    }
}