/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.dialogs.register;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import controllers.FleteController;
import controllers.OperatorController;
import controllers.PlantController;
import controllers.VehicleController;
import java.sql.SQLException;
import modelo.domain.Customer;
import modelo.domain.Flete;
import modelo.domain.Operator;
import modelo.domain.Plant;
import modelo.domain.Vehicle;
import modelo.dto.DocFleteDTO;
import modelo.enums.ParamFlete;
import modelo.util.ConfigManager;
import modelo.util.FleteSubject;
import modelo.util.IconManager;
import org.kordamp.ikonli.dashicons.Dashicons;
import patrones.facade.NuevoFleteFacade;
import vista.forms.InicialForm;
import vista.forms.minDialogs.UpdateKm;

/**
 *
 * @author drola
 */
public class RegisFlete extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegisFlete.class.getName());
    private NuevoFleteFacade nuevoFleteFacade = new NuevoFleteFacade();

    private Vehicle vehicleGlob = null;
    private Operator operatorGlob = null;

    private Plant plantGlob = null;

    private Customer customerGlob = null;

    /**
     *
     */
    public RegisFlete(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadIcons();
        //for new flete
        injectVehiclesCbx();
        injectOperatorTanksComboBox();
        injectPlantsComboBox();

    }

    private void loadIcons() {
        IconManager.setIkonliIcon(btnEditKm, Dashicons.ADMIN_GENERIC, 15);
        txtStart_Date.setText(ConfigManager.getInstance().getFechaFormateadaUI());
    }

    private void injectVehiclesCbx() {

        // limpiamos el combo por si acaso
        cbxVehicles.removeAllItems();

        // Agregamos una opción inicial
        Vehicle placeholder = new Vehicle();
        placeholder.setId(-1);
        placeholder.setManufacturer("--- Seleccione"); // esto irá a this.manufacturer
        placeholder.setModel("Vehículo ---");          // esto irá a this.model

        cbxVehicles.addItem(placeholder);

        for (Vehicle vehicles : nuevoFleteFacade.obtenerVehiculos()) {
            cbxVehicles.addItem(vehicles);
        }

    }

    //method for charge list op tanks
    private void injectOperatorTanksComboBox() {

        cbxOperators.removeAllItems();

        // Agregamos una opción inicial
        Operator placeholder = new Operator();
        placeholder.setId(-1);
        placeholder.setFullname("--- Seleccione un operador --"); // esto irá a this.manufacturer
        cbxOperators.addItem(placeholder);

        for (Operator op : nuevoFleteFacade.obtenerOperadores()) {
            cbxOperators.addItem(op);
        }

    }

    //method for charge list of plants
    private void injectPlantsComboBox() {
        // Limpiamos el combo por si acaso
        cbxPlant.removeAllItems();

        // Agregamos una opción inicial
        Plant placeholder = new Plant();
        placeholder.setId(-1);
        placeholder.setName("--- Seleccione una planta --");

        cbxPlant.addItem(placeholder);

        for (Plant pla : nuevoFleteFacade.obtenerPlantas()) {
            cbxPlant.addItem(pla);
        }

    }

    private void validarInputUsuario() {
        Object selectionVeh = cbxVehicles.getSelectedItem();
        Object selectionOpe = cbxOperators.getSelectedItem();
        Object selectionPla = cbxPlant.getSelectedItem();
        //clausula de guarda
        if (!(selectionVeh instanceof Vehicle) || ((Vehicle) selectionVeh).getId() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un Vehículo válido.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!(selectionOpe instanceof Operator) || ((Operator) selectionOpe).getId() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un Operador válido.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!(selectionPla instanceof Plant) || ((Plant) selectionPla).getId() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una Planta válida.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // validar el campo de Kilometraje (para asegurar que no esté vacío y sea un número)
        int kmInicial = 0;
        try {
            kmInicial = Integer.parseInt(txtKmStart.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El campo de Kilometraje Inicial debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // asignar a variables globales
        vehicleGlob = (Vehicle) selectionVeh;
        operatorGlob = (Operator) selectionOpe;
        plantGlob = (Plant) selectionPla;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbxVehicles = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtKmStart = new javax.swing.JTextField();
        btnEditKm = new javax.swing.JButton();
        cbxOperators = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cbxPlant = new javax.swing.JComboBox<>();
        txtStart_Date = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnSaveNewFlete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(397, 411));
        setMinimumSize(new java.awt.Dimension(397, 411));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setMaximumSize(new java.awt.Dimension(397, 411));
        jPanel1.setMinimumSize(new java.awt.Dimension(397, 411));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel7.setText("Nuevo flete");

        jLabel10.setText("Vehiculo:");

        cbxVehicles.setMaximumSize(new java.awt.Dimension(170, 22));
        cbxVehicles.setMinimumSize(new java.awt.Dimension(170, 22));
        cbxVehicles.setPreferredSize(new java.awt.Dimension(170, 22));
        cbxVehicles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxVehiclesActionPerformed(evt);
            }
        });

        jLabel14.setText("Km inicial:");

        txtKmStart.setEnabled(false);
        txtKmStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmStartActionPerformed(evt);
            }
        });

        btnEditKm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditKmActionPerformed(evt);
            }
        });

        cbxOperators.setMaximumSize(new java.awt.Dimension(170, 22));
        cbxOperators.setMinimumSize(new java.awt.Dimension(170, 22));
        cbxOperators.setPreferredSize(new java.awt.Dimension(170, 22));
        cbxOperators.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOperatorsActionPerformed(evt);
            }
        });

        jLabel9.setText("Operador:");

        jLabel15.setText("Planta:");

        cbxPlant.setMaximumSize(new java.awt.Dimension(170, 22));
        cbxPlant.setMinimumSize(new java.awt.Dimension(170, 22));
        cbxPlant.setPreferredSize(new java.awt.Dimension(170, 22));
        cbxPlant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPlantActionPerformed(evt);
            }
        });

        jLabel13.setText("Fecha:");

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnSaveNewFlete.setBackground(new java.awt.Color(102, 153, 255));
        btnSaveNewFlete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSaveNewFlete.setText("Crear ");
        btnSaveNewFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNewFleteActionPerformed(evt);
            }
        });

        jLabel1.setText("*Ajuste el kilometraje si es necesario.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(62, 62, 62)
                        .addComponent(btnSaveNewFlete)
                        .addGap(99, 99, 99))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel15)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxOperators, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStart_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxPlant, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKmStart, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditKm, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(cbxVehicles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(87, 87, 87))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(cbxVehicles, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditKm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(txtKmStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbxOperators, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cbxPlant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtStart_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveNewFlete)
                    .addComponent(jButton1))
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxVehiclesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVehiclesActionPerformed
        //insert km of vehicle selected    
        // 1. Obtenemos el objeto seleccionado
        Object selection = cbxVehicles.getSelectedItem();

        // 2. Verificamos que sea un vehículo real y no el placeholder
        if (selection instanceof Vehicle) {
            Vehicle v = (Vehicle) selection;

            if (v.getId() != -1) {
                // 3. Seteamos el kilometraje en el campo de texto
                txtKmStart.setText(String.valueOf(v.getKm()));
            } else {
                // Si selecciona el placeholder, limpiamos el campo
                txtKmStart.setText("");
            }
        }


    }//GEN-LAST:event_cbxVehiclesActionPerformed

    private void cbxOperatorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOperatorsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxOperatorsActionPerformed

    private void cbxPlantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPlantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPlantActionPerformed

    private void txtKmStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmStartActionPerformed

    private void btnSaveNewFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNewFleteActionPerformed
        validarInputUsuario();

        //uso del facade para hacer el trabajo pesado
        if (nuevoFleteFacade.crearNuevoFlete(vehicleGlob, operatorGlob, plantGlob)) {
            FleteSubject.getInstance().notifyObservers();
            this.dispose();
        }


    }//GEN-LAST:event_btnSaveNewFleteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnEditKmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditKmActionPerformed

        Object selectionVeh = cbxVehicles.getSelectedItem();
        if (!(selectionVeh instanceof Vehicle) || ((Vehicle) selectionVeh).getId() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un Vehículo válido.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        vehicleGlob = (Vehicle) selectionVeh;

        UpdateKm w = new UpdateKm(this, vehicleGlob);
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
        injectVehiclesCbx();
    }//GEN-LAST:event_btnEditKmActionPerformed

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
                RegisFlete dialog = new RegisFlete(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnEditKm;
    private javax.swing.JButton btnSaveNewFlete;
    private javax.swing.JComboBox<Operator> cbxOperators;
    private javax.swing.JComboBox<Plant> cbxPlant;
    private javax.swing.JComboBox<Vehicle> cbxVehicles;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtKmStart;
    private javax.swing.JTextField txtStart_Date;
    // End of variables declaration//GEN-END:variables
}
