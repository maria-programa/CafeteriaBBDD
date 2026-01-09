package com.marialo.cafeteriabbdd.main;

import com.marialo.cafeteriabbdd.Controlador;
import com.marialo.cafeteriabbdd.Modelo;
import com.marialo.cafeteriabbdd.gui.Vista;

public class Principal {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
    }
}
