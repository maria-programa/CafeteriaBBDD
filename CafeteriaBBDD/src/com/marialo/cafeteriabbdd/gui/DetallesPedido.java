package com.marialo.cafeteriabbdd.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DetallesPedido extends JDialog {
    private JPanel panel1;
    JComboBox comboProductos;
    JSpinner cantidadSpinner;
    JTable detallePedidoTabla;
    JButton annadirProductoButton;
    JButton finalizarPedidoButton;

    DefaultTableModel modeloTablaTemporal;

    public DetallesPedido(JFrame parent, String titulo) {
        super(parent, titulo, true);
        setContentPane(panel1);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        cantidadSpinner.setModel(spinnerModel);

        modeloTablaTemporal = new DefaultTableModel(
                new String[]{"Producto", "Cantidad", "Precio Unit.", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        detallePedidoTabla.setModel(modeloTablaTemporal);
    }

    public void annadirFilaTemporal(String producto, int cantidad, double precio, double subtotal) {
        modeloTablaTemporal.addRow(new Object[]{producto, cantidad, precio, subtotal});
    }

    // Método para obtener total TEMPORAL
    public double getTotalTemporal() {
        double total = 0;
        for (int i = 0; i < modeloTablaTemporal.getRowCount(); i++) {
            total += (double) modeloTablaTemporal.getValueAt(i, 3);
        }
        return total;
    }

    // Método para limpiar tabla TEMPORAL
    public void limpiarTablaTemporal() {
        modeloTablaTemporal.setRowCount(0);
    }

}
