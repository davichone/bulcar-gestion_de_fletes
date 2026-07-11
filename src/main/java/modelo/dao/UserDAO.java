/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import modelo.domain.User;

/**
 *
 * @author drola
 */
public interface UserDAO extends CRUD<Object>{
    User login(String username, String password) throws SQLException;
}
