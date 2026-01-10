package com.marialo.cafeteriabbdd.base.enums;

public enum TipoPago {
    TARJETA("Tarjeta"),
    EFECTIVO("Efectivo"),
    BIZUM("Bizum");

    private final String nombre;

    TipoPago(String nombre) {
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
