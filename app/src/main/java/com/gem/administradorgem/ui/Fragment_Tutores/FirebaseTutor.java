package com.gem.administradorgem.ui.Fragment_Tutores;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.ui.Fragment_Alumnos.AlumnosFragment;
import com.gem.administradorgem.ui.Fragment_Tutores.Adapter.AdapterTutor;
import com.gem.administradorgem.ui.Fragment_Tutores.Adapter.Tutor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseTutor {
    private DatabaseReference reference;
    private AdapterTutor adapter;

    private Activity activity;
    private boolean isQuery;

    private RecyclerView rv;

    public FirebaseTutor(Activity activity, AdapterTutor adapter) {
        reference = FirebaseDatabase.getInstance("https://registrogem.firebaseio.com/").getReference().child("Registro_Tutor");
        this.activity = activity;
        this.adapter = adapter;
    }

    public void DeleteObject() {
        reference.removeEventListener(listener);
        reference = null;

        listener = null;
    }

    public void getTutores(RecyclerView rv) {
        this.isQuery = false;

        adapter.deleteTutor();
        reference.addChildEventListener(listener);

        this.rv = rv;

    }

    private String matricula;

    public void getTutores(String matricula) {
        this.isQuery = true;
        this.matricula = matricula;

        adapter.deleteTutor();
        reference.addChildEventListener(listener);
    }

    private ChildEventListener listener = new ChildEventListener() {

        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getValue()!= null) {

                if (isQuery) { //Pregunta si es una consulta de matricula o es carga por grupos

                    Tutor tutor = snapshot.getValue(Tutor.class); //Obtiene la informacion del tutor
                    for (int i = 0; i < tutor.getHijos().size(); i++) { //Recorre la lista de hijos asociados con el tutor

                        //Compara que al menos uno de los hijos sea igual a la matricula
                        if (tutor.getHijos().get(i).getMatricula().equals(matricula)) {
                            //Si se cumples los valores anteriores entonces si es un tutor perteneciente a ese alumno
                            tutor.setId(snapshot.getKey());
                            adapter.addTutor(tutor);

                            ocultarLottie();
                            break;
                        }
                    }

                } else {
                    Tutor tutor = snapshot.getValue(Tutor.class); //Obtiene la informacion del tutor
                    for (int i = 0; i < tutor.getHijos().size(); i++) { //Recorre la lista de hijos asociados con el tutor

                        //Compara que al menos uno de los hijos sea igual a los valores configurados
                        if ((tutor.getHijos().get(i).getNivel().equals(BottomSheetSettingsTutor.nivel))
                                && (tutor.getHijos().get(i).getGrado().equals(BottomSheetSettingsTutor.grado))
                                && (tutor.getHijos().get(i).getGrupo().equals(BottomSheetSettingsTutor.grupo))) {

                            //Si se cumples los valores anteriores entonces si es un tutor perteneciente a ese grupo
                            tutor.setId(snapshot.getKey());
                            adapter.addTutor(tutor);

                            ocultarLottie();
                            break;
                        }
                    } //Fin Del For
                } //Fin del else isQuery
            }//Fin del if (snapshot.getValue() != null)
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
            Toast.makeText(activity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
    };

//    private String eliminarGuion(String id){
//
//        StringBuilder build = new StringBuilder();
//        for (int i = 1;i<id.length();i++){
//            build.append(id.charAt(i));
//        }
//
//        id = build.toString();
//
//        build.delete(0, build.length() - 1);
//        build = null;
//
//        return id;
//    }

    //Este metodo es mandado a llamar cuando el adapter necesita cargar informacion
    private void ocultarLottie() {

        //Si se esta mostrando entonces lo haces invisible, pero si no, entonces no hace nada
        if (TutoresFragment.lottie.getVisibility() == View.VISIBLE) {

            TutoresFragment.lottie.setVisibility(View.INVISIBLE);
            TutoresFragment.lottie.pauseAnimation();

            TutoresFragment.txt.setVisibility(View.INVISIBLE);

            rv.setVisibility(View.VISIBLE);
        }
    }
}
