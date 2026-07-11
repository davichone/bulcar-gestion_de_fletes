/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao_Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import modelo.util.conexion.ConexionBD;
import modelo.dao.LoadDetailsDAO;
import modelo.domain.Load;
import modelo.domain.LoadDetail;


/**
 *
 * @author drola
 */
public class LoadDetailDAOImpl implements LoadDetailsDAO {

    @Override
    public boolean insert(LoadDetail objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(LoadDetail objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public boolean eliminar(Object id) {
        int idd = -1;
        if (id instanceof LoadDetail) {
            LoadDetail ld = (LoadDetail) id;
            idd= ld.getId();
           
        }
         String sql = "DELETE FROM load_details WHERE id_load_detail = ?";
            try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idd);

                System.err.println("successfully delete");
                return ps.executeUpdate()> 0;

            } catch (SQLException ex) {
                //System.err.println("error DAOIMplS ");
                ex.printStackTrace();  // tool debuging console
                System.err.println("error Delete load detail");
                //System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                return false;
            }

    }

    @Override
    public List<LoadDetail> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public LoadDetail buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insertLoadDetail(int idLoadCurrent, LoadDetail ld) {
        String sql = "INSERT INTO load_details (id_load, id_product, quantity) VALUES (?,?,?) RETURNING id_load_detail";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLoadCurrent);
            ps.setInt(2, ld.getidProduct());
            ps.setDouble(3, ld.getQuantity());

            System.err.println("successfully inserted an load detail");
            //System.err.println("daoImpl ok");
//            return ps.executeUpdate() > 0;
            int idGenerado=-1;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //Obtenemos el ID de la primera columna
                    idGenerado = rs.getInt(1);
                  
                    System.err.println("successfully inserted -> load_detail in DB");
                    return idGenerado;
                }
            }
            return idGenerado;

        } catch (SQLException ex) {
            //System.err.println("error DAOIMplS ");
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new load detail");
            //System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return -1;
        }
    }

    @Override
    public boolean update(int idLoad, LoadDetail ld) throws SQLException {
        String sql = "UPDATE load_details SET quantity = ? WHERE id_load_detail = ? AND id_load =? ";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, ld.getQuantity());
            ps.setInt(2, ld.getId());
            ps.setInt(3, idLoad);
            System.err.println("update successfuly");
            return ps.executeUpdate() > 0;
        }
        //return false; la excepcion sube al controller
    }

}
