package com.example.carlos.batteryalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Configuracion extends AppCompatActivity {

    public static final int SELECCIONA_CONTACTO = 1;

    EditText listaEmails;
    EditText casillaNuevoEmail;
    EditText listaContactos;
    EditText casillaNuevoContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        listaEmails = (EditText) findViewById(R.id.mtxtEmails);
        casillaNuevoEmail = (EditText) findViewById(R.id.txtEmail);
        listaContactos = (EditText) findViewById(R.id.mtxtTelefonos);
        casillaNuevoContacto = (EditText) findViewById(R.id.txtTelf);

    }

    public void anadirEmail(View view) {

        String actualesEmail = listaEmails.getText().toString();

        String nuevoEmail = casillaNuevoEmail.getText().toString();

        if(charNumber(actualesEmail,';') < 2) {
            if (actualesEmail.equals("")) {
                actualesEmail += " " + nuevoEmail;
            } else {
                actualesEmail += ";\n " + nuevoEmail;
            }

            listaEmails.setText(actualesEmail);

            casillaNuevoEmail.setText("");
        }else{
            Toast.makeText(this, "borrar en contenido del cuadro", Toast.LENGTH_SHORT).show();
        }

    }


    public void anadirContacto(View view) {

        Intent eligeContacto = new Intent(this, selecciona.class);
        eligeContacto.putExtra("contacto", casillaNuevoContacto.getText().toString());
        startActivityForResult(eligeContacto,SELECCIONA_CONTACTO);
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        String nombreContacto=null;
        String numeroTelefono=null;

        if (requestCode == SELECCIONA_CONTACTO) {
            if (resultCode == RESULT_OK) {
                // se seleccionó correctamente el contacto
                nombreContacto = data.getStringExtra("contactoElegido");
                numeroTelefono = data.getStringExtra("contactoElegidoNumero");

                Toast.makeText(this, nombreContacto + " - " + numeroTelefono, Toast.LENGTH_SHORT).show();

            }
        }

        String contactosActuales = listaContactos.getText().toString();

        if(charNumber(contactosActuales,';') < 2) {
            if (contactosActuales.equals("")) {
                contactosActuales += " " + nombreContacto + " (" + numeroTelefono + ")";
            } else {
                contactosActuales += ";\n " + nombreContacto + " (" + numeroTelefono + ")";
            }

            listaContactos.setText(contactosActuales);
            casillaNuevoContacto.setText("");

        }else{
            Toast.makeText(this, "borrar en contenido del cuadro", Toast.LENGTH_SHORT).show();
        }


    }


    public int charNumber(String s, char patron) {

        int counter = 0;
        for( int i=0; i<s.length(); i++ ) {
            if( s.charAt(i) == patron ) {
                counter++;
            }
        }

        return counter;
    }



}
