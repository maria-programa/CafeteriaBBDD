package com.marialo.cafeteriabbdd;

import com.marialo.cafeteriabbdd.gui.Vista;

public class Controlador {
    private Modelo modelo;
    private Vista vista;
    boolean refrescar;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }
}
