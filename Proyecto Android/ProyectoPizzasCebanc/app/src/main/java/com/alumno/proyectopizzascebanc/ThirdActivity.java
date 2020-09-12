package com.alumno.proyectopizzascebanc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alumno.bd.SQLiteHelper;
import com.alumno.objetos.Bebidas;
import com.alumno.objetos.Pizzas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class ThirdActivity extends AppCompatActivity {

    String resumen;
    String email;
    String nombre;
    double precio;

    TextView textViewResumen;
    Button botonComprar;
    Button botonVerPedidos;

    List<Pizzas> listPizzas;
    List<Bebidas> listBebidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            resumen = extras.getString("resumen");
            email = extras.getString("mail");
            nombre = extras.getString("nombre");
            precio = extras.getDouble("precioTotal");

            Bundle lists = intent.getBundleExtra("bundle");
            listPizzas = (List<Pizzas>) lists.getSerializable("pizzas");
            listBebidas = (List<Bebidas>) lists.getSerializable("bebidas");
        }

        textViewResumen = findViewById(R.id.textViewResumen);
        botonComprar = findViewById(R.id.botonComprar);
        botonVerPedidos = findViewById(R.id.botonVerPedidos);

        textViewResumen.setText(resumen);
        textViewResumen.setMovementMethod(new ScrollingMovementMethod());

        // Ultimo boton con un alert si queremos confirmar la compra
        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ThirdActivity.this);
                builder.setTitle(getResources().getString(R.string.q3Title));
                builder.setMessage(getResources().getString(R.string.q3Quest));
                builder.setPositiveButton(getResources().getString(R.string.q3Comprar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Comprar
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, resumen);
                        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));

                        try {
                            // Creamos los registros
                            SQLiteHelper usdb = new SQLiteHelper(ThirdActivity.this, getResources().getString(R.string.nameDB), null, 1);
                            SQLiteDatabase db = usdb.getWritableDatabase();

                            if (db != null) {
                                // Recogemos la fecha actual
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat(getResources().getString(R.string.dateFormat));
                                String date = df.format(c);

                                // Insertamos el registro pedidos
                                String sqlPedido = "INSERT INTO pedidos VALUES (null, (SELECT cod_cliente FROM clientes WHERE nombre='" + nombre + "'), '" + date + "', " + precio + ")";
                                db.execSQL(sqlPedido);

                                // Registro pizzas
                                if (listPizzas != null) {
                                    for (int j = 0; j < listPizzas.size(); j++) {
                                        String sqlPizzas = "INSERT INTO pizzas VALUES (null, '" + listPizzas.get(j).getTipoPizza() + "', '" + listPizzas.get(j).getTamaño() + "', '" + listPizzas.get(j).getTipoMasa() + "', " + listPizzas.get(j).getCantidad() + ", " + listPizzas.get(j).precioTotal() + ", (SELECT MAX(cod_pedido) FROM pedidos))";
                                        db.execSQL(sqlPizzas);
                                    }
                                }

                                // Registro bebidas
                                if (listBebidas != null) {
                                    for (int j = 0; j < listBebidas.size(); j++) {
                                        String sqlBebidas = "INSERT INTO bebidas VALUES (null, '" + listBebidas.get(j).getTipoBebida() + "', " + listBebidas.get(j).precioTotal() + ", " + listBebidas.get(j).getCantidad() + ", (SELECT MAX(cod_pedido) FROM pedidos))";
                                        db.execSQL(sqlBebidas);
                                    }
                                }

                                // Mensaje que se han introducido todos los registros

                                db.close();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ThirdActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.q3Anular), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Intent para volver al inicio de la aplicacion
                        Intent intent = new Intent(ThirdActivity.this, InfoActivity.class);
                        startActivity(intent);
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        botonVerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThirdActivity.this, PedidosActivity.class);
                intent.putExtra("nombre", nombre);
                startActivity(intent);
            }
        });
    }
}
