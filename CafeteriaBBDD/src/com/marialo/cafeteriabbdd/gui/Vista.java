package com.marialo.cafeteriabbdd.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.marialo.cafeteriabbdd.base.enums.CategoriaProducto;
import com.marialo.cafeteriabbdd.base.enums.TipoPago;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private final static String TITULO_FRAME = "Cafetería";

    // ======== PEDIDOS ========
    JPanel JPanelPedido;
    JComboBox comboEmpleado;
    JComboBox comboCliente;
    JComboBox comboPago;
    JTable pedidosTabla;
    JButton empezarPedidoButton;
    JLabel empleadoComboLbl;
    JLabel clienteComboLbl;
    JLabel pagoLbl;

    // ======== PRODUCTOS ========
    JPanel JPanelProducto;
    JTextField codigoProductoTxt;
    JComboBox comboCategoria;
    JTextField nombreProductoTxt;
    JTextField precioProductoTxt;
    JTable productosTabla;
    JButton annadirProductoButton;
    JButton modificarProductoButton;
    JButton eliminarProductoButton;
    JLabel codigoProductoLbl;
    JLabel categoriaLbl;
    JLabel nombreProductoLbl;
    JLabel precioLbl;

    // ======== EMPLEADOS ========
    JPanel JPanelEmpleado;
    JTextField codigoEmpleadoTxt;
    JTextField nombreEmpleadoTxt;
    JTextField apellidosEmpleadoTxt;
    JTextField dniEmpleadoTxt;
    DatePicker fechaContratacionDP;
    JTable empleadosTabla;
    JButton annadirEmpleadoButton;
    JButton modificarEmpleadoButton;
    JButton eliminarEmpleadoButton;
    JLabel codigoEmpleadoLbl;
    JLabel nombreEmpleadoLbl;
    JLabel apellidosEmpleadoLbl;
    JLabel dniLbl;
    JLabel fechaContratacionLbl;

    // ======== CLIENTES ========
    JPanel JPanelCliente;
    JTextField nombreClienteTxt;
    JTextField apellidosClienteTxt;
    JTextField emailClienteTxt;
    JTextField telefonoClienteTxt;
    JTable clientesTabla;
    JButton annadirClienteButton;
    JButton modificarClienteButton;
    JButton eliminarClienteButton;
    JLabel nombreClienteLbl;
    JLabel emailClienteLbl;

    // ======== TABLAS ========
    DefaultTableModel dtmPedidos;
    DefaultTableModel dtmProductos;
    DefaultTableModel dtmEmpleados;
    DefaultTableModel dtmClientes;

    // ======== MENU BAR ========
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    // ======== CUADRO DIALOGO ========
    OptionDialog optionDialog;
    JDialog adminPasswordDialog;
    JButton btnValidate;
    JPasswordField adminPassword;

    public Vista() {
        super(TITULO_FRAME);
        initFrame();
    }

    public void initFrame() {
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();

        this.setSize(new Dimension(this.getWidth(), this.getHeight() + 100));
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        optionDialog = new OptionDialog(this);

        setMenu();
        setAdminDialog();
        setComboBox();
        setTableModels();
    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones = new JMenuItem("Opciones");
        itemOpciones.setActionCommand("Opciones");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir=new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setAdminDialog() {
        btnValidate = new JButton("Validar");
        btnValidate.setActionCommand("abrirOpciones");
        adminPassword = new JPasswordField();
        //dimension al cuadro de texto
        adminPassword.setPreferredSize(new Dimension(100,26));
        Object[] options=new Object[] {adminPassword,btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña",JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,null,options);
        adminPasswordDialog = new JDialog(this,"Opciones",true);
        adminPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        adminPasswordDialog.setContentPane(jop);
        adminPasswordDialog.pack();
        adminPasswordDialog.setLocationRelativeTo(this);
    }

    private void setComboBox() {
        for (CategoriaProducto cp : CategoriaProducto.values()) {
            comboCategoria.addItem(cp.getNombre());
        }

        for (TipoPago tp : TipoPago.values()) {
            comboPago.addItem(tp.getNombre());
        }
    }

    private void setTableModels() {
        this.dtmPedidos = new DefaultTableModel();
        this.pedidosTabla.setModel(dtmPedidos);

        this.dtmProductos = new DefaultTableModel();
        this.productosTabla.setModel(dtmProductos);

        this.dtmEmpleados = new DefaultTableModel();
        this.empleadosTabla.setModel(dtmEmpleados);

        this.dtmClientes = new DefaultTableModel();
        this.clientesTabla.setModel(dtmClientes);
    }
}
