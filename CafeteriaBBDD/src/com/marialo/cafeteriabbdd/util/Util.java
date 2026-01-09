package com.marialo.cafeteriabbdd.util;

import javax.swing.*;

public class Util {
    public static void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje,"Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int mensajeConfirmacion(String mensaje, String titulo) {
        return JOptionPane.showConfirmDialog(null, mensaje, titulo, JOptionPane.YES_NO_OPTION);
    }

    public static void mensajeInfo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
