package com.example.carlos.batteryalarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Carlos on 21/01/2018.
 */

public class conexionSQLite extends AppCompatActivity implements Serializable{

    SQLiteDatabase db;


    public conexionSQLite(String nombreDB) {
        this.db = openOrCreateDatabase(nombreDB,Context.MODE_PRIVATE, null);
    }

    public void ejecutarQueryNoResult(String query){

        db.execSQL(query);

//          para recorrer un hashmap
//        for (Map.Entry<String, String> entry : campos.entrySet()) {
//            query+= entry.getKey() + " " + entry.getValue();
//        }

    }

    public List<Object[]> ejecutarQueryResult(String query){

        List<Object[]> resultado = new ArrayList<>();
        Cursor c=db.rawQuery(query, null);
        int longitudCursor = 0;

        if(c.getCount()==0)
            resultado.add(null);
        else{
            while(c.moveToNext()) {
                longitudCursor = c.getCount();
                Object[] tmp = new Object[longitudCursor];

                for (int n = 0; n < longitudCursor; n++) {

                    //de momento solo vale si los datos que devuelve el cursor son String o int REVISAR
                    try {
                        tmp[n] = c.getString(n);
                    } catch (Exception e) {
                        tmp[n] = c.getInt(n);
                    }

                }

                resultado.add(tmp.clone());//lo clonamos para que saque una nueva referencia en memoria, si no estaria cambiando continuamente

            }
        }
        c.close();

    return resultado;

    }













}
