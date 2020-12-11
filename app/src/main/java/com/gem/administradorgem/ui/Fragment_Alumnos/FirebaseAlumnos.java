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

    public FirebaseAlumnos(Context context,AdapterAlumnos adapter) {
        reference = FirebaseDatabase.getInstance("https://registrogem.firebaseio.com/").getReference();
        this.adapter = adapter;
        this.context = context;
    }

    public void DeleteObject() {
        reference.removeEventListener(listener);
        listener = null;
        reference = null;
    }

    public void getAlumnos(String matricula){
        Query q = reference.child("Registro_"+ BottomSheetSettings.nivel).orderByChild("matricula").equalTo(matricula);
        q.addChildEventListener(listener);
    }

    public void getAlumnos(String nivel,RecyclerView rv){
        adapter.deleteAlumno();
        rv.removeAllViews();

        reference.child("Registro_" + nivel).addChildEventListener(listener);
        rv.setVisibility(View.VISIBLE);

    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getValue() !=null){

                Alumno alumno = snapshot.getValue(Alumno.class);
                if( (alumno.getGrupo().equals(BottomSheetSettings.grupo) ) &&
                        (alumno.getGrado().equals(BottomSheetSettings.grado)) ){

                    alumno.setId(snapshot.getKey());
                    alumno.setId(eliminarGuion(alumno.getId()));


                    adapter.addAlumno(alumno);
                }

                if (AlumnosFragment.lottie.getVisibility() == View.VISIBLE) {
                    AlumnosFragment.lottie.setVisibility(View.INVISIBLE);
                    AlumnosFragment.txt.setVisibility(View.INVISIBLE);
                }

                if (adapter.getItemCount() == 0){
                    AlumnosFragment.lottie.setVisibility(View.VISIBLE);
                    AlumnosFragment.txt.setVisibility(View.VISIBLE);
                }




            }else {
                AlumnosFragment.lottie.setVisibility(View.VISIBLE);
                AlumnosFragment.txt.setVisibility(View.VISIBLE);
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
