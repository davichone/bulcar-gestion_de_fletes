/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.tables;

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

import modelo.domain.transaction.Expense;
import modelo.domain.transaction.Income;
import modelo.dto.DebtDTO;
import modelo.util.ExcelExporter;
import modelo.util.IconManager;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import vista.forms.InicialForm;
import vista.forms.cards.CardLoad;

/**
 *
 * @author drola
 */
public class TableDebtors extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TableDebtors.class.getName());

    private IncomeController controllIncome = new IncomeController();
    private DebtDTO debtDTO;
    private InicialForm inicialForm;
    private Flete fleteCurrent = null;

    /**
     * Creates new form TableIncomes
     */
    public TableDebtors(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public TableDebtors(InicialForm inicialForm, Flete fleteCurrent) {
        this(new javax.swing.JFrame(), true);
        this.inicialForm = inicialForm;
        this.fleteCurrent = fleteCurrent;
        injectTable();
        injectData();
    }

    //mi methods
    private void injectData() {

        IconManager.setSvgIcon(btnExcel, "logos/excel.svg", 25);
        IconManager.setIkonliIcon(btnBack, BoxiconsRegular.ARROW_BACK, 25);

    }

    private void openJdialog(DebtDTO debtDTO) {
        // Creamos la instancia del diálogo (puedes pasarle los datos por constructor)
        EditRowDebtors w = new EditRowDebtors(debtDTO, inicialForm, fleteCurrent);
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
        // PRUEBA DE FUEGO: Si esto no sale en consola, el problema es la MODALIDAD
        System.out.println("Saliendo de EditDebtors... Iniciando refresco.");

        //this.injectTable(); // Refresca la tabla actual
        // Al cerrar el diálogo, refrescamos la tabla para ver los cambios
        this.injectTable();
    }

    private void listenerRow() {
        // 1. Preguntamos qué fila está sombreada en la tabla
        int fila = jTableDebtors.getSelectedRow();

        // 2. Validamos que no sea -1 (o sea, que haya algo seleccionado)
        if (fila != -1) {
            // 3. RECOLECTAMOS los datos en ese instante
            DebtDTO d = new DebtDTO();

            // Sacamos los datos de las columnas (ajusta los números 0,1,2.. si es necesario)
            d.setIdLoad((int) jTableDebtors.getValueAt(fila, 0));
            d.setNameCustomer(jTableDebtors.getValueAt(fila, 1).toString());
            String fechaTexto = jTableDebtors.getValueAt(fila, 2).toString();
            d.setDate(LocalDate.parse(fechaTexto));
            d.setVolumen(Double.parseDouble(jTableDebtors.getValueAt(fila, 3).toString()));
            d.setPriceFlete(Double.parseDouble(jTableDebtors.getValueAt(fila, 4).toString()));
            d.setAbono(Double.parseDouble(jTableDebtors.getValueAt(fila, 5).toString()));
            d.setBalance(Double.parseDouble(jTableDebtors.getValueAt(fila, 6).toString()));

            // 4. Ahora que 'd' tiene datos, lo pasamos al método
            openJdialog(d);

        } else {
            // Si presionó el botón sin tocar la tabla
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila de la tabla primero.");
        }

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
                    case 0:
                        return Integer.class;   // ID e ID Carga
                    case 1:
                    case 2:
                        return String.class;
                    case 3:
                        return LocalDate.class;
                    case 4:
                        return Double.class;
                    case 5:
                        return Double.class;
                    case 6:
                        return Double.class;
                    case 7:
                        return Double.class; // Importe (Para formato .00)
                    default:
                        return Integer.class;
                }
            }
        };
        modelo.addColumn("ID carga");
        modelo.addColumn("Cliente");
        modelo.addColumn("Fecha");
        modelo.addColumn("Volumen(gal)");
        modelo.addColumn("Precio flete");
        modelo.addColumn("Adelantos");
        modelo.addColumn("Pendiente");

        try {

            List<DebtDTO> listDebtors = controllIncome.getlistNotPayed();
            System.err.println("lista con  objects = " + listDebtors.size());

            if (!listDebtors.isEmpty()) {
                for (DebtDTO d : listDebtors) {
                    Object[] fila = new Object[7];
                    // Usamos 'i' que es el objeto del bucle
                    fila[0] = d.getIdLoad(); // O el ID que corresponda
                    fila[1] = d.getNameCustomer();
                    fila[2] = d.getDate();
                    fila[3] = d.getVolumen();
                    fila[4] = d.getPriceFlete();
                    fila[5] = d.getAbono();
                    fila[6] = d.getBalance();

                    modelo.addRow(fila);
                }
            }

            jTableDebtors.setModel(modelo);
            // Configurar Sorter
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
            sorter.setComparator(2, (LocalDate d1, LocalDate d2) -> d1.compareTo(d2));
            jTableDebtors.setRowSorter(sorter);
            ajustarColumnas(jTableDebtors);
            // 1. Obtener el modelo de tu tabla
// 2. Crear el ordenador (Sorter) y vincularlo al modelo

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
                ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
                return c;
            }
        };
        
        DefaultTableCellRenderer galRenderer = new DefaultTableCellRenderer() {
            private final DecimalFormat formatter = new DecimalFormat("###");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                if (value instanceof Number) {
                    value = formatter.format(((Number) value).doubleValue());
                }
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
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
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        // 3. Asignación de estilos
        if (tabla.getColumnCount() > 6) {
            tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);   // ID centrado
             tabla.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);   // ID centrado
            tabla.getColumnModel().getColumn(3).setCellRenderer(galRenderer); // Importe .00 a la derecha
            tabla.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer); // Importe .00 a la derecha
            tabla.getColumnModel().getColumn(5).setCellRenderer(currencyRenderer); // Importe .00 a la derecha
            tabla.getColumnModel().getColumn(6).setCellRenderer(currencyRenderer); // Importe .00 a la derecha
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
        jTableDebtors = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnEditRow = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Saldos Pendientes");

        jTableDebtors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID carga", "Cliente", "fecha", "Volumen (gal)", "Precio flete", "Adelanto", "Pendiente"
            }
        ));
        jScrollPane1.setViewportView(jTableDebtors);

        btnBack.setBackground(new java.awt.Color(242, 242, 242));
        btnBack.setBorder(null);
        btnBack.setMaximumSize(new java.awt.Dimension(32, 32));
        btnBack.setMinimumSize(new java.awt.Dimension(32, 32));
        btnBack.setPreferredSize(new java.awt.Dimension(32, 32));
        btnBack.addActionListener(this::btnBackActionPerformed);

        btnEditRow.setText("Registrar abono recibido");
        btnEditRow.addActionListener(this::btnEditRowActionPerformed);

        btnExcel.setBackground(new java.awt.Color(242, 242, 242));
        btnExcel.setBorder(null);
        btnExcel.setPreferredSize(new java.awt.Dimension(32, 32));
        btnExcel.addActionListener(this::btnExcelActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(btnEditRow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(jLabel1)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnEditRow)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnEditRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRowActionPerformed
        listenerRow();
    }//GEN-LAST:event_btnEditRowActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        ExcelExporter.exportar(jTableDebtors, "Reporte Deudores");
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
                TableDebtors dialog = new TableDebtors(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableDebtors;
    // End of variables declaration//GEN-END:variables
}
