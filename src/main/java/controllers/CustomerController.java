/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.dao.CustomerDAO;
import modelo.dao_Impl.CustomerDAOImpl;
import modelo.domain.Customer;

/**
 *
 * @author drola
 */
public class CustomerController {

    private CustomerDAO dao = new CustomerDAOImpl();

    //method for insert customer to dataBase
    public boolean registerCustomer(Customer customer) {
        //validacion de datos
        if (customer.getDni().isEmpty() || customer.getRucES().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Dni y Ruc obligatorios");
            return false;
        }

        dao.insert(customer);
        JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente");
        return true;
    }

    public List<Customer> getListCusForNewFlete() {

        List<Customer> list = dao.listar();
        List<Customer> listReady = new ArrayList<>();

        for (Customer c : list) {
            if (!c.getNameES().isEmpty()) {
                listReady.add(c);
            }
        }
        return listReady;
    }
    
    
    //all customers
     public List<Customer> getList() throws SQLException{

        List<Customer> list = dao.list();
        List<Customer> listReady = new ArrayList<>();

        for (Customer c : list) {
            if (!c.getNameES().isEmpty()) {
                listReady.add(c);
            }
        }
        return listReady;
    }

}
