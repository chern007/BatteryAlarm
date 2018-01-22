package com.example.carlos.batteryalarm;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos on 17/01/2018.
 */

public class Selecciona extends AppCompatActivity implements ListView.OnItemClickListener {

    ListView listaSeleccion;
    String nombreFiltro;
    ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecciona);

        listaSeleccion = (ListView) findViewById(R.id.lstContactos);

        //**********************************************************

        //cogemos el string que hemos pasado desde la otra actividad
        nombreFiltro = getIntent().getExtras().getString("contacto");


        //creamos la proyeccion de los datos que queremos que saque nuestro ContentResolver
        String proyeccion[] = {ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_ID};

        //configuramos los parametros de filtro del ContentResolver
        String filtro = ContactsContract.Contacts.DISPLAY_NAME + " like ?";
        String args_filtro[] = {"%" + nombreFiltro + "%"};


        List<String> lista_contactos = new ArrayList<String>();
        cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, proyeccion, filtro, args_filtro, null);

        //recorremos el cursor y vamos guardando los elementos en una lista de Strings
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    lista_contactos.add(name);
                }
            }
        }
        cur.close();//cerramos el cursor


        //ordenamos la lista alfabeticamente
        Collections.sort(lista_contactos);

        //planchamos la lista de strings en el elemento ListView
        ListView l = (ListView) findViewById(R.id.lstContactos);
        l.setAdapter(new ArrayAdapter<String>(this, R.layout.fila_lista, lista_contactos));
        l.setOnItemClickListener(this);//añdimos el listener

    }


    public void onItemClick(AdapterView<?> a, View view, int position, long id) {

        //sacamos el nombre del elemento seleccionado de la lista
        String nombreElegido = a.getItemAtPosition(position).toString();
        String identificador = "";
        int existeTelefono = 0;

        //preparamos el filtro
        String proyeccion[]={ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER};
        String filtro=ContactsContract.Contacts.DISPLAY_NAME + " = ?";
        String args_filtro[]={nombreElegido};

        //guardamos en un listado el resultado de la consulta al contente provider El resultado será de 1 elemento
        List<String> lista_contactos=new ArrayList<String>();
        cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,proyeccion, filtro, args_filtro, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                identificador = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                existeTelefono = Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            }
        }
        cur.close();//cerramos el cursor

        // obtenemos el numero de movil
        String numeroMovil = null;
        if (existeTelefono > 0) {
            Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{identificador}, null);
            if (cp != null && cp.moveToFirst()) {
                numeroMovil = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                numeroMovil.replace(" ", "");
                cp.close();
            }
        }


        //creamos un intent para pasar los datos al otro activity
        Intent i=new Intent();
        i.putExtra("contactoElegido",a.getItemAtPosition(position).toString());
        i.putExtra("contactoElegidoNumero",numeroMovil);
        setResult(RESULT_OK,i);
        finish();
    }

}
