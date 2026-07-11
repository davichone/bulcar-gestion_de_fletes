/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import modelo.domain.User;
import modelo.dao_Impl.UserDAOImpl;

/**
 *
 * @author drola
 */
public class UserController {
    private final UserDAOImpl dao = new UserDAOImpl();

    public User validateLogin(String username, String password) {
        return dao.login(username, password);
    }
}
