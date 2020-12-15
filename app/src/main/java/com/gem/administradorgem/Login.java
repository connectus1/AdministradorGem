package com.gem.administradorgem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private Button button;
    private byte index;
    private Spinner spn;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        iniciarComponenetes();
        if (preferences.getBoolean("login",false)){
            Siguiente();
        }

        //Se Agrega un listener para el onItem en el spinner
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = (byte) position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(v -> {
            guardarPlantel(index);
            Siguiente();
        });
    }

    @Override
    protected void onDestroy() {
        preferences= null;

        spn.setOnItemSelectedListener(null);
        button.setOnClickListener(null);

        button = null;
        spn = null;

        super.onDestroy();
    }

    //*****************************************
    //Metodo que inicializa los componentes
    //***************************************
    private void iniciarComponenetes() {
        button = findViewById(R.id.btnIniciar);
        spn = findViewById(R.id.spnEscuela);

        preferences = getSharedPreferences("ADMINGEM360", Context.MODE_PRIVATE);
    }

    //*****************************************
    //Intent que nos manda a la actividad Main
    //***************************************
    public void Siguiente() {
        Intent proximo = new Intent(this, Main.class);
        startActivity(proximo);
        finish();
    }

    //*********************************************
    //Metodo para guardar el index del plantel seleccionado en las preferences
    //del dispositivo movil
    //*********************************************
    private void guardarPlantel(byte index) {
        preferences.edit().putBoolean("login", true).apply();
        preferences.edit().putInt("index", index).apply();
    }

}