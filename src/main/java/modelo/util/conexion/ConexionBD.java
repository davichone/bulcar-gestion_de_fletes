/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util.conexion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author drola
 */
public class ConexionBD {

    //for Connection local
    private static final String URL = "jdbc:postgresql://localhost:5432/bulcar";
    private static final String USER = "postgres";
    private static final String PASSWORD = "131422";

    private static Connection conexion = null;

    //method para obtener la conexion (patron singleton)
    //----------------------------------------------------------------------------
//    public static Connection getConexion() {
//        try {
//            // 1. Si la conexión no existe o se cerró, la creamos
//            if (conexion == null || conexion.isClosed()) {
//                // Registrar el Driver (opcional en versiones modernas, pero recomendado)
//                Class.forName("org.postgresql.Driver");
//
//                // Crear la conexión
//                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
//                System.out.println("¡Conexión exitosa a PostgreSQL!");
//            }
//        } catch (ClassNotFoundException e) {
//            System.err.println("Error: No se encontró el Driver de PostgreSQL. " + e.getMessage());
//        } catch (SQLException e) {
//            System.err.println("Error de SQL al conectar: " + e.getMessage());
//        }
//        return conexion;
//    }
//
//    //close conecction
//    public static void cerrarConexion() {
//        try {
//            if (conexion != null && !conexion.isClosed()) {
//                conexion.close();
//                System.out.println("Conexión cerrada.");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error al cerrar conexión: " + e.getMessage());
//        }
//    }
    //------------------------------------------------------------------------------
    //for Neon conected
    //-----------------------------------------------------------------------------------
//    private static final HikariDataSource dataSource;
//
//    static {
//        HikariConfig config = new HikariConfig();
//
//        config.setJdbcUrl("jdbc:postgresql://ep-winter-credit-acrp5578-pooler.sa-east-1.aws.neon.tech/neondb?sslmode=require");
//        config.setUsername("neondb_owner");
//        config.setPassword("npg_C8AeypuDX9mL");
//
//        config.setMaximumPoolSize(3);   // suficiente para tu caso
//        config.setMinimumIdle(1);
//        config.setIdleTimeout(30000);
//        config.setMaxLifetime(600000);
//
//        dataSource = new HikariDataSource(config);
//    }
//
//    public static Connection getConexion() throws SQLException {
//        return dataSource.getConnection();
//    }
//
//    //---------------------------------------------------------------------------------------------------
//    public static String getServerInfo() {
//        // 1. Verificación básica
//        if (dataSource == null || dataSource.isClosed()) {
//            return "Pool Cerrado";
//        }
//
//        try (Connection tempCon = dataSource.getConnection()) {
//            // 2. PRUEBA REAL (El Ping): 
//            // Espera máximo 2 segundos para ver si el servidor responde.
//            if (tempCon.isValid(2)) {
//                String url = tempCon.getMetaData().getURL();
//                String hostAndPort = url.replace("jdbc:postgresql://", "").split("/")[0];
//                return "Conectado a: " + hostAndPort;
//            } else {
//                return "Sin Conexión (Offline)";
//            }
//
//        } catch (SQLException e) {
//            // 3. Si no hay internet, el intento de conexión fallará y vendrá aquí
//            return "Sin Conexión (Offline)";
//        }
//    }
    //--------------test perezosa clase
    private static HikariDataSource dataSource = null;

    // Configuración extraída como constantes para que sea más limpio
    private static final String HOST = "ep-winter-credit-acrp5578-pooler.sa-east-1.aws.neon.tech";
    private static final int PORT = 5432;

    // 1. INICIALIZACIÓN PEREZOSA (Solo se crea si hay internet y se necesita)
    private static synchronized void inicializarPool() {
        if (dataSource == null || dataSource.isClosed()) {
            try {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl("jdbc:postgresql://" + HOST + "/neondb?sslmode=require");
                config.setUsername("neondb_owner");
                config.setPassword("npg_C8AeypuDX9mL");

                config.setMaximumPoolSize(3);
                config.setMinimumIdle(1);
                // FUNDAMENTAL: Si falla, que no espere 30 segundos, solo 3.
                config.setConnectionTimeout(3000);

                dataSource = new HikariDataSource(config);
            } catch (Exception e) {
                System.err.println("No se pudo inicializar Hikari: " + e.getMessage());
                dataSource = null; // Lo dejamos nulo para que reintente la próxima vez
            }
        }
    }

    // 2. MÉTODO PARA OBTENER CONEXIÓN (Seguro)
    public static Connection getConexion() throws SQLException {
        inicializarPool(); // Intentamos crearlo si no existe

        if (dataSource == null) {
            throw new SQLException("No hay conexión al servidor Neon.");
        }
        return dataSource.getConnection();
    }

    // 3. EL MONITOR DE ESTADO (Completamente a prueba de fallos)
    public static String getServerInfo() {
        // PRIMERA BARRERA: Un ping súper rápido que NO involucra a Hikari.
        // Si el usuario no tiene internet, esto falla en menos de 2 segundos sin lanzar errores rojos.
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PORT), 2000);

            // SEGUNDA BARRERA: Si hay internet físico, aseguramos que el Pool esté vivo
            inicializarPool();
//            if (dataSource != null && !dataSource.isClosed()) {
//                System.err.println("\"Conectado a: \" + HOST.split(\"\\\\.\")[0]; // Muestra \"ep-winter...\"");
//                return "Conectado a: " + HOST.split("\\.")[0]; // Muestra "ep-winter..."
//            } else {
//                return "Error en BD (Credenciales/Pool)";
//            }
            if (dataSource != null && !dataSource.isClosed()) {
                // Le decimos: "Agarra el HOST completo y bórramelo desde 'ep' hasta 'credit-'"
                String n = "Conectado a: "+HOST.replace("ep-winter-credit-", "");
                return n;
            } else {
                return "Offline";
            }

        } catch (Exception e) {
            // Si atrapara el UnknownHostException o Timeout, cae directo aquí.
            // Cero explosiones en consola.
            return "Sin Conexión (Offline)";
        }
    }
}
