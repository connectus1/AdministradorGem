package com.gem.administradorgem.ui.Fragment_Tutores;

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
import com.gem.administradorgem.ui.Fragment_Alumnos.BottomSheetSettings;
import com.gem.administradorgem.ui.Fragment_Tutores.Adapter.AdapterTutor;

public class TutoresFragment extends Fragment {

    public static RecyclerView rvTutor;
    private AdapterTutor adapter;

    private FirebaseTutor firebase;

    static LottieAnimationView lottie;
    static TextView txt;

    private ImageView btnBuscar;
    private EditText txtMatricula;

    private ImageView imgSettings;
    private BottomSheetSettings settings;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
        setRvTutor();

        firebase.getTutores(BottomSheetSettings.nivel,rvTutor);
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
        rvTutor = v.findViewById(R.id.rvTutores);
        adapter = new AdapterTutor(getActivity());

        lottie = v.findViewById(R.id.lottieTutores);
        txt = v.findViewById(R.id.txtTutores);

        firebase = new FirebaseTutor(getActivity(),adapter);

        btnBuscar = v.findViewById(R.id.btnBuscar);
        txtMatricula = v.findViewById(R.id.edtxtBuscar);

        imgSettings = v.findViewById(R.id.imgSettings);

    }

    private void deleteComponents(){
        if (firebase!= null){
            firebase.DeleteObject();
            firebase = null;
        }

        if (rvTutor!= null){
            rvTutor.removeAllViews();
            rvTutor = null;
            adapter = null;
        }
    }

    private void setRvTutor(){
        GridLayoutManager glm = new GridLayoutManager(getContext(),1);
        rvTutor.setLayoutManager(glm);

        rvTutor.setAdapter(adapter);
    }

    TextWatcher changeText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i == 0){
                adapter.deleteTutor();
                firebase.getTutores(BottomSheetSettings.nivel,rvTutor);
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
            adapter.deleteTutor();
            firebase.getTutores(txtMatricula.getText().toString());
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
    public void onDestroyView() {
        super.onDestroyView();
        deleteComponents();
    }
}