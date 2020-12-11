package com.gem.administradorgem.ui.Fragment_Alumnos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.gem.administradorgem.R;
import com.gem.administradorgem.ui.Fragment_Alumnos.Adapter.AdapterAlumnos;

public class AlumnosFragment extends Fragment {

    public static RecyclerView rv_alumnos;
    private AdapterAlumnos adapter;

    private FirebaseAlumnos firebase;

    static LottieAnimationView lottie;
    static TextView txt;

    private ImageView btnBuscar;
    private EditText txtMatricula;

    private ImageView imgSettings;
    private BottomSheetSettings settings;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alumnos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
        setRv_alumnos();

        firebase.getAlumnos(BottomSheetSettings.nivel,rv_alumnos);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnBuscar();
            }
        });

        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings = new BottomSheetSettings(firebase);
                settings.show(getParentFragmentManager(),"settings");
            }
        });

    }

    private void initComponents(View v){
        rv_alumnos = v.findViewById(R.id.rvAlumnos);
        adapter = new AdapterAlumnos(getActivity());

        firebase = new FirebaseAlumnos(getContext(),adapter);

        lottie= v.findViewById(R.id.lottieAlumnos);
        txt = v.findViewById(R.id.txtAlumnos);

        btnBuscar = v.findViewById(R.id.btnBuscar);
        txtMatricula = v.findViewById(R.id.edtxtBuscar);
        imgSettings = v.findViewById(R.id.imgSettings);
    }

    private void deleteComponents(){
        if (rv_alumnos!=null){
            rv_alumnos.removeAllViews();
            rv_alumnos = null;
        }

        if (firebase!= null) {
            firebase.DeleteObject();
            firebase = null;
        }

    }

    private void setRv_alumnos(){
        GridLayoutManager glm = new GridLayoutManager(getActivity(),1);
        rv_alumnos.setLayoutManager(glm);

        rv_alumnos.setAdapter(adapter);
    }


    TextWatcher changeText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if ((i == 0)){ //Esto se ejecuta cuando el administrador borra toda la matricula
                adapter.deleteAlumno();
                firebase.getAlumnos(BottomSheetSettings.nivel,rv_alumnos);
                removeChangeText();
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void setBtnBuscar(){
        if (!TextUtils.isEmpty(txtMatricula.getText())){
            adapter.deleteAlumno();
            firebase.getAlumnos(txtMatricula.getText().toString());
        }

       txtMatricula.addTextChangedListener(changeText);

    }

    private void removeChangeText(){
        txtMatricula.removeTextChangedListener(changeText);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        deleteComponents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteComponents();

    }
}