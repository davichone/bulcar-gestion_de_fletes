/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.service;

import java.util.HashMap;
import java.util.Map;
import modelo.domain.Flete;
import modelo.domain.Load;
import patrones.proxy.LoadService;

/**
 *
 * @author drola
 */


public class LoadServiceCacheProxy implements LoadService {
    private final LoadService realService;
    // Caché en memoria usando el ID de la carga como clave y el estado como valor
    private final Map<Integer, String> cacheEstados = new HashMap<>();

    public LoadServiceCacheProxy(LoadService realService) {
        this.realService = realService;
    }

    @Override
    public int guardarCargaCompleta(Flete f, Load l) {
        // Las operaciones de escritura no se cachean, se delegan directamente.
        // Opcional: podemos limpiar la caché si es necesario.
        return realService.guardarCargaCompleta(f, l);
    }

    @Override
    public String getStatusPaymentLoad(int idLoad) {
        if (cacheEstados.containsKey(idLoad)) {
            System.out.println("[CacheProxy] RETORNANDO DESDE CACHÉ (No DB query) para ID Carga: " + idLoad);
            return cacheEstados.get(idLoad);
        }

        System.out.println("[CacheProxy] No en caché. Delegando al servicio real...");
        String status = realService.getStatusPaymentLoad(idLoad);
        cacheEstados.put(idLoad, status);
        return status;
    }

    // Método útil para cuando se registre un nuevo pago y se necesite invalidar la caché
    public void invalidarCache() {
        cacheEstados.clear();
        System.out.println("[CacheProxy] Caché invalidada.");
    }
}
