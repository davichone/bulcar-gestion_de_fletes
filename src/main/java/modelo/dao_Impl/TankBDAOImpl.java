/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao_Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.dao.CRUD;
import modelo.domain.Customer;
import modelo.domain.Load;
import modelo.domain.LoadDetail;
import modelo.domain.TankB;
import modelo.domain.Vehicle;
import modelo.util.conexion.ConexionBD;

/**
 *
 * @author drola
 */
public class TankBDAOImpl implements CRUD<TankB> {

    private static final Logger LOGGER = Logger.getLogger(TankBDAOImpl.class.getName());

    public boolean reset() {

        String sql = """
                     UPDATE tank_behavior
                     SET 
                     id_vehicle =NULL,
                     id_customer = NULL,
                     current_volumen = NULL,
                     id_product = NULL,
                     id_load = NULL,
                     id_load_detail = NULL,
                     status = 'LIBRE'
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

    public List<Integer> getFreeCompartments() {

        List<Integer> lista = new ArrayList<>();

        String sql = "select compartment from tank_behavior where status = 'LIBRE'; ";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer i = rs.getInt("compartment");
                    lista.add(i);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
            e.printStackTrace();

        }

        return lista;
    }

    @Override
    public boolean insert(TankB objeto) {
        LOGGER.log(Level.INFO, "Iniciando registro de carga para load_detail con id: {0}", objeto.getLoadDetail().getId());

        String sql = "UPDATE tank_behavior SET id_vehicle = ?, id_customer = ?, current_volumen = ?, "
                + "status = ?, id_product = ?, id_load = ?, id_load_detail = ? "
                + "WHERE compartment = ?";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, objeto.getVehicle().getId());
            ps.setInt(2, objeto.getCustomer().getId());
            ps.setDouble(3, objeto.getLoadDetail().getQuantity());
            ps.setString(4, objeto.getStatus()); // Corregido el índice a 4
            ps.setInt(5, objeto.getLoadDetail().getidProduct()); // Corregido a setInt
            ps.setInt(6, objeto.getLoad().getId()); // Corregido a setInt
            ps.setInt(7, objeto.getLoadDetail().getId()); // Corregido a setInt
            ps.setInt(8, objeto.getLoadDetail().getCompartment()); // Nuevo parámetro para el WHERE

            System.err.println("successfully inserted an load detail");
            LOGGER.log(Level.INFO, "Registro de Tank_behavior exitoso. load_detail id: {0}", objeto.getLoadDetail().getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {

            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new load detail");
            LOGGER.log(Level.SEVERE, "Fallo al guardar el registro de Tank_behavior en BD", ex);
            return false;
        }
    }

    @Override
    public boolean modificar(TankB objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<TankB> listar() {
        String sql = """
                     SELECT 
                     t.id_tank_behavior,
                     t.current_volumen,
                     t.total_capacity,
                     t.compartment,
                     t.status,
                     t.id_load,
                     t.id_load_detail,
                     t.id_vehicle,
                     c.id_customer, c.fullname,
                     p.id_product, p.name_prod
                     FROM tank_behavior AS t
                     INNER JOIN customers AS c ON t.id_customer=c.id_customer
                     INNER JOIN products AS p ON t.id_product = p.id_product
                     where status = 'OCUPADO'
                     """;

        List<TankB> listaOcupados = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // IMPORTANTE: El objeto nace ADENTRO del while
                TankB o = new TankB();
                o.setId(rs.getInt("id_tank_behavior"));

                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id_vehicle"));   //extensible para varios vehiculos
                o.setVehicle(v);

                Customer c = new Customer();
                c.setId(rs.getInt("id_customer"));
                c.setName(rs.getString("fullname"));
                o.setCustomer(c);

                Load l = new Load();
                l.setId(rs.getInt("id_load"));
                o.setLoad(l);

                LoadDetail ld = new LoadDetail();
                ld.setId(rs.getInt("id_load_detail"));
                ld.setQuantity(rs.getDouble("current_volumen"));
                ld.setCapacityBox(rs.getDouble("total_capacity"));
                ld.setCompartment(rs.getInt("compartment"));
                ld.setIdProduct(rs.getInt("id_product"));
                ld.setNameProduct(rs.getString("name_prod"));
                o.setLoadDetail(ld);

                o.setStatus(rs.getString("status"));

                listaOcupados.add(o);
            }

            System.err.println("successfully select -> " + listaOcupados.size() + " fletes en proceso");
            return listaOcupados;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error select flete in process");
            return null; // Si hay error, devuelve null
        }
    }

    @Override
    public TankB buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
