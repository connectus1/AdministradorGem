package com.gem.administradorgem.Fragment.Chat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.Fragment.Chat.Adapter.ChatAdapter;
import com.gem.administradorgem.Fragment.Chat.Adapter.itemChat;
import com.gem.administradorgem.Fragment.Chat.DatabaseSQL.DatabaseSql;
import com.gem.administradorgem.Fragment.Chat.FirebaseChat.FirebaseMessage;
import com.gem.administradorgem.Internet;
import com.gem.administradorgem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class ChatGEM extends AppCompatActivity {


    private EditText edtxtMensaje;
    private FloatingActionButton btnEnviar;

    private RecyclerView rvChat;
    private ChatAdapter adapter;

    private boolean banderIndex;
    private CharSequence matricula;

    private FirebaseMessage firebase;
    private DatabaseSql sql;

    private RecyclerView.OnChildAttachStateChangeListener addChild = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (rvChat.getChildAdapterPosition(view) == 0 && banderIndex) {
                rvChat.post(() -> sql.obtenerDatos(adapter, rvChat, false));
            } else {
                banderIndex = true;
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Establece la viste vertical
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary)); //Establece el color de la barra superior


        /*Obtiene la matricula seleccionada en el fragment usuarios*/
        matricula = obtenerMatricula(getIntent().getExtras());

        /*Inicializa los componentes*/
        initComponents();
        setAdapter();
        firebase.getMensaje(adapter);

        //*************************************************
        //Al presionar el boton sube el mensaje a la base de datos y borra el editText
        //************************************************
        btnEnviar.setOnClickListener(v -> {
            itemChat message = generarMensaje();
            if (message != null) {
                firebase.enviarMensaje(message);
                sql.registrarMensaje(message);

                adapter.addMensaje(message);
                /*limpiamos la caja de texto*/
                edtxtMensaje.setText("");

            } //Fin del if(message!=null)
        });


        //***************************************************
        //Manda a llamar el metodo obtener de datos que desplegara en pantalla los mensajes
        //guardado con anterioridad
        //*********************************************************
        sql.obtenerDatos(adapter, rvChat, true);


        //***********************************************
        // Cada que es registrado un nuevo elemento en el adapter manda a llamar
        //el setScrollbar
        //*************************************************
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });


        //***************************************
        //Esta parte se encarga de detectar cuando los elementos se añaden o desaparecen
        //cuando los elementos se añaden se obtiene la posicion del view y al ser igual a 0
        //y si es la primera vez que pasa (cuando carga el activity) no entra hasta el segundo intento
        //y crea un runnable para cargar los datos sin crashear la apliacion
        //******************************
        rvChat.addOnChildAttachStateChangeListener(addChild);


    }

    //***************************
    //Incializa el recyvler view y el adapter
    //***********************************
    private void setAdapter() {
        adapter = new ChatAdapter(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvChat.setLayoutManager(l);
        rvChat.setAdapter(adapter);
    }

    //*********************************
    //Inicializa los componentes
    //***********************************
    private void initComponents() {
        edtxtMensaje = findViewById(R.id.edtxtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        rvChat = findViewById(R.id.rvChat);

        firebase = new FirebaseMessage(matricula.toString(), ChatGEM.this);
        sql = new DatabaseSql(matricula.toString(), ChatGEM.this);
    }

    //**********************
    //Metodo para eliminar las referencias de los componentes
    //y liberar memoria
    //**********************
    private void deletComponents() {

        edtxtMensaje.setText("");
        edtxtMensaje = null;

        btnEnviar.setOnClickListener(null);
        btnEnviar = null;

        rvChat.removeOnChildAttachStateChangeListener(addChild);
        rvChat.setAdapter(null);
        rvChat = null;

        adapter = null;

        firebase.destroy();
        sql.Destroy();

    }


    private itemChat generarMensaje() {
        /*Declaramos un objeto de tipo itemChat que son los mensajes*/
        itemChat message;

        if ((!TextUtils.isEmpty(edtxtMensaje.getText())) && (Internet.isOnline(getBaseContext()))) {
            if (Internet.isOnline(getBaseContext())) {

                String texto = revisarMensaje(edtxtMensaje.getText().toString());
                if (!texto.equals("")) {
                    message = new itemChat();
                    /*Rellenamos los campos del objeto declarado*/
                    message.setMensaje(texto);
                    message.setFecha(obtenerFecha().toString());
                    message.setNombre("GEM");

                    return message;
                }
            }
        }

        return null;
    }

    private CharSequence obtenerMatricula(Bundle datos) {
        CharSequence matricula = null;
        try {
            matricula = datos.getString("matricula");
        } catch (NullPointerException e) {
            Toast.makeText(this, "Ha ocurrido un error al cargar", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        return matricula;
    }

    private CharSequence obtenerFecha() {
        Date d = new Date();
        return DateFormat.format("MMMM d, yyyy ", d.getTime());
    }


    @Override
    protected void onDestroy() {
        deletComponents();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //******************
    //El metodo se encarga de posicionar el scroll en el ultimo mensaje
    //******************
    private void setScrollbar() {
        rvChat.scrollToPosition(adapter.getItemCount() - 1);
    }

    //*****************************************************
    //Este metodo revisa que el mensaje no tenga saltos de linea vacios
    //o espacios en blanco
    //*****************************************************
    private String revisarMensaje(String mensaje) {
        char[] array = mensaje.toCharArray();
        StringBuilder newMensaje = new StringBuilder();
        int ultimoCaracter = -1;

        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] > 32 && array[i] <= 254) {
                ultimoCaracter = i;
                break;
            }
        }

        for (int i = 0; (ultimoCaracter != -1) && i <= ultimoCaracter; i++) {
            newMensaje.append(array[i]);
        }
        return newMensaje.toString();
    }

}