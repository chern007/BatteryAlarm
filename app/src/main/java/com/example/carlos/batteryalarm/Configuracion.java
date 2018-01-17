package com.example.carlos.batteryalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Configuracion extends AppCompatActivity {

    public static final int SELECCIONA_CONTACTO = 1;

    EditText listaEmails;
    EditText casillaNuevoEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        listaEmails = (EditText) findViewById(R.id.mtxtEmails);
        casillaNuevoEmail = (EditText) findViewById(R.id.txtEmail);

    }

    public void anadirEmail(View view) {

        String actualesEmail = listaEmails.getText().toString();

        String nuevoEmail = casillaNuevoEmail.getText().toString();


        if (actualesEmail.equals("")){
            actualesEmail += nuevoEmail;
        }else{
            actualesEmail += "; " + nuevoEmail;
        }

        listaEmails.setText(actualesEmail);

        casillaNuevoEmail.setText("");

    }


    public void anadirContacto(View view) {

        Intent intent = new Intent(this, selecciona.class);
        startActivityForResult(intent,SELECCIONA_CONTACTO);
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        String nombreContacto;

        if (requestCode == SELECCIONA_CONTACTO) {
            if (resultCode == RESULT_OK) {
                // se seleccionó correctamente el contacto
                nombreContacto = data.getStringExtra("NOMBRE");
            }
        }
    }


}
