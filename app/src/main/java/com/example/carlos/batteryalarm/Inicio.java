package com.example.carlos.batteryalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity {

    private TextView batteryTxt;
    public static SQLiteDatabase db;
    public static int umbral;
    public static List<String> listaContactos = new ArrayList<>();
    public static List<String> listaEmails = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        batteryTxt = (TextView) findViewById(R.id.txtBateria);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //creamos la base de datos Tarea3
        //deleteDatabase("Tarea3");//para borrar la base de datos
        db=openOrCreateDatabase("Tarea3", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS configuracionesT3(ID TEXT PRIMARY KEY, umbral INT,lista_contactos TEXT, liata_emails TEXT);");

//        //añadimos una fila a la tabla
//        db.execSQL("INSERT INTO configuracionesT3 VALUES ('carloshernandezcrespo@gmail.com',638844416)");



    }

    //instanciamos un BroadcasReciber y sobreescribimos su metodo "onRecive"
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText("Nivel de Batería actual: " + String.valueOf(level) + "%");

        }
    };


    //pasamos a la actividad de configuracion
    public void configurar(View view) {
        Intent configura = new Intent(this, Configuracion.class);

        startActivity(configura);
    }

}


