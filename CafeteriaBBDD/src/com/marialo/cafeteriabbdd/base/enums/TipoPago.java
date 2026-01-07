package com.marialo.cafeteriabbdd.base.enums;

public enum TipoPago {
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    BIZUM("Bizum"),
    VALE("Vale Regalo");

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
