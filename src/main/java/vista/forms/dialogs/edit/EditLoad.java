/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.dialogs.edit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import controllers.IncomeController;
import controllers.LoadController;
import controllers.LoadDetailController;
import modelo.domain.Load;
import modelo.domain.LoadDetail;
import modelo.dto.LoadDTO;
import modelo.dto.LoadDetailDTO;
import modelo.enums.ParamProducts;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import vista.forms.InicialForm;

/**
 *
 * @author drola
 */
public class EditLoad extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EditLoad.class.getName());
    private LoadDTO loadDTOSelectedGlob = null;
    private List<LoadDetailDTO> listLdDTO = new ArrayList<>();
    //controllers
    private IncomeController controllInc = new IncomeController();
    private LoadDetailController contollLd = new LoadDetailController();
    private LoadController controllLoad = new LoadController();

    LoadDetail objectDetailDB5 = new LoadDetail();
    LoadDetail objectDetailPremiun = new LoadDetail();
    LoadDetail objectDetailRegular = new LoadDetail();

    boolean flagDB5 = false;
    boolean flagPremiun = false;
    boolean flagRegular = false;

    /**
     * Creates new form NewJDialog1
     */
    public EditLoad(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public EditLoad(LoadDTO l) {
        this(new javax.swing.JFrame(), true);
        this.loadDTOSelectedGlob = l;
        listLdDTO = loadDTOSelectedGlob.getDetails();
        loadIcons();
        injectDataofLoad();

    }

    //mi methods
    private boolean esNumeroValido(String texto) {
    // 1. CAMBIO CLAVE: Si está vacío, ahora retorna TRUE. 
    // Significa que no cargó ese combustible, y tu método test() ya sabe qué hacer con eso.
    if (texto == null || texto.trim().isEmpty()) {
        return true; 
    }
    
    // 2. Si NO está vacío, verificamos que sea un número de verdad y que no sea negativo
    try {
        double valor = Double.parseDouble(texto.trim());
        if (valor < 0) return false; // Nada de galones negativos
        return true; 
    } catch (NumberFormatException e) {
        return false; // Explotó por letras o comas
    }
}
    
    private void injectDataofLoad() {
        lblCostumer.setText("Cliente: " + loadDTOSelectedGlob.getNameCustomer());
        lblNameEs.setText("EE.SS: "+loadDTOSelectedGlob.getNameEESS());
        List<LoadDetailDTO> l = loadDTOSelectedGlob.getDetails();

        //recorrer el aray para insertar los datos de detalles al Jdialog
        for (LoadDetailDTO loadDetail : l) {
            if (loadDetail.getProductName().equalsIgnoreCase("DB5")) {
                txtBD5.setText(String.valueOf(loadDetail.getVolume()));
                objectDetailDB5.setId(loadDetail.getId());
                objectDetailDB5.setIdProduct(loadDetail.getIdProduct());
                objectDetailDB5.setQuantity(loadDetail.getVolume());
                // System.err.println(" validacio de recorrido array id producto = "+ objectDetailDB5.getidProduct());

            } else if (loadDetail.getProductName().equalsIgnoreCase("G. Premiun")) {
                txtPremiun.setText(String.valueOf(loadDetail.getVolume()));
                objectDetailPremiun.setId(loadDetail.getId());
                objectDetailPremiun.setIdProduct(loadDetail.getIdProduct());
                objectDetailPremiun.setQuantity(loadDetail.getVolume());
            } else if (loadDetail.getProductName().equalsIgnoreCase("G. Regular")) {
                txtRegular.setText(String.valueOf(loadDetail.getVolume()));
                objectDetailRegular.setId(loadDetail.getId());
                objectDetailRegular.setIdProduct(loadDetail.getIdProduct());
                objectDetailRegular.setQuantity(loadDetail.getVolume());
            }
        }

//        if(loadSelectedGlob.getPaymentStatus().equalsIgnoreCase("Pagado")){
//            btnPagado.setSelected(true);
//        }else if(loadSelectedGlob.getPaymentStatus().equalsIgnoreCase("Pendiente")){
//            btnPagoPendiente.setSelected(true);
//        }else if(loadSelectedGlob.getPaymentStatus().equalsIgnoreCase("Adelanto")){
//            btnPagoAdelanto.setSelected(true);
//            double d = controllInc.getImportPending(loadSelectedGlob.getIdLoad());
//            txtAdelanto.setText(String.valueOf(d));
//        }
        txtAmount.setText(String.valueOf(loadDTOSelectedGlob.getAmount()));

        if (estaVacio(txtBD5)) {
            flagDB5 = true;                 //true to insert, False to update
        }

        if (estaVacio(txtPremiun)) {
            flagPremiun = true;
        }

        if (estaVacio(txtRegular)) {
            flagRegular = true;

        }
        
    }

    private void loadIcons() {
//        FontIcon iconGas = FontIcon.of(CarbonIcons.GAS_STATION);
//        iconGas.setIconSize(25);
//        lblGas.setIcon(iconGas);
    }

    //to proceed with the insetion mechanism in db
    private boolean itsTheSameValue(JTextField txt, double v1) {
        double v2 = Double.parseDouble(txt.getText());
        return v1 == v2;

    }

    private boolean estaVacio(JTextField campo) {
        // 1. .getText() obtiene el texto
        // 2. .trim() elimina espacios al inicio y al final
        // 3. .isEmpty() devuelve true si no queda nada
        return campo.getText().trim().isEmpty();
    }

    private LoadDetail getIdDetailProductAndQuantity(JTextField txt, int idProduct) {

        if (txt.getText().trim().isEmpty() == false) {
            LoadDetail ld = new LoadDetail(idProduct, Double.parseDouble(txt.getText()));
            return ld;
        }

        return null;

    }
    private boolean forSave(JTextField txt, boolean flag, LoadDetail ld, int idPro, int LoadId) {
    boolean exito = true; 

    // 1. OBTENEMOS LA CANTIDAD (Si está vacío, lo tratamos como 0)
    double quantity = 0.0;
    if (!estaVacio(txt)) {
        try {
            quantity = Double.parseDouble(txt.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El valor ingresado no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // 2. LÓGICA DE NEGOCIO (Aquí eliminamos el problema del cero)
    try {
        if (quantity > 0) {
            // TIENE CANTIDAD VÁLIDA: Guardamos o Actualizamos
            if (!flag) { // Ya existía en la BD (UPDATE)
                if (!itsTheSameValue(txt, ld.getQuantity())) {
                    ld.setQuantity(quantity);
                    ld.setIdProduct(idPro);
                    exito = contollLd.update(LoadId, ld);
                }
            } else { // Es nuevo (INSERT)
                ld.setQuantity(quantity);
                ld.setIdProduct(idPro);
                
                exito = contollLd.insertLoadDetail(LoadId, ld)>0;
            }
        } else {
            // LA CANTIDAD ES CERO (0) O LA CAJA ESTÁ VACÍA
            if (!flag) { 
                // Si la bandera es false, significa que ANTES existía en la BD.
                // Como ahora le pusieron 0 o lo borraron, ¡Debemos eliminarlo de la BD!
                ld.setIdProduct(idPro);
                exito = contollLd.delete(ld);
            }
            // (Si la bandera es true y pusieron 0, simplemente no hace nada, exito sigue siendo true).
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error BD (Combustible): " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ==========================================
    // PARTE 3: GESTIÓN DEL MONTO TOTAL (Queda igual)
    // ==========================================
    try {
        String amountText = txtAmount.getText().trim();
        if (!amountText.isEmpty()) {
            double amount2 = Double.parseDouble(amountText);
            
            if (amount2 != loadDTOSelectedGlob.getAmount()) {
                Load ldto = new Load();
                ldto.setId(loadDTOSelectedGlob.getIdLoad());
                ldto.setAmount(amount2);
                controllLoad.updateAmount(ldto);
                loadDTOSelectedGlob.setAmount(amount2); // Actualiza el global para no repetir
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El monto total no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } catch (SQLException i) {
        JOptionPane.showMessageDialog(this, "Error BD (Monto Total): " + i.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    return exito;
}    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblCostumer = new javax.swing.JLabel();
        lblNameEs = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtBD5 = new javax.swing.JTextField();
        txtPremiun = new javax.swing.JTextField();
        txtRegular = new javax.swing.JTextField();
        lblGas1 = new javax.swing.JLabel();
        lblGas2 = new javax.swing.JLabel();
        lblGas3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnEditLoad = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        lblCostumer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblCostumer.setText("Cliente:");

        lblNameEs.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNameEs.setText("EE.SS:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Producto/volumen:");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtBD5.setBackground(new java.awt.Color(204, 204, 204));
        txtBD5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBD5KeyTyped(evt);
            }
        });

        txtPremiun.setBackground(new java.awt.Color(204, 204, 204));
        txtPremiun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPremiunKeyTyped(evt);
            }
        });

        txtRegular.setBackground(new java.awt.Color(204, 204, 204));
        txtRegular.addActionListener(this::txtRegularActionPerformed);
        txtRegular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRegularKeyTyped(evt);
            }
        });

        lblGas1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGas1.setText("DB5:");

        lblGas2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGas2.setText("G. Premiun:");

        lblGas3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGas3.setText("G. Regular:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGas1)
                    .addComponent(lblGas3)
                    .addComponent(lblGas2))
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRegular, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBD5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPremiun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBD5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGas1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPremiun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGas2))
                        .addGap(9, 9, 9)
                        .addComponent(txtRegular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblGas3))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Precio flete:");

        txtAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnEditLoad.setBackground(java.awt.SystemColor.activeCaption);
        btnEditLoad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEditLoad.setText("Editar");
        btnEditLoad.addActionListener(this::btnEditLoadActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Editar Carga");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNameEs, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(68, 68, 68)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel2)
                                .addComponent(lblCostumer)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(99, 99, 99)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(125, 125, 125)
                            .addComponent(btnCancel)
                            .addGap(82, 82, 82)
                            .addComponent(btnEditLoad))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(188, 188, 188)
                            .addComponent(jLabel1))))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(47, 47, 47)
                .addComponent(lblCostumer)
                .addGap(18, 18, 18)
                .addComponent(lblNameEs)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnEditLoad))
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnEditLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditLoadActionPerformed
       // 1. PRIMERA ADUANA: Validar que el objeto global exista
    if (loadDTOSelectedGlob == null) {
        JOptionPane.showMessageDialog(this, "Error interno: No hay un registro de carga seleccionado.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        return; // Abortamos la misión
    }

    // 2. SEGUNDA ADUANA: Validar que los campos no estén vacíos y sean números válidos
    // (Usa el método auxiliar que te pongo más abajo)
    if (!esNumeroValido(txtBD5.getText()) || !esNumeroValido(txtPremiun.getText()) || !esNumeroValido(txtRegular.getText())) {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa cantidades válidas en todos los combustibles. (Usa 0 si no hay carga).", "Validación", JOptionPane.WARNING_MESSAGE);
        return; // Abortamos para que el usuario corrija
    }

    // 3. EJECUCIÓN: Si pasó las aduanas, intentamos guardar
    boolean exito1 = forSave(txtBD5, flagDB5, objectDetailDB5, ParamProducts.idDB5.getId(), loadDTOSelectedGlob.getIdLoad());
    boolean exito2 = forSave(txtPremiun, flagPremiun, objectDetailPremiun, ParamProducts.idGPremium.getId(), loadDTOSelectedGlob.getIdLoad());
    boolean exito3 = forSave(txtRegular, flagRegular, objectDetailRegular, ParamProducts.idGRegular.getId(), loadDTOSelectedGlob.getIdLoad());
    
    // 4. TERCERA ADUANA (UX): Manejo de la Interfaz
    if (exito1 && exito2 && exito3) {
        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.dispose(); // SOLO cerramos la ventana si TODO salió perfecto
    } else {
        // Si falló alguno, avisamos pero NO cerramos el JDialog (this.dispose)
        JOptionPane.showMessageDialog(this, "Error al guardar en la Base de Datos. Por favor, revisa tu conexión e intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
    }


        
        
//        // divArrayLoadDetailsAndEquals();
//        boolean exito1 =test(txtBD5, flagDB5, objectDetailDB5, ParamProducts.idDB5.getId(), loadDTOSelectedGlob.getIdLoad());
//        boolean exito2 =test(txtPremiun, flagPremiun, objectDetailPremiun, ParamProducts.idGPremium.getId(), loadDTOSelectedGlob.getIdLoad());
//        boolean exito3 =test(txtRegular, flagRegular, objectDetailRegular, ParamProducts.idGRegular.getId(), loadDTOSelectedGlob.getIdLoad());
//        
//        if(exito1&&exito2&&exito3){
//            JOptionPane.showMessageDialog(this, "Datos actualizados");
//        }else{
//            JOptionPane.showMessageDialog(this, "Error en guardar uno de los datos");
//        }
//        this.dispose();
       

    }//GEN-LAST:event_btnEditLoadActionPerformed

    private void txtBD5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBD5KeyTyped
        char c = evt.getKeyChar();
    // Si no es un número y no es un punto decimal...
    if (!Character.isDigit(c) && c != '.') {
        evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
    }
    
    // Evitar que pongan dos puntos decimales (ej: "22..5")
    if (c == '.' && txtBD5.getText().contains(".")) {
        evt.consume();
    }
    }//GEN-LAST:event_txtBD5KeyTyped

    private void txtPremiunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPremiunKeyTyped
        char c = evt.getKeyChar();
    // Si no es un número y no es un punto decimal...
    if (!Character.isDigit(c) && c != '.') {
        evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
    }
    
    // Evitar que pongan dos puntos decimales (ej: "22..5")
    if (c == '.' && txtPremiun.getText().contains(".")) {
        evt.consume();
    }
    }//GEN-LAST:event_txtPremiunKeyTyped

    private void txtRegularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRegularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRegularActionPerformed

    private void txtRegularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegularKeyTyped
        char c = evt.getKeyChar();
    // Si no es un número y no es un punto decimal...
    if (!Character.isDigit(c) && c != '.') {
        evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
    }
    
    // Evitar que pongan dos puntos decimales (ej: "22..5")
    if (c == '.' && txtRegular.getText().contains(".")) {
        evt.consume();
    }
    }//GEN-LAST:event_txtRegularKeyTyped

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
        char c = evt.getKeyChar();
    // Si no es un número y no es un punto decimal...
    if (!Character.isDigit(c) && c != '.') {
        evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
    }
    
    // Evitar que pongan dos puntos decimales (ej: "22..5")
    if (c == '.' && txtAmount.getText().contains(".")) {
        evt.consume();
    }
    }//GEN-LAST:event_txtAmountKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
      java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                EditLoad dialog = new EditLoad(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEditLoad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblCostumer;
    private javax.swing.JLabel lblGas1;
    private javax.swing.JLabel lblGas2;
    private javax.swing.JLabel lblGas3;
    private javax.swing.JLabel lblNameEs;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBD5;
    private javax.swing.JTextField txtPremiun;
    private javax.swing.JTextField txtRegular;
    // End of variables declaration//GEN-END:variables
}
