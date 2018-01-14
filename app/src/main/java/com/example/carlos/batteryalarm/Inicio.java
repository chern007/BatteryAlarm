package com.example.carlos.batteryalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity {

    private TextView batteryTxt;
    SQLiteDatabase db;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        batteryTxt = (TextView) findViewById(R.id.txtBateria);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //creamos la base de datos Tarea3
        db=openOrCreateDatabase("Tarea3", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS configuracionesT3(emails VARCHAR,telf INTEGER);");

        //añadimos una fila a la tabla
        db.execSQL("INSERT INTO configuracionesT3 VALUES ('carloshernandezcrespo@gmail.com',638844416)");

        Listar();

    }

    //instanciamos un BroadcasReciber y sobreescribimos su metodo "onRecive"
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText(String.valueOf(level) + "%");

        }
    };

    public void Listar(){

        List<String> lista = new ArrayList<String>();
        Cursor c=db.rawQuery("SELECT * FROM configuracionesT3", null);
        if(c.getCount()==0)
            lista.add("No hay registros");
        else{
            while(c.moveToNext())
                lista.add(c.getString(0)+"-"+c.getInt(1));
        }
        c.close();

        Toast.makeText(this, lista.get(0), Toast.LENGTH_LONG).show();

    }

}


