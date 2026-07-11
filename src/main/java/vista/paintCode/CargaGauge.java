/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.paintCode;

/**
 *
 * @author drola
 */
import javax.swing.*;
import java.awt.*;

public class CargaGauge extends JPanel {

    private double current = 0;        
    private double totalCapacity = 0;  
    private int loads = 0;             

    public CargaGauge() {
        setOpaque(false); 
    }

    public void actualizarDatos(double current, double totalCapacity, int loads) {
        this.current = current;
        this.totalCapacity = totalCapacity;
        this.loads = loads;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- CONFIGURACIÓN ---
        int size = 75; 
        int x = 12;    
        int y = (getHeight() - size) / 2; 
        int grosorArco = 10; 

        double porcentaje = (totalCapacity > 0) ? (current / totalCapacity) : 0;
        if (porcentaje > 1.0) porcentaje = 1.0; 
        int angulo = (int) (porcentaje * 360);
        
        Color colorEstado = (porcentaje >= 0.95) ? new Color(220, 20, 60) : 
                            (porcentaje >= 0.80) ? new Color(255, 145, 0) : 
                            new Color(56,82,115);

        // 1. DIBUJO DEL CÍRCULO
        g2.setStroke(new BasicStroke(grosorArco, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(235, 235, 235));
        g2.drawOval(x, y, size, size);

        g2.setStroke(new BasicStroke(grosorArco, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(colorEstado);
        g2.drawArc(x, y, size, size, 90, -angulo);

        // 2. TEXTO CENTRAL (PORCENTAJE ENTERO)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String txtP = String.format("%.0f%%", porcentaje * 100);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txtP, x + (size - fm.stringWidth(txtP)) / 2, y + (size / 2) + 5);

        int tx = x + size + 18;
        
        // Bloque Superior
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(new Color(100, 100, 100));
        g2.drawString("ESTADO DE CARGA", tx, y + 10);
        
        // VALORES COMO ENTEROS (Cambiado de %.1f a %.0f)
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.setColor(Color.BLACK);
        String balance = String.format("%.0f / %.0f", current, totalCapacity);
        g2.drawString(balance, tx, y + 28);
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.drawString("Capacidad (gal)", tx, y + 40);

        // --- 3. LÍNEA DIVISORA NEGRA ---
        g2.setStroke(new BasicStroke(1f)); 
        g2.setColor(Color.BLACK); 
        g2.drawLine(tx, y + 53, tx + 85, y + 53); 


    }
}