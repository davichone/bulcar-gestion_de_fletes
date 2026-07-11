/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.paintCode;

/**
 *
 * @author drola
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import javax.swing.JPanel;

public class GaugeCircularPro extends JPanel {

    private int porcentaje = 0; // Va de 0 a 100
    private String textoSecundario = "CARGADO";

    public GaugeCircularPro() {
        setPreferredSize(new Dimension(120, 120));
        setOpaque(false); // Fondo transparente para que se fusione con tu panel
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = Math.max(0, Math.min(100, porcentaje));
        repaint(); // Vuelve a dibujar el círculo cuando actualices el dato
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Suavizado para que el círculo no se vea pixelado
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int size = Math.min(ancho, alto) - 20; // Tamaño del círculo con un poco de margen
        int x = (ancho - size) / 2;
        int y = (alto - size) / 2;
        int grosorLinea = 12;

        // 1. Dibujar el círculo gris de fondo (el 100%)
        g2.setColor(new Color(204, 204, 204)); // Gris claro #ccc
        g2.setStroke(new BasicStroke(grosorLinea, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(x, y, size, size, 0, 360);

        // 2. Dibujar el arco de progreso (Color Azul del mockup)
        g2.setColor(new Color(26, 79, 138)); // Azul oscuro #1a4f8a
        int anguloProgreso = (int) (porcentaje * 3.6); // Convertimos % a grados (100% = 360°)
        // El arco empieza en 90 grados (arriba) y va en sentido horario (negativo)
        g2.drawArc(x, y, size, size, 90, -anguloProgreso);

        // 3. Dibujar el texto principal (ej. "4%")
        String textoPct = porcentaje + "%";
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (ancho - fm.stringWidth(textoPct)) / 2;
        int textY = (alto / 2) + 5; // Un poco arriba del centro
        g2.drawString(textoPct, textX, textY);

        // 4. Dibujar el texto secundario (ej. "CARGADO")
        g2.setColor(new Color(119, 119, 119)); // Texto gris
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        FontMetrics fmSub = g2.getFontMetrics();
        int subX = (ancho - fmSub.stringWidth(textoSecundario)) / 2;
        int subY = textY + 15; // Debajo del porcentaje
        g2.drawString(textoSecundario, subX, subY);
    }
}