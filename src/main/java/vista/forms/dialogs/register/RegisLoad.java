/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.dialogs.register;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import controllers.CustomerController;
import controllers.IncomeController;
import controllers.LoadController;
import controllers.LoadDetailController;
import controllers.TankBController;
import javax.swing.JButton;
import modelo.dao.LoadDAO;
import modelo.domain.Customer;
import modelo.domain.Flete;
import modelo.domain.Load;
import modelo.domain.LoadDetail;
import modelo.domain.TankB;
import modelo.domain.Vehicle;
import modelo.domain.transaction.CategoryM;
import modelo.domain.transaction.Income;
import modelo.dto.LoadDTO;
import modelo.dto.LoadDetailDTO;
import modelo.enums.ParamFlete;
import modelo.service.RealLoadService;
import modelo.util.IconManager;
import modelo.util.Validator;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import vista.forms.InicialForm;
import vista.forms.minDialogs.TankCompartments;

/**
 *
 * @author drola
 */
public class RegisLoad extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegisLoad.class.getName());
    
    private final CustomerController controllCus = new CustomerController();
    private final LoadDetailController controllLoadDetails = new LoadDetailController();
    private final LoadController cotrollLoad = new LoadController();
    private final IncomeController controllIncome = new IncomeController();
    private final TankBController controllTank = new TankBController();
    
    private Flete fleteCurrent = null;
    private Customer customerSelectedGlob = null;
    
    public static int BOX1 = 0;
    public static int BOX2=0;
    public static int BOX3=0;
    
    
    
    /**
     * Creates new form RegisLoad
     */
    public RegisLoad(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadIcons();
        updateJradioBtn();
    }

    public RegisLoad(java.awt.Frame parent, boolean modal, Flete flete) {
        super(parent, modal); // Aquí anclamos la ventana modal a tu InicialForm real
        initComponents();
        loadIcons();
        updateJradioBtn();

        this.fleteCurrent = flete;
        injectCustomersToComboBox(); // Tu método de carga
    }

    // my methods
    //method for charg list of customers to cbx
    private void injectCustomersToComboBox() {
        List<Customer> list = controllCus.getListCusForNewFlete();

        // Limpiamos el combo por si acaso
        cbxCustomers.removeAllItems();

        // Agregamos una opción inicial
        Customer placeholder = new Customer();
        placeholder.setId(-1);
        placeholder.setName("--- Seleccione un cliente");

        cbxCustomers.addItem(placeholder);

        for (Customer cus : list) {
            cbxCustomers.addItem(cus);
        }
    }

    private void loadIcons() {
        IconManager.setIkonliIcon(lblGas, CarbonIcons.GAS_STATION, 25);
        IconManager.setIkonliIcon(lblMapGas, BoxiconsRegular.MAP, 25);
        IconManager.setIkonliIcon(lblDataCustomer, FontAwesomeSolid.USER, 15);
         IconManager.setIkonliIcon(lblProductVolume, FontAwesomeSolid.HAND_HOLDING_WATER, 15);
         IconManager.setIkonliIcon(lblPrice, FontAwesomeSolid.MONEY_BILL, 15);
    }

    private void updateJradioBtn() {
        onOffInput(rbtnBD5, txtBD5, btnBox1);
        onOffInput(rbtnPremium, txtPremiun,btnBox2);
        onOffInput(rbtnRegular, txtRegular,btnBox3);
    }

    private boolean onOffInput(JRadioButton rbtn, JTextField txt, JButton boton) {

        txtAdelanto.setEnabled(false);
        if (rbtn.isSelected()) {
            txt.setEnabled(true);
            boton.setEnabled(true);
            return true;
        } else {
            txt.setEnabled(false);
            boton.setEnabled(false);
            txt.setText("");
//            System.err.println("desactivate");
            return false;
        }

    }
    // Método para blindar las cajas de texto contra letras y símbolos

    private boolean isNumeroValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(texto.trim());
            return true; // Es un número válido (ej: "150", "150.50")
        } catch (NumberFormatException e) {
            return false; // Tiene letras o formato incorrecto (ej: "150A", "15,5")
        }
    }

    private LoadDetail getIdDetailProductAndQuantity(JTextField txt, int idProduct) throws NumberFormatException {
        if (!txt.getText().trim().isEmpty()) {
            double quantity = Double.parseDouble(txt.getText().trim());

            return new LoadDetail(idProduct, quantity);
        }
        return null;
    }

    private JRadioButton getBtnSelectedSttsPay() {
        if (btnPagado.isSelected()) {
            return btnPagado;
        }
        if (btnPagoPendiente.isSelected()) {
            return btnPagoPendiente;
        }
        if (btnPagoAdelanto.isSelected()) {

            return btnPagoAdelanto;
        }
        return null; // Ninguno seleccionado
    }
   
    private boolean checkAndSetBox(int box, LoadDetail ld){
        if(box==0) return false;
        
        ld.setCompartment(box);
        return true;
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupStatusPay = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblDataCustomer = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbxCustomers = new javax.swing.JComboBox<>();
        lblGas = new javax.swing.JLabel();
        lblNameEs = new javax.swing.JLabel();
        lblMapGas = new javax.swing.JLabel();
        lblMap = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtBD5 = new javax.swing.JTextField();
        txtPremiun = new javax.swing.JTextField();
        txtRegular = new javax.swing.JTextField();
        rbtnBD5 = new javax.swing.JRadioButton();
        rbtnRegular = new javax.swing.JRadioButton();
        rbtnPremium = new javax.swing.JRadioButton();
        btnBox1 = new javax.swing.JButton();
        btnBox2 = new javax.swing.JButton();
        btnBox3 = new javax.swing.JButton();
        lblPrice = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnPagado = new javax.swing.JRadioButton();
        btnPagoPendiente = new javax.swing.JRadioButton();
        btnPagoAdelanto = new javax.swing.JRadioButton();
        txtAdelanto = new javax.swing.JTextField();
        btnCalcel = new javax.swing.JButton();
        btnInserLoad = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        panelRedondeado1 = new vista.paintCode.PanelRedondeado();
        txtAmount = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblProductVolume = new javax.swing.JLabel();
        lblStatusPay = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(656, 467));
        setMinimumSize(new java.awt.Dimension(656, 467));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setMaximumSize(new java.awt.Dimension(656, 467));
        jPanel3.setMinimumSize(new java.awt.Dimension(656, 467));
        jPanel3.setPreferredSize(new java.awt.Dimension(656, 467));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel7.setText("Nueva Carga");

        lblDataCustomer.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        lblDataCustomer.setText("Datos del Cliente:");

        jLabel8.setText("Cliente:");

        cbxCustomers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCustomersActionPerformed(evt);
            }
        });

        lblGas.setText(":");

        lblNameEs.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNameEs.setText(".................................................................................");

        lblMapGas.setText(":");

        lblMap.setText(".................................................................................");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        txtBD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBD5ActionPerformed(evt);
            }
        });
        txtBD5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBD5KeyTyped(evt);
            }
        });

        txtPremiun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPremiunKeyTyped(evt);
            }
        });

        txtRegular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRegularKeyTyped(evt);
            }
        });

        rbtnBD5.setText("BD5:");
        rbtnBD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnBD5ActionPerformed(evt);
            }
        });

        rbtnRegular.setText("G. Regular:");
        rbtnRegular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnRegularActionPerformed(evt);
            }
        });

        rbtnPremium.setText("G. Premiun:");
        rbtnPremium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPremiumActionPerformed(evt);
            }
        });

        btnBox1.setText("box");
        btnBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBox1ActionPerformed(evt);
            }
        });

        btnBox2.setText("box");
        btnBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBox2ActionPerformed(evt);
            }
        });

        btnBox3.setText("box");
        btnBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnBD5)
                    .addComponent(rbtnPremium)
                    .addComponent(rbtnRegular))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBD5)
                    .addComponent(txtPremiun)
                    .addComponent(txtRegular, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBD5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBox1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(rbtnBD5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPremiun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtnPremium)
                    .addComponent(btnBox2))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtnRegular)
                    .addComponent(btnBox3))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        lblPrice.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        lblPrice.setText("Precio flete:");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        btnGroupStatusPay.add(btnPagado);
        btnPagado.setText("Pagado");
        btnPagado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagadoActionPerformed(evt);
            }
        });

        btnGroupStatusPay.add(btnPagoPendiente);
        btnPagoPendiente.setText("Pendiente");
        btnPagoPendiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagoPendienteActionPerformed(evt);
            }
        });

        btnGroupStatusPay.add(btnPagoAdelanto);
        btnPagoAdelanto.setText("Parcial:");
        btnPagoAdelanto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagoAdelantoActionPerformed(evt);
            }
        });

        txtAdelanto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdelantoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPagado, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPagoPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnPagoAdelanto)
                        .addGap(59, 59, 59)
                        .addComponent(txtAdelanto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPagado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(btnPagoPendiente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPagoAdelanto)
                    .addComponent(txtAdelanto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        btnCalcel.setText("Cancelar");
        btnCalcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcelActionPerformed(evt);
            }
        });

        btnInserLoad.setBackground(new java.awt.Color(102, 153, 255));
        btnInserLoad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInserLoad.setText("Crear");
        btnInserLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserLoadActionPerformed(evt);
            }
        });

        jLabel3.setText("*Verifique los montos antes de confirmar el registro.");

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(102, 102, 102));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(102, 102, 102));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(102, 102, 102));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(102, 102, 102));

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(102, 102, 102));

        panelRedondeado1.setColorFondo(new java.awt.Color(0, 153, 204));

        txtAmount.setBackground(java.awt.SystemColor.activeCaption);
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
        });

        jLabel4.setText("Monto (S/.)");

        javax.swing.GroupLayout panelRedondeado1Layout = new javax.swing.GroupLayout(panelRedondeado1);
        panelRedondeado1.setLayout(panelRedondeado1Layout);
        panelRedondeado1Layout.setHorizontalGroup(
            panelRedondeado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRedondeado1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        panelRedondeado1Layout.setVerticalGroup(
            panelRedondeado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelRedondeado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        lblProductVolume.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        lblProductVolume.setText("Producto/volumen:");

        lblStatusPay.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        lblStatusPay.setText("Estado del pago:");

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbxCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(lblGas)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblNameEs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(lblMapGas)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblMap, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblDataCustomer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRedondeado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(jSeparator4)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblStatusPay, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCalcel)
                .addGap(18, 18, 18)
                .addComponent(btnInserLoad)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataCustomer)
                    .addComponent(lblProductVolume))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cbxCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGas)
                            .addComponent(lblNameEs))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMapGas)
                            .addComponent(lblMap)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblStatusPay))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblPrice)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(panelRedondeado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnInserLoad)
                        .addComponent(btnCalcel))
                    .addComponent(jLabel3))
                .addGap(191, 191, 191))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 461, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxCustomersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCustomersActionPerformed
        Object selection = cbxCustomers.getSelectedItem();
        if (selection instanceof Customer) {
            Customer c = (Customer) selection;
            lblNameEs.setText(c.getNameES());
            lblMap.setText(c.getAddressEs());

            customerSelectedGlob = c;
        }


    }//GEN-LAST:event_cbxCustomersActionPerformed

    private void btnInserLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserLoadActionPerformed
        // --- 1. VALIDACIONES DE SEGURIDAD (Cláusulas de guarda) ---

        if (customerSelectedGlob == null || customerSelectedGlob.getId() < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, elige un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; // Detiene la ejecución
        }

        if (!isNumeroValido(txtAmount.getText())) {
            JOptionPane.showMessageDialog(this, "Ingresa un importe de flete válido (Solo números).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JRadioButton btnStatusPay = getBtnSelectedSttsPay();
        if (btnStatusPay == null) {
            JOptionPane.showMessageDialog(this, "Selecciona el estado de pago.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (btnStatusPay == btnPagoAdelanto && !isNumeroValido(txtAdelanto.getText())) {
            JOptionPane.showMessageDialog(this, "Ingresa un monto de adelanto válido (Solo números).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 2. RECOPILACIÓN DE DATOS (Protegido con Try-Catch) ---
        List<LoadDetail> listLoadsDetails = new ArrayList<>();
        try {
            LoadDetail ld1 = getIdDetailProductAndQuantity(txtBD5, 1);
            if (ld1 != null) {
                if(!checkAndSetBox(BOX1, ld1)){ 
                    JOptionPane.showMessageDialog(this, "Falta Elejir Nro compartimiento");
                    return;
                }
                
                listLoadsDetails.add(ld1);
              
            }

            LoadDetail ld2 = getIdDetailProductAndQuantity(txtPremiun, 2);
            if (ld2 != null) {
                if(!checkAndSetBox(BOX2, ld2)){ 
                    JOptionPane.showMessageDialog(this, "Falta Elejir Nro compartimiento");
                    return;
                }
                listLoadsDetails.add(ld2);
              
            }

            LoadDetail ld3 = getIdDetailProductAndQuantity(txtRegular, 3);
            if (ld3 != null) {
                if(!checkAndSetBox(BOX3, ld3)){
                    JOptionPane.showMessageDialog(this, "Falta Elejir Nro compartimiento");
                    return;
                }
                listLoadsDetails.add(ld3);
               
            }

        } catch (NumberFormatException e) {
            // Si el usuario puso letras en BD5, Premium o Regular, cae aquí y no crashea
            JOptionPane.showMessageDialog(this, "Revisa las cantidades de producto. Solo se permiten números.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        

        if (listLoadsDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar al menos un producto y su cantidad.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- 3. PROCESAMIENTO Y GUARDADO (Si llegamos aquí, los datos son 100% seguros) ---
        Load loadCurrent = new Load();
        loadCurrent.setCustomer(customerSelectedGlob);  // customer
        loadCurrent.setListProducts(listLoadsDetails);  //lista de productos

        loadCurrent.setAmount(Double.parseDouble(txtAmount.getText().trim()));

        // Configurar Ingreso (Income)
        Income income = new Income();
        if (btnStatusPay == btnPagado) {
            income.setAmount(loadCurrent.getAmount());
            income.setCategory(new CategoryM(1));
            income.setDate(LocalDate.now());
            income.setDescription("Ingreso por Flete (Pagado)");
        } else if (btnStatusPay == btnPagoPendiente) {
            income.setCategory(new CategoryM(0)); // 0 = Pendiente según tu lógica
        } else if (btnStatusPay == btnPagoAdelanto) {
            income.setAmount(Double.parseDouble(txtAdelanto.getText().trim()));
            income.setCategory(new CategoryM(1));
            income.setDate(LocalDate.now());
            income.setDescription("Ingreso por Flete (Adelanto)");
        }

        // Guardar Carga en DB
        int idLoadGen = cotrollLoad.insertLoadComplete(loadCurrent, fleteCurrent);

        if (idLoadGen > 0) {
            loadCurrent.setId(idLoadGen);       //id_load 

            // Guardar Ingreso Financiero
            if (income.getCategory().getId() != 0) {
                income.setIdLoad(idLoadGen);
                controllIncome.insertIncome(income);
            }
            
            
            TankB tankBehavior = new TankB();
            tankBehavior.setCustomer(customerSelectedGlob);
            tankBehavior.setLoad(loadCurrent);
            tankBehavior.setStatus("OCUPADO");
            Vehicle scannia = new Vehicle();
            scannia.setId(1);               //mejorar
            tankBehavior.setVehicle(scannia);
            
            // Guardar Detalles de Carga
            boolean detailsOk = true;
            for (LoadDetail ld : listLoadsDetails) {
                int regOk = controllLoadDetails.insertLoadDetail(idLoadGen, ld);
                System.out.println("validacion de returnet id"+ regOk);
                tankBehavior.setLoadDetail(ld);
                tankBehavior.getLoadDetail().setId(regOk);
                boolean tankOk =controllTank.insertTank_behavior(tankBehavior);
                System.err.println(tankOk);
                //exito?
                if (regOk<0) {
                    detailsOk = false;
                    
                }
            }

            if (detailsOk) {
                JOptionPane.showMessageDialog(this, "Carga registrada exitosamente.");
                System.err.println("BOX 1---------------: "+BOX1);
                System.err.println("BOX 2---------------: "+BOX2);
                System.err.println("BOX 3---------------: "+BOX3);
                this.dispose(); // Cierra la ventana
            } else {
                JOptionPane.showMessageDialog(this, "La carga se guardó, pero hubo un error con los detalles.", "Advertencia DB", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Acceso denegado", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInserLoadActionPerformed

    private void rbtnRegularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnRegularActionPerformed
        onOffInput(rbtnRegular, txtRegular,btnBox3);


    }//GEN-LAST:event_rbtnRegularActionPerformed

    private void rbtnPremiumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPremiumActionPerformed
        onOffInput(rbtnPremium, txtPremiun,btnBox2);

    }//GEN-LAST:event_rbtnPremiumActionPerformed

    private void rbtnBD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBD5ActionPerformed
        onOffInput(rbtnBD5, txtBD5,btnBox1);

    }//GEN-LAST:event_rbtnBD5ActionPerformed

    private void btnCalcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcelActionPerformed
        this.dispose();

    }//GEN-LAST:event_btnCalcelActionPerformed

    private void btnPagoAdelantoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagoAdelantoActionPerformed
        if (btnPagoAdelanto.isSelected()) {
            txtAdelanto.setEnabled(true);
        } else {
            txtAdelanto.setEnabled(false);
        }
    }//GEN-LAST:event_btnPagoAdelantoActionPerformed

    private void btnPagadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagadoActionPerformed
        if (btnPagado.isSelected())
            txtAdelanto.setEnabled(false);
    }//GEN-LAST:event_btnPagadoActionPerformed

    private void btnPagoPendienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagoPendienteActionPerformed
        if (btnPagoPendiente.isSelected())
            txtAdelanto.setEnabled(false);
    }//GEN-LAST:event_btnPagoPendienteActionPerformed

    private void txtBD5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBD5KeyTyped
        char c = evt.getKeyChar();
        // Si no es un número y no es un punto decimal...
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
        }

        // Evitar que pongan dos puntos decimales (ej: "22..5")
        if (c == '.' && txtAmount.getText().contains(".")) {
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
        if (c == '.' && txtAmount.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPremiunKeyTyped

    private void txtRegularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegularKeyTyped
        char c = evt.getKeyChar();
        // Si no es un número y no es un punto decimal...
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
        }

        // Evitar que pongan dos puntos decimales (ej: "22..5")
        if (c == '.' && txtAmount.getText().contains(".")) {
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

    private void txtAdelantoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdelantoKeyTyped
        char c = evt.getKeyChar();
        // Si no es un número y no es un punto decimal...
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ... "consume" la tecla, es decir, ignora lo que el usuario apretó.
        }

        // Evitar que pongan dos puntos decimales (ej: "22..5")
        if (c == '.' && txtAmount.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAdelantoKeyTyped

    private void btnBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBox1ActionPerformed
        
        TankCompartments w = new TankCompartments(this,true,"data");
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
        if(w.getCompartmentSelected()>0) BOX1=w.getCompartmentSelected();
    }//GEN-LAST:event_btnBox1ActionPerformed

    private void btnBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBox2ActionPerformed
         TankCompartments w = new TankCompartments(this,true, "data");
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
         if(w.getCompartmentSelected()>0) BOX2=w.getCompartmentSelected();
    }//GEN-LAST:event_btnBox2ActionPerformed

    private void btnBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBox3ActionPerformed
         TankCompartments w = new TankCompartments(this,true,"data");
        w.setLocationRelativeTo(this); // Centrar
        w.setResizable(false);        // No redimensionar
        w.setVisible(true);
         if(w.getCompartmentSelected()>0) BOX3=w.getCompartmentSelected();
    }//GEN-LAST:event_btnBox3ActionPerformed

    private void txtBD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBD5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBD5ActionPerformed

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
                RegisLoad dialog = new RegisLoad(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBox1;
    private javax.swing.JButton btnBox2;
    private javax.swing.JButton btnBox3;
    private javax.swing.JButton btnCalcel;
    private javax.swing.ButtonGroup btnGroupStatusPay;
    private javax.swing.JButton btnInserLoad;
    private javax.swing.JRadioButton btnPagado;
    private javax.swing.JRadioButton btnPagoAdelanto;
    private javax.swing.JRadioButton btnPagoPendiente;
    private javax.swing.JComboBox<Customer> cbxCustomers;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lblDataCustomer;
    private javax.swing.JLabel lblGas;
    private javax.swing.JLabel lblMap;
    private javax.swing.JLabel lblMapGas;
    private javax.swing.JLabel lblNameEs;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblProductVolume;
    private javax.swing.JLabel lblStatusPay;
    private vista.paintCode.PanelRedondeado panelRedondeado1;
    private javax.swing.JRadioButton rbtnBD5;
    private javax.swing.JRadioButton rbtnPremium;
    private javax.swing.JRadioButton rbtnRegular;
    private javax.swing.JTextField txtAdelanto;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBD5;
    private javax.swing.JTextField txtPremiun;
    private javax.swing.JTextField txtRegular;
    // End of variables declaration//GEN-END:variables
}
