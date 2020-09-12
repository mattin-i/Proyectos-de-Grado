package com.alumno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alumno.proyectopizzascebanc.R;

import java.util.List;

/**
 * Created by Mattin on 10/02/2018.
 */

public class MyAdapterPedidos extends BaseAdapter {

    private Context context;
    private int layout;
    private List<String> codigo;
    private List<String> fecha;
    private List<String> precio;

    public MyAdapterPedidos(Context context, int layout, List<String> codigo, List<String> fecha, List<String> precio){
        this.context = context;
        this.layout = layout;
        this.codigo = codigo;
        this.fecha = fecha;
        this.precio = precio;
    }

    @Override
    public int getCount() {
        return codigo.size();
    }

    @Override
    public Object getItem(int i) {
        return codigo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            //Inflamos la vista
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            view = layoutInflater.inflate(R.layout.list_view_pedidos, null);

            holder = new MyAdapterPedidos.ViewHolder();
            //Referenciamos el objeto a modificar y  lo llenamos
            holder.codigoTextView = view.findViewById(R.id.textViewCodigo);
            holder.fechaTextView = view.findViewById(R.id.textViewFecha);
            holder.precioTextView = view.findViewById(R.id.textViewPrecio);
            view.setTag(holder);
        } else {
            holder = (MyAdapterPedidos.ViewHolder) view.getTag();
        }

        //Nos traemos el valor de la posicion
        String currentCodigo = "Código pedido: " + codigo.get(i);
        holder.codigoTextView.setText(currentCodigo);

        String currentFecha = "Fecha pedido: " + fecha.get(i);
        holder.fechaTextView.setText(currentFecha);

        String currentPrecio = "Precio pedido: " + precio.get(i) + " €";
        holder.precioTextView.setText(currentPrecio);

        return view;
    }

    static class ViewHolder{
        private TextView codigoTextView;
        private TextView fechaTextView;
        private TextView precioTextView;
    }
}
