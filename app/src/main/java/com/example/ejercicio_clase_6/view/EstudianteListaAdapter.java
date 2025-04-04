package com.example.ejercicio_clase_6.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ejercicio_clase_6.R;
import com.example.ejercicio_clase_6.model.Estudiante;

import java.util.List;

public class EstudianteListaAdapter extends BaseAdapter {
    private List<Estudiante> estudiantes;
    private Context context;

    public EstudianteListaAdapter(List<Estudiante> estudiantes, Context context) {
        this.estudiantes = estudiantes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return estudiantes.size();
    }

    @Override
    public Object getItem(int i) {
        return estudiantes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_estudiante, viewGroup, false);
        }

        // Obtener el estudiante de la lista
        Estudiante estudiante = estudiantes.get(i);

        // Referenciar elementos del XML
        TextView nombre = convertView.findViewById(R.id.nombreEstudiante);
        TextView codigo = convertView.findViewById(R.id.codigoEstudiante);

        // Asignar datos
        nombre.setText(estudiante.getNombre());
        codigo.setText(estudiante.getCodigo());

        return convertView;
    }
}
