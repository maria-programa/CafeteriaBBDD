package com.marialo.cafeteriabbdd.gui;

public class ProductoComboItem {
    private int id;
    private String nombre;
    private double precio;

    public ProductoComboItem(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
