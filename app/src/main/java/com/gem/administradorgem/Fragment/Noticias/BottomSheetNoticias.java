package com.gem.administradorgem.Fragment.Noticias;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gem.administradorgem.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetNoticias extends BottomSheetDialogFragment {

    private Spinner spnNivel;
    private Spinner spnGrupo;
    private Spinner spnGrado;

    private String[] niveles;
    private String[] grupos;
    private String[] grados;

    private Button btnNoticia;

    private ItemNoticia noticia;
    private Uri uri;
    private FirebaseNoticia firebase;

    public static String grado;
    public static String nivel;
    public static String grupo;

    public BottomSheetNoticias(ItemNoticia noticia, Uri uri, FirebaseNoticia firebase) {
        this.noticia = noticia;
        this.uri = uri;
        this.firebase = firebase;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_noticias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);

        spnNivel.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, niveles));
        spnGrado.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, grados));
        spnGrupo.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, grupos));

        //********************************************
        //Al presionar el spinner verifica que sea de preparatoria y de grado 5 para anexar
        //los grupoas A,B,C,D
        //********************************************
        spnGrado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((spnNivel.getSelectedItem().toString().equals("Preparatoria")) &&
                        (spnGrado.getSelectedItem().toString().equals("5"))) {
                    grupos = new String[]{"A", "B", "C", "D"};
                    spnGrupo.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, grupos));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnNoticia.setOnClickListener(view2 -> {
            nivel = spnNivel.getSelectedItem().toString();
            grupo = spnGrupo.getSelectedItem().toString();
            grado = spnGrado.getSelectedItem().toString();

            firebase.subirNoticia(noticia, uri);
        });
    }

    //********************************
    //Metodo que inicializa los componentes
    //*************************************
    private void initComponents(View v) {
        spnNivel = v.findViewById(R.id.spnNivel);
        spnGrupo = v.findViewById(R.id.spnGrupo);
        spnGrado = v.findViewById(R.id.spnGrado);

        btnNoticia = v.findViewById(R.id.btnNoticia);

        niveles = new String[]{"Kínder", "Primaria", "Secundaria", "Preparatoria"};
        grupos = new String[]{"A", "B", "C"};
        grados = new String[]{"1", "3", "5"};
    }

    //********************************
    //Metodo que destruye las referencias de los objetos
    //***********************************
    private void destroy() {

        spnNivel.setAdapter(null);
        spnGrupo.setAdapter(null);
        spnGrado.setAdapter(null);

        niveles = null;
        grupos = null;
        grados = null;

//        firebase.destroy();
    }

    //********************************
    //Cuando se elimina la vista del BottomSheet
    //***********************************
    @Override
    public void onDetach() {
        super.onDetach();
        destroy();
    }
}
