package com.alumno.adapter;

/**
 * Created by Mattin on 30/12/2017.
 *
 * Lista de pizzas para usar en el adapter
 */

public class PizzaList {
    public String nombre;
    public int imagen;

    public PizzaList (String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }
}