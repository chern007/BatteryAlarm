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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 17/01/2018.
 */

public class selecciona extends AppCompatActivity {

    ListView listaSeleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecciona);

        listaSeleccion = (ListView) findViewById(R.id.lstContactos);

        //**********************************************************
        String proyeccion[]={ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_ID};


        String filtro=ContactsContract.Contacts.DISPLAY_NAME + " like ?";
        String args_filtro[]={"%"+ "Mayte" +"%"};//***

        // ContentResolver cr = getContentResolver();
        //Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, proyeccion, filtro, args_filtro, null);



        List<String> lista_contactos=new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, proyeccion, filtro, args_filtro, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    lista_contactos.add(name);
                }
            }
        }
        cur.close();

        ListView l=(ListView)findViewById(R.id.lstContactos);
        l.setAdapter(new ArrayAdapter<String>(this,R.layout.fila_lista,lista_contactos));


    }

    public void onItemClick(AdapterView<?> a, View view, int position, long id){
        //creamos un intent para pasar los datos al otro activity
        Intent i=new Intent();
        i.putExtra("NOMBRE",a.getItemAtPosition(position).toString());
        setResult(RESULT_OK,i);
        finish();
    }


    public void buscarContacto(View view) {






    }


}
