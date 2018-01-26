package com.example.carlos.batteryalarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Configuracion extends AppCompatActivity {

    public static final int SELECCIONA_CONTACTO = 1;

    EditText umbral;
    EditText listaEmails;
    EditText casillaNuevoEmail;
    EditText listaContactos;
    EditText casillaNuevoContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        umbral = (EditText) findViewById(R.id.txtPorcentaje);
        listaEmails = (EditText) findViewById(R.id.mtxtEmails);
        casillaNuevoEmail = (EditText) findViewById(R.id.txtEmail);
        listaContactos = (EditText) findViewById(R.id.mtxtTelefonos);
        casillaNuevoContacto = (EditText) findViewById(R.id.txtTelf);

        actualizar();//actualizamos el formulario con los datos guardados

    }

    //creamos el metodo que infla la barra de menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_alarm_battery,menu);//inflamos la barra de menus
        return super.onCreateOptionsMenu(menu);

    }
    //creamos el método listener que captura el evento pulsacion de los botones del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.mnAtras){

            finish();//cerramos la actividad y pasaremos a la anterior directamente
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardarConfiguracion(View view){

        //vemos si esta vacio y cambiamos el valor del umbral para que no de error la consulta
        String tmp = null;
        if (umbral.getText().toString().equals("")){
            tmp = "0";
        }else{
            tmp = umbral.getText().toString();
        }

        //añadimos una fila a la tabla
        Inicio.db.execSQL("INSERT OR REPLACE INTO configuracionesT3 (ID, umbral, lista_contactos, liata_emails) VALUES ('tarea3'," + tmp + ", '"+ listaContactos.getText() + "', '"+ listaEmails.getText() + "');" );

        Toast.makeText(this, "Se ha guardado la configuración.", Toast.LENGTH_SHORT).show();
    }


    public void anadirEmail(View view) {

        String actualesEmail = listaEmails.getText().toString();

        String nuevoEmail = casillaNuevoEmail.getText().toString();

        if (charNumber(actualesEmail, ';') < 2 && !nuevoEmail.equals("") && nuevoEmail != null) {
            if (actualesEmail.equals("")) {
                actualesEmail += nuevoEmail;
            } else {
                actualesEmail += ";\n" + nuevoEmail;
            }

            listaEmails.setText(actualesEmail);

            casillaNuevoEmail.setText("");

        } else if (charNumber(actualesEmail, ';') == 2){

            borrarAlmacenados("emails");
        }

    }


    public void anadirContacto(View view) {

        Intent eligeContacto = new Intent(this, Selecciona.class);
        eligeContacto.putExtra("contacto", casillaNuevoContacto.getText().toString());
        startActivityForResult(eligeContacto, SELECCIONA_CONTACTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String nombreContacto = null;
        String numeroTelefono = null;

        if (requestCode == SELECCIONA_CONTACTO) {
            if (resultCode == RESULT_OK) {
                // se seleccionó correctamente el contacto
                nombreContacto = data.getStringExtra("contactoElegido");
                numeroTelefono = data.getStringExtra("contactoElegidoNumero");

                numeroTelefono = numeroTelefono.replace(" ","");//le quitamos los espacios

                Toast.makeText(this, nombreContacto + " - " + numeroTelefono, Toast.LENGTH_SHORT).show();

            }
        }

        String contactosActuales = listaContactos.getText().toString();

        if (charNumber(contactosActuales, ';') < 2 && nombreContacto != null && numeroTelefono != null) {
            if (contactosActuales.equals("")) {
                contactosActuales += nombreContacto + " (" + numeroTelefono + ")";
            } else {
                contactosActuales += ";\n" + nombreContacto + " (" + numeroTelefono + ")";
            }

            listaContactos.setText(contactosActuales);
            casillaNuevoContacto.setText("");

        } else if (charNumber(contactosActuales, ';') == 2){

            borrarAlmacenados("contactos");

        }
    }

    //metodo que muestra un dialogo en el que "infla" dialogo_cuadro.xml
    public void borrarAlmacenados(final String tipo) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialogo_cuadro, null);
        Button botonOK = (Button) mView.findViewById(R.id.dialog_ok);
        Button botonCancelar = (Button) mView.findViewById(R.id.btCancelar);
        TextView mensaje = (TextView) mView.findViewById(R.id.dialog_info);
        mBuilder.setView(mView);
        final AlertDialog dialogo = mBuilder.create();
        dialogo.show();


        if (tipo.equals("contactos")) {
            mensaje.setText(getResources().getString(R.string.borrarContactos));//rescatamos e valor de strings.xml
        } else {
            mensaje.setText(getResources().getString(R.string.borrarEmails));//rescatamos e valor de strings.xml
        }

        botonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tipo.equals("contactos")) {
                    listaContactos.setText("");//limpiamos la lista de contactos
                } else {
                    listaEmails.setText("");//limpiamos la lista de contactos
                }
                dialogo.hide();
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogo.hide();
            }
        });
    }

    //funcion que saca el numero de veces que aparece un caracter en una string
    public int charNumber(String s, char patron) {

        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == patron) {
                counter++;
            }
        }
        return counter;
    }


    //metodo que ejecuta el listener del boton borrar contactos
    public void borrarContactos(View view) {
        borrarAlmacenados("contactos");
    }
    //metodo que ejecuta el lsitener del boton borrar emails
    public void borrarEmails(View view) {
        borrarAlmacenados("emails");
    }


    //metodo que actualiza el formulario de configuracion con los datos almacenados en SQLite
    public void actualizar (){

        List<String> lista = new ArrayList<String>();
        Cursor c = Inicio.db.rawQuery("SELECT * FROM configuracionesT3 WHERE ID = 'tarea3'", null);
        if(c.getCount()==0) {
            Toast.makeText(this, "No hay datos guardados.", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
                if (c.getString(0).equals("tarea3")){

                    //ponemos los valores en el formulario
                    if (!c.getString(1).equals("0")){
                        umbral.setText(c.getString(1));
                    }else{
                        umbral.setText("");
                    }
                    listaContactos.setText(c.getString(2));
                    listaEmails.setText(c.getString(3));

                }
            }
        }
        c.close();

        //Toast.makeText(this, lista.get(0), Toast.LENGTH_SHORT).show();

    }





}