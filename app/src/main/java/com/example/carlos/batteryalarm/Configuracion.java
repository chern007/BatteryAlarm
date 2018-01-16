package com.example.carlos.batteryalarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Configuracion extends AppCompatActivity {

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


}
