/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util;

/**
 *
 * @author drola
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class Validator {
    
    
    
    private static final DateTimeFormatter PERU_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Validar que los campos no estén vacíos
    public static boolean isNotEmpty(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Validar DNI Peruano (8 dígitos)
    public static boolean isValidDNI(String dni) {
        return dni != null && dni.matches("\\d{8}");
    }

    // Validar Celular (9 dígitos, empieza con 9 en Perú)
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("9\\d{8}");
    }

    // Intentar parsear una fecha y avisar si falla
    public static LocalDate parseDatePeru(String dateStr) {
        try {
            return LocalDate.parse(dateStr, PERU_FORMAT);
        } catch (DateTimeParseException e) {
            return null; // Retorna null si el usuario escribió algo que no es una fecha válida
        }
    }
    
    public static boolean isComboSelected(JComboBox<?> comboBox) {
        // Verifica si es null o si el índice es -1 (nada seleccionado)
        if (comboBox.getSelectedItem() == null || comboBox.getSelectedIndex() == 1) {
            return false;
        }
        
        // Opcional: Si el primer ítem es un mensaje como "Seleccione...", lo validamos como inválido
        // return comboBox.getSelectedIndex() > 0; 
        
        return true;
    }
    
    
    public static boolean isInteger(String texto) {
    if (texto == null || texto.trim().isEmpty()) {
        return false;
    }
    try {
        Integer.parseInt(texto.trim()); // Intenta la conversión
        return true; // Si lo logra, es un int válido
    } catch (NumberFormatException e) {
        return false; // Si falla, no es un número entero
    }
}
}