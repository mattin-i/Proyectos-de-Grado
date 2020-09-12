package com.alumno.proyectopizzascebanc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alumno.adapter.MyAdapterPedidos;
import com.alumno.bd.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    String nombre;
    double precioTotal;

    ListView lvPe;
    TextView tvNCliente;
    TextView tvPrecio;

    List<String> listCodigo;
    List<String> listFecha;
    List<String> listPrecio;
    MyAdapterPedidos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        lvPe = findViewById(R.id.listViewPedidos);
        tvNCliente = findViewById(R.id.textViewNCliente);
        tvPrecio = findViewById(R.id.textViewPrecioFinal);

        listCodigo = new ArrayList<>();
        listFecha = new ArrayList<>();
        listPrecio = new ArrayList<>();

        precioTotal = 0;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            nombre = bundle.getString("nombre");
        }

        tvNCliente.setText("Cliente: " + nombre);

        try {
            // Conexion a la base de datos
            SQLiteHelper usdb = new SQLiteHelper(PedidosActivity.this, getResources().getString(R.string.nameDB), null, 1);
            SQLiteDatabase db = usdb.getReadableDatabase();

            if (db != null) {
                String sql = "SELECT * FROM pedidos WHERE cod_cliente = (SELECT cod_cliente FROM clientes WHERE nombre = '" + nombre + "')";
                Cursor miCursor = db.rawQuery(sql, null);

                if (miCursor.moveToFirst()) {
                    do {
                        listCodigo.add(miCursor.getString(miCursor.getColumnIndex("cod_pedido")));
                        listFecha.add(miCursor.getString(miCursor.getColumnIndex("fecha_pedido")));
                        listPrecio.add(miCursor.getString(miCursor.getColumnIndex("precio_total")));

                        precioTotal += miCursor.getDouble(miCursor.getColumnIndex("precio_total"));
                    } while (miCursor.moveToNext());
                }
            }

            db.close();
        } catch (Exception e) {

        }

        adapter = new MyAdapterPedidos(this, R.layout.list_view_pedidos, listCodigo, listFecha, listPrecio);
        lvPe.setAdapter(adapter);

        tvPrecio.setText("Precio Total: " + precioTotal + " €");
    }
}
