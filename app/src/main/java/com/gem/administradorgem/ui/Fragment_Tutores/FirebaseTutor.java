package com.gem.administradorgem.ui.Fragment_Tutores;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.ui.Fragment_Alumnos.BottomSheetSettings;
import com.gem.administradorgem.ui.Fragment_Tutores.Adapter.AdapterTutor;
import com.gem.administradorgem.ui.Fragment_Tutores.Adapter.Tutor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseTutor {
    private DatabaseReference reference;
    private AdapterTutor adapter;

    private Activity activity;

    public FirebaseTutor(Activity activity,AdapterTutor adapter) {
        reference = FirebaseDatabase.getInstance("https://registrogem.firebaseio.com/").getReference().child("Registro_Tutores");
        this.activity = activity;
        this.adapter = adapter;
    }

    public void DeleteObject(){
        reference.removeEventListener(listener);
        reference = null;

        listener = null;
    }

    public void getTutores(String nivel, RecyclerView rv){
        adapter.deleteTutor();
        rv.removeAllViews();

        reference.child(nivel).addChildEventListener(listener);
        rv.setVisibility(View.VISIBLE);
    }

    private String matricula;
    public void getTutores(String matricula){
        this.matricula = matricula;

        Query q = reference.child(BottomSheetSettings.nivel).orderByChild("matriculas");
        q.addChildEventListener(aux);
    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            if (snapshot.getValue()!= null){

                if (isQuery(snapshot.getRef().toString())){
                    reference.child(getKey(snapshot.getRef().toString())).addValueEventListener(value);

                }else{
                    Tutor tutor = snapshot.getValue(Tutor.class);
                    tutor.setId(eliminarGuion(snapshot.getKey()));

                    adapter.addTutor(tutor);
                }

                TutoresFragment.lottie.setVisibility(View.INVISIBLE);
                TutoresFragment.lottie.pauseAnimation();

                TutoresFragment.txt.setVisibility(View.INVISIBLE);
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
            Toast.makeText(activity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
    };

    private ChildEventListener aux = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getValue() != null){
                reference.child(snapshot.getKey()).child("matriculas").orderByValue().equalTo(matricula).addChildEventListener(listener);
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

        }
    };

    private ValueEventListener value = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Tutor tutor = snapshot.getValue(Tutor.class);
            tutor.setId(eliminarGuion(snapshot.getKey()));

            adapter.addTutor(tutor);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

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

    private boolean isQuery(String val) {
        return (val.charAt(val.length() - 1) == '0' && val.charAt(val.length()-2) == '/') ? true : false;
    }

    private String getKey(String ref){
        StringBuilder builder = new StringBuilder();

        for(int i = ref.length()-14;i> 0;i--){
            if (ref.charAt(i) =='/')
                break;

            builder.append(ref.charAt(i));
        }
        String key = builder.reverse().toString();
        System.out.println("key:" + key);

        return key;

    }
}
