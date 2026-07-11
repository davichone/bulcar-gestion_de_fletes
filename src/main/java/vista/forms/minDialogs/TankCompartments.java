/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms.minDialogs;

import controllers.TankBController;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import modelo.domain.LoadDetail;
import modelo.domain.TankB;
import patrones.state.Compartimiento;
import patrones.state.EstadoOcupado;
import vista.paintCode.PanelRedondeado;

/**
 *
 * @author drola
 */
public class TankCompartments extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TankCompartments.class.getName());
    private final TankBController controllTank = new TankBController();
    private Compartimiento[] listaCompartimientos = new Compartimiento[9];
    private int compartmentSelected = 0;
    

    public int getCompartmentSelected() {
        return this.compartmentSelected;
    }
    public void setCompartmentSelected(int id) {
        this.compartmentSelected = id;
    }

    /**
     * Creates new form TankCompartments
     */
    public TankCompartments(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public TankCompartments(java.awt.Frame parent, boolean modal, String modo) {
        super(parent, modal);
        initComponents();
        configWindow(modo);
        
    }

    public TankCompartments(java.awt.Dialog parent, boolean modal, String modo) {
        super(parent, modal);
        initComponents();
        configWindow(modo);

    }

//    public TankCompartments(java.awt.Dialog parent) {
//        this(parent, true);
//        inyectFreeCompartments();
//        initArraysComponents();
//        inyectBusylist();
//    }
    private void initContextos() {
        // Amarra cada componente visual a un objeto inteligente "Compartimiento"
        listaCompartimientos[0] = new Compartimiento(1, rb1, T1, lblNameT1, lblVolumeT1, this);
        listaCompartimientos[1] = new Compartimiento(2, rb2, T2, lblNameT2, lblVolumeT2, this);
        listaCompartimientos[2] = new Compartimiento(3, rb3, T3, lblNameT3, lblVolumeT3, this);
        listaCompartimientos[3] = new Compartimiento(4, rb4, T4, lblNameT4, lblVolumeT4, this);
        listaCompartimientos[4] = new Compartimiento(5, rb5, T5, lblNameT5, lblVolumeT5, this);
        listaCompartimientos[5] = new Compartimiento(6, rb6, T6, lblNameT6, lblVolumeT6, this);
        listaCompartimientos[6] = new Compartimiento(7, rb7, T7, lblNameT7, lblVolumeT7, this);
        listaCompartimientos[7] = new Compartimiento(8, rb8, T8, lblNameT8, lblVolumeT8, this);
        listaCompartimientos[8] = new Compartimiento(9, rb9, T9, lblNameT9, lblVolumeT9, this);
    }
    private void configWindow(String modo) {
        if (modo.equalsIgnoreCase("visual")) {
            btnSave.setVisible(false);
            btnBack.setText("Atras");
        } else if(modo.equalsIgnoreCase("data")) {
            btnSave.setVisible(true);  // para iniciar con desde Jframe
            btnBack.setText("Cancelar");
        }
        
        this.setTitle("Bulcar");
        rb1.setActionCommand("1");
        rb2.setActionCommand("2");
        rb3.setActionCommand("3");
        rb4.setActionCommand("4");
        rb5.setActionCommand("5");
        rb6.setActionCommand("6");
        rb7.setActionCommand("7");
        rb8.setActionCommand("8");
        rb9.setActionCommand("9");
        inyectFreeCompartments();
        initArraysComponents();
        initContextos();
        inyectarEstadosBD();
    }

    private void inyectFreeCompartments() {
        List<Integer> list = controllTank.getFreeCompartments();
        // Obtenemos todos los botones del grupo
        Enumeration<AbstractButton> botones = buttonGroup1.getElements();

        while (botones.hasMoreElements()) {
            AbstractButton btn = botones.nextElement();

            // Obtenemos el número del botón (el actionCommand que configuramos antes)
            int numeroBoton = Integer.parseInt(btn.getActionCommand());

            // Si el número NO está en la lista, lo desactivamos
            if (!list.contains(numeroBoton)) {
                btn.setEnabled(false); // Esto "apaga" el botón visualmente
                btn.setToolTipText("Este compartimento está ocupado"); // Un pequeño mensaje extra
            } else {
                btn.setEnabled(true); // Aseguramos que los libres estén activos
            }
        }
    }

    private void inyectarEstadosBD() {
        List<TankB> ocupados = controllTank.getBusyList();
        
        for (TankB box : ocupados) {
            // El ID viene del 1 al 9, restamos 1 para el índice del arreglo (0 al 8)
            int index = box.getLoadDetail().getCompartment() - 1;
            
            if (index >= 0 && index < listaCompartimientos.length) {
                // Extraemos la data de la base de datos
                String nombre = box.getCustomer().getName();
                double cantidad = box.getLoadDetail().getQuantity();
                double capacidad = box.getLoadDetail().getCapacityBox(); // Asumiendo que tienes esto
                int idProducto = box.getLoadDetail().getidProduct();
                
                // ¡CAMBIO DE ESTADO!
                // El objeto por dentro se encarga de bloquear el botón y pintar el panel
                listaCompartimientos[index].setEstado(new EstadoOcupado(nombre, cantidad, capacidad, idProducto));
            }
        }
    }

    private void initArraysComponents() {
//        namesCustomersGLOB = new javax.swing.JLabel[]{
//            lblNameT1, lblNameT2, lblNameT3, lblNameT4, lblNameT5, lblNameT6, lblNameT7, lblNameT8, lblNameT9
//        };
//
//        panelesGLOB = new PanelRedondeado[]{
//            T1, T2, T3, T4, T5, T6, T7, T8, T9
//        };
//
//        volumenCustomerGLOB = new javax.swing.JLabel[]{
//            lblVolumeT1, lblVolumeT2, lblVolumeT3, lblVolumeT4, lblVolumeT5, lblVolumeT6, lblVolumeT7, lblVolumeT8, lblVolumeT9
//        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        rb1 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb4 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        rb5 = new javax.swing.JRadioButton();
        btnBack = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        rb6 = new javax.swing.JRadioButton();
        rb8 = new javax.swing.JRadioButton();
        rb7 = new javax.swing.JRadioButton();
        rb9 = new javax.swing.JRadioButton();
        T1 = new vista.paintCode.PanelRedondeado();
        lblVolumeT1 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelRedondeado3 = new vista.paintCode.PanelRedondeado();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelRedondeado15 = new vista.paintCode.PanelRedondeado();
        jLabel4 = new javax.swing.JLabel();
        panelRedondeado16 = new vista.paintCode.PanelRedondeado();
        panelRedondeado17 = new vista.paintCode.PanelRedondeado();
        jLabel5 = new javax.swing.JLabel();
        panelRedondeado18 = new vista.paintCode.PanelRedondeado();
        jLabel6 = new javax.swing.JLabel();
        lblNameT8 = new javax.swing.JLabel();
        lblNameT1 = new javax.swing.JLabel();
        lblNameT2 = new javax.swing.JLabel();
        lblNameT3 = new javax.swing.JLabel();
        lblNameT4 = new javax.swing.JLabel();
        lblNameT5 = new javax.swing.JLabel();
        lblNameT6 = new javax.swing.JLabel();
        lblNameT7 = new javax.swing.JLabel();
        T3 = new vista.paintCode.PanelRedondeado();
        lblVolumeT3 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        T5 = new vista.paintCode.PanelRedondeado();
        lblVolumeT5 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        T2 = new vista.paintCode.PanelRedondeado();
        lblVolumeT2 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        T4 = new vista.paintCode.PanelRedondeado();
        lblVolumeT4 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        T9 = new vista.paintCode.PanelRedondeado();
        lblVolumeT9 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        T7 = new vista.paintCode.PanelRedondeado();
        lblVolumeT7 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        T6 = new vista.paintCode.PanelRedondeado();
        lblVolumeT6 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        T8 = new vista.paintCode.PanelRedondeado();
        lblVolumeT8 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblNameT9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        buttonGroup1.add(rb1);
        rb1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb1.setText("Nro: 1");

        buttonGroup1.add(rb2);
        rb2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb2.setText("Nro : 2");
        rb2.addActionListener(this::rb2ActionPerformed);

        buttonGroup1.add(rb4);
        rb4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb4.setText("Nro : 4");

        buttonGroup1.add(rb3);
        rb3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb3.setText("Nro : 3");
        rb3.addActionListener(this::rb3ActionPerformed);

        buttonGroup1.add(rb5);
        rb5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb5.setText("Nro : 5");

        btnBack.setText("Cancelar");
        btnBack.addActionListener(this::btnBackActionPerformed);

        btnSave.setText("Guardar");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        buttonGroup1.add(rb6);
        rb6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb6.setText("Nro : 6");

        buttonGroup1.add(rb8);
        rb8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb8.setText("Nro : 8");

        buttonGroup1.add(rb7);
        rb7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb7.setText("Nro : 7");
        rb7.addActionListener(this::rb7ActionPerformed);

        buttonGroup1.add(rb9);
        rb9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rb9.setText("Nro : 9");

        T1.setBackground(new java.awt.Color(153, 0, 255));
        T1.setForeground(new java.awt.Color(51, 51, 255));
        T1.setColorFondo(new java.awt.Color(153, 153, 153));
        T1.setRadioBorde(40);

        lblVolumeT1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT1.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT1.setText("-");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("1000 gl");

        javax.swing.GroupLayout T1Layout = new javax.swing.GroupLayout(T1);
        T1.setLayout(T1Layout);
        T1Layout.setHorizontalGroup(
            T1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T1Layout.setVerticalGroup(
            T1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Distribucion de Compartimientos");

        panelRedondeado3.setBackground(new java.awt.Color(153, 0, 255));
        panelRedondeado3.setForeground(new java.awt.Color(51, 51, 255));
        panelRedondeado3.setColorFondo(new java.awt.Color(0, 102, 0));
        panelRedondeado3.setRadioBorde(40);

        javax.swing.GroupLayout panelRedondeado3Layout = new javax.swing.GroupLayout(panelRedondeado3);
        panelRedondeado3.setLayout(panelRedondeado3Layout);
        panelRedondeado3Layout.setHorizontalGroup(
            panelRedondeado3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
        panelRedondeado3Layout.setVerticalGroup(
            panelRedondeado3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("BD5");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("G. Premium");

        panelRedondeado15.setBackground(new java.awt.Color(153, 0, 255));
        panelRedondeado15.setForeground(new java.awt.Color(51, 51, 255));
        panelRedondeado15.setColorFondo(new java.awt.Color(0, 51, 204));
        panelRedondeado15.setRadioBorde(40);

        javax.swing.GroupLayout panelRedondeado15Layout = new javax.swing.GroupLayout(panelRedondeado15);
        panelRedondeado15.setLayout(panelRedondeado15Layout);
        panelRedondeado15Layout.setHorizontalGroup(
            panelRedondeado15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
        panelRedondeado15Layout.setVerticalGroup(
            panelRedondeado15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("G. Regular");

        panelRedondeado16.setBackground(new java.awt.Color(153, 0, 255));
        panelRedondeado16.setForeground(new java.awt.Color(51, 51, 255));
        panelRedondeado16.setColorFondo(new java.awt.Color(51, 153, 0));
        panelRedondeado16.setRadioBorde(40);

        javax.swing.GroupLayout panelRedondeado16Layout = new javax.swing.GroupLayout(panelRedondeado16);
        panelRedondeado16.setLayout(panelRedondeado16Layout);
        panelRedondeado16Layout.setHorizontalGroup(
            panelRedondeado16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
        panelRedondeado16Layout.setVerticalGroup(
            panelRedondeado16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        panelRedondeado17.setBackground(new java.awt.Color(153, 0, 255));
        panelRedondeado17.setForeground(new java.awt.Color(51, 51, 255));
        panelRedondeado17.setColorFondo(new java.awt.Color(0, 153, 153));
        panelRedondeado17.setRadioBorde(40);

        javax.swing.GroupLayout panelRedondeado17Layout = new javax.swing.GroupLayout(panelRedondeado17);
        panelRedondeado17.setLayout(panelRedondeado17Layout);
        panelRedondeado17Layout.setHorizontalGroup(
            panelRedondeado17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
        panelRedondeado17Layout.setVerticalGroup(
            panelRedondeado17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Disponible");

        panelRedondeado18.setBackground(new java.awt.Color(153, 0, 255));
        panelRedondeado18.setForeground(new java.awt.Color(51, 51, 255));
        panelRedondeado18.setColorFondo(new java.awt.Color(153, 153, 153));
        panelRedondeado18.setRadioBorde(40);

        javax.swing.GroupLayout panelRedondeado18Layout = new javax.swing.GroupLayout(panelRedondeado18);
        panelRedondeado18.setLayout(panelRedondeado18Layout);
        panelRedondeado18Layout.setHorizontalGroup(
            panelRedondeado18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );
        panelRedondeado18Layout.setVerticalGroup(
            panelRedondeado18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Vacio");

        lblNameT8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT8.setText("Vacio");

        lblNameT1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT1.setText("Vacio");

        lblNameT2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT2.setText("Vacio");

        lblNameT3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT3.setText("Vacio");

        lblNameT4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT4.setText("Vacio");

        lblNameT5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT5.setText("Vacio");

        lblNameT6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT6.setText("Vacio");

        lblNameT7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT7.setText("Vacio");

        T3.setBackground(new java.awt.Color(153, 0, 255));
        T3.setForeground(new java.awt.Color(51, 51, 255));
        T3.setColorFondo(new java.awt.Color(153, 153, 153));
        T3.setRadioBorde(40);

        lblVolumeT3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT3.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT3.setText("-");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("1000 gl");

        javax.swing.GroupLayout T3Layout = new javax.swing.GroupLayout(T3);
        T3.setLayout(T3Layout);
        T3Layout.setHorizontalGroup(
            T3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T3Layout.setVerticalGroup(
            T3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel28)
                .addContainerGap())
        );

        T5.setBackground(new java.awt.Color(153, 0, 255));
        T5.setForeground(new java.awt.Color(51, 51, 255));
        T5.setColorFondo(new java.awt.Color(153, 153, 153));
        T5.setRadioBorde(40);

        lblVolumeT5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT5.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT5.setText("-");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("1000 gl");

        javax.swing.GroupLayout T5Layout = new javax.swing.GroupLayout(T5);
        T5.setLayout(T5Layout);
        T5Layout.setHorizontalGroup(
            T5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T5Layout.setVerticalGroup(
            T5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addContainerGap())
        );

        T2.setBackground(new java.awt.Color(153, 0, 255));
        T2.setForeground(new java.awt.Color(51, 51, 255));
        T2.setColorFondo(new java.awt.Color(153, 153, 153));
        T2.setRadioBorde(40);

        lblVolumeT2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT2.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT2.setText("-");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("1000 gl");

        javax.swing.GroupLayout T2Layout = new javax.swing.GroupLayout(T2);
        T2.setLayout(T2Layout);
        T2Layout.setHorizontalGroup(
            T2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T2Layout.setVerticalGroup(
            T2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addContainerGap())
        );

        T4.setBackground(new java.awt.Color(153, 0, 255));
        T4.setForeground(new java.awt.Color(51, 51, 255));
        T4.setColorFondo(new java.awt.Color(153, 153, 153));
        T4.setRadioBorde(40);

        lblVolumeT4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT4.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT4.setText("-");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("1000 gl");

        javax.swing.GroupLayout T4Layout = new javax.swing.GroupLayout(T4);
        T4.setLayout(T4Layout);
        T4Layout.setHorizontalGroup(
            T4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T4Layout.setVerticalGroup(
            T4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addContainerGap())
        );

        T9.setBackground(new java.awt.Color(153, 0, 255));
        T9.setForeground(new java.awt.Color(51, 51, 255));
        T9.setColorFondo(new java.awt.Color(153, 153, 153));
        T9.setRadioBorde(40);

        lblVolumeT9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT9.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT9.setText("-");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("1000 gl");

        javax.swing.GroupLayout T9Layout = new javax.swing.GroupLayout(T9);
        T9.setLayout(T9Layout);
        T9Layout.setHorizontalGroup(
            T9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T9Layout.setVerticalGroup(
            T9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addContainerGap())
        );

        T7.setBackground(new java.awt.Color(153, 0, 255));
        T7.setForeground(new java.awt.Color(51, 51, 255));
        T7.setColorFondo(new java.awt.Color(153, 153, 153));
        T7.setRadioBorde(40);

        lblVolumeT7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT7.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT7.setText("-");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("1000 gl");

        javax.swing.GroupLayout T7Layout = new javax.swing.GroupLayout(T7);
        T7.setLayout(T7Layout);
        T7Layout.setHorizontalGroup(
            T7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T7Layout.setVerticalGroup(
            T7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addContainerGap())
        );

        T6.setBackground(new java.awt.Color(153, 0, 255));
        T6.setForeground(new java.awt.Color(51, 51, 255));
        T6.setColorFondo(new java.awt.Color(153, 153, 153));
        T6.setRadioBorde(40);

        lblVolumeT6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT6.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT6.setText("-");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("1000 gl");

        javax.swing.GroupLayout T6Layout = new javax.swing.GroupLayout(T6);
        T6.setLayout(T6Layout);
        T6Layout.setHorizontalGroup(
            T6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T6Layout.setVerticalGroup(
            T6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel31)
                .addContainerGap())
        );

        T8.setBackground(new java.awt.Color(153, 0, 255));
        T8.setForeground(new java.awt.Color(51, 51, 255));
        T8.setColorFondo(new java.awt.Color(153, 153, 153));
        T8.setRadioBorde(40);

        lblVolumeT8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblVolumeT8.setForeground(new java.awt.Color(255, 255, 255));
        lblVolumeT8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVolumeT8.setText("-");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("1000 gl");

        javax.swing.GroupLayout T8Layout = new javax.swing.GroupLayout(T8);
        T8.setLayout(T8Layout);
        T8Layout.setHorizontalGroup(
            T8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(T8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVolumeT8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addContainerGap())
        );
        T8Layout.setVerticalGroup(
            T8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(T8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVolumeT8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addContainerGap())
        );

        lblNameT9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameT9.setText("Vacio");

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(337, 337, 337)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelRedondeado3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelRedondeado15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(panelRedondeado16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelRedondeado17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelRedondeado18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(294, 294, 294)
                        .addComponent(btnBack)
                        .addGap(46, 46, 46)
                        .addComponent(btnSave)))
                .addGap(0, 29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(T1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(T2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(T3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNameT3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNameT4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(T4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(T5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(T6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rb7)
                            .addComponent(T7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNameT1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(lblNameT2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(243, 243, 243)
                        .addComponent(lblNameT5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(lblNameT6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(lblNameT7, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(T8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameT8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNameT9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb9))
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNameT1)
                    .addComponent(lblNameT2)
                    .addComponent(lblNameT3)
                    .addComponent(lblNameT4)
                    .addComponent(lblNameT5)
                    .addComponent(lblNameT6)
                    .addComponent(lblNameT7)
                    .addComponent(lblNameT8)
                    .addComponent(lblNameT9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(T3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rb1)
                    .addComponent(rb3)
                    .addComponent(rb2)
                    .addComponent(rb4)
                    .addComponent(rb5)
                    .addComponent(rb6)
                    .addComponent(rb7)
                    .addComponent(rb9)
                    .addComponent(rb8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(panelRedondeado3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelRedondeado15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(panelRedondeado16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(panelRedondeado17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelRedondeado18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSave)
                            .addComponent(btnBack))
                        .addContainerGap())))
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

    private void rb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb2ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
       System.out.println("Guardando compartimiento seleccionado: " + compartmentSelected);
        
        // Simplemente cerramos la ventana. 
        // La ventana padre (RegisLoad) recogerá el valor al descongelarse.
        this.dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void rb7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb7ActionPerformed

    private void rb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb3ActionPerformed

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
                TankCompartments dialog = new TankCompartments(new javax.swing.JFrame(), true);
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
    private vista.paintCode.PanelRedondeado T1;
    private vista.paintCode.PanelRedondeado T2;
    private vista.paintCode.PanelRedondeado T3;
    private vista.paintCode.PanelRedondeado T4;
    private vista.paintCode.PanelRedondeado T5;
    private vista.paintCode.PanelRedondeado T6;
    private vista.paintCode.PanelRedondeado T7;
    private vista.paintCode.PanelRedondeado T8;
    private vista.paintCode.PanelRedondeado T9;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblNameT1;
    private javax.swing.JLabel lblNameT2;
    private javax.swing.JLabel lblNameT3;
    private javax.swing.JLabel lblNameT4;
    private javax.swing.JLabel lblNameT5;
    private javax.swing.JLabel lblNameT6;
    private javax.swing.JLabel lblNameT7;
    private javax.swing.JLabel lblNameT8;
    private javax.swing.JLabel lblNameT9;
    private javax.swing.JLabel lblVolumeT1;
    private javax.swing.JLabel lblVolumeT2;
    private javax.swing.JLabel lblVolumeT3;
    private javax.swing.JLabel lblVolumeT4;
    private javax.swing.JLabel lblVolumeT5;
    private javax.swing.JLabel lblVolumeT6;
    private javax.swing.JLabel lblVolumeT7;
    private javax.swing.JLabel lblVolumeT8;
    private javax.swing.JLabel lblVolumeT9;
    private vista.paintCode.PanelRedondeado panelRedondeado15;
    private vista.paintCode.PanelRedondeado panelRedondeado16;
    private vista.paintCode.PanelRedondeado panelRedondeado17;
    private vista.paintCode.PanelRedondeado panelRedondeado18;
    private vista.paintCode.PanelRedondeado panelRedondeado3;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JRadioButton rb4;
    private javax.swing.JRadioButton rb5;
    private javax.swing.JRadioButton rb6;
    private javax.swing.JRadioButton rb7;
    private javax.swing.JRadioButton rb8;
    private javax.swing.JRadioButton rb9;
    // End of variables declaration//GEN-END:variables
}
