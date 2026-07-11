/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms;

import controllers.LoadController;
import java.awt.Color;
import java.awt.Dimension;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import modelo.domain.Customer;
import modelo.dto.LoadDTO;
import modelo.util.ConfigManager;
import modelo.util.IconManager;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;
import vista.forms.InicialForm;
import vista.forms.cards.CardLoadHistory;

/**
 *
 * @author drola
 */
public class HistoryCustomer extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HistoryCustomer.class.getName());

    private LoadController controllLoad = new LoadController();
    private List<LoadDTO> listLoadDtoGlob = new ArrayList<>();
    private Customer customerGlob = null;
    private InicialForm inicialForm;

    private final DateTimeFormatter FECHA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy");

    /**
     * Creates new form MonitorFletes
     */
    public HistoryCustomer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setIconImage(parent.getIconImage());
    }

    public HistoryCustomer(InicialForm inicialForm, Customer customer) {
        this(inicialForm, true);
        this.inicialForm = inicialForm;
        this.customerGlob = customer;
        
        System.err.println("id" + customerGlob.getId());
        loadIconsAndParams();
        initData();
        inicialForm.setVisible(false);
    }

    private void initData() {
        initScroll();
        listLoadDtoGlob = initCardsLoadsHistory(customerGlob.getId());
        System.out.println("tamaño" + listLoadDtoGlob.size());
        injectDataCustomer();
        renderizarTarjetas(listLoadDtoGlob);
        
    }

    private void injectDataCustomer() {
        lblNameCustomer.setText(customerGlob.getName());
        lblNameEeSs.setText(customerGlob.getNameES());
        lblAddressEsSs.setText(customerGlob.getAddressEs());
        
        if (listLoadDtoGlob.isEmpty()) {
            return;
        }
        int n = listLoadDtoGlob.size();

        lblSizeLoads.setText(String.valueOf(n));
        lblSizeLoads2.setText(String.valueOf(n));
        lblFletesDistincts.setText(String.valueOf(listLoadDtoGlob.getFirst().getFletesDistincs()));

        //money
        double averagePurchase = 0;
        double totalPurchase = 0;
        double totalAdvance = 0;

        double averageVolume = 0;
        int totalVolume = 0;
        int totalDb5 = 0;
        int totalPremiun = 0;
        int totalReguar = 0;

        for (LoadDTO data : listLoadDtoGlob) {
            totalPurchase += data.getAmount();
            totalAdvance += data.getAdvance();

            totalVolume += data.getTotalVolume();
            totalDb5 += data.getVolumeof("DB5");
            totalPremiun += data.getVolumeof("G. Premiun");
            totalReguar += data.getVolumeof("G. Regular");

        }
        double totalBalance = totalPurchase - totalAdvance;
        averagePurchase = totalPurchase / n;
        lblTotalFactura.setText("S/ " + String.valueOf((int)totalPurchase));
        lblAveragePurcharse.setText(String.valueOf((int)averagePurchase));
        lblTotalAdvance.setText("S/ " + String.valueOf((int)totalAdvance));
        lblTotalBalance.setText("S/ " + String.valueOf((int)totalBalance));

        averageVolume = totalVolume / n;
        lblAverageVolume.setText(String.valueOf((int)averageVolume));
        lblTotalBD5.setText(String.valueOf((int)totalDb5) + " gal");
        lblTotalPremiun.setText(String.valueOf((int)totalPremiun) + " gal");
        lblTotalregular.setText(String.valueOf((int)totalReguar) + " gal");

        //lblUsualOperator.setText(listLoadDtoGlob.getFirst().g
        lblUsualVehicle.setText("Scannia R540");
        String s = ConfigManager.formatNameMonth(listLoadDtoGlob.getFirst().getDate());
        lblLastDatePurchase.setText(s);

        if (totalBalance > 0) {
            lblStatusPaymentglob.setBackground(ConfigManager.COLOR_RED);
            lblStatusPaymentglob.setText("CON DEUDA");
            //panelBalanceGlob.setColorFondo(ConfigManager.COLOR_RED);
        } else {

            lblStatusPaymentglob.setText("SIN DEUDA");
           // panelBalanceGlob.setColorFondo(new Color(45, 85, 78));
        }

    }

    private List<LoadDTO> initCardsLoadsHistory(int id_customer) {
        List<LoadDTO> l = new ArrayList<>();
        try {
            l = controllLoad.getLoadsWithDetailsCus(id_customer);
            System.err.println("metod" + l.size());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return l;
    }

    private void renderizarTarjetas(List<LoadDTO> listaDatos) {
        // 1. Configurar el Layout del panel contenedor (si no lo hiciste en el diseño visual)
        // BoxLayout en Y_AXIS apila los componentes uno debajo del otro
        panelContenedorTarjetas.setLayout(new BoxLayout(panelContenedorTarjetas, BoxLayout.Y_AXIS));

        // 2. ¡VITAL! Limpiar el panel por si el usuario presiona el botón varias veces
        panelContenedorTarjetas.removeAll();

        // 3. Iterar los datos y construir las cards
        for (LoadDTO datos : listaDatos) {

            // Instanciamos tu Card (asumiendo que le pasas los datos por el constructor o un setter)
            CardLoadHistory card = new CardLoadHistory(datos);

            // Agregamos la card al panel contenedor
            panelContenedorTarjetas.add(card);

            // TIP SENIOR: Agregar un espacio (margen) entre cada tarjeta
            panelContenedorTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // 4. ¡LA MAGIA DE SWING! 
        // Le decimos a Java que recalcule los tamaños y redibuje la pantalla
        panelContenedorTarjetas.revalidate();
        panelContenedorTarjetas.repaint();

        // 5. Mover el scroll al principio (opcional, pero excelente UX)
        javax.swing.SwingUtilities.invokeLater(() -> {
            jScrollLoads.getVerticalScrollBar().setValue(0);
        });
    }

    private void initScroll() {
        jScrollLoads.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPaneInfoCustomer.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void loadIconsAndParams() {
        this.setTitle("Bulcar");
        initUserCurrent();
        IconManager.setIkonliIcon(lblPerson, BootstrapIcons.PERSON_CIRCLE, 85);                     
        IconManager.setIkonliIcon(btnBack, BoxiconsRegular.ARROW_BACK, 25);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        tabMonitor = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollLoads = new javax.swing.JScrollPane();
        panelContenedorTarjetas = new javax.swing.JPanel();
        lblRole = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPaneInfoCustomer = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lblNameCustomer = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        fdsffds13 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        fdsffds15 = new javax.swing.JLabel();
        fdsffds16 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        fdsffds18 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        fdsffds19 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        fdsffds20 = new javax.swing.JLabel();
        lblUsualOperator = new javax.swing.JLabel();
        lblUsualVehicle = new javax.swing.JLabel();
        lblUsualProduct = new javax.swing.JLabel();
        lblDaysentrecompras = new javax.swing.JLabel();
        lblLoadBalance = new javax.swing.JLabel();
        lblNameEeSs = new javax.swing.JLabel();
        lblAddressEsSs = new javax.swing.JLabel();
        lblOperatorName3 = new javax.swing.JLabel();
        lblStatusPaymentglob = new javax.swing.JLabel();
        lblOperatorName5 = new javax.swing.JLabel();
        lblOperatorName6 = new javax.swing.JLabel();
        lblOperatorName7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        lblOperatorName8 = new javax.swing.JLabel();
        fdsffds5 = new javax.swing.JLabel();
        fdsffds11 = new javax.swing.JLabel();
        fdsffds25 = new javax.swing.JLabel();
        lblTotalBD5 = new javax.swing.JLabel();
        lblTotalPremiun = new javax.swing.JLabel();
        lblTotalregular = new javax.swing.JLabel();
        lblOperatorName9 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        lblPerson = new javax.swing.JLabel();
        panelRedondeado2 = new vista.paintCode.PanelRedondeado();
        lblSizeLoads = new javax.swing.JLabel();
        fdsffds = new javax.swing.JLabel();
        panelRedondeado3 = new vista.paintCode.PanelRedondeado();
        fdsffds2 = new javax.swing.JLabel();
        lblFletesDistincts = new javax.swing.JLabel();
        panelRedondeado4 = new vista.paintCode.PanelRedondeado();
        fdsffds4 = new javax.swing.JLabel();
        lblAveragePurcharse = new javax.swing.JLabel();
        panelRedondeado5 = new vista.paintCode.PanelRedondeado();
        fdsffds10 = new javax.swing.JLabel();
        lblAverageVolume = new javax.swing.JLabel();
        panelRedondeado6 = new vista.paintCode.PanelRedondeado();
        fdsffds14 = new javax.swing.JLabel();
        lblLastDatePurchase = new javax.swing.JLabel();
        panelRedondeado7 = new vista.paintCode.PanelRedondeado();
        fdsffds12 = new javax.swing.JLabel();
        lblAverageVehicle = new javax.swing.JLabel();
        panelRedondeado8 = new vista.paintCode.PanelRedondeado();
        jLabel15 = new javax.swing.JLabel();
        lblTotalFactura = new javax.swing.JLabel();
        panelRedondeado9 = new vista.paintCode.PanelRedondeado();
        jLabel16 = new javax.swing.JLabel();
        lblTotalAdvance = new javax.swing.JLabel();
        panelBalanceGlob = new vista.paintCode.PanelRedondeado();
        jLabel17 = new javax.swing.JLabel();
        lblTotalBalance = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        fdsffds3 = new javax.swing.JLabel();
        lblSizeLoads2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        lblOperatorName11 = new javax.swing.JLabel();
        lblOperatorName12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1255, 730));

        bg.setBackground(new java.awt.Color(204, 204, 204));
        bg.setMaximumSize(new java.awt.Dimension(1255, 730));
        bg.setMinimumSize(new java.awt.Dimension(1255, 730));
        bg.setPreferredSize(new java.awt.Dimension(1255, 730));

        tabMonitor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tabMonitor.setFont(new java.awt.Font("Segoe UI Emoji", 0, 13)); // NOI18N
        tabMonitor.setMaximumSize(new java.awt.Dimension(792, 600));
        tabMonitor.setMinimumSize(new java.awt.Dimension(792, 600));
        tabMonitor.setPreferredSize(new java.awt.Dimension(792, 600));

        jPanel6.setLayout(new java.awt.BorderLayout());

        panelContenedorTarjetas.setBackground(new java.awt.Color(204, 204, 204));
        panelContenedorTarjetas.setLayout(new java.awt.BorderLayout());
        jScrollLoads.setViewportView(panelContenedorTarjetas);

        jPanel6.add(jScrollLoads, java.awt.BorderLayout.CENTER);

        tabMonitor.addTab("Cargas", jPanel6);

        lblRole.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRole.setText("Rol");

        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("username");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel13.setText("Historial Individual");

        jScrollPaneInfoCustomer.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneInfoCustomer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPaneInfoCustomer.setMaximumSize(new java.awt.Dimension(454, 655));
        jScrollPaneInfoCustomer.setMinimumSize(new java.awt.Dimension(454, 655));
        jScrollPaneInfoCustomer.setPreferredSize(new java.awt.Dimension(454, 655));

        jPanel2.setBackground(new java.awt.Color(255, 51, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setMaximumSize(new java.awt.Dimension(386, 1022));
        jPanel7.setMinimumSize(new java.awt.Dimension(386, 1022));
        jPanel7.setPreferredSize(new java.awt.Dimension(386, 1022));

        jSeparator1.setForeground(new java.awt.Color(102, 102, 102));

        lblNameCustomer.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        lblNameCustomer.setText("Ciente");

        jPanel10.setBackground(new java.awt.Color(153, 153, 153));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fdsffds13.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds13.setText("COMPORTAMIENTO HISTORICO");

        jSeparator6.setForeground(new java.awt.Color(204, 204, 204));

        fdsffds15.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        fdsffds15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds15.setText("Operador usual");

        fdsffds16.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        fdsffds16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds16.setText("Vehiculo frecuente");

        jSeparator7.setForeground(new java.awt.Color(204, 204, 204));

        fdsffds18.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        fdsffds18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds18.setText("Producto principal");

        jSeparator8.setForeground(new java.awt.Color(204, 204, 204));

        fdsffds19.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        fdsffds19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds19.setText("Dias entre compras");

        jSeparator9.setForeground(new java.awt.Color(204, 204, 204));

        fdsffds20.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        fdsffds20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds20.setText("Cargas con deuda");

        lblUsualOperator.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        lblUsualOperator.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUsualOperator.setText("Sin asignar");

        lblUsualVehicle.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        lblUsualVehicle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUsualVehicle.setText("Sin asignar");

        lblUsualProduct.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        lblUsualProduct.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUsualProduct.setText("Sin asignar");

        lblDaysentrecompras.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        lblDaysentrecompras.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDaysentrecompras.setText("Sin asignar");

        lblLoadBalance.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        lblLoadBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoadBalance.setText("Sin asignar");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(fdsffds15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblUsualOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(fdsffds13)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(fdsffds16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblUsualVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(fdsffds18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(lblUsualProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jSeparator7)))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(fdsffds19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDaysentrecompras, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jSeparator8)))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(fdsffds20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblLoadBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jSeparator9)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds13)
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fdsffds15, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsualOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fdsffds16, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsualVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fdsffds18, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsualProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fdsffds19, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDaysentrecompras, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.DEFAULT_SIZE, 5, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fdsffds20, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoadBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        lblNameEeSs.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNameEeSs.setText("Nombre EE.SS");

        lblAddressEsSs.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAddressEsSs.setText("Ubicacion EE.SS");

        lblOperatorName3.setBackground(new java.awt.Color(56, 82, 115));
        lblOperatorName3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblOperatorName3.setForeground(new java.awt.Color(255, 255, 255));
        lblOperatorName3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOperatorName3.setText("ESTADO INACTIVO");
        lblOperatorName3.setOpaque(true);

        lblStatusPaymentglob.setBackground(new java.awt.Color(153, 153, 0));
        lblStatusPaymentglob.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblStatusPaymentglob.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatusPaymentglob.setText("NO FACTURADO");
        lblStatusPaymentglob.setOpaque(true);

        lblOperatorName5.setBackground(new java.awt.Color(209, 123, 111));
        lblOperatorName5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblOperatorName5.setText("  GRIFO");
        lblOperatorName5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblOperatorName5.setOpaque(true);

        lblOperatorName6.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName6.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName6.setText("ESTADISTICAS DEL CLIENTE");

        lblOperatorName7.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName7.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName7.setText("RESUMEN FINANCIERO TOTAL");

        jSeparator3.setForeground(new java.awt.Color(102, 102, 102));

        jSeparator4.setForeground(new java.awt.Color(102, 102, 102));

        lblOperatorName8.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName8.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName8.setText("VOLUMEN POR COMBUSTIBLE");

        fdsffds5.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds5.setText("DB5 DIESEL");

        fdsffds11.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds11.setText("G. PREMIUM");

        fdsffds25.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds25.setText("G. REGULAR");

        lblTotalBD5.setFont(new java.awt.Font("Consolas", 1, 13)); // NOI18N
        lblTotalBD5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalBD5.setText("0 gal");

        lblTotalPremiun.setFont(new java.awt.Font("Consolas", 1, 13)); // NOI18N
        lblTotalPremiun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPremiun.setText("0 gal");

        lblTotalregular.setFont(new java.awt.Font("Consolas", 1, 13)); // NOI18N
        lblTotalregular.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalregular.setText("0 gal");

        lblOperatorName9.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName9.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName9.setText("DATOS DE COMPRA FRECUENTES");

        jSeparator5.setForeground(new java.awt.Color(102, 102, 102));

        panelRedondeado2.setColorFondo(new java.awt.Color(153, 153, 153));

        lblSizeLoads.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblSizeLoads.setForeground(new java.awt.Color(0, 51, 153));
        lblSizeLoads.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSizeLoads.setText("0");

        fdsffds.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds.setText("TOTAL CARGAS");

        javax.swing.GroupLayout panelRedondeado2Layout = new javax.swing.GroupLayout(panelRedondeado2);
        panelRedondeado2.setLayout(panelRedondeado2Layout);
        panelRedondeado2Layout.setHorizontalGroup(
            panelRedondeado2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSizeLoads, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fdsffds, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRedondeado2Layout.setVerticalGroup(
            panelRedondeado2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRedondeado2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(lblSizeLoads, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRedondeado3.setColorFondo(new java.awt.Color(153, 153, 153));

        fdsffds2.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds2.setText("FLETES DISTINTOS");

        lblFletesDistincts.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblFletesDistincts.setForeground(new java.awt.Color(0, 51, 153));
        lblFletesDistincts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFletesDistincts.setText("0");

        javax.swing.GroupLayout panelRedondeado3Layout = new javax.swing.GroupLayout(panelRedondeado3);
        panelRedondeado3.setLayout(panelRedondeado3Layout);
        panelRedondeado3Layout.setHorizontalGroup(
            panelRedondeado3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fdsffds2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFletesDistincts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRedondeado3Layout.setVerticalGroup(
            panelRedondeado3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(lblFletesDistincts, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRedondeado4.setColorFondo(new java.awt.Color(153, 153, 153));

        fdsffds4.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds4.setText("PROM. COMPRA");

        lblAveragePurcharse.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblAveragePurcharse.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAveragePurcharse.setText("0");

        javax.swing.GroupLayout panelRedondeado4Layout = new javax.swing.GroupLayout(panelRedondeado4);
        panelRedondeado4.setLayout(panelRedondeado4Layout);
        panelRedondeado4Layout.setHorizontalGroup(
            panelRedondeado4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fdsffds4, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(lblAveragePurcharse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRedondeado4Layout.setVerticalGroup(
            panelRedondeado4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(lblAveragePurcharse, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRedondeado5.setColorFondo(new java.awt.Color(153, 153, 153));

        fdsffds10.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds10.setText("VOL. PROMEDIO GAL");

        lblAverageVolume.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblAverageVolume.setForeground(new java.awt.Color(0, 51, 153));
        lblAverageVolume.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAverageVolume.setText("0");

        javax.swing.GroupLayout panelRedondeado5Layout = new javax.swing.GroupLayout(panelRedondeado5);
        panelRedondeado5.setLayout(panelRedondeado5Layout);
        panelRedondeado5Layout.setHorizontalGroup(
            panelRedondeado5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fdsffds10, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(lblAverageVolume, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRedondeado5Layout.setVerticalGroup(
            panelRedondeado5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(lblAverageVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRedondeado6.setColorFondo(new java.awt.Color(153, 153, 153));

        fdsffds14.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds14.setText("ULTIMA COMPRA");

        lblLastDatePurchase.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLastDatePurchase.setForeground(new java.awt.Color(0, 51, 51));
        lblLastDatePurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLastDatePurchase.setText("----");

        javax.swing.GroupLayout panelRedondeado6Layout = new javax.swing.GroupLayout(panelRedondeado6);
        panelRedondeado6.setLayout(panelRedondeado6Layout);
        panelRedondeado6Layout.setHorizontalGroup(
            panelRedondeado6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fdsffds14, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(lblLastDatePurchase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRedondeado6Layout.setVerticalGroup(
            panelRedondeado6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(lblLastDatePurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRedondeado7.setColorFondo(new java.awt.Color(153, 153, 153));

        fdsffds12.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        fdsffds12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds12.setText("FLETE RECURRENTE");

        lblAverageVehicle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAverageVehicle.setForeground(new java.awt.Color(0, 51, 153));
        lblAverageVehicle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAverageVehicle.setText("Sin asignar");

        javax.swing.GroupLayout panelRedondeado7Layout = new javax.swing.GroupLayout(panelRedondeado7);
        panelRedondeado7.setLayout(panelRedondeado7Layout);
        panelRedondeado7Layout.setHorizontalGroup(
            panelRedondeado7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fdsffds12, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(lblAverageVehicle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRedondeado7Layout.setVerticalGroup(
            panelRedondeado7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(lblAverageVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelRedondeado8.setColorFondo(new java.awt.Color(242, 244, 245));

        jLabel15.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jLabel15.setText("Total Facturado");

        lblTotalFactura.setFont(new java.awt.Font("Consolas", 1, 13)); // NOI18N
        lblTotalFactura.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFactura.setText("     S/ 0 ");

        javax.swing.GroupLayout panelRedondeado8Layout = new javax.swing.GroupLayout(panelRedondeado8);
        panelRedondeado8.setLayout(panelRedondeado8Layout);
        panelRedondeado8Layout.setHorizontalGroup(
            panelRedondeado8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(lblTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelRedondeado8Layout.setVerticalGroup(
            panelRedondeado8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblTotalFactura))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        panelRedondeado9.setColorFondo(new java.awt.Color(242, 244, 245));

        jLabel16.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jLabel16.setText("Total Cobrado");

        lblTotalAdvance.setFont(new java.awt.Font("Consolas", 1, 13)); // NOI18N
        lblTotalAdvance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalAdvance.setText("     S/ 0 ");

        javax.swing.GroupLayout panelRedondeado9Layout = new javax.swing.GroupLayout(panelRedondeado9);
        panelRedondeado9.setLayout(panelRedondeado9Layout);
        panelRedondeado9Layout.setHorizontalGroup(
            panelRedondeado9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalAdvance, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelRedondeado9Layout.setVerticalGroup(
            panelRedondeado9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondeado9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRedondeado9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblTotalAdvance))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        panelBalanceGlob.setColorFondo(new java.awt.Color(242, 244, 245));

        jLabel17.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jLabel17.setText("Deuda Acumulada");

        lblTotalBalance.setFont(new java.awt.Font("Consolas", 1, 13)); // NOI18N
        lblTotalBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalBalance.setText("     S/ 0 ");

        javax.swing.GroupLayout panelBalanceGlobLayout = new javax.swing.GroupLayout(panelBalanceGlob);
        panelBalanceGlob.setLayout(panelBalanceGlobLayout);
        panelBalanceGlobLayout.setHorizontalGroup(
            panelBalanceGlobLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBalanceGlobLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBalanceGlobLayout.setVerticalGroup(
            panelBalanceGlobLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBalanceGlobLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBalanceGlobLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblTotalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fdsffds11, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fdsffds25, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fdsffds5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTotalBD5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTotalPremiun, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTotalregular, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblOperatorName6)
                            .addComponent(lblOperatorName7)
                            .addComponent(lblOperatorName8)
                            .addComponent(lblOperatorName9)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(panelRedondeado4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panelRedondeado7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(panelRedondeado2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelRedondeado6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelRedondeado5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelRedondeado3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(lblPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblNameEeSs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNameCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblAddressEsSs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(18, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(33, 33, 33)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelRedondeado9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelBalanceGlob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelRedondeado8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                                .addComponent(jSeparator3)
                                .addComponent(jSeparator1)
                                .addComponent(jSeparator5))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(lblOperatorName3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatusPaymentglob, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblOperatorName5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(lblNameCustomer)
                        .addGap(18, 18, 18)
                        .addComponent(lblNameEeSs)
                        .addGap(18, 18, 18)
                        .addComponent(lblAddressEsSs))
                    .addComponent(lblPerson, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperatorName3)
                    .addComponent(lblStatusPaymentglob)
                    .addComponent(lblOperatorName5))
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOperatorName6)
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelRedondeado2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRedondeado3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(panelRedondeado4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelRedondeado7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(panelRedondeado5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelRedondeado6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addComponent(lblOperatorName7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRedondeado8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panelRedondeado9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panelBalanceGlob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblOperatorName8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(fdsffds5)
                        .addGap(18, 18, 18)
                        .addComponent(fdsffds11)
                        .addGap(18, 18, 18)
                        .addComponent(fdsffds25))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblTotalBD5)
                        .addGap(18, 18, 18)
                        .addComponent(lblTotalPremiun)
                        .addGap(18, 18, 18)
                        .addComponent(lblTotalregular)))
                .addGap(18, 18, 18)
                .addComponent(lblOperatorName9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        jPanel2.add(jPanel7, java.awt.BorderLayout.CENTER);

        jScrollPaneInfoCustomer.setViewportView(jPanel2);

        btnBack.setBackground(new java.awt.Color(204, 204, 204));
        btnBack.setBorder(null);
        btnBack.setMaximumSize(new java.awt.Dimension(32, 32));
        btnBack.setMinimumSize(new java.awt.Dimension(32, 32));
        btnBack.setPreferredSize(new java.awt.Dimension(32, 32));
        btnBack.addActionListener(this::btnBackActionPerformed);

        fdsffds3.setFont(new java.awt.Font("Consolas", 1, 16)); // NOI18N
        fdsffds3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fdsffds3.setText("Total de cargas");

        lblSizeLoads2.setBackground(new java.awt.Color(56, 82, 115));
        lblSizeLoads2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSizeLoads2.setForeground(new java.awt.Color(255, 255, 255));
        lblSizeLoads2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSizeLoads2.setText("0");
        lblSizeLoads2.setOpaque(true);

        jButton1.setText("EXP");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("--/--/----");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        lblOperatorName11.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName11.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName11.setText("DESDE :");

        lblOperatorName12.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName12.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName12.setText("HASTA :");

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("--/--/----");
        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jButton2.setBackground(new java.awt.Color(153, 153, 255));
        jButton2.setText("Filtrar");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setOpaque(true);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setBackground(new java.awt.Color(255, 102, 102));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("X");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setOpaque(true);
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setBackground(new java.awt.Color(153, 153, 153));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("PDF");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setOpaque(true);
        jButton4.addActionListener(this::jButton4ActionPerformed);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(jScrollPaneInfoCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(fdsffds3)
                                .addGap(18, 18, 18)
                                .addComponent(lblSizeLoads2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(lblOperatorName11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblOperatorName12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(79, 79, 79)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tabMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRole, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRole)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSizeLoads2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOperatorName11)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOperatorName12)
                            .addComponent(jButton2)
                            .addComponent(fdsffds3)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneInfoCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        inicialForm.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void initUserCurrent() {
        lblUser.setText(ConfigManager.USER_CURRENT.getFullName());
        lblRole.setText(ConfigManager.USER_CURRENT.getRole());
        IconManager.setIkonliIcon(lblUser, BoxiconsSolid.USER_DETAIL, 20);
    }

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
                HistoryCustomer dialog = new HistoryCustomer(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel fdsffds;
    private javax.swing.JLabel fdsffds10;
    private javax.swing.JLabel fdsffds11;
    private javax.swing.JLabel fdsffds12;
    private javax.swing.JLabel fdsffds13;
    private javax.swing.JLabel fdsffds14;
    private javax.swing.JLabel fdsffds15;
    private javax.swing.JLabel fdsffds16;
    private javax.swing.JLabel fdsffds18;
    private javax.swing.JLabel fdsffds19;
    private javax.swing.JLabel fdsffds2;
    private javax.swing.JLabel fdsffds20;
    private javax.swing.JLabel fdsffds25;
    private javax.swing.JLabel fdsffds3;
    private javax.swing.JLabel fdsffds4;
    private javax.swing.JLabel fdsffds5;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollLoads;
    private javax.swing.JScrollPane jScrollPaneInfoCustomer;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblAddressEsSs;
    private javax.swing.JLabel lblAveragePurcharse;
    private javax.swing.JLabel lblAverageVehicle;
    private javax.swing.JLabel lblAverageVolume;
    private javax.swing.JLabel lblDaysentrecompras;
    private javax.swing.JLabel lblFletesDistincts;
    private javax.swing.JLabel lblLastDatePurchase;
    private javax.swing.JLabel lblLoadBalance;
    private javax.swing.JLabel lblNameCustomer;
    private javax.swing.JLabel lblNameEeSs;
    private javax.swing.JLabel lblOperatorName11;
    private javax.swing.JLabel lblOperatorName12;
    private javax.swing.JLabel lblOperatorName3;
    private javax.swing.JLabel lblOperatorName5;
    private javax.swing.JLabel lblOperatorName6;
    private javax.swing.JLabel lblOperatorName7;
    private javax.swing.JLabel lblOperatorName8;
    private javax.swing.JLabel lblOperatorName9;
    private javax.swing.JLabel lblPerson;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSizeLoads;
    private javax.swing.JLabel lblSizeLoads2;
    private javax.swing.JLabel lblStatusPaymentglob;
    private javax.swing.JLabel lblTotalAdvance;
    private javax.swing.JLabel lblTotalAdvance1;
    private javax.swing.JLabel lblTotalBD5;
    private javax.swing.JLabel lblTotalBalance;
    private javax.swing.JLabel lblTotalFactura;
    private javax.swing.JLabel lblTotalPremiun;
    private javax.swing.JLabel lblTotalregular;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUsualOperator;
    private javax.swing.JLabel lblUsualProduct;
    private javax.swing.JLabel lblUsualVehicle;
    private vista.paintCode.PanelRedondeado panelBalanceGlob;
    private javax.swing.JPanel panelContenedorTarjetas;
    private vista.paintCode.PanelRedondeado panelRedondeado10;
    private vista.paintCode.PanelRedondeado panelRedondeado2;
    private vista.paintCode.PanelRedondeado panelRedondeado3;
    private vista.paintCode.PanelRedondeado panelRedondeado4;
    private vista.paintCode.PanelRedondeado panelRedondeado5;
    private vista.paintCode.PanelRedondeado panelRedondeado6;
    private vista.paintCode.PanelRedondeado panelRedondeado7;
    private vista.paintCode.PanelRedondeado panelRedondeado8;
    private vista.paintCode.PanelRedondeado panelRedondeado9;
    private javax.swing.JTabbedPane tabMonitor;
    // End of variables declaration//GEN-END:variables
}
