package com.gem.administradorgem.Fragment.Chat.DatabaseSQL;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.Fragment.Chat.Adapter.ChatAdapter;
import com.gem.administradorgem.Fragment.Chat.Adapter.itemChat;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSql {
    private SQL sql;

    private Cursor cursor;
    private SQLiteDatabase bd;

    private int index;

    private List<itemChat> mensajes;

    public DatabaseSql(String matricula, Activity activity) {
        this.sql = new SQL(activity, matricula, null, 2);
        mensajes = new ArrayList<>();
        initBD();
    }

    private void initBD() {
        bd = sql.getWritableDatabase();

        try {
            cursor = bd.rawQuery("SELECT * FROM chat", null);
            if (cursor.moveToFirst()) {
                cursor.moveToLast();
                index = cursor.getPosition();
            }
        } catch (SQLException ignored) {

        }
    }

    public void Destroy() {
        cursor.close();
        bd.close();
        sql.close();

        mensajes.clear();
        mensajes = null;
    }

    public void registrarMensaje(itemChat mensaje) {
        ContentValues registro = new ContentValues();

        registro.put("mensaje", mensaje.getMensaje());
        registro.put("fecha", mensaje.getFecha());
        registro.put("nombre", mensaje.getNombre());


        // Inserta los datos en la base de datos
        bd.insert("chat", null, registro);
    }

    public void obtenerDatos(ChatAdapter adapter, RecyclerView rv, boolean flag) {
        if (cursor.moveToFirst()) {
            //Movemos la posicion de la base de datos segun el ultimo indice menos la cantidad de mensajes cargados
            cursor.moveToPosition(index - mensajes.size());

            byte i = 0;
            itemChat mensajeBD;

            do {
                mensajeBD = new itemChat();
                try {
                    mensajeBD.setMensaje(cursor.getString(0));
                    mensajeBD.setFecha(cursor.getString(1));

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                mensajeBD.setNombre(cursor.getString(2));
                mensajes.add(mensajeBD);

                adapter.addMensajeDatabase(mensajeBD);

            } while ((i++ < 20) && (cursor.moveToPrevious()));

            if (flag)
                rv.scrollToPosition(adapter.getItemCount() - 1);
            else
                rv.scrollToPosition(i + 5);

        }

    }

}
