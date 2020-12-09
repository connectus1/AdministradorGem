package com.gem.administradorgem.Fragment.Noticias.CargarNoticias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.gem.administradorgem.Fragment.Noticias.ItemNoticia;
import com.gem.administradorgem.Fragment.Noticias.SubirNoticias.SubirNoticia;
import com.gem.administradorgem.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentNoticias extends Fragment {

    private RecyclerView rvNoticias;
    private NoticiaAdapter noticiaAdapter;
    private LinearLayout lnNoticias;
    private LottieAnimationView lottie;

    ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            addNoticia(snapshot.getValue(ItemNoticia.class));

            if (lottie.isEnabled()) {
                desactivarLottie();
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
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noticias, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponentes(view);
        initFirebase();
        setRvNoticias();

        lnNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SubirNoticia.class);
                noticiaAdapter.delete();

                startActivity(i);
            }
        });

    }
    private String url[] = new String[]{
            /*Mante*/"https://conalep360-mante.firebaseio.com/",
            /*Matamoros*/"https://conalep360-matamoros.firebaseio.com/",
            /*Miguel Aleman*/"https://conalep360-miguelaleman.firebaseio.com/",
            /*Nuevo Laredo*/"https://conalep360-nuevolaredo.firebaseio.com/",
            /*Reynosa*/"https://conalep360-reynosa.firebaseio.com/",
            /*Rio Bravo*/"https://conalep360-riobravo.firebaseio.com/",
            /*Tampico*/"https://conalep360-tampico.firebaseio.com/",
            /*Victoria*/"https://conalep360-victoria.firebaseio.com/",
            /*Cast*/"https://conalep360-cast.firebaseio.com/"
    };

    private void initFirebase() {

        reference = FirebaseDatabase.getInstance(url[getActivity()
                .getSharedPreferences("ADMIN", Context.MODE_PRIVATE).getInt("index",0)])
                .getReference("noticias");
        reference.addChildEventListener(listener);
    }

    private void initComponentes(View view){
        /*Componentes para las noticias*/
        rvNoticias = view.findViewById(R.id.rvNoticias);
        noticiaAdapter = new NoticiaAdapter(getActivity());

        lnNoticias = view.findViewById(R.id.lnNoticias);

        lottie = view.findViewById(R.id.lottieNoticia);
    }

    private void setRvNoticias(){
        GridLayoutManager l = new GridLayoutManager(getContext(),1);
        rvNoticias.setLayoutManager(l);

        DividerItemDecoration separador = new DividerItemDecoration(getContext(),l.getOrientation());
        rvNoticias.addItemDecoration(separador);

        rvNoticias.setAdapter(noticiaAdapter);
    }


    private void desactivarLottie(){
        lottie.setEnabled(false);
        lottie.setVisibility(View.INVISIBLE);
        lottie.loop(false);
    }

    private void addNoticia(ItemNoticia noticia){
        noticiaAdapter.addNoticia(noticia);
    }

    @Override
    public void onPause() {
        reference.removeEventListener(listener);
        reference = null;

        super.onPause();
    }

    @Override
    public void onStop() {
        if (reference != null){
            reference.removeEventListener(listener);
            reference = null;
        }

        super.onStop();
    }

    @Override
    public void onResume() {
        if (reference == null){
            initFirebase();
        }
        super.onResume();
    }
}