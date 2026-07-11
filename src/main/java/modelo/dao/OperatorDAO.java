/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;
import java.util.List;
import modelo.domain.Operator;
/**
 *
 * @author drola
 */
public interface OperatorDAO extends CRUD<Operator>{
    
    public List<Operator> getLowDataList();
    
}
