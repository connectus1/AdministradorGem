package com.gem.administradorgem.Fragment.CargarUsuarios;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.gem.administradorgem.Fragment.CargarUsuarios.Adapter.AdapterMensajes;
import com.gem.administradorgem.R;


public class FragmentUsuarios extends Fragment {

    private RecyclerView rvUsuarios;
    private AdapterMensajes adapter;
    private FirebaseMensaje firebase;

    static LottieAnimationView lottie;
    static TextView txtChat;

    private SharedPreferences preferences;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComponents(view);
        setRecycler();

        firebase.getMensajes(adapter,rvUsuarios);
    }

    private void initComponents(View view) {
        rvUsuarios = view.findViewById(R.id.rvUsuarios);
        adapter = new AdapterMensajes(getActivity());

        lottie = view.findViewById(R.id.lottieChat);
        txtChat = view.findViewById(R.id.txtChat);

        preferences = getActivity().getSharedPreferences("ADMIN", Context.MODE_PRIVATE);
        firebase = new FirebaseMensaje(getActivity(),url[preferences.getInt("index",7)]);

    }

    private void deleteComponents(){
        if (firebase!= null){
            firebase.DeleteObject();
        }

        if (rvUsuarios!=null){
            rvUsuarios.removeAllViews();
            rvUsuarios = null;

            adapter = null;
        }

    }

    private void setRecycler() {
        LinearLayoutManager l = new LinearLayoutManager(getContext());

        rvUsuarios.setLayoutManager(l);
        rvUsuarios.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deleteComponents();
    }
}