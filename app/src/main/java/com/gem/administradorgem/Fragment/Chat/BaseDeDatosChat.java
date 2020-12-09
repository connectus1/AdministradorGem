package com.gem.administradorgem.Fragment.Chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class BaseDeDatosChat extends SQLiteOpenHelper {

    public BaseDeDatosChat(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table chat(mensaje text, fecha text,nombre text,Id boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists chat");
        db.execSQL("create table chat(mensaje text, fecha text,nombre text, Id text)");
    }
}
