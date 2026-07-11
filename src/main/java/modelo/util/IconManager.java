/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.swing.FontIcon;

/**
 *
 * @author drola
 */
// SINGLETON IMPLICITO
public class IconManager {

    public static void setSvgIcon(JButton button, String path, int size) {
        FlatSVGIcon icon = new FlatSVGIcon(path, size, size);
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> color));
        button.setIcon(icon);
    }

    public static void setIkonliIcon(JButton btn, Ikon ikon, int size) {
        FontIcon icon = FontIcon.of(ikon);
        icon.setIconSize(size);
        btn.setIcon(icon);
    }

    public static void setIkonliIcon(JLabel lbl, Ikon ikon, int size) {
        FontIcon icon = FontIcon.of(ikon);
        icon.setIconSize(size);
        lbl.setIcon(icon);
    }

}
