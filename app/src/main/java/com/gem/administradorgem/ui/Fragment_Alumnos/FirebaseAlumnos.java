package com.gem.administradorgem.ui.Fragment_Alumnos;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.ui.Fragment_Alumnos.Adapter.AdapterAlumnos;
import com.gem.administradorgem.ui.Fragment_Alumnos.Adapter.Alumno;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

class FirebaseAlumnos {
    private DatabaseReference reference;
    private AdapterAlumnos adapter;
    private Context context;

    public FirebaseAlumnos(Context context) {
        reference = FirebaseDatabase.getInstance().getReference().child("Registro_Alumnos");
        this.context = context;
    }

    public void DeleteObject() {
        reference.removeEventListener(listener);
        listener = null;
        reference = null;
    }

    public void getAlumnos(AdapterAlumnos adapter, RecyclerView rv){
        this.adapter = adapter;
        reference.addChildEventListener(listener);

        rv.setVisibility(View.VISIBLE);
    }

    public void getAlumnos(String matricula){
        Query q = reference.orderByChild("matricula").equalTo(matricula);
        q.addChildEventListener(listener);
    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getValue() !=null){

                Alumno alumno = snapshot.getValue(Alumno.class);
                alumno.setId(snapshot.getKey());
                alumno.setId(eliminarGuion(alumno.getId()));

                adapter.addAlumno(alumno);

                AlumnosFragment.lottie.setVisibility(View.INVISIBLE);
                AlumnosFragment.txt.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
    };

    private String eliminarGuion(String id){

        StringBuilder build = new StringBuilder();
        for (int i = 1;i<id.length();i++){
            build.append(id.charAt(i));
        }

        id = build.toString();

        build.delete(0,build.length()-1);
        build = null;

        return id;
    }

}
