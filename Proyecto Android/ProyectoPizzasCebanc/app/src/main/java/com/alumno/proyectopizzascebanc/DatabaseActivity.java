package com.alumno.proyectopizzascebanc;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.alumno.adapter.MyAdapterConsultas;
import com.alumno.bd.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseActivity extends AppCompatActivity {

    Button btnT1B1;
    Button btnT1B2;
    Button btnT1B3;
    Button btnT1B4;
    Button btnT1B5;
    ListView listViewConsultas;

    String tabla;
    String clave;
    String cod;
    List<String> registros1 = new ArrayList<>();
    List<String> registros2 = new ArrayList<>();

    MyAdapterConsultas adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // Referenciamos botones y listviews
        btnT1B1 = findViewById(R.id.botonT1B1);
        btnT1B2 = findViewById(R.id.botonT1B2);
        btnT1B3 = findViewById(R.id.botonT1B3);
        btnT1B4 = findViewById(R.id.botonT1B4);
        btnT1B5 = findViewById(R.id.botonT1B5);
        listViewConsultas = findViewById(R.id.listViewConsultas);

        // Creamos una instancia de la clase SQLiteHelper y abrimos la base de datos 'DBUsuarios'
        final SQLiteHelper usdb = new SQLiteHelper(this, getResources().getString(R.string.nameDB), null, 1);

        // Listener botones
        // BotonT1B1 Select
        btnT1B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Cuadro para que el usuario elija la tabla que quiere usar
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setTitle(R.string.titleDialog1);
                String[] types = {getResources().getString(R.string.tablaCli), getResources().getString(R.string.tablaPed), getResources().getString(R.string.tablaPiz), getResources().getString(R.string.tablaBeb)};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        switch (i) {
                            case 0:
                                // Tabla Clientes
                                tabla = "clientes";
                                clave = "cod_cliente";
                                break;
                            case 1:
                                // Tabla Pedidos
                                tabla = "pedidos";
                                clave = "cod_pedido";
                                break;
                            case 2:
                                // Tabla Pizzas
                                tabla = "pizzas";
                                clave = "cod_pizza";
                                break;
                            case 3:
                                // Tabla Bebidas
                                tabla = "bebidas";
                                clave = "cod_bebida";
                                break;
                        }
                        // Alert para pedir codigo al usuario
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(DatabaseActivity.this);
                        builder2.setTitle(R.string.titleDialog2);
                        final EditText input = new EditText(DatabaseActivity.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder2.setView(input);
                        builder2.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!input.getText().toString().isEmpty()) {
                                    try {
                                        cod = input.getText().toString();

                                        // Conexion a la base de datos
                                        SQLiteDatabase db = usdb.getReadableDatabase();
                                        if (db != null) {
                                            String sql = "SELECT * FROM " + tabla + " WHERE " + clave + "=" + cod;
                                            Cursor miCursor = db.rawQuery(sql, null);

                                            if (miCursor.moveToFirst()) {
                                                registros1.clear();
                                                registros2.clear();
                                                String text1 = "Hey";
                                                String text2 = "Hey";
                                                switch (tabla) {
                                                    case "clientes":
                                                        text1 = "Cod_Cliente: " + cod + ", Nombre: " + miCursor.getString(miCursor.getColumnIndex("nombre"));
                                                        text2 = "Email: " + miCursor.getString(miCursor.getColumnIndex("email")) + ", Teléfono: " + miCursor.getString(miCursor.getColumnIndex("telefono")) + ", Dirección: " + miCursor.getString(miCursor.getColumnIndex("direccion"));
                                                        break;
                                                    case "pedidos":
                                                        text1 = "Cod_Pedido: " + cod + ", Cod_Cliente: " + miCursor.getString(miCursor.getColumnIndex("cod_cliente"));
                                                        text2 = "Fecha: " + miCursor.getString(miCursor.getColumnIndex("fecha_pedido")) + ", Precio: " + miCursor.getString(miCursor.getColumnIndex("precio_total"));
                                                        break;
                                                    case "pizzas":
                                                        text1 = "Cod_Pizza: " + cod + ", Tipo Pizza: " + miCursor.getString(miCursor.getColumnIndex("tipo_pizza")) + ", Cod_Pedido: " + miCursor.getString(miCursor.getColumnIndex("cod_pedido"));
                                                        text2 = "Tamaño: " + miCursor.getString(miCursor.getColumnIndex("tamano_pizza")) + ", Masa: " + miCursor.getString(miCursor.getColumnIndex("tipo_masa")) + ", Cantidad: " + miCursor.getString(miCursor.getColumnIndex("cantidad")) + ", Precio: " + miCursor.getString(miCursor.getColumnIndex("precio"));
                                                        break;
                                                    case "bebidas":
                                                        text1 = "Cod_Bebida: " + cod + ", Tipo Bebida: " + miCursor.getString(miCursor.getColumnIndex("tipo_bebida")) + ", Cod_Pedido: " + miCursor.getString(miCursor.getColumnIndex("cod_pedido"));
                                                        text2 = "Cantidad: " + miCursor.getString(miCursor.getColumnIndex("cantidad")) + ", Precio: " + miCursor.getString(miCursor.getColumnIndex("precio"));
                                                        break;
                                                }
                                                registros1.add(text1);
                                                registros2.add(text2);

                                                adapter = new MyAdapterConsultas(DatabaseActivity.this, R.layout.list_view_consultas, registros1, registros2);
                                                listViewConsultas.setAdapter(adapter);
                                            } else {
                                                // No existe el registro con el cod introducido
                                                registros1.clear();
                                                registros2.clear();
                                                String msgError = "*** ERROR: No se ha encontrado el registro solicitado ***";
                                                registros1.add(msgError);
                                                registros2.add("");
                                                adapter = new MyAdapterConsultas(DatabaseActivity.this, R.layout.list_view_consultas, registros1, registros2);
                                                listViewConsultas.setAdapter(adapter);
                                            }
                                        }

                                        db.close();
                                    } catch (Exception e) {
                                        Toast.makeText(DatabaseActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(DatabaseActivity.this, "No has introducido nada", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder2.setNegativeButton("CANCELAR", null);
                        builder2.show();
                    }
                });
                builder.show();
            }
        });

        // BotonT1B2 Actualizar
        btnT1B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuadro para que el usuario elija la tabla que quiere usar
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setTitle(R.string.titleDialog1);
                String[] types = {getResources().getString(R.string.tablaCli), getResources().getString(R.string.tablaPed), getResources().getString(R.string.tablaPiz), getResources().getString(R.string.tablaBeb)};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        switch (i) {
                            case 0:
                                // Tabla Clientes
                                tabla = "clientes";
                                final AlertDialog.Builder builderA1 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderA1.setTitle("COD PARA ACTUALIZAR:");
                                final EditText input1 = new EditText(DatabaseActivity.this);
                                input1.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builderA1.setView(input1);
                                builderA1.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!input1.getText().toString().isEmpty()) {
                                            cod = input1.getText().toString();

                                            try {
                                                SQLiteDatabase db = usdb.getReadableDatabase();
                                                if (db != null) {
                                                    String sql = "SELECT * FROM clientes WHERE cod_cliente=" + cod;
                                                    Cursor miCursor = db.rawQuery(sql, null);
                                                    if (miCursor.moveToNext()) {
                                                        // El codigo existe vamos a actualizarlo
                                                        AlertDialog.Builder builderAA1 = new AlertDialog.Builder(DatabaseActivity.this);
                                                        builderAA1.setTitle("Actualizar:");
                                                        LinearLayout layout = new LinearLayout(DatabaseActivity.this);
                                                        layout.setOrientation(LinearLayout.VERTICAL);
                                                        // Nombre
                                                        final EditText inputName = new EditText(DatabaseActivity.this);
                                                        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
                                                        inputName.setHint("Nombre");
                                                        layout.addView(inputName);
                                                        // Email
                                                        final EditText inputEmail = new EditText(DatabaseActivity.this);
                                                        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                                                        inputEmail.setHint("Email");
                                                        layout.addView(inputEmail);
                                                        // Telefono
                                                        final EditText inputTel = new EditText(DatabaseActivity.this);
                                                        inputTel.setInputType(InputType.TYPE_CLASS_TEXT);
                                                        inputTel.setHint("Telefono");
                                                        layout.addView(inputTel);
                                                        // Direccion
                                                        final EditText inputDir = new EditText(DatabaseActivity.this);
                                                        inputDir.setInputType(InputType.TYPE_CLASS_TEXT);
                                                        inputDir.setHint("Direccion");
                                                        layout.addView(inputDir);
                                                        builderAA1.setView(layout);
                                                        builderAA1.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if (!inputName.getText().toString().trim().isEmpty() &&
                                                                        !inputEmail.getText().toString().trim().isEmpty() &&
                                                                        !inputTel.getText().toString().trim().isEmpty() &&
                                                                        !inputDir.getText().toString().trim().isEmpty() &&
                                                                        emailValidator(inputEmail.getText().toString()) &&
                                                                        inputTel.getText().toString().trim().length() == 9) {
                                                                    String nombre = inputName.getText().toString();
                                                                    String email = inputEmail.getText().toString();
                                                                    String telefono = inputTel.getText().toString();
                                                                    String direccion = inputDir.getText().toString();
                                                                    // Conexion a la DB
                                                                    try {
                                                                        SQLiteDatabase db = usdb.getWritableDatabase();
                                                                        if (db != null) {
                                                                            String sql = "UPDATE clientes SET nombre = '" + nombre + "', email = '" + email + "', telefono = '" + telefono + "', direccion = '" + direccion + "' WHERE cod_cliente = " + cod;
                                                                            db.execSQL(sql);
                                                                            Toast.makeText(DatabaseActivity.this, "Actualizado registro", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        db.close();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(DatabaseActivity.this, "No existe el registro a actualizar", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        builderAA1.setNegativeButton("CANCELAR", null);
                                                        builderAA1.show();
                                                    } else {
                                                        // No existe el codigo introducido
                                                        Toast.makeText(DatabaseActivity.this, "No existe el codigo introducido", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                // Error
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "No has introducido nada", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderA1.setNegativeButton("CANCELAR", null);
                                builderA1.show();
                                break;
                            case 1:
                                // Tabla Pedidos
                                tabla = "pedidos";
                                AlertDialog.Builder builderA2 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderA2.setTitle("COD PARA ACTUALIZAR:");
                                final EditText input2 = new EditText(DatabaseActivity.this);
                                input2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builderA2.setView(input2);
                                builderA2.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!input2.getText().toString().isEmpty()) {
                                            cod = input2.getText().toString();

                                            try {
                                                SQLiteDatabase db = usdb.getReadableDatabase();
                                                if (db != null) {
                                                    String sql = "SELECT * FROM pedidos WHERE cod_pedido=" + cod;
                                                    Cursor miCursor = db.rawQuery(sql, null);
                                                    if (miCursor.moveToNext()) {
                                                        // El codigo existe vamos a actualizarlo
                                                        AlertDialog.Builder builderAA1 = new AlertDialog.Builder(DatabaseActivity.this);
                                                        builderAA1.setTitle("Actualizar:");
                                                        LinearLayout layout = new LinearLayout(DatabaseActivity.this);
                                                        layout.setOrientation(LinearLayout.VERTICAL);
                                                        // Fecha
                                                        final EditText inputFecha = new EditText(DatabaseActivity.this);
                                                        inputFecha.setInputType(InputType.TYPE_CLASS_DATETIME);
                                                        inputFecha.setHint("Fecha");
                                                        layout.addView(inputFecha);
                                                        // Precio
                                                        final EditText inputPrecio = new EditText(DatabaseActivity.this);
                                                        inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        inputPrecio.setHint("Precio");
                                                        layout.addView(inputPrecio);
                                                        builderAA1.setView(layout);
                                                        builderAA1.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if (!inputFecha.getText().toString().trim().isEmpty() &&
                                                                        !inputPrecio.getText().toString().trim().isEmpty() &&
                                                                        inputFecha.getText().toString().length()==10) {
                                                                    // Conexion a la DB
                                                                    try {
                                                                        String fecha = inputFecha.getText().toString();
                                                                        double precio = Double.parseDouble(inputPrecio.getText().toString());
                                                                        SQLiteDatabase db = usdb.getWritableDatabase();
                                                                        if (db != null) {
                                                                            String sql = "UPDATE pedidos SET fecha_pedido = '" + fecha + "', precio_total = " + precio + " WHERE cod_pedido = " + cod;
                                                                            db.execSQL(sql);
                                                                            Toast.makeText(DatabaseActivity.this, "Actualizado registro", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        db.close();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(DatabaseActivity.this, "No existe el registro a actualizar", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        builderAA1.setNegativeButton("CANCELAR", null);
                                                        builderAA1.show();
                                                    } else {
                                                        // No existe el codigo introducido
                                                        Toast.makeText(DatabaseActivity.this, "No existe el codigo introducido", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                // Error
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "No has introducido nada", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderA2.setNegativeButton("CANCELAR", null);
                                builderA2.show();
                                break;
                            case 2:
                                // Tabla Pizzas
                                tabla = "pizzas";
                                AlertDialog.Builder builderA3 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderA3.setTitle("COD PARA ACTUALIZAR:");
                                final EditText input3 = new EditText(DatabaseActivity.this);
                                input3.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builderA3.setView(input3);
                                builderA3.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!input3.getText().toString().isEmpty()) {
                                            cod = input3.getText().toString();

                                            try {
                                                SQLiteDatabase db = usdb.getReadableDatabase();
                                                if (db != null) {
                                                    String sql = "SELECT * FROM pizzas WHERE cod_pizza=" + cod;
                                                    Cursor miCursor = db.rawQuery(sql, null);
                                                    if (miCursor.moveToNext()) {
                                                        // El codigo existe vamos a actualizarlo
                                                        AlertDialog.Builder builderAA1 = new AlertDialog.Builder(DatabaseActivity.this);
                                                        builderAA1.setTitle("Actualizar:");
                                                        LinearLayout layout = new LinearLayout(DatabaseActivity.this);
                                                        layout.setOrientation(LinearLayout.VERTICAL);
                                                        // Tipo Pizza
                                                        String[] tp = {"Barbacoa", "Carbonara", "4 Quesos", "Vegetal", "Tropical"};
                                                        final ArrayAdapter<String> adpTP = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tp);
                                                        final Spinner spTipoPizza = new Spinner(DatabaseActivity.this);
                                                        spTipoPizza.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                        spTipoPizza.setAdapter(adpTP);
                                                        layout.addView(spTipoPizza);
                                                        // Tamaño Pizza
                                                        String[] tap = {"Individual", "Mediana", "Familiar"};
                                                        final ArrayAdapter<String> adpTAP = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tap);
                                                        final Spinner spTamanoPizza = new Spinner(DatabaseActivity.this);
                                                        spTamanoPizza.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                        spTamanoPizza.setAdapter(adpTAP);
                                                        layout.addView(spTamanoPizza);
                                                        // Tipo Masa
                                                        String[] tm = {"Masa Fina", "Masa Gorda"};
                                                        final ArrayAdapter<String> adpTM = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tm);
                                                        final Spinner spTipoMasa = new Spinner(DatabaseActivity.this);
                                                        spTipoMasa.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                        spTipoMasa.setAdapter(adpTM);
                                                        layout.addView(spTipoMasa);
                                                        // Cantidad
                                                        final EditText inputCantidad = new EditText(DatabaseActivity.this);
                                                        inputCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        inputCantidad.setHint("Cantidad");
                                                        layout.addView(inputCantidad);
                                                        builderAA1.setView(layout);
                                                        builderAA1.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if (!inputCantidad.getText().toString().trim().isEmpty()) {
                                                                    String tipoPizza = spTipoPizza.getSelectedItem().toString();
                                                                    String tamanoPizza = spTamanoPizza.getSelectedItem().toString();
                                                                    String tipoMasa = spTipoMasa.getSelectedItem().toString();
                                                                    int cantidad = Integer.parseInt(inputCantidad.getText().toString());
                                                                    double precio;
                                                                    switch (tamanoPizza) {
                                                                        case "Familiar":
                                                                            precio = 15 * cantidad;
                                                                            break;
                                                                        case "Mediana":
                                                                            precio = 10 * cantidad;
                                                                            break;
                                                                        case "Individual":
                                                                            precio = 5 * cantidad;
                                                                            break;
                                                                        default:
                                                                            precio = 10 * cantidad;
                                                                            break;
                                                                    }
                                                                    // Conexion a la DB
                                                                    try {
                                                                        SQLiteDatabase db = usdb.getWritableDatabase();
                                                                        if (db != null) {
                                                                            String sql = "UPDATE pizzas SET tipo_pizza = '" + tipoPizza + "', tamano_pizza = '" + tamanoPizza + "', tipo_masa = '" + tipoMasa + "', cantidad = " + cantidad + ", precio = " + precio + " WHERE cod_pizza = " + cod;
                                                                            db.execSQL(sql);
                                                                            Toast.makeText(DatabaseActivity.this, "Actualizado registro", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        db.close();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(DatabaseActivity.this, "No existe el registro a actualizar", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        builderAA1.setNegativeButton("CANCELAR", null);
                                                        builderAA1.show();
                                                    } else {
                                                        // No existe el codigo introducido
                                                        Toast.makeText(DatabaseActivity.this, "No existe el codigo introducido", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                // Error
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "No has introducido nada", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderA3.setNegativeButton("CANCELAR", null);
                                builderA3.show();
                                break;
                            case 3:
                                // Tabla Bebidas
                                tabla = "bebidas";
                                AlertDialog.Builder builderA4 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderA4.setTitle("COD PARA ACTUALIZAR:");
                                final EditText input4 = new EditText(DatabaseActivity.this);
                                input4.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builderA4.setView(input4);
                                builderA4.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!input4.getText().toString().isEmpty()) {
                                            cod = input4.getText().toString();

                                            try {
                                                SQLiteDatabase db = usdb.getReadableDatabase();
                                                if (db != null) {
                                                    String sql = "SELECT * FROM bebidas WHERE cod_bebida=" + cod;
                                                    Cursor miCursor = db.rawQuery(sql, null);
                                                    if (miCursor.moveToNext()) {
                                                        // El codigo existe vamos a actualizarlo
                                                        AlertDialog.Builder builderAA1 = new AlertDialog.Builder(DatabaseActivity.this);
                                                        builderAA1.setTitle("Actualizar:");
                                                        LinearLayout layout = new LinearLayout(DatabaseActivity.this);
                                                        layout.setOrientation(LinearLayout.VERTICAL);
                                                        // Tipo Bebida
                                                        String[] tb = {"Agua", "Coca Cola", "Fanta Naranja", "Fanta Limon", "Nestea", "RedBull", "Cerveza"};
                                                        final ArrayAdapter<String> adpTB = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tb);
                                                        final Spinner spTipoBebida = new Spinner(DatabaseActivity.this);
                                                        spTipoBebida.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                        spTipoBebida.setAdapter(adpTB);
                                                        layout.addView(spTipoBebida);
                                                        // Cantidad
                                                        final EditText inputCantidad = new EditText(DatabaseActivity.this);
                                                        inputCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        inputCantidad.setHint("Cantidad");
                                                        layout.addView(inputCantidad);
                                                        builderAA1.setView(layout);
                                                        builderAA1.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if (!inputCantidad.getText().toString().trim().isEmpty()) {
                                                                    String tipoBebida = spTipoBebida.getSelectedItem().toString();
                                                                    int cantidad = Integer.parseInt(inputCantidad.getText().toString());
                                                                    double precio;
                                                                    switch (tipoBebida) {
                                                                        case "Coca Cola":
                                                                            precio = 1.6 * cantidad;
                                                                            break;
                                                                        case "Fanta Naranja":
                                                                            precio = 1.6 * cantidad;
                                                                            break;
                                                                        case "Fanta Limon":
                                                                            precio = 1.6 * cantidad;
                                                                            break;
                                                                        case "RedBull":
                                                                            precio = 2 * cantidad;
                                                                            break;
                                                                        case "Nestea":
                                                                            precio = 1.5 * cantidad;
                                                                            break;
                                                                        case "Cerveza":
                                                                            precio = 1.8 * cantidad;
                                                                            break;
                                                                        case "Agua":
                                                                            precio = 1 * cantidad;
                                                                            break;
                                                                        default:
                                                                            precio = 1 * cantidad;
                                                                            break;
                                                                    }
                                                                    // Conexion a la DB
                                                                    try {
                                                                        SQLiteDatabase db = usdb.getWritableDatabase();
                                                                        if (db != null) {
                                                                            String sql = "UPDATE bebidas SET tipo_bebida = '" + tipoBebida + "',  cantidad = " + cantidad + ", precio = " + precio + " WHERE cod_bebida = " + cod;
                                                                            db.execSQL(sql);
                                                                            Toast.makeText(DatabaseActivity.this, "Actualizado registro", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        db.close();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(DatabaseActivity.this, "No existe el registro a actualizar", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        builderAA1.setNegativeButton("CANCELAR", null);
                                                        builderAA1.show();
                                                    } else {
                                                        // No existe el codigo introducido
                                                        Toast.makeText(DatabaseActivity.this, "No existe el codigo introducido", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                // Error
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "No has introducido nada", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderA4.setNegativeButton("CANCELAR", null);
                                builderA4.show();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        // BotonT1B3 Borrar
        btnT1B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuadro para que el usuario elija la tabla que quiere usar
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setTitle(R.string.titleDialog1);
                String[] types = {getResources().getString(R.string.tablaCli), getResources().getString(R.string.tablaPed), getResources().getString(R.string.tablaPiz), getResources().getString(R.string.tablaBeb)};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        switch (i) {
                            case 0:
                                // Tabla Clientes
                                tabla = "clientes";
                                clave = "cod_cliente";
                                break;
                            case 1:
                                // Tabla Pedidos
                                tabla = "pedidos";
                                clave = "cod_pedido";
                                break;
                            case 2:
                                // Tabla Pizzas
                                tabla = "pizzas";
                                clave = "cod_pizza";
                                break;
                            case 3:
                                // Tabla Bebidas
                                tabla = "bebidas";
                                clave = "cod_bebida";
                                break;
                        }
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(DatabaseActivity.this);
                        builder3.setTitle("Código para borrar:");
                        final EditText input = new EditText(DatabaseActivity.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder3.setView(input);
                        builder3.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!input.getText().toString().trim().isEmpty()) {
                                    cod = input.getText().toString().trim();
                                    AlertDialog.Builder builder31 = new AlertDialog.Builder(DatabaseActivity.this);
                                    builder31.setTitle("Seguro que quieres borrar?");
                                    builder31.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            try {
                                                SQLiteDatabase db = usdb.getWritableDatabase();
                                                if (db != null) {
                                                    String sql = "DELETE FROM " + tabla + " WHERE " + clave + "=" + cod;
                                                    db.execSQL(sql);
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(DatabaseActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder31.setNegativeButton("NO", null);
                                    builder31.show();
                                } else {
                                    Toast.makeText(DatabaseActivity.this, "No has introducido nada", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder3.setNegativeButton("CANCELAR", null);
                        builder3.show();
                    }
                });
                builder.show();
            }
        });

        // BotonT1B4 Nuevo
        btnT1B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuadro para que el usuario elija la tabla que quiere usar
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setTitle(R.string.titleDialog1);
                String[] types = {getResources().getString(R.string.tablaCli), getResources().getString(R.string.tablaPed), getResources().getString(R.string.tablaPiz), getResources().getString(R.string.tablaBeb)};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        switch (i) {
                            case 0:
                                // Tabla Clientes
                                tabla = "clientes";
                                AlertDialog.Builder builderAA1 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderAA1.setTitle("Nuevo:");
                                LinearLayout layout = new LinearLayout(DatabaseActivity.this);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                // Nombre
                                final EditText inputName = new EditText(DatabaseActivity.this);
                                inputName.setInputType(InputType.TYPE_CLASS_TEXT);
                                inputName.setHint("Nombre");
                                layout.addView(inputName);
                                // Email
                                final EditText inputEmail = new EditText(DatabaseActivity.this);
                                inputEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                                inputEmail.setHint("Email");
                                layout.addView(inputEmail);
                                // Telefono
                                final EditText inputTel = new EditText(DatabaseActivity.this);
                                inputTel.setInputType(InputType.TYPE_CLASS_TEXT);
                                inputTel.setHint("Telefono");
                                layout.addView(inputTel);
                                // Direccion
                                final EditText inputDir = new EditText(DatabaseActivity.this);
                                inputDir.setInputType(InputType.TYPE_CLASS_TEXT);
                                inputDir.setHint("Direccion");
                                layout.addView(inputDir);
                                builderAA1.setView(layout);
                                builderAA1.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!inputName.getText().toString().trim().isEmpty() &&
                                                !inputEmail.getText().toString().trim().isEmpty() &&
                                                !inputTel.getText().toString().trim().isEmpty() &&
                                                !inputDir.getText().toString().trim().isEmpty() &&
                                                emailValidator(inputEmail.getText().toString()) &&
                                                inputTel.getText().toString().trim().length() == 9) {
                                            String nombre = inputName.getText().toString();
                                            String email = inputEmail.getText().toString();
                                            String telefono = inputTel.getText().toString();
                                            String direccion = inputDir.getText().toString();
                                            // Conexion a la DB
                                            try {
                                                SQLiteDatabase db = usdb.getWritableDatabase();
                                                if (db != null) {
                                                    String sql = "INSERT INTO clientes VALUES (null, '" + nombre + "', '" + email + "', '" + telefono + "', '" + direccion + "')";
                                                    db.execSQL(sql);
                                                    Toast.makeText(DatabaseActivity.this, "Nuevo registro añadido", Toast.LENGTH_SHORT).show();
                                                }
                                                db.close();
                                            } catch (Exception e) {
                                                Toast.makeText(DatabaseActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderAA1.setNegativeButton("CANCELAR", null);
                                builderAA1.show();
                                break;
                            case 1:
                                // Tabla Pedidos
                                tabla = "pedidos";
                                AlertDialog.Builder builderN2 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderN2.setTitle("Nuevo:");
                                LinearLayout layout2 = new LinearLayout(DatabaseActivity.this);
                                layout2.setOrientation(LinearLayout.VERTICAL);
                                List<String> lCod = new ArrayList<>();
                                try {
                                    SQLiteDatabase dbA = usdb.getReadableDatabase();
                                    if (dbA != null) {
                                        String sql = "SELECT * FROM clientes";
                                        Cursor miCursor = dbA.rawQuery(sql, null);
                                        if (miCursor.moveToFirst()) {
                                            do {
                                                lCod.add(miCursor.getString(miCursor.getColumnIndex("cod_cliente")));
                                            } while(miCursor.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(DatabaseActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                                String[] cc = new String [lCod.size()];
                                for (int z = 0; z < lCod.size(); z++) {
                                    cc[z] = lCod.get(z);
                                }
                                // Cod Clientes
                                final ArrayAdapter<String> adpCC = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, cc);
                                final Spinner spCC = new Spinner(DatabaseActivity.this);
                                spCC.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spCC.setAdapter(adpCC);
                                layout2.addView(spCC);
                                // Fecha
                                final EditText inputFecha = new EditText(DatabaseActivity.this);
                                inputFecha.setInputType(InputType.TYPE_CLASS_DATETIME);
                                inputFecha.setHint("Fecha");
                                layout2.addView(inputFecha);
                                // Precio
                                final EditText inputPrecio = new EditText(DatabaseActivity.this);
                                inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER);
                                inputPrecio.setHint("Precio");
                                layout2.addView(inputPrecio);
                                builderN2.setView(layout2);
                                builderN2.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!inputFecha.getText().toString().trim().isEmpty() &&
                                                !inputPrecio.getText().toString().trim().isEmpty() &&
                                                inputFecha.getText().toString().length()==10) {
                                            // Conexion a la DB
                                            try {
                                                int f_cod = Integer.parseInt(spCC.getSelectedItem().toString());
                                                String fecha = inputFecha.getText().toString();
                                                double precio = Double.parseDouble(inputPrecio.getText().toString());
                                                SQLiteDatabase db = usdb.getWritableDatabase();
                                                if (db != null) {
                                                    String sql = "INSERT INTO pedidos VALUES (null, " + f_cod + ",'" + fecha + "', " + precio + ")";
                                                    db.execSQL(sql);
                                                    Toast.makeText(DatabaseActivity.this, "Nuevo registro realizado", Toast.LENGTH_SHORT).show();
                                                }
                                                db.close();
                                            } catch (Exception e) {
                                                Toast.makeText(DatabaseActivity.this, "No existe el registro a actualizar", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderN2.setNegativeButton("CANCELAR", null);
                                builderN2.show();
                                break;
                            case 2:
                                // Tabla Pizzas
                                tabla = "pizzas";
                                AlertDialog.Builder builderN3 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderN3.setTitle("Actualizar:");
                                LinearLayout layout3 = new LinearLayout(DatabaseActivity.this);
                                layout3.setOrientation(LinearLayout.VERTICAL);
                                // Cod Pedido
                                List<String> lCod2 = new ArrayList<>();
                                try {
                                    SQLiteDatabase dbA = usdb.getReadableDatabase();
                                    if (dbA != null) {
                                        String sql = "SELECT * FROM pedidos";
                                        Cursor miCursor = dbA.rawQuery(sql, null);
                                        if (miCursor.moveToFirst()) {
                                            do {
                                                lCod2.add(miCursor.getString(miCursor.getColumnIndex("cod_pedido")));
                                            } while(miCursor.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(DatabaseActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                                String[] cp = new String [lCod2.size()];
                                for (int z = 0; z < lCod2.size(); z++) {
                                    cp[z] = lCod2.get(z);
                                }
                                // Cod Pedido
                                final ArrayAdapter<String> adpCP = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, cp);
                                final Spinner spCP = new Spinner(DatabaseActivity.this);
                                spCP.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spCP.setAdapter(adpCP);
                                layout3.addView(spCP);
                                // Tipo Pizza
                                String[] tp = {"Barbacoa", "Carbonara", "4 Quesos", "Vegetal", "Tropical"};
                                final ArrayAdapter<String> adpTP = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tp);
                                final Spinner spTipoPizza = new Spinner(DatabaseActivity.this);
                                spTipoPizza.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spTipoPizza.setAdapter(adpTP);
                                layout3.addView(spTipoPizza);
                                // Tamaño Pizza
                                String[] tap = {"Individual", "Mediana", "Familiar"};
                                final ArrayAdapter<String> adpTAP = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tap);
                                final Spinner spTamanoPizza = new Spinner(DatabaseActivity.this);
                                spTamanoPizza.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spTamanoPizza.setAdapter(adpTAP);
                                layout3.addView(spTamanoPizza);
                                // Tipo Masa
                                String[] tm = {"Masa Fina", "Masa Gorda"};
                                final ArrayAdapter<String> adpTM = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tm);
                                final Spinner spTipoMasa = new Spinner(DatabaseActivity.this);
                                spTipoMasa.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spTipoMasa.setAdapter(adpTM);
                                layout3.addView(spTipoMasa);
                                // Cantidad
                                final EditText inputCantidad = new EditText(DatabaseActivity.this);
                                inputCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                inputCantidad.setHint("Cantidad");
                                layout3.addView(inputCantidad);
                                builderN3.setView(layout3);
                                builderN3.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!inputCantidad.getText().toString().trim().isEmpty()) {
                                            int f_cod2 = Integer.parseInt(spCP.getSelectedItem().toString());
                                            String tipoPizza = spTipoPizza.getSelectedItem().toString();
                                            String tamanoPizza = spTamanoPizza.getSelectedItem().toString();
                                            String tipoMasa = spTipoMasa.getSelectedItem().toString();
                                            int cantidad = Integer.parseInt(inputCantidad.getText().toString());
                                            double precio;
                                            switch (tamanoPizza) {
                                                case "Familiar":
                                                    precio = 15 * cantidad;
                                                    break;
                                                case "Mediana":
                                                    precio = 10 * cantidad;
                                                    break;
                                                case "Individual":
                                                    precio = 5 * cantidad;
                                                    break;
                                                default:
                                                    precio = 10 * cantidad;
                                                    break;
                                            }
                                            // Conexion a la DB
                                            try {
                                                SQLiteDatabase db = usdb.getWritableDatabase();
                                                if (db != null) {
                                                    String sql = "INSERT INTO pizzas VALUES (null, '" + tipoPizza + "', '" + tamanoPizza + "', '" + tipoMasa + "', " + cantidad + ", " + precio + ", " + f_cod2 + ")";
                                                    db.execSQL(sql);
                                                    Toast.makeText(DatabaseActivity.this, "Nuevo registro realizado", Toast.LENGTH_SHORT).show();
                                                }
                                                db.close();
                                            } catch (Exception e) {
                                                Toast.makeText(DatabaseActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderN3.setNegativeButton("CANCELAR", null);
                                builderN3.show();
                                break;
                            case 3:
                                // Tabla Bebidas
                                tabla = "bebidas";
                                AlertDialog.Builder builderN4 = new AlertDialog.Builder(DatabaseActivity.this);
                                builderN4.setTitle("Actualizar:");
                                LinearLayout layout4 = new LinearLayout(DatabaseActivity.this);
                                layout4.setOrientation(LinearLayout.VERTICAL);
                                // Cod Pedido
                                List<String> lCod3 = new ArrayList<>();
                                try {
                                    SQLiteDatabase dbA = usdb.getReadableDatabase();
                                    if (dbA != null) {
                                        String sql = "SELECT * FROM pedidos";
                                        Cursor miCursor = dbA.rawQuery(sql, null);
                                        if (miCursor.moveToFirst()) {
                                            do {
                                                lCod3.add(miCursor.getString(miCursor.getColumnIndex("cod_pedido")));
                                            } while(miCursor.moveToNext());
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(DatabaseActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                                String[] cp2 = new String [lCod3.size()];
                                for (int z = 0; z < lCod3.size(); z++) {
                                    cp2[z] = lCod3.get(z);
                                }
                                // Cod Pedido
                                final ArrayAdapter<String> adpCP2 = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, cp2);
                                final Spinner spCP2 = new Spinner(DatabaseActivity.this);
                                spCP2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spCP2.setAdapter(adpCP2);
                                layout4.addView(spCP2);
                                // Tipo Bebida
                                String[] tb = {"Agua", "Coca Cola", "Fanta Naranja", "Fanta Limon", "Nestea", "RedBull", "Cerveza"};
                                final ArrayAdapter<String> adpTB = new ArrayAdapter<String>(DatabaseActivity.this, android.R.layout.simple_spinner_item, tb);
                                final Spinner spTipoBebida = new Spinner(DatabaseActivity.this);
                                spTipoBebida.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                spTipoBebida.setAdapter(adpTB);
                                layout4.addView(spTipoBebida);
                                // Cantidad
                                final EditText inputCantidad2 = new EditText(DatabaseActivity.this);
                                inputCantidad2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                inputCantidad2.setHint("Cantidad");
                                layout4.addView(inputCantidad2);
                                builderN4.setView(layout4);
                                builderN4.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!inputCantidad2.getText().toString().trim().isEmpty()) {
                                            int f_cod3 = Integer.parseInt(spCP2.getSelectedItem().toString());
                                            String tipoBebida = spTipoBebida.getSelectedItem().toString();
                                            int cantidad = Integer.parseInt(inputCantidad2.getText().toString());
                                            double precio;
                                            switch (tipoBebida) {
                                                case "Coca Cola":
                                                    precio = 1.6 * cantidad;
                                                    break;
                                                case "Fanta Naranja":
                                                    precio = 1.6 * cantidad;
                                                    break;
                                                case "Fanta Limon":
                                                    precio = 1.6 * cantidad;
                                                    break;
                                                case "RedBull":
                                                    precio = 2 * cantidad;
                                                    break;
                                                case "Nestea":
                                                    precio = 1.5 * cantidad;
                                                    break;
                                                case "Cerveza":
                                                    precio = 1.8 * cantidad;
                                                    break;
                                                case "Agua":
                                                    precio = 1 * cantidad;
                                                    break;
                                                default:
                                                    precio = 1 * cantidad;
                                                    break;
                                            }
                                            // Conexion a la DB
                                            try {
                                                SQLiteDatabase db = usdb.getWritableDatabase();
                                                if (db != null) {
                                                    String sql = "INSERT INTO bebidas VALUES (null, '" + tipoBebida + "', " + precio + ", " + cantidad + ", " + f_cod3 + ")";
                                                    db.execSQL(sql);
                                                    Toast.makeText(DatabaseActivity.this, "Actualizado registro", Toast.LENGTH_SHORT).show();
                                                }
                                                db.close();
                                            } catch (Exception e) {
                                                Toast.makeText(DatabaseActivity.this, "No existe el registro a actualizar", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(DatabaseActivity.this, "Error en la introducción de datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builderN4.setNegativeButton("CANCELAR", null);
                                builderN4.show();
                                break;
                        }

                    }
                });
                builder.show();
            }
        });

        // BotonT1B5 SelectAll
        btnT1B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuadro para que el usuario elija la tabla que quiere usar
                AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);
                builder.setTitle(R.string.titleDialog1);
                String[] types = {getResources().getString(R.string.tablaCli), getResources().getString(R.string.tablaPed), getResources().getString(R.string.tablaPiz), getResources().getString(R.string.tablaBeb)};
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        switch (i) {
                            case 0:
                                // Tabla Clientes
                                tabla = "clientes";
                                break;
                            case 1:
                                // Tabla Pedidos
                                tabla = "pedidos";
                                break;
                            case 2:
                                // Tabla Pizzas
                                tabla = "pizzas";
                                break;
                            case 3:
                                // Tabla Bebidas
                                tabla = "bebidas";
                                break;
                        }
                        try {
                            SQLiteDatabase db = usdb.getReadableDatabase();
                            String sql = "SELECT * FROM " + tabla;
                            Cursor miCursor = db.rawQuery(sql, null);

                            if (miCursor.moveToFirst()) {
                                registros1.clear();
                                registros2.clear();
                                do {
                                    String text1 = "Hey";
                                    String text2 = "Hey";
                                    switch (tabla) {
                                        case "clientes":
                                            text1 = "Cod_Cliente: " + miCursor.getString(miCursor.getColumnIndex("cod_cliente")) + " | Nombre: " + miCursor.getString(miCursor.getColumnIndex("nombre"));
                                            text2 = "Email: " + miCursor.getString(miCursor.getColumnIndex("email")) + " | Teléfono: " + miCursor.getString(miCursor.getColumnIndex("telefono")) + " | Dirección: " + miCursor.getString(miCursor.getColumnIndex("direccion"));
                                            break;
                                        case "pedidos":
                                            text1 = "Cod_Pedido: " + miCursor.getString(miCursor.getColumnIndex("cod_pedido")) + " | Cod_Cliente: " + miCursor.getString(miCursor.getColumnIndex("cod_cliente"));
                                            text2 = "Fecha: " + miCursor.getString(miCursor.getColumnIndex("fecha_pedido")) + " | Precio: " + miCursor.getString(miCursor.getColumnIndex("precio_total"));
                                            break;
                                        case "pizzas":
                                            text1 = "Cod_Pizza: " + miCursor.getString(miCursor.getColumnIndex("cod_pizza")) + " | Tipo Pizza: " + miCursor.getString(miCursor.getColumnIndex("tipo_pizza")) + " | Cod_Pedido: " + miCursor.getString(miCursor.getColumnIndex("cod_pedido"));
                                            text2 = "Tamaño: " + miCursor.getString(miCursor.getColumnIndex("tamano_pizza")) + " | Masa: " + miCursor.getString(miCursor.getColumnIndex("tipo_masa")) + " | Cantidad: " + miCursor.getString(miCursor.getColumnIndex("cantidad")) + " | Precio: " + miCursor.getString(miCursor.getColumnIndex("precio")) + " €";
                                            break;
                                        case "bebidas":
                                            text1 = "Cod_Bebida: " + miCursor.getString(miCursor.getColumnIndex("cod_bebida")) + " | Tipo Bebida: " + miCursor.getString(miCursor.getColumnIndex("tipo_bebida")) + " | Cod_Pedido: " + miCursor.getString(miCursor.getColumnIndex("cod_pedido"));
                                            text2 = "Cantidad: " + miCursor.getString(miCursor.getColumnIndex("cantidad")) + " | Precio: " + miCursor.getString(miCursor.getColumnIndex("precio")) + " €";
                                            break;
                                    }
                                    registros1.add(text1);
                                    registros2.add(text2);
                                } while (miCursor.moveToNext());

                                adapter = new MyAdapterConsultas(DatabaseActivity.this, R.layout.list_view_consultas, registros1, registros2);
                                listViewConsultas.setAdapter(adapter);
                            }

                            db.close();
                        } catch (Exception e) {
                            Toast.makeText(DatabaseActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.show();
            }
        });
    }

    public boolean emailValidator (String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = getResources().getString(R.string.emailValidator);
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
