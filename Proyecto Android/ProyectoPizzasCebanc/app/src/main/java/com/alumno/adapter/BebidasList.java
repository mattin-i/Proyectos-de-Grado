package com.alumno.adapter;

/**
 * Created by Mattin on 30/12/2017.
 *
 * Lista de bebidas para usar en el adapter
 */

public class BebidasList {
    public String nombre;
    public int imagen;
    public String precio;

    public BebidasList (String nombre, int imagen, String precio) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public String getPrecio() {
        return precio;
    }
}
