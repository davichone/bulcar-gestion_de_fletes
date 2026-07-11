/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones.facade;

import controllers.FleteController;
import controllers.OperatorController;
import controllers.PlantController;
import controllers.VehicleController;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.domain.Flete;
import modelo.domain.Operator;
import modelo.domain.Plant;
import modelo.domain.Vehicle;
import modelo.dto.DocFleteDTO;
import modelo.enums.ParamFlete;

/**
 *
 * @author drola
 */
// patron FACADE
public class NuevoFleteFacade {

    //variables clave que hacen el trabajo pesado
    private final VehicleController controllVehicles = new VehicleController();
    private final OperatorController controllOpe = new OperatorController();
    private final PlantController controllPla = new PlantController();
    private final FleteController controllFle = new FleteController();
    private LocalDate todayGlob = null;
    private Flete newFlete = null;

    public boolean crearNuevoFlete(Vehicle vehicleGlob, Operator operatorGlob, Plant plantGlob) {
        //generar identificador del flete
        todayGlob = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MMddyy");
        String fechaFormateada = todayGlob.format(formato);
        String lastNum = "";
        String nuevoIdentificador = "";

        //obtains the last number of doc_num
        try {
            DocFleteDTO doc = this.getLastDoc();
            boolean equalDate = true;
            if (doc == null) {
                equalDate = false;
            } else {
                equalDate = doc.equalDate();
            }

            if (doc != null && equalDate) {
                lastNum = doc.getLastNumbers();
                int i = 1 + Integer.parseInt(lastNum);
                String s = String.valueOf(i);
                nuevoIdentificador = ParamFlete.IDENTIFIER.getText() + fechaFormateada + "-" + s;
                newFlete = new Flete(
                        todayGlob,
                        plantGlob,
                        vehicleGlob,
                        operatorGlob,
                        ParamFlete.EN_PROCESO.getText(),
                        nuevoIdentificador
                );
            } else if (!equalDate) {

                nuevoIdentificador = ParamFlete.IDENTIFIER.getText() + fechaFormateada + "-" + "1";
                newFlete = new Flete(
                        todayGlob,
                        plantGlob,
                        vehicleGlob,
                        operatorGlob,
                        ParamFlete.EN_PROCESO.getText(),
                        nuevoIdentificador
                );
            }

        } catch (SQLException e) {

            return false;
        }

        // 5. Instanciar el nuevo Flete
        // Nota: Revisa si tu constructor de Flete acepta el kilometraje, si es así, pásale 'kmInicial'. 
        // Si lo toma directo del objeto vehicleGlob, déjalo como lo tienes.
        if (newFlete != null) {
            if (controllFle.registerFlete(newFlete)) {
                JOptionPane.showMessageDialog(null, "Flete creado exitosamente.");
                return true;// Cierra el diálogo y retorna al InicialForm donde se ejecutará chargeFleteInProcess()
            } else {

                JOptionPane.showMessageDialog(null, "Error de base de datos al registrar el flete.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error de logica xd", "erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<Vehicle> obtenerVehiculos() {
        return controllVehicles.getlistTank();
    }

    public List<Operator> obtenerOperadores() {
        return controllOpe.getListOpTanks();
    }

    public List<Plant> obtenerPlantas() {
        return controllPla.getListForNewFlete();
    }

    public DocFleteDTO getLastDoc() throws SQLException {
        try {
            return controllFle.getLastDoc();
        } catch (SQLException ex) {
            System.getLogger(NuevoFleteFacade.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }
}
