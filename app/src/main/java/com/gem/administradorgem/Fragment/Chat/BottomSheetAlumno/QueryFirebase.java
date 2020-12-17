package com.gem.administradorgem.Fragment.Chat.BottomSheetAlumno;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gem.administradorgem.ui.Fragment_Alumnos.Adapter.Alumno;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class QueryFirebase {
    private DatabaseReference ref;
    private TextView[] datos;

    private String[] registros = new String[]{"Registro_Kínder", "Registro_Primaria",
            "Registro_Secundaria", "Registro_Preparatoria"};

    private int cont;
    private String matricula;
    private Context context;

    public QueryFirebase(TextView[] datos, String matricula, Context context) {
        this.ref = FirebaseDatabase.getInstance("https://registrogem.firebaseio.com/").getReference();
        this.datos = datos;
        this.matricula = matricula;
        this.context = context;
    }

    public void getDatos() {
        ref.child(registros[cont]).child(matricula).addValueEventListener(listener);
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.getValue() != null) {
                Alumno alumno = snapshot.getValue(Alumno.class);
                datos[0].setText(alumno.getNombre());
                datos[1].setText(alumno.getNivel());
                datos[2].setText(alumno.getGrado());
                datos[3].setText(alumno.getGrupo());
                datos[4].setText(alumno.getMatricula());

                ref.removeEventListener(listener);
            } else {
                if (++cont != registros.length) {
                    getDatos();
                } else
                    Toast.makeText(context, "Datos No Encontrados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}
