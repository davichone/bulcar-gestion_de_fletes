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
import modelo.dao.CategoryMDAO;
import modelo.domain.transaction.CategoryM;

/**
 *
 * @author drola
 */
public class CategoryMDAOImpl implements CategoryMDAO {

    @Override
    public boolean insert(CategoryM objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(CategoryM objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CategoryM> listar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public CategoryM buscarPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CategoryM> listExpense() throws SQLException {
        List<CategoryM> list = new ArrayList<>();
        String sql = "SELECT*FROM categories_expenses";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryM c = new CategoryM();
                c.setId(rs.getInt("id_cat_exp"));
                c.setName(rs.getString("name_cat"));
                list.add(c);
            }

            System.err.println("successfully select -> list of Categories");
            return list;

        } 
    }

    @Override
    public List<CategoryM> listIncomes() throws SQLException {
        List<CategoryM> list = new ArrayList<>();
        String sql = "SELECT*FROM categories_incomes";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryM c = new CategoryM();
                c.setId(rs.getInt("id_cat_inc"));
                c.setName(rs.getString("name_cat"));
                list.add(c);
            }

            System.err.println("successfully select -> list of Categories");
            return list;

        } 
    }

}
