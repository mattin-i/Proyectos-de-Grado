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
 * Adaptador para el recyclerview de bebidas
 */

public class MyAdapterBebidas extends RecyclerView.Adapter<MyAdapterBebidas.ViewHolder>{

    private List<BebidasList> bebidas;
    private int layout;
    private OnClickListener onClickListener;

    private Context context;

    public MyAdapterBebidas(List<BebidasList> bebidas, int layout, OnClickListener onClickListener){
        this.bebidas = bebidas;
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
        holder.bind(bebidas.get(position), (OnClickListener) onClickListener);
    }

    @Override
    public int getItemCount() {
        return bebidas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageViewBebida;
        public TextView textPrecio;
        public Spinner sp4;
        public Button btn2;

        public ViewHolder(View itemView) {
            //Recibe la view completa. La pasa al constructor padre y enlazamos referencias UI con nuestras propiedades ViewHolder declaradas justo arriba
            super(itemView);
            textViewName = itemView.findViewById(R.id.textNombreBebida);
            imageViewBebida = itemView.findViewById(R.id.imageViewBebida);
            sp4 = itemView.findViewById(R.id.spinner4);
            btn2 = itemView.findViewById(R.id.buttonAdd2);
            textPrecio = itemView.findViewById(R.id.textPrecioBebida);

            // Rellenamos el spinner con un array
            ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(context, R.array.cantidad, R.layout.spinner_item);
            adapter4.setDropDownViewResource(R.layout.spinner_item_dropdown);
            sp4.setAdapter(adapter4);
        }

        public void bind(final BebidasList bebida, final OnClickListener listener){
            //Procesamos los datos a renderizar
            textViewName.setText(bebida.getNombre());
            Picasso.with(context).load(bebida.getImagen()).fit().into(imageViewBebida);
            textPrecio.setText(bebida.getPrecio());
            // Definimos que por cada elemento de nuestra recyclerview, tenemos un click listener que se comporta de la siguiente manera
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pasamos nuetro objeto modelo (este caso String) y posicion
                    listener.onClick(bebida, getAdapterPosition(), sp4.getSelectedItem().toString());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los metodo/s a implementar
    public interface OnClickListener{
        void onClick(BebidasList bebidasList, int position, String cantidad);
    }
}