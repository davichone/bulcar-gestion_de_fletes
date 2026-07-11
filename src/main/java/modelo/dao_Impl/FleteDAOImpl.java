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
import modelo.util.conexion.ConexionBD;
import modelo.dao.FleteDAO;
import modelo.domain.Flete;
import modelo.domain.Operator;
import modelo.domain.Plant;
import modelo.domain.Vehicle;
import modelo.dto.DocFleteDTO;
import modelo.dto.SummaryFlete;
import modelo.enums.ParamFlete;

/**
 *
 * @author drola
 */
public class FleteDAOImpl implements FleteDAO {

    @Override
    public boolean insert(Flete objeto) {
        String sql = "INSERT INTO fletes (start_date, km_start, id_plant, id_vehicle, id_operator, status, num_doc) VALUES (?,?,?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(objeto.getStarDate()));
            ps.setInt(2, objeto.getKmstart());
            ps.setInt(3, objeto.getIdPlant());
            ps.setInt(4, objeto.getIdVehicle());
            ps.setInt(5, objeto.getIdOperator());
            ps.setString(6, objeto.getStatus());
            ps.setString(7, objeto.getNumDoc());
            System.err.println("successfully inserted an new flete");
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new customer");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar(Flete objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Flete> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public Flete buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Flete> getAllFletesInProcess() {
        // Le quitamos el LIMIT 1 para que traiga TODOS los que estén en proceso
        String sql = """
                     SELECT f.id_flete, f.status, f.num_doc, f.start_date, f.km_start, f.id_plant, p.name_plant, 
                                                                                   f.id_vehicle, v.manufacturer, v.model, 
                                                                                   f.id_operator, o.fullname											
                     FROM fletes AS f
                     INNER JOIN plants AS p ON p.id_plant=f.id_plant
                     INNER JOIN vehicles AS v ON v.id_vehicle = f.id_vehicle
                     INNER JOIN operators AS o ON o.id_operator = f.id_operator
                     WHERE f.status = 'En proceso';
                     """;

        // Creamos la lista vacía que vamos a llenar
        List<Flete> listaFletes = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // IMPORTANTE: El objeto nace ADENTRO del while
                Flete obj = new Flete();

                obj.setId(rs.getInt("id_flete"));
                obj.setStarDate(rs.getDate("start_date").toLocalDate());
                obj.setStatus(rs.getString("status"));
                obj.setNumDoc(rs.getString("num_doc"));

                Plant p = new Plant();
                p.setId(rs.getInt("id_plant"));
                p.setName(rs.getString("name_plant"));
                obj.setPlant(p);

                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id_vehicle"));
                v.setManufacturer(rs.getString("manufacturer"));
                v.setModel(rs.getString("model"));
                v.setKm(rs.getInt("km_start"));
                obj.setVehicle(v);

                Operator o = new Operator();
                o.setId(rs.getInt("id_operator"));
                o.setFullname(rs.getString("fullname"));
                obj.setOperator(o);

                // Agregamos este flete a nuestra lista
                listaFletes.add(obj);
            }

            System.err.println("successfully select -> " + listaFletes.size() + " fletes en proceso");
            return listaFletes;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error select flete in process");
            return null; // Si hay error, devuelve null
        }
    }

    @Override
    public boolean closeFleteCurrent(Flete f) {
        String sql = "UPDATE fletes SET status = ?, end_date = ?, km_end = ? WHERE id_flete = ?";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ParamFlete.FINALIZADO.getText());
            ps.setDate(2, java.sql.Date.valueOf(f.getEndDate()));
            ps.setInt(3, f.getKmEnd());
            ps.setInt(4, f.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error to closed flete");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public DocFleteDTO getLastDoc() throws SQLException {
        String sql = """
                     SELECT id_flete, num_doc
                     FROM fletes
                     ORDER BY id_flete DESC
                     LIMIT 1;
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                DocFleteDTO f = new DocFleteDTO();
                f.setId(rs.getInt("id_flete"));
                f.setDocNum(rs.getString("num_doc"));
                return f;
            }

            return null;

        }
    }

    //for MonitorFlete
    @Override
    public List<SummaryFlete> getList() {
        List<SummaryFlete> l = new ArrayList<>();
     String sql = "SELECT*FROM table_summary_fletes";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SummaryFlete s = new SummaryFlete();
                s.setId(rs.getInt("id_flete"));
                s.setNumDoc(rs.getString("num_doc"));
                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id_vehicle"));
                v.setManufacturer(rs.getString("manufacturer"));
                v.setModel(rs.getString("model"));
                s.setVehicle(v);
                Operator o = new Operator();
                o.setId(rs.getInt("id_operator"));
                o.setFullname(rs.getString("fullname"));
                s.setOperator(o);
                //s.setStarDate(rs.getDate("start_date").toLocalDate());
                s.setStarDate(rs.getObject("start_date", java.time.LocalDate.class));
                //s.setEndDate(rs.getDate("end_date").toLocalDate());
                s.setEndDate(rs.getObject("end_date", java.time.LocalDate.class));
                s.setSizeLoads(rs.getInt("cargas"));
                s.setVolumeTotal(rs.getDouble("volumen"));
                s.setKmTraveled(rs.getInt("kilometraje"));
                s.setIncomes(rs.getDouble("total_ingresos"));
                s.setExpenses(rs.getDouble("total_egresos"));
                s.setProfit(rs.getDouble("utilidad"));
                s.setStatus(rs.getString("status"));
                l.add(s);
            }
            return l;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            
            e.printStackTrace();
            return l;
        }

        
    }

    @Override
    public boolean deleted(int id) throws SQLException {
        
        String sql = "DELETE FROM fletes WHERE id_flete = ?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            System.err.println("successfully delete");
            return ps.executeUpdate() > 0;
        }
    }

}
