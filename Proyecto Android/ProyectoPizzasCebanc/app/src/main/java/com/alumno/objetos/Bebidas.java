package com.alumno.objetos;

import java.io.Serializable;

/**
 * Created by adminportatil on 19/12/2017.
 *
 * Clase para la creacion de la bebidas
 */

public class Bebidas implements Serializable {
    private String tipoBebida; //cocacola, fantanaranja, fantalimon, redbull, nestea, cerveza, agua
    private double precio; // diferente para cada bebida
    private int cantidad;

    public Bebidas (String tipoBebida, int cantidad) {
        this.tipoBebida = tipoBebida;
        this.cantidad = cantidad;

        // A través de un switch le damos valor a los precio segun qué bebida sea
        switch (tipoBebida) {
            case "Coca Cola":
                this.precio = 1.6;
                break;
            case "Fanta Naranja":
                this.precio = 1.6;
                break;
            case "Fanta Limon":
                this.precio = 1.6;
                break;
            case "RedBull":
                this.precio = 2;
                break;
            case "Nestea":
                this.precio = 1.5;
                break;
            case "Cerveza":
                this.precio = 1.8;
                break;
            case "Agua":
                this.precio = 1;
                break;
            default:
                this.precio = 1;
                break;
        }
    }

    public String getTipoBebida() {
        return tipoBebida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double precioTotal() {
        return cantidad*precio;
    }

    @Override
    public String toString() {
        return getCantidad() + "x Bebida " + getTipoBebida() + "\n" + precioTotal() + "€\n";
    }
}
