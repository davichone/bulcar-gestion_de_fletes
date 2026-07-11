/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.util.List;


/**
 * @author drola
 * Interfaz genérica para operaciones básicas de base de datos
 * @param <T> Representa la Entidad (Carga, Vehiculo, etc.)
 */

public interface CRUD<T> {
    boolean insert(T objeto);
    boolean modificar(T objeto);
    boolean eliminar(Object id);
    
    List<T> listar();
    T buscarPorId(Object id);
}
