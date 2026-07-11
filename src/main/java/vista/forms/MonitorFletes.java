/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.forms;

import controllers.FleteController;
import controllers.LoadController;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import modelo.domain.Load;
import modelo.dto.LoadDTO;
import modelo.dto.SummaryFlete;
import modelo.util.ExcelExporter;
import modelo.util.FleteObserver;
import modelo.util.FleteSubject;
import modelo.util.IconManager;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;
import vista.forms.InicialForm;
import vista.forms.cards.CardLoadHistory;
import vista.paintCode.BadgeEstadoRenderer;
import vista.paintCode.BotonVerRenderer;
import vista.paintCode.RenderizadorLetras;
import vista.paintCode.RendimientoRenderer;

/**
 *
 * @author drola
 */
public class MonitorFletes extends javax.swing.JDialog implements FleteObserver {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MonitorFletes.class.getName());

    private LoadController controllLoad = new LoadController();
    private FleteController controllFle = new FleteController();
    
    private List<SummaryFlete> listFletesGlob = new ArrayList<>();
    private String numDocFleteGlob ="";
    private final DateTimeFormatter FECHA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy");

    /**
     * Creates new form MonitorFletes
     */
    public MonitorFletes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         // Suscripción al Observer
        FleteSubject.getInstance().addObserver(this);
        initTable();
        
    }

    private void initTable() {
        // 1. Configuraciones de datos y orden

        initTextUI();
        tblFletes.setAutoCreateRowSorter(true);
        jScrollLoads.getVerticalScrollBar().setUnitIncrement(16);
        // 2. Aplicar el pintor de letras/colores SOLO a las columnas de datos (0 a 10)
        // Dejamos libres la 11, 12 y 13 para que no pierdan sus botones/badges
        RenderizadorLetras pintorGeneral = new RenderizadorLetras();
        for (int i = 0; i <= 10; i++) {
            tblFletes.getColumnModel().getColumn(i).setCellRenderer(pintorGeneral);
        }

        // 3. Aplicar tus estilos (Cabecera, Botones, Badges y Rendimiento)
        aplicarEstiloTablaFletes();
        tblFletes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                // 1. Detectar coordenadas exactas del clic
                int filaVisual = tblFletes.rowAtPoint(evt.getPoint());
                int columnaVisual = tblFletes.columnAtPoint(evt.getPoint());

                // 2. Si el clic fue en una fila válida Y exactamente en la columna 13 (tu botón)
                if (filaVisual >= 0 && columnaVisual == 13) {

                    // 3. Traducimos la fila por si la tabla está ordenada
                    int filaModelo = tblFletes.convertRowIndexToModel(filaVisual);

                    // 5. ¡TU ACCIÓN VA AQUÍ! 
                    Object doc_num = tblFletes.getModel().getValueAt(filaModelo, 0);
                    numDocFleteGlob = (String) doc_num;
                    System.out.println("¡Botón presionado al primer clic! Flete ID: " + doc_num);
                    SummaryFlete s = MonitorFletes.this.listFletesGlob.get(filaModelo);
                    initInfoFleteSelect(s);
                    //init cards loads
                    List<LoadDTO> list = initCardsLoadsHistory(numDocFleteGlob);
                    if (!list.isEmpty()) {
                        renderizarTarjetas(list);
                        
                        double part = 0;
                        
                        for (LoadDTO d : list) {
                            System.err.println("aca");
                            part += d.getTotalVolume();
                        }
                        
                        double total = 30000;
                        double porcentaje = (total > 0) ? (part / total) * 100 : 0.0;
                        int i = (int) porcentaje;
                        MonitorFletes.circlePercent.setPorcentaje(i);
                    }

                }
            }
        });
        // 4. Cargar la información desde la base de datos
        loadDaraAsincronico();
        // Ajusta el "16" para hacerlo más rápido o más lento según prefieras

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

    private List<LoadDTO> initCardsLoadsHistory(String num_doc) {
        List<LoadDTO> l = new ArrayList<>();
        try {
            l = controllLoad.getLoadsWithDetails(num_doc);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return l;
    }

    private String getSafeString(String value, String defaultValue) {
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }

    private void initInfoFleteSelect(SummaryFlete s) {
        lblTitleDocFlete.setText(getSafeString(" Estado del Flete:    " + s.getNumDoc(), "Sin asignar"));
        lblSizeLoads.setText(getSafeString(String.valueOf(s.getSizeLoads()), "0"));
        lblOperatorName.setText(getSafeString("👤" + s.getOperator().getFullname(), "Sin asignar"));
        lblVehicleName.setText(getSafeString("🚛" + s.getVehicle().getManufacturer(), "---"));
        lblStartDate.setText(s.getStarDate() != null ? "📅 Inicio: " + s.getStarDate().format(FECHA_FORMAT) : "--/--/--");
        lblKmTraveled.setText(getSafeString(String.valueOf(s.getKmTraveled()), "0 km"));
        lblIncomes.setText(String.valueOf(s.getIncomes()));
        lblExpense.setText(String.valueOf(s.getExpenses()));
        lblProfit.setText(String.valueOf(s.getProfit()));
    }

    private void aplicarEstiloTablaFletes() {
        // 1. Configurar la Cabecera
        javax.swing.table.JTableHeader header = tblFletes.getTableHeader();
        header.setBackground(new java.awt.Color(200, 196, 191));
        header.setForeground(new java.awt.Color(34, 34, 34));
        header.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        header.setOpaque(true);
        header.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(136, 136, 136)));
        header.setReorderingAllowed(false);

        // 2. Configurar filas
        tblFletes.setRowHeight(35); // Un poco más alto para que el BOTÓN se vea bien y sea fácil de clickear
        tblFletes.setShowVerticalLines(true);
        tblFletes.setShowHorizontalLines(true);
        tblFletes.setGridColor(new java.awt.Color(230, 230, 230));

        // 3. Colores de selección
        tblFletes.setSelectionBackground(new java.awt.Color(184, 208, 240));
        tblFletes.setSelectionForeground(java.awt.Color.BLACK);

        // 4. RENDERERS ESPECIALES (Aquí es donde ocurre la magia que no querías perder)
        // Columna 11: Rendimiento
        tblFletes.getColumnModel().getColumn(11).setCellRenderer(new RendimientoRenderer());

        // Columna 12: Estado (Badge)
        tblFletes.getColumnModel().getColumn(12).setCellRenderer(new BadgeEstadoRenderer());

        // Columna 13: EL BOTÓN (Pintor y Editor de clic)
        tblFletes.getColumnModel().getColumn(13).setCellRenderer(new BotonVerRenderer());

        // Ajustamos el ancho del botón para que no sea tan gigante
        tblFletes.getColumnModel().getColumn(13).setPreferredWidth(80);
    }

    private void initTextUI() {
        IconManager.setIkonliIcon(lblUser, BoxiconsSolid.USER_DETAIL, 20);
        this.setTitle("Bulcar - Resumen de Flestes");

        tabMonitor.setTitleAt(0, "📦 Historial de cargas");
        tabMonitor.setTitleAt(1, "⛽ Combustible");
        tabMonitor.setTitleAt(2, "📊 Rendimiento");
        tabMonitor.setTitleAt(3, "⚠️ Alertas");

        lblOperatorName.setText("👤 Operador");
        lblStartDate.setText("📅 Inicio:--/--/--");
        lblDaysTraveled.setText("🕐 En Ruta: ? dias");
        lblVehicleName.setText("🚛 Vehiculo");

        lblUser.setText(InicialForm.currentUser.getFullName());
        lblRole.setText(InicialForm.currentUser.getRole());
        lblTitleDocFlete.setText("📋  Estado del Flete:    -----------");
        IconManager.setIkonliIcon(btnBack, BoxiconsRegular.ARROW_BACK, 25);
    }

    private void loadDaraAsincronico() {
        // 1. UX: Mostrar mensaje temporal antes de lanzar el hilo
        DefaultTableModel modeloBase = (DefaultTableModel) tblFletes.getModel();
        modeloBase.setRowCount(0);
        // Puse los 14 espacios para que coincida con tus 14 columnas
        modeloBase.addRow(new Object[]{"⏳ Cargando datos desde DataBase...", "", "", "", "", "", "", "", "", "", "", "", "", ""});

        SwingWorker<List<SummaryFlete>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<SummaryFlete> doInBackground() {
                FleteController dao = new FleteController();
                try {
                    return dao.getList();
                } catch (SQLException e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Error DB: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    List<SummaryFlete> fletes = get();

                    // Si hubo un error en la BD y regresó null, detenemos el proceso
                    if (fletes == null) {
                        return;
                    }
                    MonitorFletes.this.listFletesGlob = fletes;
                    DefaultTableModel modelo = (DefaultTableModel) tblFletes.getModel();
                    modelo.setRowCount(0); // Borramos el mensaje de "Cargando..."

                    java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy");

                    // Llenamos con los datos reales y LIMPIOS (solo números, sin textos como S/ o Km aquí)
                    for (SummaryFlete f : fletes) {
                        modelo.addRow(new Object[]{
                            f.getNumDoc(),
                            f.getVehicle() != null ? f.getVehicle().toString() : "",
                            f.getOperator() != null ? f.getOperator().getFullname() : "",
                            f.getStarDate() != null ? f.getStarDate().format(dtf) : "-",
                            f.getEndDate() != null ? f.getEndDate().format(dtf) : "EN RUTA",
                            f.getSizeLoads(),
                            f.getVolumeTotal(),
                            f.getKmTraveled(),
                            f.getIncomes(),
                            f.getExpenses(),
                            f.getProfit(),
                            "----",
                            f.getStatus(),
                            "----"
                        });
                    }
                    MonitorFletes.this.txtTotalFletes.setText("Total: " + String.valueOf(fletes.size()));
                    System.out.println("✅ Tabla cargada con éxito.");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Buena práctica restaurar el flag
                    System.err.println("Hilo interrumpido: " + e.getMessage());
                    e.printStackTrace();
                } catch (java.util.concurrent.ExecutionException e) {
                    // Separar este catch te da el error REAL del doInBackground
                    System.err.println("Error en consulta: " + e.getCause().getMessage());
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private int searchId(String numDoc){
        for (SummaryFlete data : listFletesGlob) {
            if(data.getNumDoc().equalsIgnoreCase(numDoc)){
                return data.getId();
            }
        }
        return -1;
    }
    @Override
    public void onFleteChanged() {
         System.out.println("[MonitorFletes] Recibido evento Observer. Recargando tabla...");
        loadDaraAsincronico(); // Tu método existente para refrescar la tabla
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
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFletes = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        circlePercent = new vista.paintCode.GaugeCircularPro();
        jSeparator1 = new javax.swing.JSeparator();
        lblTitleDocFlete = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        fdsffds = new javax.swing.JLabel();
        lblSizeLoads = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lblOperatorName = new javax.swing.JLabel();
        lblVehicleName = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblDaysTraveled = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblIncomes = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblKmTraveled = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblProfit = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblExpense = new javax.swing.JLabel();
        btnDeletedFlete = new javax.swing.JButton();
        tabMonitor = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollLoads = new javax.swing.JScrollPane();
        panelContenedorTarjetas = new javax.swing.JPanel();
        sa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtTotalFletes = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        lblOperatorName11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        lblOperatorName12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setText("Exportar");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        tblFletes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblFletes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Flete", "Vehículo", "Operador", "Fecha inicio", "Fecha fin", "Cargas", "Vol. Total (gal)", "Kilometraje", "Ingresos", "Egresos", "Utilidad", "% Rendimiento", "Estado", "Acciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblFletes);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setMaximumSize(new java.awt.Dimension(1233, 307));
        jPanel2.setMinimumSize(new java.awt.Dimension(1233, 307));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(1233, 307));

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        circlePercent.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout circlePercentLayout = new javax.swing.GroupLayout(circlePercent);
        circlePercent.setLayout(circlePercentLayout);
        circlePercentLayout.setHorizontalGroup(
            circlePercentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 94, Short.MAX_VALUE)
        );
        circlePercentLayout.setVerticalGroup(
            circlePercentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 85, Short.MAX_VALUE)
        );

        jSeparator1.setForeground(new java.awt.Color(102, 102, 102));

        lblTitleDocFlete.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        lblTitleDocFlete.setText(" Estado del Flete:    -----------");

        jSeparator2.setForeground(new java.awt.Color(102, 102, 102));

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fdsffds.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        fdsffds.setText("Cargas");

        lblSizeLoads.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblSizeLoads.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSizeLoads.setText("------");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(fdsffds, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSizeLoads, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fdsffds)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSizeLoads)
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblOperatorName.setFont(new java.awt.Font("Segoe UI Emoji", 0, 13)); // NOI18N
        lblOperatorName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOperatorName.setText("Operador");

        lblVehicleName.setFont(new java.awt.Font("Segoe UI Emoji", 0, 13)); // NOI18N
        lblVehicleName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblVehicleName.setText("Vehiculo");

        lblStartDate.setFont(new java.awt.Font("Segoe UI Emoji", 0, 13)); // NOI18N
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Inicio: -/-/-");

        lblDaysTraveled.setFont(new java.awt.Font("Segoe UI Emoji", 0, 13)); // NOI18N
        lblDaysTraveled.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDaysTraveled.setText(" En Ruta: ?? dias");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOperatorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVehicleName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDaysTraveled, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOperatorName)
                    .addComponent(lblStartDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVehicleName)
                    .addComponent(lblDaysTraveled))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel6.setText("Ingresos");

        lblIncomes.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblIncomes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIncomes.setText("S/ ??");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIncomes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(lblIncomes)
                .addContainerGap())
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel3.setText("Km Recorrido");

        lblKmTraveled.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblKmTraveled.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKmTraveled.setText("------");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(15, 15, 15))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKmTraveled, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(lblKmTraveled)
                .addContainerGap())
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel5.setText("   Rendimiento %");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("??");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel8.setText("Utilidad");

        lblProfit.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblProfit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProfit.setText("S/ ??");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel8)
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProfit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(lblProfit)
                .addContainerGap())
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel7.setText("Egresos");

        lblExpense.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblExpense.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblExpense.setText("S/ ??");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(31, 31, 31))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(lblExpense, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(lblExpense)
                .addContainerGap())
        );

        btnDeletedFlete.setBackground(new java.awt.Color(255, 102, 102));
        btnDeletedFlete.setForeground(new java.awt.Color(255, 255, 255));
        btnDeletedFlete.setText("X");
        btnDeletedFlete.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnDeletedFlete.setOpaque(true);
        btnDeletedFlete.addActionListener(this::btnDeletedFleteActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTitleDocFlete)
                        .addGap(73, 73, 73)
                        .addComponent(btnDeletedFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jSeparator2)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(circlePercent, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitleDocFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeletedFlete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(circlePercent, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        tabMonitor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tabMonitor.setFont(new java.awt.Font("Segoe UI Emoji", 0, 13)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelContenedorTarjetasLayout = new javax.swing.GroupLayout(panelContenedorTarjetas);
        panelContenedorTarjetas.setLayout(panelContenedorTarjetasLayout);
        panelContenedorTarjetasLayout.setHorizontalGroup(
            panelContenedorTarjetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
        );
        panelContenedorTarjetasLayout.setVerticalGroup(
            panelContenedorTarjetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        jScrollLoads.setViewportView(panelContenedorTarjetas);

        jPanel3.add(jScrollLoads, java.awt.BorderLayout.CENTER);

        tabMonitor.addTab("Historial de cargas", jPanel3);

        sa.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Seleccione un Flete ");

        javax.swing.GroupLayout saLayout = new javax.swing.GroupLayout(sa);
        sa.setLayout(saLayout);
        saLayout.setHorizontalGroup(
            saLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saLayout.createSequentialGroup()
                .addGap(317, 317, 317)
                .addComponent(jLabel1)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        saLayout.setVerticalGroup(
            saLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jLabel1)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        tabMonitor.addTab("Combustible", sa);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 791, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 262, Short.MAX_VALUE)
        );

        tabMonitor.addTab("Rendimiento", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 791, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 262, Short.MAX_VALUE)
        );

        tabMonitor.addTab("Alertas", jPanel6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabMonitor)))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("username");

        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRole.setText("Rol");

        btnBack.setBackground(new java.awt.Color(204, 204, 204));
        btnBack.setBorder(null);
        btnBack.setMaximumSize(new java.awt.Dimension(32, 32));
        btnBack.setMinimumSize(new java.awt.Dimension(32, 32));
        btnBack.setPreferredSize(new java.awt.Dimension(32, 32));
        btnBack.addActionListener(this::btnBackActionPerformed);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel13.setText("Resumen");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 906, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRole, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lblUser)
                        .addGap(12, 12, 12)
                        .addComponent(lblRole))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTotalFletes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalFletes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTotalFletes.setText("Total:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotalFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTotalFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jButton4.setBackground(new java.awt.Color(102, 204, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("PDF");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setOpaque(true);
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("--/--/----");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("--/--/----");
        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        lblOperatorName11.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName11.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName11.setText("DESDE :");

        jButton2.setBackground(new java.awt.Color(153, 153, 255));
        jButton2.setText("Filtrar");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setOpaque(true);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        lblOperatorName12.setFont(new java.awt.Font("Segoe UI Emoji", 1, 13)); // NOI18N
        lblOperatorName12.setForeground(new java.awt.Color(102, 102, 102));
        lblOperatorName12.setText("HASTA :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(266, 266, 266)
                        .addComponent(lblOperatorName11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOperatorName12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addGap(23, 23, 23))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOperatorName11)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOperatorName12)
                            .addComponent(jButton2)
                            .addComponent(jButton4)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
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

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ExcelExporter.exportar(tblFletes, "Historial");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnDeletedFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletedFleteActionPerformed
       if(numDocFleteGlob.isBlank()){
           JOptionPane.showMessageDialog(this, "Seleccione un Flete");
           return;
       }
       
       
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas eliminar este flete: "+numDocFleteGlob+"?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            
            try {
                
                boolean success = controllFle.deleted(searchId(numDocFleteGlob));
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registro eliminado");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: "+e.getMessage());
            }
          
        } 
    }//GEN-LAST:event_btnDeletedFleteActionPerformed

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
                MonitorFletes dialog = new MonitorFletes(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDeletedFlete;
    public static vista.paintCode.GaugeCircularPro circlePercent;
    private javax.swing.JLabel fdsffds;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollLoads;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblDaysTraveled;
    private javax.swing.JLabel lblExpense;
    private javax.swing.JLabel lblIncomes;
    private javax.swing.JLabel lblKmTraveled;
    private javax.swing.JLabel lblOperatorName;
    private javax.swing.JLabel lblOperatorName11;
    private javax.swing.JLabel lblOperatorName12;
    private javax.swing.JLabel lblProfit;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSizeLoads;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblTitleDocFlete;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblVehicleName;
    private javax.swing.JPanel panelContenedorTarjetas;
    private javax.swing.JPanel sa;
    private javax.swing.JTabbedPane tabMonitor;
    private javax.swing.JTable tblFletes;
    private javax.swing.JLabel txtTotalFletes;
    // End of variables declaration//GEN-END:variables

    
}
