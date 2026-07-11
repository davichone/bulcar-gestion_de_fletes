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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.util.conexion.ConexionBD;
import modelo.dao.LoadDAO;
import modelo.domain.Flete;
import modelo.domain.Load;
import modelo.dto.LoadDTO;
import modelo.dto.LoadDetailDTO;

/**
 *
 * @author drola
 */
public class LoadDAOImpl implements LoadDAO {

    @Override
    public boolean insert(Load objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(Load objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        int idd = -1;
        if (id instanceof Load) {
            Load ld = (Load) id;
            idd = ld.getId();

        }
        String sql = "DELETE FROM loads WHERE id_load = ?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idd);

            System.err.println("successfully delete");
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            //System.err.println("error DAOIMplS ");
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Delete load ");
            //System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public List<Load> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public Load buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insertLoadOfFleteCurrent(Load l, Flete f, Connection con) throws SQLException {
        String sql = "INSERT INTO loads (id_flete, id_customer, amount) VALUES (?,?,?) RETURNING id_load";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, f.getId());
            ps.setInt(2, l.getCustomer().getId());
            ps.setDouble(3, l.getAmount());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //Obtenemos el ID de la primera columna
                    int idGenerado = rs.getInt(1);
                  
                    System.err.println("successfully inserted -> load current of flete current in DB");
                    return idGenerado;
                }
            }

            return -1;

        }

    }
    
    //for view cards by flete current
    @Override
    public List<LoadDTO> getLoadsWithDetails(int idFlete) throws SQLException {
        Map<Integer, LoadDTO> map = new LinkedHashMap<>();

        String sql = """
                 SELECT  f.id_flete, l.id_load,  l.amount, 
                        l.id_customer, c.fullname, c.name_eess, c.address_eess,
                        ld.id_load_detail, p.id_product, p.name_prod, ld.quantity,
                        COALESCE((SELECT SUM(amount) FROM incomes WHERE id_load = l.id_load ), 0) AS advance,
                 
                 CASE 
                         WHEN (SELECT COALESCE(SUM(i.amount), 0) FROM incomes i WHERE i.id_load = l.id_load) <= 0 
                             THEN 'Pendiente'
                         WHEN (SELECT COALESCE(SUM(i.amount), 0) FROM incomes i WHERE i.id_load = l.id_load) < l.amount 
                             THEN 'Adelanto'
                         ELSE 'Pagado'
                     END AS payment_stts
                 
                                            FROM fletes AS f
                                            INNER JOIN loads AS l ON  l.id_flete = f.id_flete
                                            INNER JOIN customers as c on c.id_customer = l.id_customer
                                            INNER JOIN load_details AS ld ON l.id_load = ld.id_load
                                            INNER JOIN products AS p ON ld.id_product = p.id_product
                                            
                                            WHERE f.status = 'En proceso' AND f.id_flete = ? 
                                            ORDER BY l.id_load
                 """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. Inyectamos el ID del flete en la consulta
            ps.setInt(1, idFlete);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_load");

                    LoadDTO load = map.get(id);

                    if (load == null) {
                        load = new LoadDTO();
                        load.setIdLoad(id);
                        load.setIdFlete(rs.getInt("id_flete"));
                        load.setAdvance(rs.getDouble("advance"));
                        load.setAmount(rs.getDouble("amount"));
                        load.setPaymentStatus(rs.getString("payment_stts"));
                        load.setIdCustomer(rs.getInt("id_customer"));
                        load.setNameCustomer(rs.getString("fullname"));
                        load.setNameEESS(rs.getString("name_eess"));
                        load.setAddressEESS(rs.getString("address_eess"));

                        map.put(id, load);
                    }

                    LoadDetailDTO detail = new LoadDetailDTO(
                            rs.getInt("id_load_detail"),
                            rs.getInt("id_product"),
                            rs.getString("name_prod"),
                            rs.getDouble("quantity")
                    );

                    load.addDetail(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>(map.values());
    }

    @Override
    public double getAmountById(int idLoad) {

        String sql = "SELECT amount FROM loads WHERE id_load = ?";
        double total = 0;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. PRIMERO pasamos el parámetro
            ps.setInt(1, idLoad);

            // 3. DESPUÉS ejecutamos la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 4. Obtenemos el resultado de la suma (es la columna 1)
                    total = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return total;

    }

    @Override
    public boolean update(Load l) throws SQLException {
        String sql = "UPDATE loads SET amount = ? WHERE id_load =?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, l.getAmount());
            ps.setInt(2, l.getId());
            System.err.println("update successfuly");
            return ps.executeUpdate() > 0;
        }
        //return false; la excepcion sube al controller
    }
    
    
    //for summary_flete
    @Override
    public List<LoadDTO> getLoadsWithDetails(String num_doc) throws SQLException {
        Map<Integer, LoadDTO> map = new LinkedHashMap<>();
        String sql = """
                 SELECT  f.id_flete, f.num_doc, f.start_date, l.id_load,
                    l.id_customer, c.fullname, c.name_eess, c.address_eess,
                    ld.id_load_detail, p.id_product, p.name_prod, ld.quantity, l.amount, 
                    COALESCE((SELECT SUM(amount) FROM incomes WHERE id_load = l.id_load ), 0) AS advance,
              CASE 
                    WHEN (SELECT COALESCE(SUM(i.amount), 0) FROM incomes i WHERE i.id_load = l.id_load) <= 0 
                        THEN 'Pendiente'
                    WHEN (SELECT COALESCE(SUM(i.amount), 0) FROM incomes i WHERE i.id_load = l.id_load) < l.amount 
                        THEN 'Adelanto'
                    ELSE 'Pagado'
                       END AS payment_stts
                 FROM fletes AS f
                    INNER JOIN loads AS l ON  l.id_flete = f.id_flete
                    INNER JOIN customers as c on c.id_customer = l.id_customer
                    INNER JOIN load_details AS ld ON l.id_load = ld.id_load
                    INNER JOIN products AS p ON ld.id_product = p.id_product
                 WHERE f.num_doc = ?
                 """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. Inyectamos el ID del flete en la consulta
            ps.setString(1, num_doc);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_load");

                    LoadDTO load = map.get(id);

                    if (load == null) {
                        load = new LoadDTO();
                        load.setIdLoad(id);
                        load.setIdFlete(rs.getInt("id_flete"));
                        load.setNum_doc(rs.getString("num_doc"));
                        load.setDate(rs.getDate("start_date").toLocalDate());
                        load.setAmount(rs.getDouble("amount"));
                        load.setAdvance(rs.getDouble("advance"));
                        load.setPaymentStatus(rs.getString("payment_stts"));
                        load.setIdCustomer(rs.getInt("id_customer"));
                        load.setNameCustomer(rs.getString("fullname"));
                        load.setNameEESS(rs.getString("name_eess"));
                        load.setAddressEESS(rs.getString("address_eess"));

                        map.put(id, load);
                    }

                    LoadDetailDTO detail = new LoadDetailDTO(
                            rs.getInt("id_load_detail"),
                            rs.getInt("id_product"),
                            rs.getString("name_prod"),
                            rs.getDouble("quantity")
                    );

                    load.addDetail(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>(map.values());
    }
    
    //for history customer
    @Override
    public List<LoadDTO> getLoadsWithDetailsCust(int id_customer) throws SQLException {
          Map<Integer, LoadDTO> map = new LinkedHashMap<>();
        String sql = """
                 SELECT  
                     f.id_flete,
                     f.num_doc,
                     f.start_date,
                     l.id_load,
                     l.id_customer, 
                     c.fullname, 
                     c.name_eess, 
                     c.address_eess,
                     ld.id_load_detail, 
                     p.id_product, 
                     p.name_prod, 
                     ld.quantity, 
                     l.amount, 
                     
                     COALESCE((SELECT SUM(amount) FROM incomes WHERE id_load = l.id_load ), 0) AS advance,
                     
                     (SELECT COUNT(DISTINCT ll.id_flete) FROM loads ll WHERE ll.id_customer = l.id_customer) AS fletes_distincts,
                                     
                     CASE 
                         WHEN (SELECT COALESCE(SUM(i.amount), 0) FROM incomes i WHERE i.id_load = l.id_load) <= 0 
                             THEN 'Pendiente'
                         WHEN (SELECT COALESCE(SUM(i.amount), 0) FROM incomes i WHERE i.id_load = l.id_load) < l.amount 
                             THEN 'Adelanto'
                         ELSE 'Pagado'
                     END AS payment_stts
                 
                 FROM fletes AS f
                 INNER JOIN loads AS l ON l.id_flete = f.id_flete
                 INNER JOIN customers as c on c.id_customer = l.id_customer
                 INNER JOIN load_details AS ld ON l.id_load = ld.id_load
                 INNER JOIN products AS p ON ld.id_product = p.id_product
                 
                 WHERE l.id_customer = ?
                 ORDER BY f.start_date DESC;
                 """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. Inyectamos el ID del flete en la consulta
            ps.setInt(1, id_customer);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_load");

                    LoadDTO load = map.get(id);

                    if (load == null) {
                        load = new LoadDTO();
                        load.setIdLoad(id);
                        load.setIdFlete(rs.getInt("id_flete"));
                        load.setNum_doc(rs.getString("num_doc"));
                        load.setDate(rs.getDate("start_Date").toLocalDate());
                        load.setAmount(rs.getDouble("amount"));
                        load.setAdvance(rs.getDouble("advance"));
                        load.setPaymentStatus(rs.getString("payment_stts"));
                        load.setIdCustomer(rs.getInt("id_customer"));
                        load.setNameCustomer(rs.getString("fullname"));
                        load.setNameEESS(rs.getString("name_eess"));
                        load.setAddressEESS(rs.getString("address_eess"));
                        load.setFletesDistincs(rs.getInt("fletes_distincts"));

                        map.put(id, load);
                    }

                    LoadDetailDTO detail = new LoadDetailDTO(
                            rs.getInt("id_load_detail"),
                            rs.getInt("id_product"),
                            rs.getString("name_prod"),
                            rs.getDouble("quantity")
                    );

                    load.addDetail(detail);
                }
            }
        } 
        return new ArrayList<>(map.values());
    }

}
