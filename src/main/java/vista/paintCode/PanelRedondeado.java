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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class PanelRedondeado extends JPanel {

    // --- PROPIEDADES DE NETBEANS (Intactas para no romper el IDE) ---
    private int radioBorde = 30;
    private Color colorFondo = new Color(21, 255, 255);

    // --- VARIABLES DEL EFECTO TANQUE (Líquido) ---
    private double porcentajeLlenado = 1.0; 
    private Color colorLlenado = new Color(21, 255, 255); 
    private final Color colorFondoVacio = new Color(153,153,153); // Gris para la parte vacía

    public PanelRedondeado() {
        setOpaque(false);
    }

    public int getRadioBorde() {
        return radioBorde;
    }

    public void setRadioBorde(int radioBorde) {
        this.radioBorde = radioBorde;
        repaint(); 
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        this.colorLlenado = colorFondo; 
        repaint(); 
    }

  

    public void actualizarEstadoCarga(double volumenActual, double capacidadTotal, int idProduct) {
        if (capacidadTotal <= 0) {
            this.porcentajeLlenado = 0.0;
            this.colorLlenado = colorFondoVacio;
            repaint();
            return;
        }

        // calculamos cuánto sube el nivel
        this.porcentajeLlenado = volumenActual / capacidadTotal;
        
        // límite de seguridad
        if (this.porcentajeLlenado > 1.0) this.porcentajeLlenado = 1.0; 

        // selección de color del líquido basado en el producto
        if (porcentajeLlenado == 0) {
            this.colorLlenado = colorFondoVacio; // Si está vacío, se queda gris
        } else {
            switch (idProduct) {
                case 1:
                    // DIESEL - Verde Petróleo Oscuro (Color Petroperú)
                    this.colorLlenado = new Color(20, 90, 50);
                    break;
                case 2:
                    // GASOLINA PREMIUM - Azul (Color Petroperú)
                    this.colorLlenado = new Color(0, 85, 164);
                    break;
                case 3:
                    // GASOLINA REGULAR - Verde Claro/Brillante (Color Petroperú)
                    this.colorLlenado = new Color(0, 166, 80);
                    break;
                default:
                    // PRODUCTO DESCONOCIDO - Color de advertencia o gris neutro
                    this.colorLlenado = new Color(156, 163, 175); 
                    break;
            }
        }

        repaint();
    }

    // =========================================================
    // RENDERIZADO VISUAL (Efecto de máscara de recorte)
    // =========================================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();

        // 1. Forma base del panel
        RoundRectangle2D formaRedondeada = new RoundRectangle2D.Float(0, 0, ancho - 1, alto - 1, radioBorde, radioBorde);

        // 2. Pintamos el fondo general (simulando que el tanque está vacío)
        g2.setColor(colorFondoVacio);
        g2.fill(formaRedondeada);

        // 3. Activamos la máscara para que el líquido no se salga de las curvas
        g2.setClip(formaRedondeada);

        // 4. Dibujamos el "líquido" subiendo desde abajo
        int alturaLiquido = (int) (alto * porcentajeLlenado);
        int yLiquido = alto - alturaLiquido; // Invertimos Y porque 0 es arriba en Java
        
        g2.setColor(colorLlenado);
        g2.fillRect(0, yLiquido, ancho, alturaLiquido);

        // 5. Desactivamos la máscara para pintar el borde
        g2.setClip(null);

        // 6. Borde sutil exterior
        g2.setColor(new Color(200, 200, 200));
        g2.draw(formaRedondeada);

        g2.dispose();
    }
}