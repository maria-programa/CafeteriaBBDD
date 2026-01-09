package com.marialo.cafeteriabbdd.base.enums;

public enum CategoriaProducto {
    BEBIDA_CALIENTE("Bebida Caliente"),
    BEBIDA_FRIA("Bebida Fría"),
    COMIDA("Comida"),
    POSTRE("Postre"),
    BOLLERIA("Bollería");

    private final String nombre;

    CategoriaProducto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
