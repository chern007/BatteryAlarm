package com.example.carlos.batteryalarm;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity {

    private TextView batteryTxt;
    public static SQLiteDatabase db;
    private int nivelBateriaActual = 100;
    public static int umbral;
    public static String[] listaContactos;
    public static String[] listaEmails;


    //si vuelve a saltar la actividad cargamos los datos de nuevo
    @Override
    public void onResume() {
        super.onResume();

        recuperaDatos();

        nivelBateriaActual = cargaBateria();//llamamos al metodo que consulta la carga actual de la bateria y lo guardamos en la variable

        if (nivelBateriaActual < umbral){

            Toast.makeText(this, "La bateria esta por debajo del umbral!!!", Toast.LENGTH_SHORT).show();

            //***Envio de AVISOS***
            //EnviarSMS();
            //enviarMail("carloshernandezcrespo@gmail.com","Probando","Esto es una prueba.");
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        batteryTxt = (TextView) findViewById(R.id.txtBateria);

        //creamos la base de datos Tarea3
        //deleteDatabase("Tarea3");//para borrar la base de datos
        db=openOrCreateDatabase("Tarea3", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS configuracionesT3(ID TEXT PRIMARY KEY, umbral INT,lista_contactos TEXT, liata_emails TEXT);");

        //recuperaDatos();// se deshabilita ya que se cargará en el metodo onResume

    }

    //instanciamos un BroadcastReciber y sobreescribimos su metodo "onRecive"
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            nivelBateriaActual = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText("Nivel de Batería actual: " + String.valueOf(nivelBateriaActual) + "%");
        }
    };


    public int cargaBateria ()
    {
        try
        {
            IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent battery = this.registerReceiver(null, batIntentFilter);
            int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            return nivelBateria;
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),
                    "Error al obtener estado de la batería",
                    Toast.LENGTH_SHORT).show();
            return 0;
        }
    }





    //pasamos a la actividad de configuracion
    public void configurar(View view) {
        Intent configura = new Intent(this, Configuracion.class);

        startActivity(configura);
    }

    public void enviarMail(String[] destinatarios, String asunto, String contenido){

        Intent chooser=null;
        Intent i = new Intent();

        i.setAction(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        String para[]=destinatarios;
        i.putExtra(Intent.EXTRA_EMAIL,para);
        i.putExtra(Intent.EXTRA_SUBJECT,asunto);
        i.putExtra(Intent.EXTRA_TEXT,contenido);
        i.setType("message/rfc822");
        chooser=i.createChooser(i,"Enviar Email");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Se ha producido un error al mandar el email.", Toast.LENGTH_SHORT).show();
        }

    }


    public void EnviarSMS(String[] destinatarios, String contenido){

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("636340858", null, contenido, null, null);
            Toast.makeText(getApplicationContext(), "SMS enviado.",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"SMS no enviado, por favor, inténtalo otra vez.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void recuperaDatos(){
        Cursor c = db.rawQuery("SELECT * FROM configuracionesT3 WHERE ID = 'tarea3'", null);

        if(c.getCount()==0) {
            Toast.makeText(this, "No hay datos guardados.", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
                if (c.getString(0).equals("tarea3")) {

                    umbral = Integer.parseInt(c.getString(1));//actualizamos el umbral

                    String[] PRElistaContactos = c.getString(2).split(";");

                    listaContactos = new String[PRElistaContactos.length];
                    int puntero = 0;
                    int posicionTelefono;
                    for (String elem : PRElistaContactos){
                        posicionTelefono  = elem.split(" \\(").length - 1;
                        listaContactos[puntero] = elem.split(" \\(")[posicionTelefono].replace(")","");//actualizamos el array de numeros de contacto
                        puntero++;
                    }
                    listaEmails = c.getString(3).split(";");//actualizamos el array de emails
                    //listaEmails[0]=listaEmails[0].replace(" ","");//quitamos el espacion inicial
                }
            }
        }
        c.close();

    }


}


