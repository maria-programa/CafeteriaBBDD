package com.marialo.cafeteriabbdd.gui;

import com.marialo.cafeteriabbdd.util.Util;
import com.marialo.cafeteriabbdd.util.Validadores;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {
    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.cardPanelDetallesPedido.setVisible(false);

        modelo.conectar();
        setOptions();
        addActionListeners(this);
        addWindowListeners(this);
        configurarTablaPedidos();
        refrescarTodo();

        vista.cardPanelProducto.setVisible(false);
        vista.cardPanelEmpleado.setVisible(false);
        vista.cardPanelCliente.setVisible(false);
    }

    private void refrescarTodo() {
        refrescarPedidos();
        refrescarProductos();
        refrescarEmpleados();
        refrescarClientes();
    }

    private void addActionListeners(ActionListener listener) {
        vista.annadirClienteButton.addActionListener(listener);
        vista.annadirClienteButton.setActionCommand("annadirCliente");
        vista.modificarClienteButton.addActionListener(listener);
        vista.modificarClienteButton.setActionCommand("editarCliente");
        vista.eliminarClienteButton.addActionListener(listener);
        vista.eliminarClienteButton.setActionCommand("eliminarCliente");
        vista.aceptarClienteButton.addActionListener(listener);
        vista.aceptarClienteButton.setActionCommand("aceptarCliente");
        vista.cancelarClienteButton.addActionListener(listener);
        vista.cancelarClienteButton.setActionCommand("cancelarCliente");

        vista.annadirEmpleadoButton.addActionListener(listener);
        vista.annadirEmpleadoButton.setActionCommand("annadirEmpleado");
        vista.modificarEmpleadoButton.addActionListener(listener);
        vista.modificarEmpleadoButton.setActionCommand("editarEmpleado");
        vista.eliminarEmpleadoButton.addActionListener(listener);
        vista.eliminarEmpleadoButton.setActionCommand("eliminarEmpleado");
        vista.aceptarEmpleadoButton.addActionListener(listener);
        vista.aceptarEmpleadoButton.setActionCommand("aceptarEmpleado");
        vista.cancelarEmpleadoButton.addActionListener(listener);
        vista.cancelarEmpleadoButton.setActionCommand("cancelarEmpleado");

        vista.annadirProductoButton.addActionListener(listener);
        vista.annadirProductoButton.setActionCommand("annadirProducto");
        vista.modificarProductoButton.addActionListener(listener);
        vista.modificarProductoButton.setActionCommand("editarProducto");
        vista.eliminarProductoButton.addActionListener(listener);
        vista.eliminarProductoButton.setActionCommand("eliminarProducto");
        vista.aceptarProductoButton.addActionListener(listener);
        vista.aceptarProductoButton.setActionCommand("aceptarProducto");
        vista.cancelarProductoButton.addActionListener(listener);
        vista.cancelarProductoButton.setActionCommand("cancelarProducto");

        vista.empezarPedidoButton.addActionListener(listener);
        vista.empezarPedidoButton.setActionCommand("empezarPedido");
        vista.eliminarPedidoButton.addActionListener(listener);
        vista.eliminarPedidoButton.setActionCommand("eliminarPedido");

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
            case "empezarPedido":
                empezarPedido();
                break;
            case "eliminarPedido":
                eliminarPedido();
                break;
            case "annadirProducto":
                altaProducto();
                break;
            case "editarProducto":
                editarProducto();
                break;
            case "aceptarProducto":
                aceptarProducto();
                break;
            case "cancelarProducto":
                cancelarProducto();
                break;
            case "eliminarProducto":
                eliminarProducto();
                break;
            case "annadirEmpleado":
                altaEmpleado();
                break;
            case "editarEmpleado":
                editarEmpleado();
                break;
            case "aceptarEmpleado":
                aceptarEmpleado();
                break;
            case "cancelarEmpleado":
                cancelarEmpleado();
                break;
            case "eliminarEmpleado":
                eliminarEmpleado();
                break;
            case "annadirCliente":
                altaCliente();
                break;
            case "editarCliente":
                editarCliente();
                break;
            case "aceptarCliente":
                aceptarCliente();
                break;
            case "cancelarCliente":
                cancelarCliente();
                break;
            case "eliminarCliente":
                eliminarCliente();
                break;
        }
    }

    // ========= PEDIDOS =========
    private void empezarPedido() {
        DetallesPedido dialog = new DetallesPedido(vista, "Nuevo pedido");
        cargarProductosEnDialogo(dialog);

        dialog.annadirProductoButton.addActionListener(e -> {
            ProductoComboItem item = (ProductoComboItem) dialog.comboProductos.getSelectedItem();
            if (item != null) {
                int cantidad = (int) dialog.cantidadSpinner.getValue();
                double subtotal = item.getPrecio() * cantidad;
                dialog.annadirFilaTemporal(item.toString(), cantidad, item.getPrecio(), subtotal);
            }
        });

        dialog.finalizarPedidoButton.addActionListener(e -> {
            guardarPedido(dialog);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void cargarProductosEnDialogo(DetallesPedido dialog) {
        dialog.comboProductos.removeAllItems();
        try {
            ResultSet rs = modelo.consultarProductos();
            while (rs.next()) {
                String nombre = rs.getString("Nombre");
                double precio = rs.getDouble("Precio");
                int id = rs.getInt("ID");
                dialog.comboProductos.addItem(new ProductoComboItem(id, nombre, precio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarPedido(DetallesPedido dialog) {
        if (dialog.modeloTablaTemporal.getRowCount() == 0) {
            Util.mensajeError("Has de añadir algún producto al pedido");
            return;
        }
        String codigoEmpleado = vista.comboEmpleado.getSelectedItem().toString().split(" ")[0];
        int idEmpleado = modelo.obtenerIDEmpleado(codigoEmpleado);
        String emailCliente = vista.comboCliente.getSelectedItem().toString().split(" - ")[1];
        int idCliente = modelo.obtenerIDCliente(emailCliente);
        double total = dialog.getTotalTemporal();
        modelo.insertarPedido(idCliente, idEmpleado, total, vista.comboPago.getSelectedItem().toString());

        int idPedido = modelo.obtenerUltimoIdPedido();
        for (int i = 0; i < dialog.modeloTablaTemporal.getRowCount(); i++) {
            String productoTexto = (String) dialog.modeloTablaTemporal.getValueAt(i, 0);
            int cantidad = (int) dialog.modeloTablaTemporal.getValueAt(i, 1);
            double precio = (double) dialog.modeloTablaTemporal.getValueAt(i, 2);
            double subtotal = (double) dialog.modeloTablaTemporal.getValueAt(i, 3);

            String idStr = productoTexto.split(" - ")[0];
            int idProducto = Integer.parseInt(idStr);

            modelo.insertarDetallePedido(idPedido, idProducto, cantidad, subtotal);
        }
        refrescarPedidos();
    }

    private void eliminarPedido() {
        int filaSeleccionada = vista.pedidosTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor, seleccione un pedido para eliminar");
            return;
        }

        int id_pedido = (int) vista.pedidosTabla.getValueAt(filaSeleccionada, 0);

        int confirmar = Util.mensajeConfirmacion("¿Estás seguro/a de que quieres eliminar el pedido número: " + id_pedido + "?",
                "Confirmar eliminación de producto");

        if (confirmar == JOptionPane.YES_OPTION) {
            modelo.eliminarDetalle(id_pedido);
            modelo.eliminarPedido(id_pedido);
            refrescarPedidos();
            Util.mensajeInfo("Pedido eliminado correctamente.");
        }
    }

    private void configurarTablaPedidos() {
        vista.pedidosTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble click
                    int fila = vista.pedidosTabla.getSelectedRow();
                    if (fila != -1) {
                        alternarDetallesPedido(fila);
                    }
                }
            }
        });
    }

    private void alternarDetallesPedido(int filaSeleccionada) {
        if (vista.cardPanelDetallesPedido.isVisible()) {
            vista.cardPanelDetallesPedido.setVisible(false);
            vista.revalidate();
            vista.repaint();
        } else {
            mostrarDetallesPedido(filaSeleccionada);
            vista.cardPanelDetallesPedido.setVisible(true);
            vista.revalidate();
            vista.repaint();
        }
    }

    private void mostrarDetallesPedido(int filaSeleccionada) {
        try {
            int idPedido = (int) vista.dtmPedidos.getValueAt(filaSeleccionada, 0);
            vista.detallesTabla.setModel(tableModelDetalles(modelo.consultarDetallesPedido(idPedido)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel tableModelDetalles(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> nombreColumnas = new Vector<>();
        int numeroColumnas = metaData.getColumnCount();

        for (int columna = 1; columna <= numeroColumnas; columna++) {
            nombreColumnas.add(metaData.getColumnLabel(columna));
        }

        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, numeroColumnas, data);

        vista.dtmDetalles.setDataVector(data, nombreColumnas);
        return vista.dtmDetalles;
    }

    // ========= PRODUCTOS =========
    private void altaProducto() {
        crearProducto(false);
    }

    private void aceptarProducto() {
        crearProducto(true);
        vista.cardPanelProducto.setVisible(false);
        actualizarBotonesProducto(true);
    }

    private void cancelarProducto() {
        Util.mensajeInfo("Acción cancelada.");
        vista.productosTabla.clearSelection();
        vista.cardPanelProducto.setVisible(false);
        actualizarBotonesProducto(true);
        borrarCamposProducto();
    }

    private void crearProducto(boolean editando) {
        if (camposVaciosProducto()) {
            return;
        } else if (modelo.existeProducto(vista.codigoProductoTxt.getText()) && !editando) {
            Util.mensajeError("Ya existe un producto con el código: " + vista.codigoProductoTxt.getText());
            vista.productosTabla.clearSelection();
            return;
        }

        try {
            String codigo = vista.codigoProductoTxt.getText();
            String nombre = vista.nombreProductoTxt.getText();
            String categoria = String.valueOf(vista.comboCategoria.getSelectedItem());
            double precio = Double.parseDouble(vista.precioProductoTxt.getText());

            if (precio < 0) {
                Util.mensajeError("El precio no puede ser negativo");
                vista.precioProductoTxt.requestFocus();
                return;
            }

            if (editando) {
                modelo.modificarProducto((Integer) vista.productosTabla.getValueAt(vista.productosTabla.getSelectedRow(), 0),
                        codigo, nombre, categoria, precio);
            } else {
                modelo.insertarProducto(codigo, nombre, categoria, precio);
            }

            refrescarProductos();
            borrarCamposProducto();

            if (editando) {
                Util.mensajeInfo("Producto actualizado con éxito");
            } else {
                Util.mensajeInfo("Producto añadido con éxito");
            }

        } catch (NumberFormatException ex) {
            Util.mensajeError("El precio ha de ser un número válido");
        }
    }

    private boolean camposVaciosProducto() {
        if (vista.codigoProductoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.codigoProductoLbl.getText() + " es obligatorio.");
            return true;
        } else if (vista.nombreProductoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.nombreProductoLbl.getText() + " es obligatorio.");
            return true;
        } else if (vista.precioProductoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.precioLbl.getText() + " es obligatorio.");
            return true;
        }
        return false;
    }

    private void borrarCamposProducto() {
        vista.codigoProductoTxt.setText("");
        vista.comboCategoria.setSelectedIndex(0);
        vista.nombreProductoTxt.setText("");
        vista.precioProductoTxt.setText("");
    }

    private void editarProducto() {
        int filaSeleccionada = vista.productosTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor seleccione un producto para editar.");
            return;
        }

        cargarDatosProducto(filaSeleccionada);
        actualizarBotonesProducto(false);
        vista.cardPanelProducto.setVisible(true);
    }

    private void cargarDatosProducto(int productoSeleccionado) {
        vista.codigoProductoTxt.setText(String.valueOf(vista.productosTabla.getValueAt(productoSeleccionado, 1)));
        vista.comboCategoria.setSelectedItem(String.valueOf(vista.productosTabla.getValueAt(productoSeleccionado, 3)));
        vista.nombreProductoTxt.setText(String.valueOf(vista.productosTabla.getValueAt(productoSeleccionado, 2)));
        vista.precioProductoTxt.setText(String.valueOf(vista.productosTabla.getValueAt(productoSeleccionado, 4)));

        vista.codigoProductoTxt.setEnabled(false);
    }

    private void actualizarBotonesProducto(boolean visible) {
        vista.annadirProductoButton.setVisible(visible);
        vista.modificarProductoButton.setVisible(visible);
        vista.eliminarProductoButton.setVisible(visible);
    }

    private void eliminarProducto() {
        int filaSeleccionada = vista.productosTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor, seleccione un producto para eliminar");
            return;
        }

        int id_producto = (int) vista.productosTabla.getValueAt(filaSeleccionada, 0);
        String codigo = (String) vista.productosTabla.getValueAt(filaSeleccionada, 1);
        int confirmar = Util.mensajeConfirmacion("¿Estás seguro/a de que quieres eliminar el producto con el código: " + codigo + "?",
                "Confirmar eliminación de producto");

        if (confirmar == JOptionPane.YES_OPTION) {
            modelo.eliminarProducto(id_producto);
            refrescarProductos();
            Util.mensajeInfo("Producto eliminado correctamente.");
        }
    }

    // ========= EMPLEADOS =========
    private void altaEmpleado() {
        crearEmpleado(false);
    }

    private void aceptarEmpleado() {
        crearEmpleado(true);
        vista.cardPanelEmpleado.setVisible(false);
        actualizarBotonesEmpleado(true);
    }

    private void cancelarEmpleado() {
        Util.mensajeInfo("Acción cancelada.");
        vista.empleadosTabla.clearSelection();
        vista.cardPanelEmpleado.setVisible(false);
        actualizarBotonesEmpleado(true);
        borrarCamposEmpleado();
    }

    private void crearEmpleado(boolean editando) {
        if (camposVaciosEmpleado()) {
            return;
        } else if (modelo.existeEmpleado(vista.codigoEmpleadoTxt.getText()) && !editando) {
            Util.mensajeError("Ya existe un empleado con el código: " + vista.codigoEmpleadoTxt.getText());
            vista.empleadosTabla.clearSelection();
            return;
        }

        if (!Validadores.validarDNI(vista.dniEmpleadoTxt.getText())) {
            Util.mensajeError("Por favor, introduzca un DNI válido");
            return;
        }

        String codigoEmpleado = vista.codigoEmpleadoTxt.getText();
        String nombre = vista.nombreEmpleadoTxt.getText();
        String apellidos = vista.apellidosEmpleadoTxt.getText();
        String dni = vista.dniEmpleadoTxt.getText();
        LocalDate fechaContratacion = vista.fechaContratacionDP.getDate();

        if (editando) {
            modelo.modificarEmpleado((Integer) vista.empleadosTabla.getValueAt(vista.empleadosTabla.getSelectedRow(), 0),
                    codigoEmpleado, nombre, apellidos, dni, fechaContratacion);
        } else {
            modelo.insertarEmpleado(codigoEmpleado, nombre, apellidos, dni, fechaContratacion);
        }

        refrescarEmpleados();
        borrarCamposEmpleado();

        if (editando) {
            Util.mensajeInfo("Empleado actualizado con éxito");
        } else {
            Util.mensajeInfo("Empleado añadido con éxito");
        }
    }

    private boolean camposVaciosEmpleado() {
        if (vista.codigoEmpleadoTxt.getText().isEmpty()) {
            Util.mensajeError("El campo " + vista.codigoEmpleadoLbl.getText() + " es obligatorio.");
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

    private void borrarCamposEmpleado() {
        vista.codigoEmpleadoTxt.setText("");
        vista.nombreEmpleadoTxt.setText("");
        vista.apellidosEmpleadoTxt.setText("");
        vista.dniEmpleadoTxt.setText("");
        vista.fechaContratacionDP.setText("");
    }

    private void editarEmpleado() {
        int filaSeleccionada = vista.empleadosTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor seleccione un empleado para editar.");
            return;
        }

        cargarDatosEmpleado(filaSeleccionada);
        actualizarBotonesEmpleado(false);
        vista.cardPanelEmpleado.setVisible(true);
    }

    private void cargarDatosEmpleado(int empleadoSeleccionado) {
        vista.codigoEmpleadoTxt.setText(String.valueOf(vista.empleadosTabla.getValueAt(empleadoSeleccionado, 1)));
        vista.nombreEmpleadoTxt.setText(String.valueOf(vista.empleadosTabla.getValueAt(empleadoSeleccionado, 2)));
        vista.apellidosEmpleadoTxt.setText(String.valueOf(vista.empleadosTabla.getValueAt(empleadoSeleccionado, 3)));
        vista.dniEmpleadoTxt.setText(String.valueOf(vista.empleadosTabla.getValueAt(empleadoSeleccionado, 4)));
        vista.fechaContratacionDP.setDate((Date.valueOf(String.valueOf(vista.empleadosTabla.getValueAt(empleadoSeleccionado, 5)))).toLocalDate());

        vista.codigoEmpleadoTxt.setEnabled(false);
    }

    private void actualizarBotonesEmpleado(boolean visible) {
        vista.annadirEmpleadoButton.setVisible(visible);
        vista.modificarEmpleadoButton.setVisible(visible);
        vista.eliminarEmpleadoButton.setVisible(visible);
    }

    private void eliminarEmpleado() {
        int filaSeleccionada = vista.empleadosTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor, seleccione un empleado para eliminar");
            return;
        }

        int id_empleado = (int) vista.empleadosTabla.getValueAt(filaSeleccionada, 0);
        String codigo = (String) vista.empleadosTabla.getValueAt(filaSeleccionada, 1);
        int confirmar = Util.mensajeConfirmacion("¿Estás seguro/a de que quieres eliminar el empleado con el código: " + codigo + "?",
                "Confirmar eliminación de empleado");

        if (confirmar == JOptionPane.YES_OPTION) {
            modelo.eliminarEmpleado(id_empleado);
            refrescarEmpleados();
            Util.mensajeInfo("Empleado eliminado correctamente.");
        }
    }

    // ========= CLIENTES =========
    private void altaCliente() {
        crearCliente(false);
    }

    private void aceptarCliente() {
        crearCliente(true);
        vista.cardPanelCliente.setVisible(false);
        actualizarBotonesCliente(true);
    }

    private void cancelarCliente() {
        Util.mensajeInfo("Acción cancelada.");
        vista.productosTabla.clearSelection();
        vista.cardPanelCliente.setVisible(false);
        actualizarBotonesCliente(true);
        borrarCamposCliente();
    }

    private void crearCliente(boolean editando) {
        if (camposVaciosCliente()) {
            return;
        } else if (modelo.existeCliente(vista.emailClienteTxt.getText()) && !editando) {
            Util.mensajeError("Ya existe un cliente con el correo: " + vista.emailClienteTxt.getText());
            vista.clientesTabla.clearSelection();
            return;
        }

        if (!Validadores.validarEmail(vista.emailClienteTxt.getText())) {
            Util.mensajeError("Por favor, introduzca un correo válido.");
            return;
        }
        String nombre = vista.nombreClienteTxt.getText();
        String apellidos = vista.apellidosClienteTxt.getText();
        String email = vista.emailClienteTxt.getText();
        String telefono = vista.telefonoClienteTxt.getText();

        if (editando) {
            modelo.modificarCliente((Integer) vista.clientesTabla.getValueAt(vista.clientesTabla.getSelectedRow(), 0),
                    nombre, apellidos, email, telefono);
        } else {
            modelo.insertarCliente(nombre, apellidos, email, telefono);
        }

        refrescarClientes();
        borrarCamposCliente();

        if (editando) {
            Util.mensajeInfo("Cliente actualizado con éxito");
        } else {
            Util.mensajeInfo("Cliente añadido con éxito");
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

    private void borrarCamposCliente() {
        vista.nombreClienteTxt.setText("");
        vista.apellidosClienteTxt.setText("");
        vista.emailClienteTxt.setText("");
        vista.telefonoClienteTxt.setText("");
    }

    private void editarCliente() {
        int filaSeleccionada = vista.clientesTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor seleccione un cliente para editar.");
            return;
        }

        cargarDatosCliente(filaSeleccionada);
        actualizarBotonesCliente(false);
        vista.cardPanelCliente.setVisible(true);

    }

    private void cargarDatosCliente(int clienteSeleccionado) {
        vista.nombreClienteTxt.setText(String.valueOf(vista.clientesTabla.getValueAt(clienteSeleccionado, 1)));
        vista.apellidosClienteTxt.setText(String.valueOf(vista.clientesTabla.getValueAt(clienteSeleccionado, 2)));
        vista.emailClienteTxt.setText(String.valueOf(vista.clientesTabla.getValueAt(clienteSeleccionado, 3)));
        vista.telefonoClienteTxt.setText(String.valueOf(vista.clientesTabla.getValueAt(clienteSeleccionado, 5)));
    }

    private void actualizarBotonesCliente(boolean visible) {
        vista.annadirClienteButton.setVisible(visible);
        vista.modificarClienteButton.setVisible(visible);
        vista.eliminarClienteButton.setVisible(visible);
    }

    private void eliminarCliente() {
        int filaSeleccionada = vista.clientesTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            Util.mensajeError("Por favor, seleccione un cliente para eliminar");
            return;
        }

        int id_cliente = (int) vista.clientesTabla.getValueAt(filaSeleccionada, 0);
        String email = (String) vista.clientesTabla.getValueAt(filaSeleccionada, 3);
        int confirmar = Util.mensajeConfirmacion("¿Estás seguro/a de que quieres eliminar al cliente con el email: " + email + "?",
                "Confirmar eliminación de empleado");

        if (confirmar == JOptionPane.YES_OPTION) {
            modelo.eliminarCliente(id_cliente);
            refrescarClientes();
            Util.mensajeInfo("Cliente eliminado correctamente.");
        }
    }

    private void refrescarPedidos() {
        try {
            vista.pedidosTabla.setModel(tableModelPedidos(modelo.consultarPedidos()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel tableModelPedidos(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> nombreColumnas = new Vector<>();
        int numeroColumnas = metaData.getColumnCount();

        for (int columna = 1; columna <= numeroColumnas; columna++) {
            nombreColumnas.add(metaData.getColumnLabel(columna));
        }

        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, numeroColumnas, data);

        vista.dtmPedidos.setDataVector(data, nombreColumnas);
        return vista.dtmPedidos;
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
                vista.comboEmpleado.addItem(vista.dtmEmpleados.getValueAt(i, 1) + " - " + vista.dtmEmpleados.getValueAt(i, 3)
                    + ", " + vista.dtmEmpleados.getValueAt(i, 2));
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
