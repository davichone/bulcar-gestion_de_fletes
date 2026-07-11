package modelo.util;

import java.awt.Color;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;
import modelo.domain.User;

public final class ConfigManager {

    public static final String VERSION_SOFTWARE = "v0.1.0-BETA";
    private static final String FILE_NAME = "config.properties";

    private static final DateTimeFormatter FORMATO_BUILDING
            = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");

    private static final DateTimeFormatter FORMAT_TEX
            = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");

    public static final DateTimeFormatter FORMAT_UI_DMY
            = DateTimeFormatter.ofPattern("dd/MM/yy");

    public static User USER_CURRENT;

    public static final Color COLOR_RED = new Color(209, 123, 111);
    public static final Color COLOR_GREEN = new Color(56, 82, 115);
    public static final Color COLOR_BLUE = new Color(56, 82, 115);
    public static final Color COLOR_GREEN_LEMON = new Color(153, 153, 0);

    private final Properties props = new Properties();

    // SINGLETON
    private static final ConfigManager INSTANCE = new ConfigManager();

    private ConfigManager() {
        cargarConfiguracion();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private void cargarConfiguracion() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            props.load(input);
        } catch (IOException ex) {
            System.out.println("Archivo de configuración no encontrado, se creará uno nuevo.");
        }
    }

    public void guardarPrivacidad(boolean oculto) {
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            props.setProperty("modo_privacidad", String.valueOf(oculto));
            props.store(output, "Configuracion de Bulcar ERP");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public boolean leerPrivacidad() {
        String valor = props.getProperty("modo_privacidad", "false");
        return Boolean.parseBoolean(valor);
    }

    public static String getVersionAndBuildNumber() {
        LocalDateTime build = LocalDateTime.now();
        return "Versión: " + VERSION_SOFTWARE + " | Build: " + build.format(FORMATO_BUILDING);
    }

    public static String formatNameMonth(LocalDate fecha) {
        if (fecha == null) {
            return "---";
        }

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("MMM dd", new Locale("es"));

        return fecha.format(formatter).replace(".", "");
    }

    public static void iniciarLogger() {
        try {
            java.util.logging.FileHandler fh
                    = new java.util.logging.FileHandler("bulcar_log.txt", true);

            fh.setFormatter(new java.util.logging.SimpleFormatter());

            java.util.logging.Logger.getLogger("").addHandler(fh);

        } catch (Exception e) {
            System.err.println("No se pudo iniciar el sistema de logs: " + e.getMessage());
        }
    }

    public String getFechaFormateadaUI() {
        LocalDate hoy = LocalDate.now();
        // Definimos el patrón (dd = día, MM = mes, yyyy = año)
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Aplicamos el formato
        String fechaFormateada = hoy.format(formato);
        return fechaFormateada;
    }
}
