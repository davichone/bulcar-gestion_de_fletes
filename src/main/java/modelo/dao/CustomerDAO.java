/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import java.util.List;
import modelo.domain.Customer;

/**
 *
 * @author drola
 */
public interface CustomerDAO extends CRUD<Customer> {

    // Método específico que no está en el CRUD genérico
    Customer searchForName(String placa);

    List<Customer> list() throws SQLException;
}
