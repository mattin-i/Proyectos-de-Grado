package com.alumno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alumno.proyectopizzascebanc.R;

import java.util.List;

/**
 * Created by adminportatil on 10/01/2018.
 *
 * Adaptador para el listview de cesta
 */

public class MyAdapterCesta extends BaseAdapter {

    private Context context;
    private int layout;
    private List<String> item;
    private List<Double> precios;

    public MyAdapterCesta(Context context, int layout, List<String> item, List<Double> precios){
        this.context = context;
        this.layout = layout;
        this.item = item;
        this.precios = precios;
    }

    @Override
    public int getCount() {
        return this.item.size();
    }

    @Override
    public Object getItem(int i) {
        return this.item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //View Holder
        ViewHolder holder;

        if(view == null){
            //Inflamos la vista
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            view = layoutInflater.inflate(R.layout.list_view_cesta, null);

            holder = new ViewHolder();
            //Referenciamos el objeto a modificar y  lo llenamos
            holder.nameTextView = view.findViewById(R.id.textViewCesta);
            holder.btnQuitar = view.findViewById(R.id.buttonQuitar);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Nos traemos el valor de la posicion
        String currentName = item.get(i);

        holder.nameTextView.setText(currentName);

        // Hacemos que el boton elimine el item en cuestion
        holder.btnQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.remove(i);
                precios.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    static class ViewHolder{
        private TextView nameTextView;
        private Button btnQuitar;
    }
}
