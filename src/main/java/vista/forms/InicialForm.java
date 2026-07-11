/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.forms;

import vista.forms.cards.CardLoad;
import java.awt.CardLayout;
import java.awt.Color;

import java.awt.Dimension;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import controllers.ExpenseController;
import controllers.FleteController;
import controllers.IncomeController;
import controllers.LoadController;
import java.awt.BorderLayout;
import java.awt.Image;
import modelo.domain.User;
import modelo.domain.Flete;
import modelo.dto.DebtDTO;
import modelo.dto.LoadDTO;
import modelo.dto.LoadDetailDTO;
import modelo.util.ConfigManager;
import modelo.util.FleteObserver;
import modelo.util.FleteSubject;
import modelo.util.IconManager;
import modelo.util.conexion.ConexionBD;
import net.coobird.thumbnailator.Thumbnails;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import vista.forms.dialogs.register.RegisCustomer;
import vista.forms.dialogs.register.RegisExpenseFlete;
import vista.forms.dialogs.register.RegisFlete;
import vista.forms.dialogs.register.RegisIncomeFlete;
import vista.forms.dialogs.register.RegisLoad;
import vista.forms.dialogs.register.RegisOperator;
import vista.forms.dialogs.register.RegisVehicle;
import vista.forms.minDialogs.CloseFlete;
import vista.forms.minDialogs.SelectCustomer;
import vista.forms.minDialogs.SelectOperator;
import vista.forms.minDialogs.TankCompartments;
import vista.forms.tables.TableDebtors;
import vista.forms.tables.TableExpensesFleteCurrent;
import vista.forms.tables.TableExpensesMonth;
import vista.forms.tables.TableIncomeFleteCurrent;
import vista.forms.tables.TableIncomesMonth;
import vista.paintCode.CargaGauge;

/**
 *
 * @author drola
 */
