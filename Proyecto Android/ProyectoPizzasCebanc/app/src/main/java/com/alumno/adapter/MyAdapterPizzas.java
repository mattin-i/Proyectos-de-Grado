package com.alumno.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alumno.proyectopizzascebanc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mattin on 07/12/2017.
 *
 * Adaptador para el recyclerview de pizzas
 */

public class MyAdapterPizzas extends RecyclerView.Adapter<MyAdapterPizzas.ViewHolder>{

    private List<PizzaList> pizzas;
    private int layout;
    private OnClickListener onClickListener;

    private Context context;

    public MyAdapterPizzas(List<PizzaList> pizzas, int layout, OnClickListener onClickListener){
        this.pizzas = pizzas;
        this.layout = layout;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos el layout y se lo pasamos al constructor del ViewHolder, donde manejaremos toda la logica como extraer los datos, referencias...
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(pizzas.get(position), (OnClickListener) onClickListener);
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageViewPizza;
        public Button btn;
        public Spinner sp1;
        public Spinner sp2;
        public Spinner sp3;

        public ViewHolder(View itemView) {
            //Recibe la view completa. La pasa al constructor padre y enlazamos referencias UI con nuestras propiedades ViewHolder declaradas justo arriba
            super(itemView);
            textViewName = itemView.findViewById(R.id.textNombrePizza);
            imageViewPizza = itemView.findViewById(R.id.imageViewPizza);
            btn = itemView.findViewById(R.id.buttonAdd);
            sp1 = itemView.findViewById(R.id.spinner);
            sp2 = itemView.findViewById(R.id.spinner2);
            sp3 = itemView.findViewById(R.id.spinner3);

            // Rellenamos los spinners con diferentes arrays
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.masas, R.layout.spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
            sp1.setAdapter(adapter);

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.tamaños, R.layout.spinner_item);
            adapter2.setDropDownViewResource(R.layout.spinner_item_dropdown);
            sp2.setAdapter(adapter2);

            ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(context, R.array.cantidad, R.layout.spinner_item);
            adapter3.setDropDownViewResource(R.layout.spinner_item_dropdown);
            sp3.setAdapter(adapter3);
        }

        public void bind(final PizzaList pizza, final OnClickListener listener){
            //Procesamos los datos a renderizar
            textViewName.setText(pizza.getNombre());
            Picasso.with(context).load(pizza.getImagen()).fit().into(imageViewPizza);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(pizza, getAdapterPosition(), sp1.getSelectedItem().toString(), sp2.getSelectedItem().toString(), sp3.getSelectedItem().toString());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los metodo/s a implementar
    public interface OnClickListener{
        void onClick(PizzaList pizzaList, int position, String masa, String tamaño, String cantidad);
    }
}