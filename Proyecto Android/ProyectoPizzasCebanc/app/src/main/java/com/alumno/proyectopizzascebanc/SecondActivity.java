package com.alumno.proyectopizzascebanc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alumno.adapter.BebidasList;
import com.alumno.adapter.MyAdapterBebidas;
import com.alumno.adapter.MyAdapterCesta;
import com.alumno.adapter.MyAdapterPizzas;
import com.alumno.adapter.PizzaList;
import com.alumno.objetos.Bebidas;
import com.alumno.objetos.Pedidos;
import com.alumno.objetos.Pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private List<PizzaList> pizzas;
    private List<BebidasList> bebidas;

    private RecyclerView mReclyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mReclyclerView2;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager2;

    private boolean aDomicilio;
    private String nombre;
    private String tlfn;
    private String email;
    private String direccion;
    private TextView textNombre;
    private TextView textTlfn;
    private TextView textEmail;
    private TextView textDireccion;

    private ConstraintLayout layout1;
    private LinearLayout layout2;

    private MyAdapterCesta myAdapterCesta;

    private double precioFinal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Definimos los layouts
        layout1 = findViewById(R.id.Layout1);
        layout2 = findViewById(R.id.Layout2);

        //Recogemos los datos del main activity y los mostramos en los textviews
        textNombre = findViewById(R.id.textViewNombre);
        textEmail = findViewById(R.id.textViewEmail);
        textTlfn = findViewById(R.id.textViewTelefono);
        textDireccion = findViewById(R.id.textViewDireccion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            aDomicilio = extras.getBoolean("aDomicilio");
            nombre = extras.getString("nombre");
            textNombre.setText(nombre);
            tlfn = extras.getString("tlfn");
            textTlfn.setText(tlfn);
            email = extras.getString("email");
            textEmail.setText(email);
            if (aDomicilio) {
                direccion = extras.getString("direccion");
                textDireccion.setText(direccion);
            }
        }

        //Boton abrir cesta
        Button botonCesta = findViewById(R.id.botonCesta);
        botonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            }
        });

        //Boton cerrar cesta
        ImageButton botonCerrarCesta = findViewById(R.id.imageCerrarCesta);
        botonCerrarCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
            }
        });

        //Creamos el tabhost
        TabHost host = findViewById(R.id.tabHost);
        host.setup();

        //Tab1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.tabPizzas));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tabPizzas));
        host.addTab(spec);

        //Tab2
        spec = host.newTabSpec(getResources().getString(R.string.tabBebidas));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.tabBebidas));
        host.addTab(spec);

        //Creamos el array para el listview
        ListView listView = findViewById(R.id.list_view);
        final List<String> names = new ArrayList<String>();
        final List<Double> precios = new ArrayList<Double>();

        myAdapterCesta = new MyAdapterCesta(this, R.layout.list_view_cesta, names, precios);
        listView.setAdapter(myAdapterCesta);

        //Creamos las pizzas y las añadimos al primer reclyclerview
        pizzas = this.getAllPizzas();

        mReclyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        // Array de pizzas
        final List<Pizzas> listPizzas = new ArrayList<Pizzas>();

        mAdapter = new MyAdapterPizzas(pizzas, R.layout.recycler_view_pizzas, new MyAdapterPizzas.OnClickListener() {
            @Override
            public void onClick(PizzaList pizzaList, int position, String masa, String tamaño, String cantidad) {
                // Notificamos que el elemento se ha añadido y añadimos el elemento y su precio a sus respectivos arraylists
                Toast.makeText(SecondActivity.this, getResources().getString(R.string.pizzaAdd) + pizzaList.getNombre(), Toast.LENGTH_SHORT).show();
                Pizzas p = new Pizzas(pizzaList.getNombre(), masa, tamaño, Integer.parseInt(cantidad));
                listPizzas.add(p);
                names.add(p.toString());
                precios.add((double) p.precioTotal());
                myAdapterCesta.notifyDataSetChanged();
            }
        });

        mReclyclerView.setHasFixedSize(true);
        mReclyclerView.setItemAnimator(new DefaultItemAnimator());
        mReclyclerView.setLayoutManager(mLayoutManager);
        mReclyclerView.setAdapter(mAdapter);

        //Lo mismo que con las pizzas hacemos con las bebidas
        bebidas = this.getAllBebidas();

        mReclyclerView2 = findViewById(R.id.recyclerView2);
        mLayoutManager2 = new LinearLayoutManager(this);

        // Array de bebidas
        final List<Bebidas> listBebidas = new ArrayList<Bebidas>();

        mAdapter2 = new MyAdapterBebidas(bebidas, R.layout.recycler_view_bebidas, new MyAdapterBebidas.OnClickListener() {
            @Override
            public void onClick(BebidasList bebidasList, int position, String cantidad) {
                // Notificamos que el elemento se ha añadido y añadimos el elemento y su precio a sus respectivos arraylists
                Toast.makeText(SecondActivity.this, getResources().getString(R.string.bebidaAdd) + bebidasList.getNombre(), Toast.LENGTH_SHORT).show();
                Bebidas b = new Bebidas (bebidasList.getNombre(), Integer.parseInt(cantidad));
                listBebidas.add(b);
                names.add(b.toString());
                precios.add(b.precioTotal());
                myAdapterCesta.notifyDataSetChanged();
            }
        });

        mReclyclerView2.setHasFixedSize(true);
        mReclyclerView2.setItemAnimator(new DefaultItemAnimator());
        mReclyclerView2.setLayoutManager(mLayoutManager2);
        mReclyclerView2.setAdapter(mAdapter2);

        // Boton comprar del listview cesta
        Button btnComprar = findViewById(R.id.buttonComprar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                precioFinal = 0;
                for (int j = 0; j<precios.size(); j++){
                    precioFinal += precios.get(j);
                }
                if (names.size() != 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                    builder.setTitle(getResources().getString(R.string.q2Title));
                    builder.setMessage(getResources().getString(R.string.q2Quest) + "\nPrecio Total: " + precioFinal + "€");
                    builder.setPositiveButton(getResources().getString(R.string.q2Confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Confirma la compra
                            Pedidos pedido = new Pedidos(nombre, tlfn, email, names, precioFinal);
                            if (aDomicilio) {
                                pedido.setDireccion(direccion);
                            }
                            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                            intent.putExtra("resumen", pedido.toString());
                            intent.putExtra("mail", email);
                            intent.putExtra("nombre", pedido.getNombre());
                            intent.putExtra("precioTotal", pedido.getPrecio());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("pizzas", (Serializable) listPizzas);
                            bundle.putSerializable("bebidas", (Serializable) listBebidas);
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.q2Seguir), null);

                    Dialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(SecondActivity.this, getResources().getString(R.string.noItems), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private List<PizzaList> getAllPizzas() {
        return new ArrayList<PizzaList>(){{
            add(new PizzaList(getResources().getString(R.string.pBarbacoa), R.drawable.p_barbacoa));
            add(new PizzaList(getResources().getString(R.string.pCarbonara), R.drawable.p_carbonara));
            add(new PizzaList(getResources().getString(R.string.pQuesos), R.drawable.p_quesos));
            add(new PizzaList(getResources().getString(R.string.pVegetal), R.drawable.p_vegetal));
            add(new PizzaList(getResources().getString(R.string.pTropical), R.drawable.p_tropical));
        }};
    }

    private List<BebidasList> getAllBebidas() {
        return new ArrayList<BebidasList>(){{
            add(new BebidasList(getResources().getString(R.string.pAgua), R.drawable.agua, "1'00€"));
            add(new BebidasList(getResources().getString(R.string.pCola), R.drawable.cocacola, "1'60€"));
            add(new BebidasList(getResources().getString(R.string.pNaranja), R.drawable.fanta_naranja, "1'60€"));
            add(new BebidasList(getResources().getString(R.string.pLimon), R.drawable.fanta_limon, "1'60€"));
            add(new BebidasList(getResources().getString(R.string.pNestea), R.drawable.nestea, "1'50€"));
            add(new BebidasList(getResources().getString(R.string.pRedbull), R.drawable.redbull, "2'00€"));
            add(new BebidasList(getResources().getString(R.string.pCerveza), R.drawable.cerveza, "1'80€"));
        }};
    }
}