/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.service;
import modelo.domain.Flete;
import modelo.domain.Load;
import modelo.util.ConfigManager;
import patrones.proxy.LoadService;
/**
 *
 * @author drola
 */




public class LoadServiceCadeneroProxy implements LoadService {
    private final LoadService nextService;

    public LoadServiceCadeneroProxy(LoadService nextService) {
        this.nextService = nextService;
    }

    @Override
    public int guardarCargaCompleta(Flete f, Load l) {
        // El cadenero revisa si el usuario actual ha iniciado sesión
        if (ConfigManager.USER_CURRENT == null) {
            System.out.println("[CadeneroProxy] ACCESO DENEGADO: No hay una sesión de usuario activa.");
            return -1;
        }

        // Supongamos que solo los administradores pueden registrar cargas
        String role = ConfigManager.USER_CURRENT.getRole();
        if (!"Administrador".equalsIgnoreCase(role)) {
            System.out.println("[CadeneroProxy] ACCESO DENEGADO: El rol '" + role + "' no tiene permisos para crear cargas.");
            return -1; 
        }

        System.out.println("[CadeneroProxy] Permiso concedido al usuario '" + ConfigManager.USER_CURRENT.getUsername() + "' para guardar carga.");
        return nextService.guardarCargaCompleta(f, l);
    }

    @Override
    public String getStatusPaymentLoad(int idLoad) {
        // Cualquiera con sesión activa puede ver el estado del pago
        if (ConfigManager.USER_CURRENT == null) {
            System.out.println("[CadeneroProxy] ACCESO DENEGADO: Inicie sesión para ver estados de pago.");
            return "Acceso Denegado";
        }
        
        return nextService.getStatusPaymentLoad(idLoad);
    }
}