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
 * Created by adminportatil on 13/02/2018.
 */

public class MyAdapterConsultas extends BaseAdapter {

    private Context context;
    private int layout;
    private List<String> consulta1;
    private List<String> consulta2;

    public MyAdapterConsultas(Context context, int layout, List<String> consulta1, List<String> consulta2) {
        this.context = context;
        this.layout = layout;
        this.consulta1 = consulta1;
        this.consulta2 = consulta2;
    }

    @Override
    public int getCount() {
        return consulta1.size();
    }

    @Override
    public Object getItem(int i) {
        return consulta1.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyAdapterConsultas.ViewHolder holder;

        if(view == null){
            //Inflamos la vista
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            view = layoutInflater.inflate(R.layout.list_view_consultas, null);

            holder = new MyAdapterConsultas.ViewHolder();
            //Referenciamos el objeto a modificar y  lo llenamos
            holder.consulta1TV = view.findViewById(R.id.textConsulta1);
            holder.consulta2TV = view.findViewById(R.id.textConsulta2);
            view.setTag(holder);
        } else {
            holder = (MyAdapterConsultas.ViewHolder) view.getTag();
        }

        //Nos traemos el valor de la posicion
        String currentConsulta1 = consulta1.get(i);
        holder.consulta1TV.setText(currentConsulta1);

        String currentConsulta2 = consulta2.get(i);
        holder.consulta2TV.setText(currentConsulta2);

        return view;
    }

    static class ViewHolder {
        private TextView consulta1TV;
        private TextView consulta2TV;
    }
}
