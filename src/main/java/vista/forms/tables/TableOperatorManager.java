/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.tables;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import vista.forms.dialogs.edit.EditRowDebtors;
import java.awt.Component;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import controllers.ExpenseController;
import controllers.IncomeController;
import modelo.domain.Flete;
import modelo.domain.Operator;

import modelo.domain.transaction.Expense;
import modelo.domain.transaction.Income;
import modelo.dto.DebtDTO;
import modelo.dto.FinanceOperatorDTO;
import modelo.util.ExcelExporter;
import modelo.util.IconManager;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import vista.forms.InicialForm;
import vista.forms.cards.CardLoad;
import vista.forms.dialogs.edit.AddImportOperator;
import vista.forms.dialogs.edit.EditRowOperatorManager;

/**
 *
 * @author drola
 */
public class TableOperatorManager extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TableOperatorManager.class.getName());

    private ExpenseController controllExpense = new ExpenseController();
    private FinanceOperatorDTO financeOperatorDTO;
    private Operator operator;
    private InicialForm inicialForm;
    private Flete fleteCurrent = null;

    /**
     * Creates new form TableIncomes
     * @param parent
     * @param modal
     */
    public TableOperatorManager(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public TableOperatorManager(InicialForm inicialForm, Operator operator) {
        this(inicialForm, true);
        this.inicialForm = inicialForm;
        this.operator = operator;
        injectTable();
    }

    //mi methods
    private void injectData() {
        lblTitleWithOperator.setText("Movimientos de " + operator.getFullname());
        IconManager.setSvgIcon(btnExcel, "logos/excel.svg", 25);
        IconManager.setIkonliIcon(btnBack, BoxiconsRegular.ARROW_BACK, 25);
    }

    private void openJdialog(FinanceOperatorDTO financeOperatorDTO) {

// Creamos la instancia del diálogo (puedes pasarle los datos por constructor)
        financeOperatorDTO.setOperator(operator);
        EditRowOperatorManager w = new EditRowOperatorManager(financeOperatorDTO, inicialForm);
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
        // PRUEBA DE FUEGO: Si esto no sale en consola, el problema es la MODALIDAD
        //System.out.println("Saliendo de EditDebtors... Iniciando refresco.");

        //this.injectTable(); // Refresca la tabla actual
        // Al cerrar el diálogo, refrescamos la tabla para ver los cambios
        this.injectTable();
    }

    private void listenerRow() {
        // 1. Preguntamos qué fila está sombreada en la tabla
        int fila = jTableOperatorManager.getSelectedRow();

        // 2. Validamos que no sea -1 (o sea, que haya algo seleccionado)
        if (fila != -1) {
            // 3. RECOLECTAMOS los datos en ese instante
            FinanceOperatorDTO f = new FinanceOperatorDTO();

            // Sacamos los datos de las columnas (ajusta los números 0,1,2.. si es necesario)
            f.setId((int) jTableOperatorManager.getValueAt(fila, 0));
            f.setIdFlete((int) jTableOperatorManager.getValueAt(fila, 1));
            f.setDescription(jTableOperatorManager.getValueAt(fila, 3).toString());
             String fechaTexto = jTableOperatorManager.getValueAt(fila, 4).toString();
            f.setDate(LocalDate.parse(fechaTexto));
            f.setAmount(Double.parseDouble(jTableOperatorManager.getValueAt(fila, 5).toString()));

            // 4. Ahora que 'd' tiene datos, lo pasamos al método
            openJdialog(f);

        } else {
            // Si presionó el botón sin tocar la tabla
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila de la tabla primero.");
        }

    }

    public void injectTable() {

        injectData();
        DefaultTableModel modelo = new DefaultTableModel() {
            // Tip de Ingeniero: Hacer que las celdas no sean editables al hacer doble clic
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
             public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class;   // ID Carga
                    case 1:
                        return Integer.class;     // id
                    case 2:
                        return String.class;   // Cat
                    case 3:
                        return String.class; // Amount
                    case 4:
                        return LocalDate.class;    // Fecha (Se ordena cronológicamente)
                    default:
                        return Double.class;   //Desc
                }
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("ID flete");
        modelo.addColumn("Categoria");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Fecha");
        modelo.addColumn("Importe");

        try {

            List<FinanceOperatorDTO> listFinanceOp = controllExpense.getlistForOperator(operator.getId());
            System.err.println("lista con  objects = " + listFinanceOp.size());

            if (!listFinanceOp.isEmpty()) {
                for (FinanceOperatorDTO r : listFinanceOp) {
                    Object[] fila = new Object[6];
                    // Usamos 'i' que es el objeto del bucle
                    fila[0] = r.getId(); // O el ID que corresponda
                    fila[1] = r.getIdFlete();
                    fila[2] = r.getCategory().getName();
                    fila[3] = r.getDescription();
                    fila[4] = r.getDate();
                    fila[5] = r.getAmount();

                    modelo.addRow(fila);
                }
            }

           
        jTableOperatorManager.setModel(modelo);
        
        // Configurar Sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        jTableOperatorManager.setRowSorter(sorter);
        
        // Aplicar diseño y alineación
        ajustarColumnas(jTableOperatorManager);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    
    
    
    public void ajustarColumnas(JTable tabla) {
    // 1. Medición de anchos
    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    for (int column = 0; column < tabla.getColumnCount(); column++) {
        TableColumn tableColumn = tabla.getColumnModel().getColumn(column);
        int preferredWidth = tableColumn.getMinWidth();

        TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
        Component headerComp = headerRenderer.getTableCellRendererComponent(tabla, tableColumn.getHeaderValue(), false, false, 0, column);
        preferredWidth = Math.max(preferredWidth, headerComp.getPreferredSize().width + 20);

        for (int row = 0; row < tabla.getRowCount(); row++) {
            TableCellRenderer cellRenderer = tabla.getCellRenderer(row, column);
            Component c = tabla.prepareRenderer(cellRenderer, row, column);
            int width = c.getPreferredSize().width + tabla.getIntercellSpacing().width + 15;
            preferredWidth = Math.max(preferredWidth, width);
        }
        tableColumn.setPreferredWidth(preferredWidth);
    }

    // 2. Renderizador para Moneda (Columna 5) y Fecha (Columna 4)
    DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
        private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value instanceof Number) {
                value = formatter.format(((Number) value).doubleValue());
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT); 
            return c;
        }
    };

    // Renderizador para centrar la Fecha
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof LocalDate) {
                value = ((LocalDate) value).format(dtf);
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    };

    // 3. Asignación de estilos
    if (tabla.getColumnCount() > 5) {
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);   // ID centrado
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);   // Fecha centrada
        tabla.getColumnModel().getColumn(5).setCellRenderer(currencyRenderer); // Importe .00 a la derecha
    }

    tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitleWithOperator = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableOperatorManager = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnEditRow = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitleWithOperator.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitleWithOperator.setText("Movimientos de Name");

        jTableOperatorManager.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID ", "ID flete", "Categoria", "Descripcion", "Fecha", "Importe"
            }
        ));
        jScrollPane1.setViewportView(jTableOperatorManager);

        btnBack.setBackground(new java.awt.Color(242, 242, 242));
        btnBack.setBorder(null);
        btnBack.setMaximumSize(new java.awt.Dimension(32, 32));
        btnBack.setMinimumSize(new java.awt.Dimension(32, 32));
        btnBack.setPreferredSize(new java.awt.Dimension(32, 32));
        btnBack.addActionListener(this::btnBackActionPerformed);

        jButton2.setText("Eliminar ");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        btnEditRow.setText("Modificar registro");
        btnEditRow.addActionListener(this::btnEditRowActionPerformed);

        jButton3.setText("Añadir Importe");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        btnExcel.setBackground(new java.awt.Color(242, 242, 242));
        btnExcel.setBorder(null);
        btnExcel.setPreferredSize(new java.awt.Dimension(32, 32));
        btnExcel.addActionListener(this::btnExcelActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jButton3)
                                .addGap(68, 68, 68)
                                .addComponent(btnEditRow)
                                .addGap(63, 63, 63)
                                .addComponent(jButton2)
                                .addGap(49, 49, 49)
                                .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(lblTitleWithOperator)))
                .addGap(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblTitleWithOperator)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(btnEditRow, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnEditRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRowActionPerformed
        listenerRow();
    }//GEN-LAST:event_btnEditRowActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        AddImportOperator w = new AddImportOperator(operator);
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
        this.injectTable();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        ExcelExporter.exportar(jTableOperatorManager, "Reporte Deudores");
    }//GEN-LAST:event_btnExcelActionPerformed

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
                TableOperatorManager dialog = new TableOperatorManager(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnEditRow;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableOperatorManager;
    private javax.swing.JLabel lblTitleWithOperator;
    // End of variables declaration//GEN-END:variables
}
