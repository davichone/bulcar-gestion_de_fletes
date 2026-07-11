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
import modelo.dao.ExpenseDAO;
import modelo.domain.Operator;
import modelo.domain.transaction.CategoryM;
import modelo.domain.transaction.Expense;
import modelo.dto.FinanceOperatorDTO;

/**
 *
 * @author drola
 */
public class ExpenseDAOImpl implements ExpenseDAO {

    @Override
    public boolean insert(Expense o) {
        String sql = "INSERT INTO expenses (id_flete, id_cat_exp, amount, expense_date, description) VALUES (?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, o.getIdFlete());
            ps.setInt(2, o.getCategory().getId());
            ps.setDouble(3, o.getAmount());
            ps.setDate(4, java.sql.Date.valueOf(o.getDate()));
            ps.setString(5, o.getDescription());
            System.err.println("successfully inserted an new expense");
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new expense");
            return false;
        }
    }

    @Override
    public boolean modificar(Expense objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //obtain list expense for finance global
    @Override
    public List<Expense> listar() {
        List<Expense> list = new ArrayList<>();

        String sql = """
                      SELECT e.id_expense, e.id_flete, c.id_cat_exp, c.name_cat, e.amount, e.expense_date, e.description 
                      FROM expenses AS e
                      INNER JOIN categories_expenses AS c ON c.id_cat_exp = e.id_cat_exp
                      WHERE expense_date >= date_trunc('month', CURRENT_DATE) 
                      AND expense_date < date_trunc('month', CURRENT_DATE) + INTERVAL '1 month'
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Expense i = new Expense();
                i.setId(rs.getInt("id_expense"));
                i.setIdFlete(rs.getInt("id_flete"));
                CategoryM cat = new CategoryM();
                cat.setId(rs.getInt("id_cat_exp"));
                cat.setName(rs.getString("name_cat"));
                i.setCategory(cat);
                i.setAmount(rs.getDouble("amount"));
                LocalDate fecha = rs.getObject("expense_date", LocalDate.class);
                i.setDate(fecha);
                i.setDescription(rs.getString("description"));
                list.add(i);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
            e.printStackTrace();

        }

        return list;
    }

    @Override
    public Expense buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double getImportTotal(int id) throws SQLException {
        String sql = """
                     SELECT COALESCE(SUM(ex.amount), 0) AS importe_total
                                          FROM expenses AS ex
                                          INNER JOIN fletes AS f ON ex.id_flete = f.id_flete
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
            System.err.println("Error al calcular total expense en proceso: " + e.getMessage());
            e.printStackTrace();
            throw e; // Es bueno relanzarla para que el Controller sepa que falló
        }

        return total;
    }

    @Override
    public double getImportTotal() throws SQLException {
        String sql = """
                     SELECT COALESCE(SUM(ex.amount), 0) AS importe_total
                    FROM expenses AS ex
                    WHERE expense_date >= date_trunc('month', CURRENT_DATE) 
                     AND expense_date < date_trunc('month', CURRENT_DATE) + INTERVAL '1 month'
                     """;
        double total = 0;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total expense en proceso: " + e.getMessage());
            e.printStackTrace();
            throw e; // Es bueno relanzarla para que el Controller sepa que falló
        }

        return total;
    }

    @Override
    public List<Expense> listar(int idFleteCurrent) {
        List<Expense> list = new ArrayList<>();

        String sql = """
                       SELECT e.id_expense, e.id_flete, c.id_cat_exp, c.name_cat, e.amount, e.expense_date, e.description 
                       FROM expenses AS e
                       INNER JOIN categories_expenses AS c ON c.id_cat_exp = e.id_cat_exp
                       WHERE e.id_flete = ?
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFleteCurrent);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Expense i = new Expense();
                    i.setId(rs.getInt("id_expense"));
                    i.setIdFlete(rs.getInt("id_flete"));
                    CategoryM cat = new CategoryM();
                    cat.setId(rs.getInt("id_cat_exp"));
                    cat.setName(rs.getString("name_cat"));
                    i.setCategory(cat);
                    i.setAmount(rs.getDouble("amount"));
                    LocalDate fecha = rs.getObject("expense_date", LocalDate.class);
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

    @Override
    public List<FinanceOperatorDTO> listForOperatorManager(int idOperator) {
        List<FinanceOperatorDTO> list = new ArrayList<>();

        String sql = """
                        SELECT e.id_expense, e.id_flete, e.id_cat_exp, c.name_cat, e.amount, e.expense_date, e.description
                        FROM expenses AS e
                        INNER JOIN categories_expenses AS c ON e.id_cat_exp = c.id_cat_exp
                        WHERE id_operator = ?
                     """;

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOperator);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FinanceOperatorDTO i = new FinanceOperatorDTO();
                    Operator o = new Operator();
                    o.setId(idOperator);
                    i.setOperator(o);
                    i.setId(rs.getInt("id_expense"));
                    i.setIdFlete(rs.getInt("id_flete"));
                    CategoryM cat = new CategoryM();
                    cat.setId(rs.getInt("id_cat_exp"));
                    cat.setName(rs.getString("name_cat"));
                    i.setCategory(cat);
                    i.setAmount(rs.getDouble("amount"));
                    LocalDate fecha = rs.getObject("expense_date", LocalDate.class);
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

    @Override
    public boolean InsertFinanceOpe(FinanceOperatorDTO f) {
        String sql = "INSERT INTO expenses (id_flete, id_cat_exp, amount, expense_date, description, id_operator) VALUES (?,?,?,?,?,?)";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            
            if (f.getIdFlete() <= 0) {
                ps.setNull(1, java.sql.Types.INTEGER); // Esto evita el error de FK
            } else {
                ps.setInt(1, f.getIdFlete());
            }

            ps.setInt(2, f.getCategory().getId());
            ps.setDouble(3, f.getAmount());
            ps.setDate(4, java.sql.Date.valueOf(f.getDate()));
            ps.setString(5, f.getDescription());
            ps.setInt(6, f.getOperator().getId());
            System.err.println("successfully inserted an new expense with operator id");
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error Insert new expense with operator id");
            return false;
        }
    }

    @Override
    public boolean UpdateWithOpe(FinanceOperatorDTO f) {
        String sql = "UPDATE expenses SET amount = ?, description = ? WHERE id_expense = ?";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, f.getAmount());
            ps.setString(2, f.getDescription());
            ps.setInt(3, f.getId());
            System.err.println("successfully update an new expense with operator id");
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();  // tool debuging console
            System.err.println("error update new expense with operator id");
            return false;
        }
    }

}
