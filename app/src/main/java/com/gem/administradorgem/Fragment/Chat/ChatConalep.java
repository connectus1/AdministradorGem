package com.gem.administradorgem.Fragment.Chat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.Internet;
import com.gem.administradorgem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatConalep extends AppCompatActivity {

    /*Componentes del chat*/
    private EditText edtxtMensaje;
    private FloatingActionButton btnEnviar;
    private RecyclerView rvChat;

    private ChatAdapter adapter;
    private DatabaseReference databaseReference;
//    private String[] plantel;


    private SQLiteDatabase bd;
    private Cursor indexBD;

    /*Esta lista se encarga de guardar las key de los mensajes a eliminar
     * o sea los mensajes de la institucion que ya fueron registrado en la base de datos
     * del telefono*/
    private ArrayList<String> keyEliminar;
    private int indexLastBD;
    private boolean banderIndex;
    private CharSequence matricula;
    private List<itemChat> mensajes;

    private ChildEventListener mensajesListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            itemChat mensaje = dataSnapshot.getValue(itemChat.class);

            if (mensaje.isId()) {

                adapter.addMensaje(mensaje);
                registrarMensaje(mensaje);
                keyEliminar.add(dataSnapshot.getKey());
            }


        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    RecyclerView.OnChildAttachStateChangeListener addChild = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (rvChat.getChildAdapterPosition(view) == 0 && banderIndex) {
                rvChat.post(new Runnable() {
                    @Override
                    public void run() {
                        obtenerDatos(false);
                    }
                });
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
        getWindow().setStatusBarColor(Color.parseColor("#005747")); //Establece el color de la barra superior


//        plantel = getResources().getStringArray(R.array.arrayPlantel); //Obtiene los plantel de los String

        /*Obtiene la matricula seleccionada en el fragment usuarios*/
        Bundle extras = getIntent().getExtras();
        matricula = obtenerMatricula(extras);

        /*Obtiene la fecha del dispositivo*/
        final CharSequence fecha = obtenerFecha();

        /*Inicializa los componentes*/
        initComponents();
        initFirebase(matricula);

        setAdapter();

        /*Al presionar el boton sube el mensaje a la base de datos y borra el editText*/
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtxtMensaje.getText())) {

                    if (Internet.isOnline(getBaseContext())) {
                        /*Declaramos un objeto de tipo itemChat que son los mensajes*/
                        itemChat message = new itemChat();

                        if (!TextUtils.isEmpty(edtxtMensaje.getText())) {
                            String mensaje = revisarMensaje(edtxtMensaje.getText().toString());

                            if (!mensaje.equals("")) {
                                /*Rellenamos los campos del objeto declarado*/
                                message.setMensaje(mensaje);
                                message.setFecha((String) DateFormat.format("MMMM d, yyyy ", new Date()));
                                message.setId(false);
                                message.setNombre("CONALEP");
//                                message.setNombre(getSharedPreferences("CONALEP360", Context.MODE_PRIVATE).getString("alumno", "visitante"));

                                /*Registramos en firebase asi com en la base de datos del telefono*/
                                databaseReference.push().setValue(message);
                                registrarMensaje(message);
                                adapter.addMensaje(message);
                                /*limpiamos la caja de texto*/
                                edtxtMensaje.setText("");


                            }
                        }
                    } else {
                        Internet.dialogoNoIternet(ChatConalep.this);
                    }
                }
            }
        });
        /*Fin boton*/


//      Manda a llamar el metodo obtener de datos que desplegara en pantalla los mensajes
//      guardado con anterioridad
        try {
            obtenerDatos(true);
        } catch (SQLException e) {
            Log.e("ObtenerDatos()", e.toString());
        }



        /*Cada que es registrado un nuevo elemento en el adapter manda a llamar
         * el setScrollbar*/
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });
        /*Fin del registro de apadater*/


        //      *****************
//      Esta parte se encarga de detectar cuando los elementos se añaden o desaparecen
//      cuando los elementos se añaden se obtiene la posicion del view y al ser igual a 0
//      y si es la primera vez que pasa (cuando carga el activity) no entra hasta el segundo intento
//      y crea un runnable para cargar los datos sin crashear la apliacion
        rvChat.addOnChildAttachStateChangeListener(addChild);

