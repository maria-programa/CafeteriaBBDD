package com.marialo.cafeteriabbdd.gui;

import com.marialo.cafeteriabbdd.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {
    private Modelo modelo;
    private Vista vista;
    boolean refrescar;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;

        modelo.conectar();
        setOptions();
        addActionListeners(this);
        addWindowListeners(this);
        refrescarTodo();
    }

    private void refrescarTodo() {
        refrescarProductos();
        refrescarEmpleados();
        refrescarClientes();
    }

    private void addActionListeners(ActionListener listener) {
        vista.annadirClienteButton.addActionListener(listener);
        vista.modificarClienteButton.addActionListener(listener);
        vista.eliminarClienteButton.addActionListener(listener);

        vista.annadirEmpleadoButton.addActionListener(listener);
        vista.modificarEmpleadoButton.addActionListener(listener);
        vista.eliminarEmpleadoButton.addActionListener(listener);

        vista.annadirProductoButton.addActionListener(listener);
        vista.modificarProductoButton.addActionListener(listener);
        vista.eliminarProductoButton.addActionListener(listener);

        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.setActionCommand("guardarOpciones");
        vista.itemOpciones.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
        vista.btnValidate.addActionListener(listener);
    }

    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Opciones":
                vista.adminPasswordDialog.setVisible(true);
                break;
            case "Desconectar":
                modelo.desconectar();
                break;
            case "Salir":
                System.exit(0);
                break;
            case "abrirOpciones":
                if(String.valueOf(vista.adminPassword.getPassword()).equals(modelo.getAdminPassword())) {
                    vista.adminPassword.setText("");
                    vista.adminPasswordDialog.dispose();
                    vista.optionDialog.setVisible(true);
                } else {
                    Util.mensajeError("La contraseña introducida no es correcta.");
                }
                break;
            case "guardarOpciones":
                modelo.setPropValues(vista.optionDialog.txtIP.getText(), vista.optionDialog.txtUsuario.getText(),
                        String.valueOf(vista.optionDialog.pfPass.getPassword()), String.valueOf(vista.optionDialog.pfAdmin.getPassword()));
                vista.optionDialog.dispose();
                vista.dispose();
                new Controlador(new Modelo(), new Vista());
                break;
        }
    }

    private boolean camposVaciosCliente() {
        if (vista.nombreClienteTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.nombreClienteLbl.getText() + " es obligatorio.");
            return true;
        } else if (vista.emailClienteTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.emailClienteLbl.getText() + " es obligatorio.");
            return true;
        }
        return false;
    }

    private boolean camposVaciosEmpleado() {
        if (vista.codigoEmpleadoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.codigoEmpleadoLbl + " es obligatorio.");
            return true;
        } else if (vista.nombreEmpleadoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.nombreEmpleadoLbl.getText() + " es obligatorio.");
            return true;
        } else if (vista.apellidosEmpleadoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.apellidosEmpleadoLbl.getText() + " es obligatorio.");
            return true;
        } else if (vista.dniEmpleadoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.dniLbl.getText() + " es obligatorio.");
            return true;
        } else if (vista.fechaContratacionDP.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.fechaContratacionLbl.getText() + " es obligatorio.");
            return true;
        }
        return false;
    }

    private boolean camposVaciosProducto() {
        if (vista.codigoProductoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.codigoProductoLbl + " es obligatorio.");
            return true;
        } else if (vista.nombreProductoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.nombreProductoLbl + " es obligatorio.");
            return true;
        } else if (vista.precioProductoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.precioLbl.getText() + " es obligatorio.");
            return true;
        }
        return false;
    }

    private void refrescarProductos()  {
        try {
            vista.productosTabla.setModel(tableModelProductos(modelo.consultarProductos()));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel tableModelProductos(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> nombreColumnas = new Vector<>();
        int numeroColumnas = metaData.getColumnCount();

        for (int columna = 1; columna <= numeroColumnas; columna++) {
            nombreColumnas.add(metaData.getColumnLabel(columna));
        }

        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, numeroColumnas, data);

        vista.dtmProductos.setDataVector(data, nombreColumnas);
        return vista.dtmProductos;
    }
    private void refrescarEmpleados() {
        try {
            vista.empleadosTabla.setModel(tableModelEmpleados(modelo.consultarEmpleados()));
            vista.comboEmpleado.removeAllItems();

            for (int i = 0; i < vista.dtmEmpleados.getRowCount(); i++) {
                vista.comboEmpleado.addItem(vista.dtmEmpleados.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel tableModelEmpleados(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> nombreColumnas = new Vector<>();
        int numeroColumnas = metaData.getColumnCount();

        for (int columna = 1; columna <= numeroColumnas; columna++) {
            nombreColumnas.add(metaData.getColumnLabel(columna));
        }

        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, numeroColumnas, data);

        vista.dtmEmpleados.setDataVector(data, nombreColumnas);
        return vista.dtmEmpleados;
    }

    private void refrescarClientes() {
        try {
            vista.clientesTabla.setModel(tableModelClientes(modelo.consultarClientes()));
            vista.comboCliente.removeAllItems();

            for (int i = 0; i < vista.dtmClientes.getRowCount(); i++) {
                vista.comboCliente.addItem(vista.dtmClientes.getValueAt(i, 1) + " - " + vista.dtmClientes.getValueAt(i, 3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel tableModelClientes(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> nombreColumnas = new Vector<>();
        int numeroColumnas = metaData.getColumnCount();

        for (int columna = 1; columna <= numeroColumnas; columna++) {
            nombreColumnas.add(metaData.getColumnLabel(columna));
        }

        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, numeroColumnas, data);

        vista.dtmClientes.setDataVector(data, nombreColumnas);
        return vista.dtmClientes;
    }

    private void setDataVector(ResultSet rs, int numeroColumnas, Vector<Vector<Object>> data) throws SQLException {
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= numeroColumnas; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }

            data.add(vector);
        }
    }

    private void setOptions() {
        vista.optionDialog.txtIP.setText(modelo.getIp());
        vista.optionDialog.txtUsuario.setText(modelo.getUser());
        vista.optionDialog.pfPass.setText(modelo.getPassword());
        vista.optionDialog.pfAdmin.setText(modelo.getAdminPassword());
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int resp = Util.mensajeConfirmacion("¿Desea cerrar la ventana?", "Salir");
        if (resp == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
