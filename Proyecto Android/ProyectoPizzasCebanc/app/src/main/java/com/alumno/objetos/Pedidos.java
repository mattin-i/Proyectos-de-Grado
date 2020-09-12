package com.alumno.objetos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by adminportatil on 11/01/2018.
 *
 * Clase para la creacion de pedidos
 */

public class Pedidos implements Serializable {
    private String nombre;
    private String phoneNumber;
    private String email;
    private String direccion;
    private List<String> pedido;
    private double precio;
    private boolean premio1 = false;
    private boolean premio2 = false;

    public Pedidos (String nombre, String phoneNumber, String email, List<String> pedido, double precio) {
        this.nombre = nombre;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pedido = pedido;
        this.precio = precio;

        //Comprobamos si se le asignaran los premios o no
        if (this.precio > 30) {
            premio1 = true;
        }
        if (this.precio > 40) {
            premio2 = true;
        }
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        String msg = "Nombre: " + nombre + "\nTeléfono: " + phoneNumber + "\nEmail: " + email;

        if (direccion != null) {
            msg += "\nDirección: " + direccion;
        }

        msg += "\n\nPedido:\n";

        for (int i = 0; i < pedido.size(); i++) {
            msg += "\n\n" + pedido.get(i);
        }

        msg += "\n\n Precio Total: " + precio + "€";

        if (premio1) {
            msg += "\n\n  Enhorabuena, por haber hecho un pedido superior a 30€ te llevas de regalo un peluche del muñeco de Android.";
        }

        if (premio2) {
            msg += "\n\n  Además por haber hecho un pedido superior a 40€ también te regalamos un vale para comer en el comedor de Cebanc.";
        }

        return msg;
    }
}