//      Fin del listener
//      ******************************

        databaseReference.addChildEventListener(mensajesListener);
    }

    private void initComponents() {
        edtxtMensaje = (EditText) findViewById(R.id.edtxtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);

        BaseDeDatosChat admin = new BaseDeDatosChat(this,
                matricula.toString(), null, 2);

        bd = admin.getWritableDatabase();

        try {
            indexBD = bd.rawQuery("SELECT * FROM chat", null);

            if (indexBD.moveToFirst()) {
                indexBD.moveToLast();
                indexLastBD = indexBD.getPosition();

            }
        } catch (SQLException e) {

        }


        mensajes = new ArrayList<>();
        keyEliminar = new ArrayList<>();
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

    private void initFirebase(CharSequence matricula) {

        databaseReference = FirebaseDatabase.getInstance("https://gem360.firebaseio.com/")
                .getReference("Chat/-" +matricula);
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

    private void setAdapter() {
        adapter = new ChatAdapter(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvChat.setLayoutManager(l);
        rvChat.setAdapter(adapter);
    }

    //  **********************
//  Metodo para eliminar las referencias de los componentes
//  y liberar memoria
//  **********************
    private void deletComponents() {


        /*Ejecuta el servicio para eliminar los mensajes recibidos por parte de la institucion*/
        Intent i = new Intent(ChatConalep.this, RemoveDataBase.class);
        i.putStringArrayListExtra("key", keyEliminar);
        i.putExtra("matricula",matricula);

        startService(i);

        edtxtMensaje.setText("");
        edtxtMensaje = null;

        btnEnviar.setOnClickListener(null);
        btnEnviar = null;

        rvChat.removeOnChildAttachStateChangeListener(addChild);
        rvChat.setAdapter(null);
        rvChat = null;

        adapter = null;

        mensajes.clear();
        mensajes = null;

        databaseReference.removeEventListener(mensajesListener);
        databaseReference = null;

        bd.close();

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

    //    ******************
//    El metodo se encarga de posicionar el scroll en el ultimo mensaje
//    ******************
    private void setScrollbar() {
        rvChat.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void setScrollbar(int index) {
        rvChat.scrollToPosition(index + 5);
    }

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

    //    ******************
//    Este metodo se encarga de dar de alta o registrar los mensajes
//    ******************
    private void registrarMensaje(itemChat mensaje) {
        ContentValues registro = new ContentValues();

        registro.put("mensaje", mensaje.getMensaje());
        registro.put("fecha", mensaje.getFecha());
        registro.put("nombre", mensaje.getNombre());
        registro.put("Id", mensaje.isId());

        // Inserta los datos en la base de datos
        bd.insert("chat", null, registro);
    }

    protected void obtenerDatos(boolean bandera) {
        try {
            //Nos aseguramos de que existe al menos un registro
            if (indexBD.moveToFirst()) {

                //Movemos la posicion de la base de datos segun el ultimo indice menos la cantidad de mensajes cargados
                indexBD.moveToPosition(indexLastBD - mensajes.size());


                byte i = 0;

                itemChat mensajeBD;
                do{


                    mensajeBD = new itemChat();
                    try {
                        mensajeBD.setMensaje(indexBD.getString(0));
                        mensajeBD.setFecha(indexBD.getString(1));

                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }

                    if ((indexBD.getString(3)).equals("1"))
                        mensajeBD.setId(true);
                    else {
                        mensajeBD.setId(false);
                    }

                    mensajeBD.setNombre(indexBD.getString(2));
                    mensajes.add(mensajeBD);

                    adapter.addMensajeDatabase(mensajeBD);

                }while ( (i++ < 20) && (indexBD.moveToPrevious()));


                if (bandera)
                    setScrollbar();
                else
                    setScrollbar(i);
            }
        } catch (NullPointerException e) {
            Log.e("obtenerDatos()", e.toString());
        }

    }


}