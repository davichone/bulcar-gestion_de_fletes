/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.tables;

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

import modelo.domain.transaction.Expense;
import modelo.util.ExcelExporter;
import modelo.util.IconManager;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import vista.forms.InicialForm;

/**
 *
 * @author drola
 */
public class TableExpensesFleteCurrent extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TableExpensesFleteCurrent.class.getName());
    
     private ExpenseController controllIncome = new ExpenseController();
    
    /**
     * Creates new form TableIncomes
     */
    public TableExpensesFleteCurrent(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        injectTable();
        injectData();
    }
    
    
    
    
    
    //mi methods
     private void injectData() {
      
        IconManager.setSvgIcon(btnExcel, "logos/excel.svg", 25);
       IconManager.setIkonliIcon(btnBack, BoxiconsRegular.ARROW_BACK, 25);
    }
    private void injectTable() {
        DefaultTableModel modelo = new DefaultTableModel() {
            // Tip de Ingeniero: Hacer que las celdas no sean editables al hacer doble clic
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: case 1: return Integer.class;   // ID e ID Carga
                case 2: case 3: return String.class;    // Categoría y Descripción
                case 4: return LocalDate.class;         // Fecha (Objeto puro para orden cronológico)
                case 5: return Double.class;            // Importe (Para formato .00)
                default: return String.class;
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

            List<Expense> listaSolicitudes = controllIncome.getlist(InicialForm.ID_FLETE_CURRENT);
            for (Expense i : listaSolicitudes) {
                Object[] fila = new Object[6];
                // Usamos 'i' que es el objeto del bucle
                fila[0] = i.getId(); // O el ID que corresponda
                fila[1] = i.getIdFlete();
                fila[2] = i.getCategory().getName();
                fila[3] = i.getDescription();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fila[4] = i.getDate().format(dtf);
                fila[5] = i.getAmount();

                modelo.addRow(fila);
            }

            TableExpenses.setModel(modelo);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        TableExpenses.setRowSorter(sorter);
            ajustarColumnas(TableExpenses);
            //TableIncome.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableExpenses = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Gastos del Flete");

        TableExpenses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "ID carga", "Categoria", "Monto", "Fecha", "Descripcion"
            }
        ));
        jScrollPane1.setViewportView(TableExpenses);

        btnBack.setBackground(new java.awt.Color(242, 242, 242));
        btnBack.setBorder(null);
        btnBack.setMaximumSize(new java.awt.Dimension(32, 32));
        btnBack.setMinimumSize(new java.awt.Dimension(32, 32));
        btnBack.setPreferredSize(new java.awt.Dimension(32, 32));
        btnBack.addActionListener(this::btnBackActionPerformed);

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(jLabel1)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        ExcelExporter.exportar(TableExpenses, "Reporte Deudores");
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
                TableExpensesFleteCurrent dialog = new TableExpensesFleteCurrent(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TableExpenses;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExcel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
