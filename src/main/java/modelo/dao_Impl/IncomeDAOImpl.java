/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao_Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.util.conexion.ConexionBD;
import modelo.dao.IncomeDAO;
import modelo.domain.Vehicle;
import modelo.domain.transaction.CategoryM;
import modelo.domain.transaction.Expense;
import modelo.domain.transaction.Income;
import modelo.dto.DebtDTO;

/**
 *
 * @author drola
 */
public class IncomeDAOImpl implements IncomeDAO {

    //insert new income of load current
    @Override
    public boolean insert(Income objeto) {
        String sql = "INSERT INTO incomes (id_load, id_cat_inc, amount, income_date, description) VALUES (?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            if(objeto.getIdLoad() > 0){
                ps.setInt(1, objeto.getIdLoad());
            }else{
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            
            ps.setInt(2, objeto.getCategory().getId());
            ps.setDouble(3, objeto.getAmount());
            ps.setDate(4, java.sql.Date.valueOf(objeto.getDate()));
            ps.setString(5, objeto.getDescription());
            System.err.println("successfully inserted an new income");
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new income");
            System.getLogger(CustomerDAOImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    @Override
    public boolean modificar(Income objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //lis of incomes month
    @Override
    public List<Income> listar() {
        List<Income> list = new ArrayList<>();

        String sql = """
                      SELECT i.id_income ,i.id_load, c.id_cat_inc, c.name_cat, i.amount, i.income_date, i.description 
                      FROM incomes AS i
                      INNER JOIN categories_incomes AS c ON c.id_cat_inc = i.id_cat_inc
                      WHERE income_date >= date_trunc('month', CURRENT_DATE) 
                      AND income_date < date_trunc('month', CURRENT_DATE) + INTERVAL '1 month'
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Income i = new Income();
                i.setId(rs.getInt("id_income"));
                i.setIdLoad(rs.getInt("id_load"));
                CategoryM cat = new CategoryM();
                cat.setId(rs.getInt("id_cat_inc"));
                cat.setName(rs.getString("name_cat"));
                i.setCategory(cat);
                i.setAmount(rs.getDouble("amount"));
                LocalDate fecha = rs.getObject("income_date", LocalDate.class);
                i.setDate(fecha);
                i.setDescription(rs.getString("description"));
                list.add(i);
            }

        } catch (SQLException e) {
            System.err.println("Error al calcular total en proceso: " + e.getMessage());
            e.printStackTrace();

        }

        return list;
    }

    @Override
    public Income buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //Obtain the total calculated amount of a charge to determine the payment status.
    @Override
    public double getCalculusAmountByID(int idLoad) throws SQLException {
        String sql = "SELECT SUM(amount) FROM incomes WHERE id_load = ?";
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
            System.err.println("Error al calcular total en proceso: " + e.getMessage());
            e.printStackTrace();
            throw e; // Es bueno relanzarla para que el Controller sepa que falló
        }

        return total;
    }

    //obtain the total import of flete current
    @Override
    public double getImportTotal(int id) throws SQLException {
        String sql = """
                     SELECT COALESCE(SUM(l.amount), 0) AS importe_total
                     FROM loads l
                     INNER JOIN fletes f ON l.id_flete = f.id_flete
                     WHERE f.id_flete = ?;
                     """;
        double total = 0;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. PRIMERO pasamos el parámetro
            ps.setInt(1, id);

            // 3. DESPUÉS ejecutamos la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 4. Obtenemos el resultado de la suma (es la columna 1)
                    total = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total en proceso: " + e.getMessage());
            e.printStackTrace();
            throw e; // Es bueno relanzarla para que el Controller sepa que falló
        }

        return total;

    }

    //obtain impor total global
    @Override
    public double getImportTotal() throws SQLException {
        String sql = """
                     SELECT COALESCE(SUM(l.amount), 0) AS importe_total
                     FROM loads l
                     INNER JOIN fletes f ON l.id_flete = f.id_flete
                     WHERE start_date >= date_trunc('month', CURRENT_DATE) 
                     		 AND start_date < date_trunc('month', CURRENT_DATE) + INTERVAL '1 month'
                     """;
        double total = 0;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al calcular total en proceso: " + e.getMessage());
            e.printStackTrace();
            throw e; // Es bueno relanzarla para que el Controller sepa que falló
        }

        return total;
    }

    @Override
    public double getImportPending(int id) throws SQLException {
        String sql = """
                     SELECT COALESCE(amount) AS amount FROM incomes WHERE id_load = ?
                     """;
        double total = 0;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. PRIMERO pasamos el parámetro
            ps.setInt(1, id);

            // 3. DESPUÉS ejecutamos la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 4. Obtenemos el resultado de la suma (es la columna 1)
                    total = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total en proceso: " + e.getMessage());
            e.printStackTrace();
            throw e; // Es bueno relanzarla para que el Controller sepa que falló
        }

        return total;
    }

    @Override
    public List<Income> listar(int idFleteCurrent) {
        List<Income> list = new ArrayList<>();

        String sql = """
                       SELECT i.id_income, i.id_load, c.id_cat_inc, c.name_cat, i.amount, i.income_date, i.description 
                       FROM incomes AS i
                       INNER JOIN categories_incomes AS c ON c.id_cat_inc = i.id_cat_inc
                       INNER JOIN loads AS l ON i.id_load = l.id_load
                       INNER JOIN fletes As f ON l.id_flete = f.id_flete
                       WHERE f.id_flete = ?
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFleteCurrent);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Income i = new Income();
                    i.setId(rs.getInt("id_income"));
                    i.setIdLoad(rs.getInt("id_load"));
                    CategoryM cat = new CategoryM();
                    cat.setId(rs.getInt("id_cat_inc"));
                    cat.setName(rs.getString("name_cat"));
                    i.setCategory(cat);
                    i.setAmount(rs.getDouble("amount"));
                    LocalDate fecha = rs.getObject("income_date", LocalDate.class);
                    i.setDate(fecha);
                    i.setDescription(rs.getString("description"));
                    list.add(i);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
            e.printStackTrace();

        }

        return list;
    }

    //list that customers not payed
    @Override
    public List<DebtDTO> listNotPayed() {
        List<DebtDTO> list = new ArrayList<>();

        String sql = """
                     SELECT 
                          l.id_load, 
                          c.fullname,
                          f.start_date,
                          det.total_volumen AS volume,
                          l.amount AS price_flete, 
                          COALESCE(SUM(i.amount), 0) AS total_abonado, 
                          (l.amount - COALESCE(SUM(i.amount), 0)) AS balance 
                      FROM loads AS l
                      INNER JOIN fletes AS f ON f.id_flete = l.id_flete
                      INNER JOIN customers AS c ON c.id_customer = l.id_customer
                      -- Subconsulta para el volumen (aislada para evitar multiplicar filas)
                      INNER JOIN (
                          SELECT id_load, SUM(quantity) AS total_volumen 
                          FROM load_details 
                          GROUP BY id_load
                      ) AS det ON l.id_load = det.id_load
                      -- Unimos los ingresos (abonos)
                      LEFT JOIN incomes AS i ON i.id_load = l.id_load
                      --WHERE l.id_load = 86 
                      GROUP BY 
                          l.id_load, 
                          c.fullname, 
                          f.start_date, 
                          l.amount, 
                          det.total_volumen
                      HAVING (l.amount - COALESCE(SUM(i.amount), 0)) > 0
                      ORDER BY balance DESC;
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DebtDTO i = new DebtDTO();
                i.setIdLoad(rs.getInt("id_load"));
                i.setNameCustomer(rs.getString("fullname"));
                LocalDate fecha = rs.getObject("start_date", LocalDate.class);
                i.setDate(fecha);
                i.setVolumen(rs.getDouble("volume"));
                i.setPriceFlete(rs.getDouble("price_flete"));
                i.setAbono(rs.getDouble("total_abonado"));
                i.setBalance(rs.getDouble("balance"));
                list.add(i);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar deudores: " + e.getMessage());
            e.printStackTrace();

        }

        return list;
    }
}
