package com.gem.administradorgem.Fragment.Chat.BottomSheetAlumno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gem.administradorgem.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAlumno extends BottomSheetDialogFragment {

    private TextView[] datos;
    private String id;

    public BottomSheetAlumno(String id) {
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);

        //Declara el objeto para consultar al alumno
        QueryFirebase firebase = new QueryFirebase(datos, id, getContext());
        firebase.getDatos();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        delete();
    }

    private void initComponents(View v) {
        datos = new TextView[5];
        datos[0] = v.findViewById(R.id.txtNombre);
        datos[1] = v.findViewById(R.id.txtNivel);
        datos[2] = v.findViewById(R.id.txtGrado);
        datos[3] = v.findViewById(R.id.txtGrupo);
        datos[4] = v.findViewById(R.id.txtMatricula);
    }

    private void delete() {
        for (TextView txt : datos) {
            txt.setText("");
        }
        datos = null;
    }
}
