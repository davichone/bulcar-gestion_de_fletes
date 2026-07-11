/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.dao_Impl.LoadDAOImpl;
import modelo.domain.Flete;
import modelo.domain.Load;
import modelo.dto.LoadDTO;
import modelo.service.LoadServiceCacheProxy;
import modelo.service.LoadServiceCadeneroProxy;
import modelo.service.RealLoadService;
import patrones.proxy.LoadService;

/**
 *
 * @author drola
 */
public class LoadController {

    //private final RealLoadService lService = new RealLoadService();
    private final LoadService lService = new LoadServiceCadeneroProxy(
    new LoadServiceCacheProxy(
        new RealLoadService()
    )
);
    private final LoadDAOImpl dao = new LoadDAOImpl();

    public int insertLoadComplete(Load l, Flete f) {
        int id = -1;

        //verificar si la carga esta vacia
        if (f.getId() > 0 && !l.getListProducts().isEmpty()) {
            id = lService.guardarCargaCompleta(f, l);
        }

        return id;
    }

    public List<LoadDTO> getLoadsWithDetails(Flete f) throws SQLException {
        // 1. Validación defensiva
        if (f == null || f.getId() <= 0) {
            return new ArrayList<>();
        }

        // 2. Si el objeto local dice "En Proceso", buscamos en BD pasándole el ID
        if (f.getStatus().equalsIgnoreCase("En Proceso")) {
            // AHORA SÍ LE PASAMOS EL ID
            return dao.getLoadsWithDetails(f.getId());
        }

        // 3. Si el flete ya se cerró o canceló, devolvemos vacío
        return new ArrayList<>();
    }

    public List<LoadDTO> getLoadsWithDetails(String num_doc) throws SQLException {
        // 1. Validación defensiva
        if (num_doc == null) {
            return new ArrayList<>();
        }

        return dao.getLoadsWithDetails(num_doc);
    }
    
    //for history customer 
    public List<LoadDTO> getLoadsWithDetailsCus(int id_customer) throws SQLException {
        // 1. Validación defensiva
        if (id_customer< 0) {
            return new ArrayList<>();
        }

        return dao.getLoadsWithDetailsCust(id_customer);
    }

    public String getStatusPaymentByID(int idLoad) {

        if (idLoad > 0) {
            return lService.getStatusPaymentLoad(idLoad);
        }

        return "error status";
    }

    public boolean updateAmount(Load l) throws SQLException {
        if (l.getId() < 0 || l.getAmount() < 0) {
            return false;
        }

        return dao.update(l);
    }

    public boolean delete(Load l) {
        if (l.getId() > 0) {
            return dao.eliminar(l);
        }
        return false;
    }

    public double getAmount(int id) {
        double d = 0;
        if (id < 0) {
            return d;
        }

        return dao.getAmountById(id);
    }
}
