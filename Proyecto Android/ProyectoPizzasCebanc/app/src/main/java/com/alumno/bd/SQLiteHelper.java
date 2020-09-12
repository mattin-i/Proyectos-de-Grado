package com.alumno.bd;

import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.alumno.proyectopizzascebanc.R;

/**
 * Created by adminportatil on 15/01/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    String sqltabla1 = "CREATE TABLE clientes (cod_cliente INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, email TEXT, telefono TEXT, direccion TEXT)";
    String sqltabla2 = "CREATE TABLE pedidos (cod_pedido INTEGER PRIMARY KEY AUTOINCREMENT, cod_cliente INTEGER, fecha_pedido DATE, precio_total REAL, FOREIGN KEY(cod_cliente) REFERENCES clientes(cod_cliente))";
    String sqltabla3 = "CREATE TABLE pizzas (cod_pizza INTEGER PRIMARY KEY AUTOINCREMENT, tipo_pizza TEXT, tamano_pizza TEXT, tipo_masa TEXT, cantidad INTEGER, precio REAL, cod_pedido INTEGER, FOREIGN KEY(cod_pedido) REFERENCES pedidos(cod_pedido))";
    String sqltabla4 = "CREATE TABLE bebidas (cod_bebida INTEGER PRIMARY KEY AUTOINCREMENT, tipo_bebida TEXT, precio REAL, cantidad INTEGER, cod_pedido INTEGER, FOREIGN KEY(cod_pedido) REFERENCES pedidos(cod_pedido))";
    String sqlDrop1 = "DROP TABLE IF EXISTS clientes";
    String sqlDrop2 = "DROP TABLE IF EXISTS pedidos";
    String sqlDrop3 = "DROP TABLE IF EXISTS pizzas";
    String sqlDrop4 = "DROP TABLE IF EXISTS bebidos";

    public SQLiteHelper (Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Se ejecuta sentencia SQL para crear las tablas
        sqLiteDatabase.execSQL(sqltabla1);
        sqLiteDatabase.execSQL(sqltabla2);
        sqLiteDatabase.execSQL(sqltabla3);
        sqLiteDatabase.execSQL(sqltabla4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Metodo para actualizar la base de datos
        // En este caso solo borraremos las tablas y crearemos unas nuevas

        // Borramos
        sqLiteDatabase.execSQL(sqlDrop1);
        sqLiteDatabase.execSQL(sqlDrop2);
        sqLiteDatabase.execSQL(sqlDrop3);
        sqLiteDatabase.execSQL(sqlDrop4);

        // Creamos nueva
        sqLiteDatabase.execSQL(sqltabla1);
        sqLiteDatabase.execSQL(sqltabla2);
        sqLiteDatabase.execSQL(sqltabla3);
        sqLiteDatabase.execSQL(sqltabla4);
    }
}
