package com.example.ejercicio_clase_6.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ejercicio_clase_6.R;
import com.example.ejercicio_clase_6.controller.EstudianteController;
import com.example.ejercicio_clase_6.controller.NotaController;
import com.example.ejercicio_clase_6.databinding.ActivityAgregarEstudiantesBinding;
import com.example.ejercicio_clase_6.databinding.ActivityDetallesEstudianteBinding;
import com.example.ejercicio_clase_6.model.Estudiante;
import com.example.ejercicio_clase_6.model.Nota;

import java.util.ArrayList;
import java.util.List;

public class DetallesEstudianteActivity extends AppCompatActivity {
    ActivityDetallesEstudianteBinding binding;
    NotaController notaController = new NotaController(this);
    EstudianteController estudianteController = new EstudianteController(this);

    List<Nota> notas = new ArrayList<>();
    NotaListaAdapter notasAdapter = new NotaListaAdapter(this, notas);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallesEstudianteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ListView listNotas = binding.listaDeNotas;

        listNotas.setAdapter(notasAdapter);



        //Accion ver promedio
        binding.verPromedioBtn.setOnClickListener(v -> {
        String codigoEstudiante = binding.codigoEstudiante.getText().toString().trim();

            if (codigoEstudiante.isEmpty()) {
                //Toast.makeText(this, "Por favor ingresa un código", Toast.LENGTH_SHORT).show();
                binding.codigoEstudiante.setError("Este campo no puede estar vacio!");
                return;
            }

            Estudiante estudiante = estudianteController.obtenerEstudiantePorCodigo(codigoEstudiante);

            if (estudiante == null) {
                binding.codigoEstudiante.setError("Estudiante no encontrado!");
                return;
            }

            // Actualiza las notas del estudiante
            notas.clear();
            notas.addAll(notaController.obtenerNotasPorEstudiante(estudiante.getId()));
            notasAdapter.notifyDataSetChanged();

            // Calcula el promedio con las notas actualizadas
            double promedio = notaController.calcularPromedio(notas);


            // Muestra los datos
            binding.tvNombre.setText(estudiante.getNombre());
            binding.tvPromedio.setText(String.format("%.1f", promedio));

        });

        //Accion Agregar notas
        binding.btnAgregarNotaDetalles.setOnClickListener(view->{
            String codigoEstudiante = binding.codigoEstudiante.getText().toString().trim();
            String nota = String.valueOf(binding.inputAgregarNotaDetalles.getText());
            Estudiante estudiante = estudianteController.obtenerEstudiantePorCodigo(codigoEstudiante);

            if (codigoEstudiante.isEmpty() || nota.isEmpty()) {
//              Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                binding.codigoEstudiante.setError("Este campo no puede estar vacio!");
                binding.inputAgregarNotaDetalles.setError("Este campo no puede estar vacio!");
                return;
            }

            //valida la nota agregada
            double notaAgregada = Double.parseDouble(nota);
            if(notaAgregada < 1 || notaAgregada > 5){
                binding.inputAgregarNotaDetalles.setError("La nota debe ser entre 1 y 5!");
                return;
            }

            //Agrega la nota y actualiza
            notaController.agregarNota(estudiante.getId(), notaAgregada);
            notas.clear();
            notas.addAll(notaController.obtenerNotasPorEstudiante(estudiante.getId()));
            notasAdapter.notifyDataSetChanged();

            // Calcula el promedio con las notas actualizadas
            double promedio = notaController.calcularPromedio(notas);

            Toast.makeText(this, "Nota agregada!", Toast.LENGTH_SHORT).show();
            binding.tvPromedio.setText(String.format("%.1f", promedio));
            binding.inputAgregarNotaDetalles.setText("");
        });

        //Accion para editar o eliminar notas
        binding.listaDeNotas.setOnItemClickListener((parent, view, position, id)->{
            Nota notaSeleccionada = notas.get(position);
            mostrarDialogoOpciones(notaSeleccionada);
        });

        //cambio de actividad
        binding.btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }
    private void mostrarDialogoOpciones(Nota nota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una acción")
                .setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
                    if (which == 0) {
                        // Editar
                        Intent intent = new Intent(this, EditarNotaActivity.class);
                        intent.putExtra("idNota", nota.getId()); // pasar el id de la nota
                        intent.putExtra("notaActual", nota.getValor());
                        startActivity(intent);

                    } else if (which == 1) {
                        // Eliminar
                        mostrarDialogoConfirmacion(nota);
                    }
                })
                .show();
    }
    private void mostrarDialogoConfirmacion(Nota nota) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar esta nota?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    notaController.eliminarNota(nota.getId());
                    notas.remove(nota);
                    notasAdapter.notifyDataSetChanged(); // actualiza la lista
                    Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                    binding.tvNombre.setText("Ingresa el codigo");
                    binding.tvPromedio.setText("Ingresa el codigo");
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}