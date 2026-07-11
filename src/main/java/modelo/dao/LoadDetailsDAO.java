/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.SQLException;
import java.util.List;
import modelo.domain.LoadDetail;
import modelo.dto.LoadDTO;
import modelo.dto.LoadDetailDTO;

/**
 *
 * @author drola
 */
public interface LoadDetailsDAO extends CRUD<LoadDetail> {

    int insertLoadDetail(int idLoadCurrent, LoadDetail ld);

    boolean update(int idLoad, LoadDetail lddto) throws SQLException;
}
