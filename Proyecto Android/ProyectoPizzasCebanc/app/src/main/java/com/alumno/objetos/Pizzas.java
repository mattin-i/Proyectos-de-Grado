package com.alumno.objetos;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by adminportatil on 19/12/2017.
 *
 * Clase para la creacion de la pizzas
 */

public class Pizzas implements Serializable {
    private String tipoMasa; // Masa fina o masa gorda
    private String tipoPizza; // Barbacoa, carbonara, 4quesos, vegetal, tropical
    private String tamaño; // Familiar, mediana y individual
    private int precio; // Lo define el tamaño
    private int cantidad;

    public Pizzas (String tipoPizza, String tipoMasa, String tamaño, int cantidad) {
        this.tipoPizza = tipoPizza;
        this.tipoMasa = tipoMasa;
        this.tamaño = tamaño;
        this.cantidad = cantidad;

        // Asignamos el valor del precio según que tamaño de pizza sea
        switch (tamaño) {
            case "Familiar":
                this.precio = 15;
                break;
            case "Mediana":
                this.precio = 10;
                break;
            case "Individual":
                this.precio = 5;
                break;
            default:
                this.precio = 10;
                break;
        }
    }

    protected Pizzas(Parcel in) {
        tipoMasa = in.readString();
        tipoPizza = in.readString();
        tamaño = in.readString();
        precio = in.readInt();
        cantidad = in.readInt();
    }

    public String getTipoMasa() {
        return tipoMasa;
    }

    public String getTipoPizza() {
        return tipoPizza;
    }

    public String getTamaño() {
        return tamaño;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int precioTotal(){
        return cantidad*precio;
    }

    @Override
    public String toString() {
        return getCantidad() + "x Pizza "+ getTipoPizza() + "\n" + getTipoMasa() + " - Tamaño: " + getTamaño() + " " + precioTotal() + "€";
    }
}