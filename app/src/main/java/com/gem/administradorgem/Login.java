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


        /******************SPINNER**********************/

        /*Se agregan las instituciones a un arreglo que despues es colocado
         * en un adaptaer al spinner que al mismo tiempo usa un estilo layout*/
//        String[] planteles =getResources().getStringArray(R.array.arrayPlantel);// {"Cd. Mante", "Cd. Victoria", "Matamoros", "Miguel Aleman", "Nuevo Laredo", "Rio Bravo", "Reynosa", "Tampico", "Cast Matamoros"};
//        spn.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.personalizar_spinner, planteles));

        /*Las imagenes del arreglo de int se asignan en el hoyente al spinner que captura
         * cada que cambia el item del spinner y esa misma posicion se asgina al index*/

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = (byte) position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /************Fin del spinner*******************/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPlantel(index);

                Siguiente();

            }
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

    private void iniciarComponenetes() {
        button = (Button) findViewById(R.id.btnIniciar);
        spn = (Spinner) findViewById(R.id.spnEscuela);

        preferences = getSharedPreferences("ADMINGEM360",Context.MODE_PRIVATE);
    }

    //INTENTO DE PASAR A LA ACTIVITY MAIN//
    public void Siguiente() {
        Intent proximo = new Intent(this, Main.class);
        startActivity(proximo);
        finish();
    }

    //Metodo para guardar el index del plantel seleccionado en las preferences
    // del dispositivo movil
    private void guardarPlantel(byte index) {
        preferences.edit().putBoolean("login", true).commit();
        preferences.edit().putInt("index", index).commit();
    }

}