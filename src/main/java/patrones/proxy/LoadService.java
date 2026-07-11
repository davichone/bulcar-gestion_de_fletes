/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package patrones.proxy;

import modelo.domain.Flete;
import modelo.domain.Load;
/**
 *
 * @author drola
 */

public interface LoadService {
    int guardarCargaCompleta(Flete f, Load l);
    String getStatusPaymentLoad(int idLoad);
}
