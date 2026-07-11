/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.paintCode;

import javax.swing.*;
import java.awt.*;

public class CardResumenPro extends JPanel {

    private String titulo = "INGRESOS";
    private String monto = "S/ 0.00";
    private Color accentColor = new Color(0, 122, 255);
    
    private JLabel lblTitulo;
    private JLabel lblValor;

    // --- CONSTRUCTOR VACÍO (Indispensable para NetBeans Palette) ---
    public CardResumenPro() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(210, 110));
        setBorder(BorderFactory.createEmptyBorder(18, 20, 25, 20));

        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitulo.setForeground(new Color(130, 130, 130));

        lblValor = new JLabel(monto);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(new Color(40, 40, 40));

        add(lblTitulo, BorderLayout.NORTH);
        add(lblValor, BorderLayout.CENTER);
    }

    // --- MÉTODOS SET (Para que NetBeans los vea en Properties) ---
    public void setTitulo(String titulo) {
        this.titulo = titulo;
        if (lblTitulo != null) lblTitulo.setText(titulo);
        repaint();
    }

    public void setMonto(String monto) {
        this.monto = monto;
        if (lblValor != null) lblValor.setText(monto);
        repaint();
    }

    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 20;
        
        // Sombra
        g2.setColor(new Color(0, 0, 0, 15));
        g2.fillRoundRect(3, 8, getWidth() - 7, getHeight() - 13, arc, arc);

        // Fondo Blanco
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 4, getWidth() - 5, getHeight() - 11, arc, arc);

        // Línea superior de color
        g2.setColor(accentColor);
        Shape oldClip = g2.getClip();
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 4, getWidth() - 5, getHeight() - 11, arc, arc));
        g2.fillRect(0, 4, getWidth(), 6);
        g2.setClip(oldClip);

        super.paintComponent(g);
    }
}