public class InicialForm extends javax.swing.JFrame implements FleteObserver  {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InicialForm.class.getName());

    private FleteController controllFle = new FleteController();
    private IncomeController controllIncome = new IncomeController();
    private ExpenseController controllExpe = new ExpenseController();
    private LoadController cotrollLoad = new LoadController();

    //flete
    private Flete fleteCurrent = null;
    public static int ID_FLETE_CURRENT;
    private boolean viewMoney;
    public static List<LoadDTO> listaCargasGlob = new ArrayList<>();
    //pains
    private CargaGauge miniGauge = new CargaGauge();
    private CardLayout cardLayoutt;
    // user
    public static User currentUser;
    private ConfigManager config = ConfigManager.getInstance();

    /**
     * Creates new form InicialForm
     */
    public InicialForm() {

        initComponents();

    }

    public InicialForm(User loggedUser) {
        initComponents();
        //user
        currentUser = loggedUser;
        ConfigManager.USER_CURRENT = currentUser;
        initUserCurrent();
        
        
         // Suscripción al Observer
        FleteSubject.getInstance().addObserver(this);
        
        
        //flete
        loadIconsAndParamsGlob();
        chargeFletesInProcess();
        try {
            List<LoadDTO> l = loadListLoadsWithDetails(fleteCurrent);
            if (l != null) {
                refreshCardsLoads(l);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        //aplica la configuración a la UI
        this.viewMoney = config.leerPrivacidad();
        updateViewPrivacity();
        initPanels2D();
    }

    private void updateViewPrivacity() {
        loadFinanceGlobal();
        if (viewMoney) {
            FontIcon eyeFill = FontIcon.of(BootstrapIcons.EYE_FILL);
            eyeFill.setIconSize(20);
            btnPrivacidad.setIcon(eyeFill);
        } else {
            FontIcon eyeSlash = FontIcon.of(BootstrapIcons.EYE_SLASH_FILL);
            eyeSlash.setIconSize(20);
            btnPrivacidad.setIcon(eyeSlash);

        }
        if (fleteCurrent != null) {
            loadFinanceFlete();
        }
    }

    private void initUserCurrent() {
        lblUser.setText(ConfigManager.USER_CURRENT.getFullName());
        lblRole.setText(ConfigManager.USER_CURRENT.getRole());

    }

    private void initPanels2D() {
        // panelTank.removeAll();
        panelTank.setLayout(new BorderLayout());
        panelTank.add(miniGauge, BorderLayout.CENTER);
        panelTank.revalidate();

    }

    private void loadIconsAndParamsGlob() {

        // 1. Título de la app
        this.setTitle("Bulcar - Gestion de fletes");
        //this.setMinimumSize(new java.awt.Dimension(1024, 600));
        this.setLocationRelativeTo(null);

        try {
            java.net.URL urlLogo = getClass().getResource("/logos/logoMin4.png");

// 2. Verificamos que la imagen se haya encontrado para evitar errores
            if (urlLogo != null) {
                Image icono = new ImageIcon(urlLogo).getImage();

                // 3. Se lo asignamos al JDialog (o al JFrame)
                this.setIconImage(icono);

                // Si estás dentro de la clase de la ventana, simplemente usas:
                // setIconImage(icono);
            } else {
                System.out.println("No se encontró el archivo de imagen");
            }
        } catch (Exception e) {
        }

        //loadLogoEnterprise
        try {
            java.net.URL resource = getClass().getResource("/logos/logoPrincipal.png");

            if (resource == null) {
                System.err.println("ERROR: El archivo no existe en la ruta especificada.");
                return;
            }

            BufferedImage logoRedimensionado = Thumbnails.of(resource)
                    .size(188, 260)
                    .keepAspectRatio(true)
                    .asBufferedImage();

            lblLogo.setIcon(new ImageIcon(logoRedimensionado));
            lblLogo.setText(""); // Quita el texto "jLabel1" si existe
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Altura ligeramente mayor a 260 para que la barra de scroll (15px aprox) no tape la tarjeta
        jScrollPane3.setPreferredSize(new Dimension(188, 285));
        jScrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane3.setBorder(null); // Para un look más limpio

        //icons boton Edit load
        FontIcon iconPen = FontIcon.of(FontAwesomeSolid.PEN_SQUARE);
        iconPen.setIconSize(35);
        FontIcon iconPen2 = FontIcon.of(FontAwesomeSolid.PEN_SQUARE);
        iconPen2.setIconSize(35);
        iconPen2.setIconColor(Color.WHITE);

        //efect text mouse listener
        InicialForm.efectText(btnCardFletes, Color.DARK_GRAY, Color.BLACK);
        InicialForm.efectText(btnSummaryFlete, Color.DARK_GRAY, Color.BLACK);
        InicialForm.efectText(btnRegisterCustomer, Color.DARK_GRAY, Color.BLACK);
        InicialForm.efectText(btnHistoryCustomer, Color.DARK_GRAY, Color.BLACK);
        InicialForm.efectText(btnOperator, Color.DARK_GRAY, Color.BLACK);
        cardLayoutt = (CardLayout) cardPrincipal_Finanzas.getLayout();
        cardLayoutt.show(cardPrincipal_Finanzas, "cardFletes");

//        IconManager.setIkonliIcon(btnRegisterCustomer, CarbonIcons.USER_FOLLOW, 25);
        IconManager.setIkonliIcon(btnManegerOperator, BootstrapIcons.PERSON_BADGE_FILL, 25);
        IconManager.setIkonliIcon(lblUser, BoxiconsSolid.USER_DETAIL, 20);
        IconManager.setIkonliIcon(btnOperator, BoxiconsSolid.BUOY, 20);
        IconManager.setIkonliIcon(btnHistoryCustomer, BoxiconsSolid.TIME_FIVE, 20);
        IconManager.setIkonliIcon(btnRegisterCustomer, BoxiconsSolid.USER_PLUS, 20);
        IconManager.setIkonliIcon(btnSummaryFlete, BoxiconsSolid.DETAIL, 20);
        IconManager.setIkonliIcon(btnVehiculo, BoxiconsSolid.TRUCK, 20);
        IconManager.setIkonliIcon(btnCardFletes, BoxiconsSolid.GRID_ALT, 20);
        //btn flete actually
        IconManager.setIkonliIcon(btnEndTravel, BoxiconsSolid.LOCK, 16);
        IconManager.setIkonliIcon(btnAddLoad, BoxiconsSolid.PLUS_CIRCLE, 16);
        IconManager.setIkonliIcon(btnIncomeFlete, BootstrapIcons.BOX_ARROW_IN_UP, 16);
        IconManager.setIkonliIcon(btnExpenseFlete, BootstrapIcons.BOX_ARROW_IN_DOWN, 16);

        lblVersionAndBuild.setText(ConfigManager.getVersionAndBuildNumber());

        cbxFletesActivos.setBackground(new Color(240, 240, 240));
        CardExpensesFleteCurrent.setTitulo("Egresos");
        CardIncomesFleteCurrent.setTitulo("Ingresos");
        cardProfitFleteCurrent.setTitulo("Utilidad");

    }

    private void validateNetworkAndExecute(Runnable accionSiHayRed) {
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        new Thread(() -> {
            String estado = ConexionBD.getServerInfo();
            boolean estaOffline = estado.contains("Offline") || estado.contains("Error");

            java.awt.EventQueue.invokeLater(() -> {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                if (estaOffline) {
                    // Actualizamos el Label a ROJO
                    lblStatusDB.setText("● DESCONECTADO (Sin Internet)");
                    lblStatusDB.setForeground(new Color(192, 57, 43));

                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Módulo OFFLINE.\nVerifique su conexión a internet o el estado del servidor Neon.",
                            "Acceso Denegado",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                } else {
                    // Actualizamos el Label a VERDE
                    lblStatusDB.setText("● " + estado);
                    lblStatusDB.setForeground(new Color(40, 180, 99));

                    // ¡AQUÍ ESTÁ LA MAGIA! Ejecutamos lo que el botón quería hacer
                    accionSiHayRed.run();
                }
            });
        }).start();
    }

    private String formatPrivacity(double monto) {
        if (viewMoney) {
            return "*****";
        } else {
            // Formateamos como moneda (S/ 1,250.00)
            return String.format("S/ %.1f", monto);
        }
    }

    private void injectTanque(double current, double totalCapacity, int loadsSize) {
        miniGauge.actualizarDatos(current, totalCapacity, loadsSize);
        lbSizeLoads.setText("Clientes:   " + loadsSize);

        panelSizeLoads.setBackground(loadsSize > 0 ? ConfigManager.COLOR_BLUE : ConfigManager.COLOR_RED);

    }

    public void loadFinanceGlobal() {
        //set total incomes glob
        double d3 = controllIncome.getTotalAmount();
        lblTotalIncome.setText(formatPrivacity(d3));

        //set total expense glob
        double d4 = controllExpe.getTotalAmount();
        lblTotalExpenses.setText(formatPrivacity(d4));

        // set Profit GLOBAL
        double p2 = d3 - d4;
        lblProfit.setText(formatPrivacity(p2));

        //set Not payed Global
        double debt = 0;

        try {
            List<DebtDTO> l = controllIncome.getlistNotPayed();
            for (DebtDTO debtDTO : l) {
                debt += debtDTO.getBalance();

            }
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        }
        lblDebt.setText(formatPrivacity(debt));

    }

    public void loadFinanceFlete() {
        //set text to lbl total amount incomes FLETE CURRENT
        double d = controllIncome.getTotalAmount(fleteCurrent.getId());
        CardIncomesFleteCurrent.setMonto(formatPrivacity(d));
        CardIncomesFleteCurrent.setAccentColor(ConfigManager.COLOR_BLUE);

        //set text to lbl total amount expenses FLETE CURRENT
        double d2 = controllExpe.getTotalAmount(fleteCurrent.getId());
        CardExpensesFleteCurrent.setMonto(formatPrivacity(d2));
        CardExpensesFleteCurrent.setAccentColor(Color.ORANGE);

        //set text to lbl profit FLETE CURRENT
        double p = d - d2;
        cardProfitFleteCurrent.setMonto(formatPrivacity(p));
        cardProfitFleteCurrent.setAccentColor(new Color(40, 167, 69));

    }

    private void chargeFletesInProcess() {
        loadFinanceGlobal();

        // 1. Limpiamos el combo para evitar duplicados
        cbxFletesActivos.removeAllItems();

        // 2. Traemos la LISTA de fletes en proceso
        // IMPORTANTE: Asegúrate de tener este método en tu controlador que devuelva un List<Flete>
        List<Flete> listaFletes = controllFle.getAllFletesInProcess();

        if (listaFletes != null && !listaFletes.isEmpty()) {
            // Llenamos el ComboBox
            for (Flete f : listaFletes) {
                cbxFletesActivos.addItem(f);
            }
            // Seleccionamos el primero por defecto
            cbxFletesActivos.setSelectedIndex(0);
        } else {
            // Si no hay fletes activos, llamamos a tu limpiador
            cleanParamsFleteUI();
        }
    }

    private void cleanParamsFleteUI() {
        txtDate.setText("Fecha inicio: ");
        txtOperator.setText("Operador: ");
        lbSizeLoads.setText("Cargas: 0/9");
        lblStatusFlete.setText("Ningun flete en proceso");
        lblStatusFlete.setBackground(ConfigManager.COLOR_RED);

        Color defaultColor = javax.swing.UIManager.getColor("Label.background");
        lblStatusFlete.setBackground(defaultColor);
        txtVehicle.setText("Vehiculo: ");
        txtIdentifier.setText("F-0000");
        cardProfitFleteCurrent.setMonto("0.00");
        CardExpensesFleteCurrent.setMonto("0.00");
        CardIncomesFleteCurrent.setMonto("0.00");
        injectTanque(0, 0, 0);

        // Limpieza de variables globales
        this.fleteCurrent = null;
        this.ID_FLETE_CURRENT = -1;

        // Apagado de botones de operación
        btnAddLoad.setEnabled(false);
        btnEndTravel.setEnabled(false);
        btnIncomeFlete.setEnabled(false);
        btnExpenseFlete.setEnabled(false);
        btnViewTableIncomesFleteCurrent.setEnabled(false);
        btnViewTableIExpensesFleteCurrent.setEnabled(false);

        // Encendemos el botón de crear nuevo flete
        btnNewFlete.setEnabled(true);

        // Limpiamos las tarjetas de cargas (Verifica que este sea el nombre de tu panel)
        if (panelPrincipalOfScroll != null) {
            panelPrincipalOfScroll.removeAll();
            panelPrincipalOfScroll.revalidate(); // Recalcula
            panelPrincipalOfScroll.repaint();    // Pinta de nuevo

            // Si necesitas forzar el tamaño del scroll:
            // jScrollPane3.setPreferredSize(new Dimension(188, 260));
        }
    }

    private void viewDetailsFlete(Flete obj) {
        if (obj == null) {
            return;
        }

        // 1. Actualizamos variables globales
        this.fleteCurrent = obj;
        this.ID_FLETE_CURRENT = fleteCurrent.getId();

        // 2. Pintamos los textos
        txtDate.setText(obj.getStarDate() != null ? "Fecha inicio: " + obj.getStarDate().format(ConfigManager.FORMAT_UI_DMY) : "Fecha inicio: " + "--/--/--");
        txtOperator.setText("Operador: " + obj.getOperator().getFullname());
        lblStatusFlete.setText(obj.getStatus());
        lblStatusFlete.setBackground(new Color(40, 167, 69));
        txtVehicle.setText("Vehiculo: " + obj.getVehicle().getManufacturer() + "-" + obj.getVehicle().getModel());
        txtIdentifier.setText(obj.getNumDoc());

        // 3. Activamos los botones para operar este flete
        btnEndTravel.setEnabled(true);
        btnAddLoad.setEnabled(true);
        btnIncomeFlete.setEnabled(true);
        btnExpenseFlete.setEnabled(true);
        btnViewTableIncomesFleteCurrent.setEnabled(true);
        btnViewTableIExpensesFleteCurrent.setEnabled(true);
        btnNewFlete.setEnabled(true);

        // 4. Cargamos finanzas y cargas de ESTE flete específico
        loadFinanceFlete();

        try {
            List<LoadDTO> l = loadListLoadsWithDetails(fleteCurrent);
            // Si tiene cargas, las dibuja. Si viene null/vacía, tu propio método limpiará el ScrollPane.
            refreshCardsLoads(l);
            // listaCargasGlob=l;

        } catch (Exception e) {
            System.err.println("Error cargando detalles del flete: " + e.getMessage());
        }
    }

    public List<LoadDTO> loadListLoadsWithDetails(Flete f) {

        List<LoadDTO> listaCargasVariable;
        if (f == null) {
            listaCargasVariable = null;
            return listaCargasVariable;
        }
        double current = 0;
        double capacityTotal = 30000;
        InicialForm.listaCargasGlob.clear();

        try {

            listaCargasVariable = cotrollLoad.getLoadsWithDetails(f);
            // listaCargasGlob=listaCargasVariable;
        } catch (SQLException ex) {
            listaCargasVariable = null;
            System.getLogger(InicialForm.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        // 1. EL CAMBIO IMPORTANTE ESTÁ AQUÍ
        if (listaCargasVariable == null || listaCargasVariable.isEmpty()) {
            System.out.println("⚠ No se encontraron cargas. Reseteando tanque a 0.");

            // ¡Obligamos a resetear el tanque visual!
            injectTanque(0, capacityTotal, 0);

            return listaCargasVariable; // Retornamos null o vacío para que el otro método limpie las cards

        } else {

            //se encontraron cargas
            for (LoadDTO carga : listaCargasVariable) {
                List<LoadDetailDTO> detalles = carga.getDetails();
                if (!detalles.isEmpty()) {
                    for (LoadDetailDTO det : detalles) {
                        current += det.getVolume();
                    }
                }
            }

            // Actualizamos el tanque con los datos reales
            injectTanque(current, capacityTotal, listaCargasVariable.size());
        }

        return listaCargasVariable;
    }

    public void refreshCardsLoads(List<LoadDTO> l) {
        listaCargasGlob = l;
        // 1. Limpieza absoluta (Solo necesitas llamarlo una vez)
        panelPrincipalOfScroll.removeAll();

        // 2. Si la lista está vacía o es nula
        if (l == null || l.isEmpty()) {
            jScrollPane3.getViewport().setViewSize(new Dimension(0, 0));
            jScrollPane3.setPreferredSize(new Dimension(170, 260));

            panelPrincipalOfScroll.revalidate();
            panelPrincipalOfScroll.repaint();
            return;
        }

        // OPTIMIZACIÓN 1: Creamos el borde una sola vez FUERA del bucle.
        // No tiene sentido decirle al panel que cambie su borde por cada tarjeta que entra.
        panelPrincipalOfScroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // 3. BUCLE DE ENSAMBLAJE (Solo cargamos datos a la RAM, sin dibujar)
        for (LoadDTO dto : l) {

            // EL ESCUDO: Solo agregamos si la carga le pertenece al flete
            if (fleteCurrent != null && dto.getIdFlete() == fleteCurrent.getId()) {

                // Instanciamos y agregamos a la memoria del panel
                CardLoad newCarga = new CardLoad(dto, this, fleteCurrent);
                panelPrincipalOfScroll.add(newCarga);
                panelPrincipalOfScroll.add(Box.createRigidArea(new Dimension(10, 0))); // Separador
            }
        }

        // OPTIMIZACIÓN 2: Cálculos y redibujado FUERA del bucle.
        // Solo después de que TODAS las tarjetas están en el panel, calculamos su tamaño real.
        int anchoContenido = panelPrincipalOfScroll.getPreferredSize().width;

        if (anchoContenido <= 1150) {// ancio --- 820
            jScrollPane3.setPreferredSize(new Dimension(anchoContenido, 285));
        } else {
            jScrollPane3.setPreferredSize(new Dimension(1005, 285)); // @param para determinar crecimiento maximo de tarjeta
        }

        // OPTIMIZACIÓN 3: Refrescar y validar UNA SOLA VEZ.
        // Esto hace que la pantalla se actualice de golpe ("Flash") en lugar de hacerlo paso a paso,
        // ahorrando muchísimo procesamiento gráfico y memoria.
        panelPrincipalOfScroll.revalidate();
        panelPrincipalOfScroll.repaint();

        this.revalidate();
        this.repaint();
    }

    public static void efectText(JButton button, Color colorHover, Color colorNormal) {
        //metodo para cambiar color al jbutton al detectar el mouse
        button.addMouseListener(new MouseAdapter() {
            // Lógica de ENTRADA
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Solo afecta al botón que fue pasado como parámetro
                button.setForeground(colorHover);
            }

            // Lógica de SALIDA
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setForeground(colorNormal);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        cardPrincipal_Finanzas = new javax.swing.JPanel();
        cardFinanzas = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cardClientes = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cardServicios = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cardVehiculos = new javax.swing.JPanel();
        btnNewOperator = new javax.swing.JButton();
        btnManegerOperator = new javax.swing.JButton();
        cardLadrillera = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cardFletes = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelPrincipalViajeCurso = new javax.swing.JPanel();
        btnAddLoad = new javax.swing.JButton();
        btnExpenseFlete = new javax.swing.JButton();
        btnIncomeFlete = new javax.swing.JButton();
        btnEndTravel = new javax.swing.JButton();
        pnlInfoViajeActual = new javax.swing.JPanel();
        lblStatusFlete = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtVehicle = new javax.swing.JLabel();
        txtOperator = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        txtDate = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        txtIdentifier = new javax.swing.JLabel();
        panelSizeLoads = new javax.swing.JPanel();
        lbSizeLoads = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelPrincipalOfScroll = new javax.swing.JPanel();
        btnViewTableIncomesFleteCurrent = new javax.swing.JButton();
        cardProfitFleteCurrent = new vista.paintCode.CardResumenPro();
        btnViewTableIExpensesFleteCurrent = new javax.swing.JButton();
        CardExpensesFleteCurrent = new vista.paintCode.CardResumenPro();
        CardIncomesFleteCurrent = new vista.paintCode.CardResumenPro();
        panelTank = new javax.swing.JPanel();
        btnAddLoad1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        lblTotalIncome = new javax.swing.JLabel();
        btnViewIncomesMonth = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        lblTotalExpenses = new javax.swing.JLabel();
        btnExpensesMonth = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        lblProfit = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        lblDebt = new javax.swing.JLabel();
        btnTableDebtors = new javax.swing.JButton();
        btnPrivacidad = new javax.swing.JToggleButton();
        lblVersionAndBuild = new javax.swing.JLabel();
        lblStatusDB = new javax.swing.JLabel();
        cbxFletesActivos = new javax.swing.JComboBox<>();
        lblRole = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jPanelOpciones = new javax.swing.JPanel();
        btnCardFletes = new javax.swing.JButton();
        btnRegisterCustomer = new javax.swing.JButton();
        btnSummaryFlete = new javax.swing.JButton();
        btnOperator = new javax.swing.JButton();
        btnHistoryCustomer = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        btnPromociones = new javax.swing.JButton();
        btnNewFlete = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        btnPromociones1 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        btnPromociones2 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        btnVehiculo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        background.setBackground(new java.awt.Color(102, 153, 255));
        background.setMaximumSize(new java.awt.Dimension(1280, 720));
        background.setName(""); // NOI18N
        background.setPreferredSize(new java.awt.Dimension(1280, 720));

        cardPrincipal_Finanzas.setBackground(new java.awt.Color(255, 0, 0));
        cardPrincipal_Finanzas.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardPrincipal_Finanzas.setMinimumSize(new java.awt.Dimension(1045, 720));
        cardPrincipal_Finanzas.setPreferredSize(new java.awt.Dimension(1045, 720));
        cardPrincipal_Finanzas.setLayout(new java.awt.CardLayout());

        cardFinanzas.setBackground(new java.awt.Color(204, 204, 204));
        cardFinanzas.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardFinanzas.setName(""); // NOI18N
        cardFinanzas.setPreferredSize(new java.awt.Dimension(1045, 720));

        jLabel8.setText("...............................");

        javax.swing.GroupLayout cardFinanzasLayout = new javax.swing.GroupLayout(cardFinanzas);
        cardFinanzas.setLayout(cardFinanzasLayout);
        cardFinanzasLayout.setHorizontalGroup(
            cardFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardFinanzasLayout.createSequentialGroup()
                .addGap(381, 381, 381)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(550, Short.MAX_VALUE))
        );
        cardFinanzasLayout.setVerticalGroup(
            cardFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardFinanzasLayout.createSequentialGroup()
                .addGap(342, 342, 342)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardPrincipal_Finanzas.add(cardFinanzas, "cardFinanzas");

        cardClientes.setBackground(new java.awt.Color(204, 204, 204));
        cardClientes.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardClientes.setPreferredSize(new java.awt.Dimension(1045, 720));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setText(".................");

        javax.swing.GroupLayout cardClientesLayout = new javax.swing.GroupLayout(cardClientes);
        cardClientes.setLayout(cardClientesLayout);
        cardClientesLayout.setHorizontalGroup(
            cardClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardClientesLayout.createSequentialGroup()
                .addGap(303, 303, 303)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(621, Short.MAX_VALUE))
        );
        cardClientesLayout.setVerticalGroup(
            cardClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardClientesLayout.createSequentialGroup()
                .addGap(323, 323, 323)
                .addComponent(jLabel6)
                .addContainerGap(381, Short.MAX_VALUE))
        );

        cardPrincipal_Finanzas.add(cardClientes, "cardClientes");

        cardServicios.setBackground(new java.awt.Color(204, 204, 204));
        cardServicios.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardServicios.setPreferredSize(new java.awt.Dimension(1045, 720));

        jLabel2.setText(".................");

        javax.swing.GroupLayout cardServiciosLayout = new javax.swing.GroupLayout(cardServicios);
        cardServicios.setLayout(cardServiciosLayout);
        cardServiciosLayout.setHorizontalGroup(
            cardServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardServiciosLayout.createSequentialGroup()
                .addGap(450, 450, 450)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(459, Short.MAX_VALUE))
        );
        cardServiciosLayout.setVerticalGroup(
            cardServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardServiciosLayout.createSequentialGroup()
                .addContainerGap(286, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(359, 359, 359))
        );

        cardPrincipal_Finanzas.add(cardServicios, "cardServicios");

        cardVehiculos.setBackground(new java.awt.Color(204, 204, 204));
        cardVehiculos.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardVehiculos.setPreferredSize(new java.awt.Dimension(1045, 720));

        btnNewOperator.setText("Nuevo Operador");
        btnNewOperator.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNewOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOperatorActionPerformed(evt);
            }
        });

        btnManegerOperator.setText("Registros operador");
        btnManegerOperator.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnManegerOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManegerOperatorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardVehiculosLayout = new javax.swing.GroupLayout(cardVehiculos);
        cardVehiculos.setLayout(cardVehiculosLayout);
        cardVehiculosLayout.setHorizontalGroup(
            cardVehiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardVehiculosLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(cardVehiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNewOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnManegerOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(880, Short.MAX_VALUE))
        );
        cardVehiculosLayout.setVerticalGroup(
            cardVehiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardVehiculosLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(btnManegerOperator)
                .addGap(18, 18, 18)
                .addComponent(btnNewOperator)
                .addContainerGap(616, Short.MAX_VALUE))
        );

        cardPrincipal_Finanzas.add(cardVehiculos, "cardVehiculos");

        cardLadrillera.setBackground(new java.awt.Color(204, 204, 204));
        cardLadrillera.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardLadrillera.setPreferredSize(new java.awt.Dimension(1045, 720));

        jLabel5.setText(".................");

        javax.swing.GroupLayout cardLadrilleraLayout = new javax.swing.GroupLayout(cardLadrillera);
        cardLadrillera.setLayout(cardLadrilleraLayout);
        cardLadrilleraLayout.setHorizontalGroup(
            cardLadrilleraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardLadrilleraLayout.createSequentialGroup()
                .addGap(444, 444, 444)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(474, Short.MAX_VALUE))
        );
        cardLadrilleraLayout.setVerticalGroup(
            cardLadrilleraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardLadrilleraLayout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addComponent(jLabel5)
                .addContainerGap(293, Short.MAX_VALUE))
        );

        cardPrincipal_Finanzas.add(cardLadrillera, "cardLadrillera");

        cardFletes.setBackground(new java.awt.Color(204, 204, 204));
        cardFletes.setMaximumSize(new java.awt.Dimension(1045, 720));
        cardFletes.setPreferredSize(new java.awt.Dimension(1045, 720));
        cardFletes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel4.setText("Control de Fletes");
        cardFletes.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        panelPrincipalViajeCurso.setBackground(new java.awt.Color(153, 153, 153));
        panelPrincipalViajeCurso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelPrincipalViajeCurso.setMaximumSize(new java.awt.Dimension(810, 430));
        panelPrincipalViajeCurso.setMinimumSize(new java.awt.Dimension(810, 430));
        panelPrincipalViajeCurso.setPreferredSize(new java.awt.Dimension(810, 430));
        panelPrincipalViajeCurso.setVerifyInputWhenFocusTarget(false);
        panelPrincipalViajeCurso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAddLoad.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnAddLoad.setText("Nueva carga");
        btnAddLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLoadActionPerformed(evt);
            }
        });
        panelPrincipalViajeCurso.add(btnAddLoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 390, 130, 40));

        btnExpenseFlete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnExpenseFlete.setText("Egreso ");
        btnExpenseFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpenseFleteActionPerformed(evt);
            }
        });
        panelPrincipalViajeCurso.add(btnExpenseFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 440, 130, 40));

        btnIncomeFlete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnIncomeFlete.setText(" Ingreso");
        btnIncomeFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncomeFleteActionPerformed(evt);
            }
        });
        panelPrincipalViajeCurso.add(btnIncomeFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 390, 130, 40));

        btnEndTravel.setBackground(new java.awt.Color(255, 153, 153));
        btnEndTravel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnEndTravel.setText("Cerrar viaje");
        btnEndTravel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndTravelActionPerformed(evt);
            }
        });
        panelPrincipalViajeCurso.add(btnEndTravel, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 440, 130, 40));

        pnlInfoViajeActual.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInfoViajeActual.setMaximumSize(new java.awt.Dimension(809, 40));
        pnlInfoViajeActual.setMinimumSize(new java.awt.Dimension(809, 40));
        pnlInfoViajeActual.setPreferredSize(new java.awt.Dimension(809, 40));

        lblStatusFlete.setBackground(new java.awt.Color(220, 53, 69));
        lblStatusFlete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatusFlete.setForeground(new java.awt.Color(255, 255, 255));
        lblStatusFlete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatusFlete.setText("Ningun fete en proceso");
        lblStatusFlete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblStatusFlete.setOpaque(true);

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        txtVehicle.setText("Vehiculo : ");

        txtOperator.setText("Operador: ");

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        txtDate.setText("Fecha inicio:");

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        txtIdentifier.setBackground(new java.awt.Color(204, 255, 204));
        txtIdentifier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtIdentifier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtIdentifier.setText("F------------");
        txtIdentifier.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtIdentifier.setOpaque(true);

        panelSizeLoads.setBackground(new java.awt.Color(56, 82, 115));
        panelSizeLoads.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelSizeLoads.setMaximumSize(new java.awt.Dimension(89, 32767));

        lbSizeLoads.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbSizeLoads.setForeground(new java.awt.Color(255, 255, 255));
        lbSizeLoads.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbSizeLoads.setText("Carga:");

        javax.swing.GroupLayout panelSizeLoadsLayout = new javax.swing.GroupLayout(panelSizeLoads);
        panelSizeLoads.setLayout(panelSizeLoadsLayout);
        panelSizeLoadsLayout.setHorizontalGroup(
            panelSizeLoadsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbSizeLoads, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelSizeLoadsLayout.setVerticalGroup(
            panelSizeLoadsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSizeLoadsLayout.createSequentialGroup()
                .addComponent(lbSizeLoads)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInfoViajeActualLayout = new javax.swing.GroupLayout(pnlInfoViajeActual);
        pnlInfoViajeActual.setLayout(pnlInfoViajeActualLayout);
        pnlInfoViajeActualLayout.setHorizontalGroup(
            pnlInfoViajeActualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoViajeActualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIdentifier, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblStatusFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtVehicle, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSizeLoads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        pnlInfoViajeActualLayout.setVerticalGroup(
            pnlInfoViajeActualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfoViajeActualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoViajeActualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator4)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoViajeActualLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlInfoViajeActualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdentifier)
                            .addComponent(lblStatusFlete)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInfoViajeActualLayout.createSequentialGroup()
                        .addGroup(pnlInfoViajeActualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDate, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtOperator, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtVehicle, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelSizeLoads, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelPrincipalViajeCurso.add(pnlInfoViajeActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 1030, -1));

        jScrollPane3.setMaximumSize(new java.awt.Dimension(1105, 225));

        panelPrincipalOfScroll.setMaximumSize(new java.awt.Dimension(800, 228));
        panelPrincipalOfScroll.setMinimumSize(new java.awt.Dimension(2, 2));
        panelPrincipalOfScroll.setLayout(new javax.swing.BoxLayout(panelPrincipalOfScroll, javax.swing.BoxLayout.X_AXIS));
        jScrollPane3.setViewportView(panelPrincipalOfScroll);

        panelPrincipalViajeCurso.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 59, -1, -1));

        btnViewTableIncomesFleteCurrent.setText("detalle =");
        btnViewTableIncomesFleteCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTableIncomesFleteCurrentActionPerformed(evt);
            }
        });
        panelPrincipalViajeCurso.add(btnViewTableIncomesFleteCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 100, 15));
        panelPrincipalViajeCurso.add(cardProfitFleteCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 370, 130, 120));

        btnViewTableIExpensesFleteCurrent.setText("= detalle");
        btnViewTableIExpensesFleteCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTableIExpensesFleteCurrentActionPerformed(evt);
            }
        });
        panelPrincipalViajeCurso.add(btnViewTableIExpensesFleteCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 100, 15));
        panelPrincipalViajeCurso.add(CardExpensesFleteCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 140, 120));
        panelPrincipalViajeCurso.add(CardIncomesFleteCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 140, 120));

        panelTank.setBackground(new java.awt.Color(204, 204, 204));
        panelTank.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelTank.setMaximumSize(new java.awt.Dimension(209, 120));
        panelTank.setMinimumSize(new java.awt.Dimension(209, 120));
        panelTank.setPreferredSize(new java.awt.Dimension(209, 120));

        btnAddLoad1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 11)); // NOI18N
        btnAddLoad1.setText("Compartimientos");
        btnAddLoad1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLoad1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTankLayout = new javax.swing.GroupLayout(panelTank);
        panelTank.setLayout(panelTankLayout);
        panelTankLayout.setHorizontalGroup(
            panelTankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTankLayout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(btnAddLoad1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelTankLayout.setVerticalGroup(
            panelTankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTankLayout.createSequentialGroup()
                .addContainerGap(98, Short.MAX_VALUE)
                .addComponent(btnAddLoad1)
                .addContainerGap())
        );

        panelPrincipalViajeCurso.add(panelTank, new org.netbeans.lib.awtextra.AbsoluteConstraints(769, 350, 240, 130));

        cardFletes.add(panelPrincipalViajeCurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 1020, 500));

        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("© 2026 Bulcar. Todos los derechos reservados.");
        cardFletes.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 698, -1, -1));

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setMaximumSize(new java.awt.Dimension(170, 110));
        jPanel5.setPreferredSize(new java.awt.Dimension(170, 110));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Ingreso Mensual");

        lblTotalIncome.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalIncome.setText("s/ ----------");

        btnViewIncomesMonth.setText("=");
        btnViewIncomesMonth.setPreferredSize(new java.awt.Dimension(20, 20));
        btnViewIncomesMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewIncomesMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25)
                    .addComponent(lblTotalIncome, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnViewIncomesMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalIncome)
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnViewIncomesMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        cardFletes.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 92, -1, 82));

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel6.setMaximumSize(new java.awt.Dimension(170, 110));
        jPanel6.setPreferredSize(new java.awt.Dimension(170, 110));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Egreso Mensual");

        lblTotalExpenses.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalExpenses.setText("s/ ----------");

        btnExpensesMonth.setText("=");
        btnExpensesMonth.setMaximumSize(new java.awt.Dimension(20, 20));
        btnExpensesMonth.setPreferredSize(new java.awt.Dimension(20, 20));
        btnExpensesMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpensesMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTotalExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(btnExpensesMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addGap(39, 39, 39))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalExpenses)
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnExpensesMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        cardFletes.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 92, -1, 82));

        jPanel9.setBackground(new java.awt.Color(153, 153, 153));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setMaximumSize(new java.awt.Dimension(170, 110));
        jPanel9.setPreferredSize(new java.awt.Dimension(170, 110));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Utilidad Mensual");

        lblProfit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblProfit.setText("s/ ----------");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32)
                    .addComponent(lblProfit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblProfit)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        cardFletes.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 92, -1, 82));

        jPanel11.setBackground(new java.awt.Color(153, 153, 153));
        jPanel11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel11.setMaximumSize(new java.awt.Dimension(170, 110));
        jPanel11.setPreferredSize(new java.awt.Dimension(170, 110));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Saldos pendientes");

        lblDebt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDebt.setText("s/ ----------");

        btnTableDebtors.setText("=");
        btnTableDebtors.setPreferredSize(new java.awt.Dimension(20, 20));
        btnTableDebtors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableDebtorsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblDebt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btnTableDebtors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addGap(27, 27, 27))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDebt)
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnTableDebtors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        cardFletes.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 92, -1, 82));

        btnPrivacidad.setBackground(new java.awt.Color(204, 204, 204));
        btnPrivacidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrivacidadActionPerformed(evt);
            }
        });
        cardFletes.add(btnPrivacidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 150, 21, 21));

        lblVersionAndBuild.setForeground(new java.awt.Color(102, 102, 102));
        lblVersionAndBuild.setText("Version: v0.1.0-BETA | Build: 20260221-1706");
        cardFletes.add(lblVersionAndBuild, new org.netbeans.lib.awtextra.AbsoluteConstraints(808, 698, -1, -1));

        lblStatusDB.setForeground(new java.awt.Color(255, 51, 51));
        lblStatusDB.setText("                                                 Sin conexion");
        cardFletes.add(lblStatusDB, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 698, 391, -1));

        cbxFletesActivos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbxFletesActivos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cbxFletesActivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxFletesActivosActionPerformed(evt);
            }
        });
        cardFletes.add(cbxFletesActivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 90, 250, -1));

        lblRole.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRole.setText("Rol");
        cardFletes.add(lblRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 40, 160, -1));

        lblUser.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("username");
        cardFletes.add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 10, 160, -1));

        cardPrincipal_Finanzas.add(cardFletes, "cardFletes");

        jPanelOpciones.setBackground(new java.awt.Color(204, 204, 204));
        jPanelOpciones.setMaximumSize(new java.awt.Dimension(230, 720));
        jPanelOpciones.setPreferredSize(new java.awt.Dimension(230, 720));

        btnCardFletes.setBackground(new java.awt.Color(204, 204, 204));
        btnCardFletes.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnCardFletes.setText("Principal");
        btnCardFletes.setContentAreaFilled(false);
        btnCardFletes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCardFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCardFletesActionPerformed(evt);
            }
        });

        btnRegisterCustomer.setBackground(new java.awt.Color(204, 204, 204));
        btnRegisterCustomer.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnRegisterCustomer.setText("Registrar");
        btnRegisterCustomer.setContentAreaFilled(false);
        btnRegisterCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRegisterCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterCustomerActionPerformed(evt);
            }
        });

        btnSummaryFlete.setBackground(new java.awt.Color(204, 204, 204));
        btnSummaryFlete.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnSummaryFlete.setText("Resumen de Fletes");
        btnSummaryFlete.setContentAreaFilled(false);
        btnSummaryFlete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSummaryFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSummaryFleteActionPerformed(evt);
            }
        });

        btnOperator.setBackground(new java.awt.Color(204, 204, 204));
        btnOperator.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnOperator.setText("Operador");
        btnOperator.setContentAreaFilled(false);
        btnOperator.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorActionPerformed(evt);
            }
        });

        btnHistoryCustomer.setBackground(new java.awt.Color(204, 204, 204));
        btnHistoryCustomer.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnHistoryCustomer.setText("Historial Individual");
        btnHistoryCustomer.setContentAreaFilled(false);
        btnHistoryCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistoryCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryCustomerActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Powered by Bulcar Dev ");

        btnPromociones.setBackground(new java.awt.Color(204, 204, 204));
        btnPromociones.setText("Servicios");
        btnPromociones.setContentAreaFilled(false);
        btnPromociones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromocionesActionPerformed(evt);
            }
        });

        btnNewFlete.setFont(new java.awt.Font("JetBrains Mono", 1, 14)); // NOI18N
        btnNewFlete.setText("+ Nuevo Flete");
        btnNewFlete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNewFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewFleteActionPerformed(evt);
            }
        });

        jSeparator6.setForeground(new java.awt.Color(153, 153, 153));

        btnPromociones1.setBackground(new java.awt.Color(204, 204, 204));
        btnPromociones1.setText("Clientes");
        btnPromociones1.setContentAreaFilled(false);
        btnPromociones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromociones1ActionPerformed(evt);
            }
        });

        jSeparator7.setForeground(new java.awt.Color(153, 153, 153));

        btnPromociones2.setBackground(new java.awt.Color(204, 204, 204));
        btnPromociones2.setText("Flota");
        btnPromociones2.setContentAreaFilled(false);
        btnPromociones2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromociones2ActionPerformed(evt);
            }
        });

        jSeparator8.setForeground(new java.awt.Color(153, 153, 153));

        btnVehiculo.setBackground(new java.awt.Color(204, 204, 204));
        btnVehiculo.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        btnVehiculo.setText("Vehiculo");
        btnVehiculo.setContentAreaFilled(false);
        btnVehiculo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnVehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehiculoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOpcionesLayout = new javax.swing.GroupLayout(jPanelOpciones);
        jPanelOpciones.setLayout(jPanelOpcionesLayout);
        jPanelOpcionesLayout.setHorizontalGroup(
            jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpcionesLayout.createSequentialGroup()
                .addComponent(btnPromociones)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jSeparator6)
            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpcionesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSummaryFlete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpcionesLayout.createSequentialGroup()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
            .addGroup(jPanelOpcionesLayout.createSequentialGroup()
                .addGroup(jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpcionesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCardFletes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVehiculo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelOpcionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelOpcionesLayout.createSequentialGroup()
                                .addGroup(jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnPromociones1)
                                    .addComponent(btnPromociones2)
                                    .addComponent(jLabel1))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanelOpcionesLayout.createSequentialGroup()
                                .addGap(0, 32, Short.MAX_VALUE)
                                .addGroup(jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnHistoryCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnRegisterCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnOperator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnNewFlete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanelOpcionesLayout.setVerticalGroup(
            jPanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpcionesLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(btnPromociones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCardFletes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSummaryFlete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPromociones1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegisterCustomer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHistoryCustomer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPromociones2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOperator)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVehiculo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNewFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addComponent(jPanelOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPrincipal_Finanzas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cardPrincipal_Finanzas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLoadActionPerformed

        validateNetworkAndExecute(() -> {

            if (fleteCurrent == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un flete válido primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            RegisLoad w = new RegisLoad(this, true, fleteCurrent);

            w.setLocationRelativeTo(this); // Centrar en la pantalla
            w.setResizable(false);         // No redimensionar
            w.setVisible(true);            // MOSTRAR (El código se pausa en esta línea)

            System.out.println("🔄 Refrescando vista tras agregar nueva carga...");

            try {
                viewDetailsFlete(fleteCurrent);
                loadFinanceFlete();
                loadFinanceGlobal();

            } catch (Exception e) {
                System.err.println("Error al refrescar las cargas: " + e.getMessage());
            }
        });

        System.out.println("TAMAÑO DE LISTA DE CARGAS GLOBALES btn carga: " + listaCargasGlob.size());
    }//GEN-LAST:event_btnAddLoadActionPerformed

    private void btnEndTravelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndTravelActionPerformed

        validateNetworkAndExecute(() -> {

            if (this.fleteCurrent == null) {
                return;
            }

            CloseFlete w = new CloseFlete(this, fleteCurrent);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);

            chargeFletesInProcess();

        });
        System.out.println("TAMAÑO DE LISTA DE CARGAS GLOBALES btn flete: " + listaCargasGlob.size());
    }//GEN-LAST:event_btnEndTravelActionPerformed

    private void btnPromocionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromocionesActionPerformed
        cardLayoutt.show(cardPrincipal_Finanzas, "cardFletes");
        System.out.println("boton execute");
    }//GEN-LAST:event_btnPromocionesActionPerformed

    private void btnHistoryCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryCustomerActionPerformed

//        cardLayoutt.show(cardPrincipal_Finanzas, "cardLadrillera");
//        System.out.println("boton execute");
        validateNetworkAndExecute(() -> {
            SelectCustomer w = new SelectCustomer(this);
            w.setLocationRelativeTo(this);
            w.setResizable(false);
            w.setVisible(true);
        });
    }//GEN-LAST:event_btnHistoryCustomerActionPerformed

    private void btnOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorActionPerformed
        cardLayoutt.show(cardPrincipal_Finanzas, "cardVehiculos");
