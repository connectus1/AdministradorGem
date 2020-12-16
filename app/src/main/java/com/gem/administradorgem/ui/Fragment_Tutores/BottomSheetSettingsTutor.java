package com.gem.administradorgem.ui.Fragment_Tutores;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gem.administradorgem.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetSettingsTutor extends BottomSheetDialogFragment {

    private Spinner spnGrupo;
    private Spinner spnGrado;

    private CheckBox check[] = new CheckBox[4];
    private String[] arreglo_grado = new String[]{"1", "3", "5"};
    private String[] arreglo_grupos = new String[]{"A", "B", "C"};

    public static int index = 0;
    public static String grupo = "A";
    public static String grado = "1";
    public static String nivel = "Kínder";

    private FirebaseTutor tutor;

    public BottomSheetSettingsTutor(FirebaseTutor tutor) {
        this.tutor = tutor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);

        for (CheckBox checkBox : check) //Todos los check les asigna un listener
            checkBox.setOnCheckedChangeListener(listener);

        //Infla los datos con los default
        spnGrado.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, arreglo_grado));
        spnGrupo.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, arreglo_grupos));

        //El listener que detecta cuando cambia el spinner grado
        spnGrado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (CheckBox checkBox : check) {
                    if (checkBox.isChecked() && !checkBox.getText().toString().equals(nivel)) {
                        if (spnGrado.getSelectedItem().toString().equals("5") && (index == 3)) {
                            arreglo_grupos = new String[]{"A", "B", "C", "D"};
                        }
                        spnGrupo.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, arreglo_grupos));
                        break;
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        fillData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        grupo = spnGrupo.getSelectedItem().toString();
        grado = spnGrado.getSelectedItem().toString();
        nivel = check[index].getText().toString();

        tutor.getTutores(TutoresFragment.rvTutor);
    }

    private void initComponents(View v) {
        check[0] = v.findViewById(R.id.chKinder);
        check[1] = v.findViewById(R.id.chPrimaria);
        check[2] = v.findViewById(R.id.chSecundaria);
        check[3] = v.findViewById(R.id.chPreparatoria);

        spnGrado = v.findViewById(R.id.spnGrado);
        spnGrupo = v.findViewById(R.id.spnGrupo);
    }

    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            for (int i = 0; i < check.length && b; i++) {
                if (!check[i].getText().equals(compoundButton.getText().toString())) {
                    check[i].setChecked(false);
                } else {
                    index = i;
                }
            }

            if ((b) && (!check[index].getText().toString().equals(nivel))) {
                Log.e("Entre", "checkbox");

                switch (compoundButton.getText().toString()) {
                    case "Kínder":
                    case "Primaria":
                    case "Secundaria":
                    case "Preparatoria":
                        arreglo_grado = new String[]{"1", "3", "5"};
                        break;
                }
                spnGrado.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, arreglo_grado));
            }


        }
    };

    private void fillData() {//Rellena los datos si ya ha existido una busqueda anterior
        check[index].setChecked(true);

        if (grado.equals("5")) {
            arreglo_grupos = new String[]{"A", "B", "C", "D"};
            spnGrupo.setAdapter(new ArrayAdapter<>(getContext(), R.layout.personalizar_spinner, arreglo_grupos));
        }


        for (int i = 0; i < arreglo_grupos.length; i++) {
            if (grupo.equals(arreglo_grupos[i])) {
                spnGrupo.setSelection(i);
            }
        }

        for (int i = 0; i < arreglo_grado.length; i++) {
            if (grado.equals(arreglo_grado[i])) {
                spnGrado.setSelection(i);
            }
        }


    }
}
