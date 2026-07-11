/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.service;

import java.sql.Connection;
import java.sql.SQLException;
import modelo.util.conexion.ConexionBD;
import modelo.dao_Impl.IncomeDAOImpl;
import modelo.dao_Impl.LoadDAOImpl;
import modelo.domain.Flete;
import modelo.domain.Load;
import patrones.proxy.LoadService;

/**
 *
 * @author drola
 */
public class RealLoadService implements LoadService{

    public RealLoadService(){}
    
    
    @Override
    public int guardarCargaCompleta(Flete f, Load l) {
        Connection con = null;
        try {
            con = ConexionBD.getConexion(); 
            con.setAutoCommit(false); //para el rollback

            LoadDAOImpl daoImpl = new LoadDAOImpl();

            int idGen =daoImpl.insertLoadOfFleteCurrent(l, f, con);
            con.commit();
            System.out.println("Carga y productos del flete actual guardados correctamente.");

            // Insertar cada producto del detalle usando ese ID
            return idGen;

        } catch (SQLException e) {
            if (con != null) try {
                con.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return -1;
        } finally {
            if (con != null) try {
                con.close();
            } catch (SQLException ex) {
            }
        }

    }
    //obtain status payment basado en el calculo del importe
    @Override
    public String getStatusPaymentLoad(int idLoad){
        
        String stts = "";
        try {
            LoadDAOImpl daoLoad = new LoadDAOImpl();
         IncomeDAOImpl daoInc = new IncomeDAOImpl();
         
         double amountLoad =daoLoad.getAmountById(idLoad);
         double calculoIncome = daoInc.getCalculusAmountByID(idLoad);
         
         double total = amountLoad-calculoIncome;
         
         
         if (total==amountLoad) {
             stts ="Pagado";
        }else if (calculoIncome<amountLoad) {
            stts = "Pago parcial";
            
        }else if(calculoIncome==0){
            stts = "Pendiente";
        }
         
        return stts;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("error mecanismo stts");
            return stts;
        }
         
    }

}