//        System.out.println("boton execute");
    }//GEN-LAST:event_btnOperatorActionPerformed

    private void btnSummaryFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSummaryFleteActionPerformed

//        cardLayoutt.show(cardPrincipal_Finanzas, "cardClientes");
//        System.out.println("boton execute");
        validateNetworkAndExecute(() -> {
            MonitorFletes w = new MonitorFletes(this, true);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);

        });

    }//GEN-LAST:event_btnSummaryFleteActionPerformed

    private void btnRegisterCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterCustomerActionPerformed

//        cardLayoutt.show(cardPrincipal_Finanzas, "cardFinanzas");
//        System.out.println("boton execute");
        validateNetworkAndExecute(() -> {
            RegisCustomer window = new RegisCustomer(this, true);

            window.setLocationRelativeTo(this);
            window.setResizable(false);
            window.setVisible(true);
        });
    }//GEN-LAST:event_btnRegisterCustomerActionPerformed

    private void btnCardFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCardFletesActionPerformed
        cardLayoutt.show(cardPrincipal_Finanzas, "cardFletes");
//        cardLayoutt.show(cardPrincipal_Finanzas, "cardServicios");

    }//GEN-LAST:event_btnCardFletesActionPerformed

    private void btnNewOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOperatorActionPerformed

        validateNetworkAndExecute(() -> {

            RegisOperator window = new RegisOperator(this, true);

            window.setLocationRelativeTo(this); // Centrar
            window.setResizable(false);        // No redimensionar
            window.setVisible(true);            //mostrar
        });


    }//GEN-LAST:event_btnNewOperatorActionPerformed

    private void btnExpenseFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpenseFleteActionPerformed

        validateNetworkAndExecute(() -> {

            RegisExpenseFlete w = new RegisExpenseFlete(fleteCurrent);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);            //mostrar
            loadFinanceGlobal();
            loadFinanceFlete();
        });


    }//GEN-LAST:event_btnExpenseFleteActionPerformed

    private void btnViewIncomesMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewIncomesMonthActionPerformed

        validateNetworkAndExecute(() -> {
            TableIncomesMonth w = new TableIncomesMonth(this, true);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);
            w.setVisible(true);            //mostrar
        });


    }//GEN-LAST:event_btnViewIncomesMonthActionPerformed

    private void btnExpensesMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpensesMonthActionPerformed

        validateNetworkAndExecute(() -> {
            TableExpensesMonth w = new TableExpensesMonth(this, true);
            w.setLocationRelativeTo(this); // Centrar
            //w.setResizable(false);        // No redimensionar
            w.setVisible(true);            //mostrar
        });


    }//GEN-LAST:event_btnExpensesMonthActionPerformed

    private void btnViewTableIExpensesFleteCurrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTableIExpensesFleteCurrentActionPerformed
        TableExpensesFleteCurrent w = new TableExpensesFleteCurrent(this, true);
        w.setLocationRelativeTo(this); // Centrar
        //w.setResizable(false);        // No redimensionar
        w.setVisible(true);            //mostrar
    }//GEN-LAST:event_btnViewTableIExpensesFleteCurrentActionPerformed

    private void btnTableDebtorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableDebtorsActionPerformed
        validateNetworkAndExecute(() -> {
            TableDebtors w = new TableDebtors(this, fleteCurrent);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);            //mostrar
            loadFinanceGlobal();
            if (fleteCurrent != null) {
                loadFinanceFlete();
                List<LoadDTO> l = loadListLoadsWithDetails(fleteCurrent);
                if (l != null) {
                    refreshCardsLoads(l);
                }
            }
        });


    }//GEN-LAST:event_btnTableDebtorsActionPerformed

    private void btnPrivacidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrivacidadActionPerformed
        validateNetworkAndExecute(() -> {
            // Cambiamos el estado (si es true pasa a false y viceversa)
            viewMoney = !viewMoney;
            // GUARDAR en el archivo .properties
            config.guardarPrivacidad(viewMoney);

            updateViewPrivacity();
        });


    }//GEN-LAST:event_btnPrivacidadActionPerformed

    private void btnManegerOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManegerOperatorActionPerformed

        validateNetworkAndExecute(() -> {
            SelectOperator w = new SelectOperator(this);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);
            w.setVisible(true);            //mostrar
        });


    }//GEN-LAST:event_btnManegerOperatorActionPerformed

    private void cbxFletesActivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxFletesActivosActionPerformed

        validateNetworkAndExecute(() -> {
            Object seleccionado = cbxFletesActivos.getSelectedItem();

            if (seleccionado != null && seleccionado instanceof Flete) {
                Flete f = (Flete) seleccionado;
                viewDetailsFlete(f);
            }
        });


    }//GEN-LAST:event_cbxFletesActivosActionPerformed

    private void btnViewTableIncomesFleteCurrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTableIncomesFleteCurrentActionPerformed
        validateNetworkAndExecute(() -> {

            TableIncomeFleteCurrent w = new TableIncomeFleteCurrent(this, true);
            w.setLocationRelativeTo(this); // Centrar
            //w.setResizable(false);        // No redimensionar
            w.setVisible(true);            //mostrar
        });


    }//GEN-LAST:event_btnViewTableIncomesFleteCurrentActionPerformed

    private void btnIncomeFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncomeFleteActionPerformed

        validateNetworkAndExecute(() -> {

            RegisIncomeFlete w = new RegisIncomeFlete(fleteCurrent);
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);            //mostrar
            loadFinanceGlobal();
            loadFinanceFlete();
        });
    }//GEN-LAST:event_btnIncomeFleteActionPerformed

    private void btnPromociones1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromociones1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPromociones1ActionPerformed

    private void btnPromociones2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromociones2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPromociones2ActionPerformed

    private void btnVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehiculoActionPerformed

        validateNetworkAndExecute(() -> {

            RegisVehicle w = new RegisVehicle(this, true);

            w.setLocationRelativeTo(this);
            w.setResizable(false);
            w.setVisible(true);
        });
    }//GEN-LAST:event_btnVehiculoActionPerformed

    private void btnNewFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewFleteActionPerformed
        if (fleteCurrent != null) {
            JOptionPane.showMessageDialog(this, "Flete actual en Proceso");
            return;
        }

        validateNetworkAndExecute(() -> {
            RegisFlete w = new RegisFlete(this, true);

            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);
            chargeFletesInProcess();
        });
    }//GEN-LAST:event_btnNewFleteActionPerformed

    private void btnAddLoad1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLoad1ActionPerformed

        validateNetworkAndExecute(() -> {
            TankCompartments w = new TankCompartments(this, true, "visual");
            w.setLocationRelativeTo(this); // Centrar
            w.setResizable(false);        // No redimensionar
            w.setVisible(true);
        });


    }//GEN-LAST:event_btnAddLoad1ActionPerformed

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
        System.setProperty("sun.java2d.uiScale", "1.0");

        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
            // O usa FlatDarkLaf.setup() para modo oscuro
        } catch (Exception ex) {
            System.err.println("Error al iniciar FlatLaf");
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new InicialForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vista.paintCode.CardResumenPro CardExpensesFleteCurrent;
    private vista.paintCode.CardResumenPro CardIncomesFleteCurrent;
    private javax.swing.JPanel background;
    private javax.swing.JButton btnAddLoad;
    private javax.swing.JButton btnAddLoad1;
    private javax.swing.JButton btnCardFletes;
    private javax.swing.JButton btnEndTravel;
    private javax.swing.JButton btnExpenseFlete;
    private javax.swing.JButton btnExpensesMonth;
    private javax.swing.JButton btnHistoryCustomer;
    private javax.swing.JButton btnIncomeFlete;
    private javax.swing.JButton btnManegerOperator;
    private javax.swing.JButton btnNewFlete;
    private javax.swing.JButton btnNewOperator;
    private javax.swing.JButton btnOperator;
    private javax.swing.JToggleButton btnPrivacidad;
    private javax.swing.JButton btnPromociones;
    private javax.swing.JButton btnPromociones1;
    private javax.swing.JButton btnPromociones2;
    private javax.swing.JButton btnRegisterCustomer;
    private javax.swing.JButton btnSummaryFlete;
    private javax.swing.JButton btnTableDebtors;
    private javax.swing.JButton btnVehiculo;
    private javax.swing.JButton btnViewIncomesMonth;
    private javax.swing.JButton btnViewTableIExpensesFleteCurrent;
    private javax.swing.JButton btnViewTableIncomesFleteCurrent;
    private javax.swing.JPanel cardClientes;
    private javax.swing.JPanel cardFinanzas;
    private javax.swing.JPanel cardFletes;
    private javax.swing.JPanel cardLadrillera;
    private javax.swing.JPanel cardPrincipal_Finanzas;
    private vista.paintCode.CardResumenPro cardProfitFleteCurrent;
    private javax.swing.JPanel cardServicios;
    private javax.swing.JPanel cardVehiculos;
    private javax.swing.JComboBox<Flete> cbxFletesActivos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelOpciones;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lbSizeLoads;
    private javax.swing.JLabel lblDebt;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblProfit;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblStatusDB;
    private javax.swing.JLabel lblStatusFlete;
    private javax.swing.JLabel lblTotalExpenses;
    private javax.swing.JLabel lblTotalIncome;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblVersionAndBuild;
    private javax.swing.JPanel panelPrincipalOfScroll;
    private javax.swing.JPanel panelPrincipalViajeCurso;
    private javax.swing.JPanel panelSizeLoads;
    private javax.swing.JPanel panelTank;
    private javax.swing.JPanel pnlInfoViajeActual;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtIdentifier;
    private javax.swing.JLabel txtOperator;
    private javax.swing.JLabel txtVehicle;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onFleteChanged() {
        chargeFletesInProcess(); // Tu método existente para recargar fletes
    }
}
