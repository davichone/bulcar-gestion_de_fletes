/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.domain.transaction;

import java.time.LocalDate;
import modelo.domain.transaction.CategoryM;
import modelo.domain.transaction.Transaction;

/**
 *
 * @author drola
 */

public abstract class TransactionCreator {

    // Este es el "Factory Method"
    public abstract Transaction createTransaction();

    // metodo que utiliza la fábrica para inicializar propiedades comunes
    public Transaction prepararTransaccion(double monto, String descripcion, CategoryM categoria) {
        Transaction t = createTransaction();
        t.setAmount(monto);
        t.setDescription(descripcion);
        t.setDate(LocalDate.now());
        t.setCategory(categoria);
        return t;
    }
}
