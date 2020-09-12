package com.alumno.proyectopizzascebanc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alumno.bd.SQLiteHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggle1;
    private ToggleButton toggle2;
    private EditText editText;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private Boolean aDomicilio = true;
    private String nombre;
    private String tlfn;
    private String email;
    private String direccion;
    private Button btn;
    private Button btnBuscarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle1 = findViewById(R.id.toggleButton);
        toggle2 = findViewById(R.id.toggleButton2);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        btn = findViewById(R.id.button2);
        btnBuscarCliente = findViewById(R.id.buttonBuscarCliente);

        // Creamos una instancia de la clase SQLiteHelper y abrimos la base de datos 'DBUsuarios'
        final SQLiteHelper usdb = new SQLiteHelper(this, getResources().getString(R.string.nameDB), null, 1);



        // RECUERDA CERRAR LA DB 

        // Creamos el listener para le boton de buscar el cliente
        btnBuscarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // La abrimos en modo de escritura
                SQLiteDatabase db = usdb.getReadableDatabase();// Modo solo lectura --> usdb.getReadableDatabase();
                // Comprobamos que la base de datos está abierta
                if (db != null) {
                    // Creamos un drawable para usarlo de icono del mensaje de error
                    Drawable errorIcon = getResources().getDrawable(R.drawable.icon_error);
                    // Establecemos el tamaño del icono
                    errorIcon.setBounds(0, 0, 80, 80);

                    if (editText.getText().toString().length()==0){
                        editText.setError(getResources().getString(R.string.setError), errorIcon);
                    }else {
                        try {
                            // Comprobamos si el nombre está en la base de datos
                            String nombre;
                            nombre = editText.getText().toString();
                            Cursor miCursor = db.rawQuery("SELECT * FROM clientes WHERE nombre='" + nombre + "'", null);

                            if (miCursor.moveToFirst()) {
                                editText2.setText(miCursor.getString(3));// Telefono
                                editText3.setText(miCursor.getString(2));// Email
                                editText4.setText(miCursor.getString(4));// Direccion
                            } else {
                                // No existe el registro
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.notFound), Toast.LENGTH_SHORT).show();
                                // Vaciamos en resto de edittexts
                                editText2.setText("");
                                editText3.setText("");
                                editText4.setText("");
                            }
                        } catch (Exception e) {
                            editText2.setText(getResources().getString(R.string.error));
                        }
                    }
                }
                db.close();
            }
        });



        // Con los dos siguientes listener hacemos que un toggle button se 'descheckee' al pulsar el otro botón y viceversa.
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggle2.setChecked(false);
                    toggle1.setEnabled(false);
                    toggle1.setTextColor(getResources().getColor(R.color.toggleTextOn));
                    toggle2.setTextColor(getResources().getColor(R.color.toggleTextOff));
                    editText4.setVisibility(View.GONE);
                    aDomicilio = false;

                } else {
                    toggle2.setChecked(true);
                    toggle1.setEnabled(true);
                    toggle1.setTextColor(getResources().getColor(R.color.toggleTextOff));
                    toggle2.setTextColor(getResources().getColor(R.color.toggleTextOn));
                    editText4.setVisibility(View.VISIBLE);
                    aDomicilio = true;
                }
            }
        });

        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggle1.setChecked(false);
                    toggle2.setEnabled(false);
                    toggle1.setTextColor(getResources().getColor(R.color.toggleTextOff));
                    toggle2.setTextColor(getResources().getColor(R.color.toggleTextOn));
                    editText4.setVisibility(View.VISIBLE);
                    aDomicilio = true;
                } else {
                    toggle1.setChecked(true);
                    toggle2.setEnabled(true);
                    toggle1.setTextColor(getResources().getColor(R.color.toggleTextOn));
                    toggle2.setTextColor(getResources().getColor(R.color.toggleTextOff));
                    editText4.setVisibility(View.GONE);
                    aDomicilio = false;
                }
            }
        });

        // Creamos el listener para cuando pulsemos el boton lanze la siguiente actividad
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean error = false;
                // Creamos un drawable para usarlo de icono del mensaje de error
                Drawable errorIcon = getResources().getDrawable(R.drawable.icon_error);
                // Establecemos el tamaño del icono
                errorIcon.setBounds(0, 0, 80, 80);

                // Validamos si todos los campos están correctamente rellenados.
                if (editText.getText().toString().length()==0){
                    editText.setError(getResources().getString(R.string.fEmpty), errorIcon);
                }
                if (editText2.getText().toString().trim().length()==0){
                    editText2.setError(getResources().getString(R.string.fEmpty), errorIcon);
                }
                if (editText3.getText().toString().trim().length()==0){
                    editText3.setError(getResources().getString(R.string.fEmpty), errorIcon);
                }
                if (editText4.getText().toString().trim().length()==0){
                    editText4.setError(getResources().getString(R.string.fEmpty), errorIcon);
                }
                // Validamos que el campo del telefono tenga 9 caracteres
                if (editText2.getText().toString().trim().length()!=9) {
                    editText2.setError(getResources().getString(R.string.numeroValido), errorIcon);
                }
                // Validamos el correo tenga el formato adecuado
                if (!emailValidator(editText3.getText().toString().trim())) {
                    editText3.setError(getResources().getString(R.string.emailValido), errorIcon);
                }
                // Hacemos un trycatch para asegurarnos que el campo del telefono solo hay numeros
                try{
                    Integer.parseInt(editText2.getText().toString().trim());
                }catch (Exception e) {
                    error = true;
                    editText2.setError(getResources().getString(R.string.numeroValido), errorIcon);
                }
                // En el caso de que estén todos los campos correctos lanzamos la siguiente actividad
                if (aDomicilio){
                    if(editText.getText().toString().length()>0 &&
                            editText2.getText().toString().trim().length()==9 &&
                            editText3.getText().toString().trim().length()>0 &&
                            editText4.getText().toString().trim().length()>0 &&
                            !error &&
                            emailValidator(editText3.getText().toString().trim())) {
                        final Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        nombre = editText.getText().toString();
                        tlfn = editText2.getText().toString().trim();
                        email = editText3.getText().toString().trim();
                        direccion = editText4.getText().toString();

                        // La abrimos en modo de escritura
                        SQLiteDatabase db = usdb.getReadableDatabase();// Modo solo lectura --> usdb.getReadableDatabase();
                        // Comprobamos que la base de datos está abierta
                        if (db != null) {
                            try {
                                // Comprobamos si el nombre está en la base de datos
                                String nombreSql;
                                nombreSql = editText.getText().toString();
                                final Cursor miCursor = db.rawQuery("SELECT * FROM clientes WHERE nombre='" + nombreSql + "'", null);

                                if (miCursor.moveToFirst()) {
                                    if (editText2.getText().toString().equals(miCursor.getString(3)) &&
                                            editText3.getText().toString().equals(miCursor.getString(2)) &&
                                            editText4.getText().toString().equals(miCursor.getString(4))) {
                                        // El usuario ya existe y con los mismos datos, seguimos normal
                                        // Sin cambios
                                        intent.putExtra("nombre", nombre);
                                        intent.putExtra("tlfn", tlfn);
                                        intent.putExtra("email", email);
                                        intent.putExtra("direccion", direccion);
                                        intent.putExtra("aDomicilio", aDomicilio);
                                        startActivity(intent);
                                    } else if (miCursor.getString(3).equals(editText2.getText().toString()) &&
                                            miCursor.getString(2).equals(editText3.getText().toString()) &&
                                            miCursor.getString(4) == null) {
                                        // No hay direccion asi que actualizamos con la que esta puesta
                                        // Actualizamos registro
                                        SQLiteDatabase db4 = usdb.getWritableDatabase();

                                        if (db4 != null) {
                                            try {
                                                String sqlActualizar = "UPDATE clientes SET email = '" + email + "', telefono = '" + tlfn + "', direccion = '" + direccion + "' WHERE nombre = '" + nombre + "'";
                                                db4.execSQL(sqlActualizar);

                                                intent.putExtra("nombre", nombre);
                                                intent.putExtra("tlfn", tlfn);
                                                intent.putExtra("email", email);
                                                intent.putExtra("direccion", direccion);
                                                intent.putExtra("aDomicilio", aDomicilio);
                                                startActivity(intent);
                                            } catch(Exception e) {

                                            }
                                        }
                                        db4.close();

                                    } else {
                                        // El usuario ya existe pero con diferentes datos, preguntar al usuario que datos quiere usar.
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle(getResources().getString(R.string.qTitle));
                                        builder.setMessage(getResources().getString(R.string.qRegis));
                                        builder.setPositiveButton(getResources().getString(R.string.qExis), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Usamos el registro existente
                                                tlfn = miCursor.getString(3);
                                                email = miCursor.getString(2);
                                                direccion = miCursor.getString(4);

                                                intent.putExtra("nombre", nombre);
                                                intent.putExtra("tlfn", tlfn);
                                                intent.putExtra("email", email);
                                                intent.putExtra("direccion", direccion);
                                                intent.putExtra("aDomicilio", aDomicilio);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton(getResources().getString(R.string.qNuevo), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Actualizamos registro
                                                SQLiteDatabase db2 = usdb.getWritableDatabase();

                                                if (db2 != null) {
                                                    try {
                                                        String sqlActualizar = "UPDATE clientes SET email = '" + email + "', telefono = '" + tlfn + "', direccion = '" + direccion + "' WHERE nombre = '" + nombre + "'";
                                                        db2.execSQL(sqlActualizar);

                                                        intent.putExtra("nombre", nombre);
                                                        intent.putExtra("tlfn", tlfn);
                                                        intent.putExtra("email", email);
                                                        intent.putExtra("direccion", direccion);
                                                        intent.putExtra("aDomicilio", aDomicilio);
                                                        startActivity(intent);
                                                    } catch(Exception e) {

                                                    }
                                                }
                                                db2.close();
                                            }
                                        });
                                        Dialog dialog = builder.create();
                                        dialog.show();
                                    }
                                } else {
                                    // No existe el registro, crear uno nuevo
                                    SQLiteDatabase db3 = usdb.getWritableDatabase();

                                    if (db3 != null) {
                                        try {
                                            String sqlNuevoRegistro = "INSERT INTO clientes VALUES (null, '" + nombre + "','" + email + "', '" + tlfn + "', '" + direccion + "')";
                                            db3.execSQL(sqlNuevoRegistro);

                                            intent.putExtra("nombre", nombre);
                                            intent.putExtra("tlfn", tlfn);
                                            intent.putExtra("email", email);
                                            intent.putExtra("direccion", direccion);
                                            intent.putExtra("aDomicilio", aDomicilio);
                                            startActivity(intent);
                                        } catch(Exception e) {

                                        }
                                    }
                                    db3.close();
                                }
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        db.close();


                    }
                }else{
                    if(editText.getText().toString().length()>0 &&
                            editText2.getText().toString().trim().length()==9 &&
                            editText3.getText().toString().trim().length()>0 &&
                            !error &&
                            emailValidator(editText3.getText().toString().trim())) {
                        final Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        nombre = editText.getText().toString();
                        tlfn = editText2.getText().toString();
                        email = editText3.getText().toString();

                        // La abrimos en modo de escritura
                        SQLiteDatabase db = usdb.getReadableDatabase();// Modo solo lectura --> usdb.getReadableDatabase();
                        // Comprobamos que la base de datos está abierta
                        if (db != null) {
                            try {
                                // Comprobamos si el nombre está en la base de datos
                                String nombreSql;
                                nombreSql = editText.getText().toString();
                                final Cursor miCursor = db.rawQuery("SELECT * FROM clientes WHERE nombre='" + nombreSql + "'", null);

                                if (miCursor.moveToFirst()) {
                                    if (miCursor.getString(3).equals(editText2.getText().toString()) &&
                                            miCursor.getString(2).equals(editText3.getText().toString())) {
                                        // El usuario ya existe y con los mismos datos, seguimos normal
                                        // Sin cambios
                                        intent.putExtra("nombre", nombre);
                                        intent.putExtra("tlfn", tlfn);
                                        intent.putExtra("email", email);
                                        intent.putExtra("aDomicilio", aDomicilio);
                                        startActivity(intent);
                                    } else {
                                        // El usuario ya existe pero con diferentes datos, preguntar al usuario que datos quiere usar.
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle(getResources().getString(R.string.qTitle));
                                        builder.setMessage(getResources().getString(R.string.qRegis));
                                        builder.setPositiveButton(getResources().getString(R.string.qExis), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Usamos el registro existente
                                                tlfn = miCursor.getString(3);
                                                email = miCursor.getString(2);

                                                intent.putExtra("nombre", nombre);
                                                intent.putExtra("tlfn", tlfn);
                                                intent.putExtra("email", email);
                                                intent.putExtra("aDomicilio", aDomicilio);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton(getResources().getString(R.string.qNuevo), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Actualizamos registro
                                                SQLiteDatabase db2 = usdb.getWritableDatabase();

                                                if (db2 != null) {
                                                    try {
                                                        String sqlActualizar = "UPDATE clientes SET email = '" + email + "', telefono = '" + tlfn + "', direccion = null WHERE nombre = '" + nombre + "'";
                                                        db2.execSQL(sqlActualizar);

                                                        intent.putExtra("nombre", nombre);
                                                        intent.putExtra("tlfn", tlfn);
                                                        intent.putExtra("email", email);
                                                        intent.putExtra("aDomicilio", aDomicilio);
                                                        startActivity(intent);
                                                    } catch(Exception e) {

                                                    }
                                                }
                                                db2.close();
                                            }
                                        });
                                        Dialog dialog = builder.create();
                                        dialog.show();
                                    }
                                } else {
                                    // No existe el registro, crear uno nuevo
                                    SQLiteDatabase db3 = usdb.getWritableDatabase();

                                    if (db3 != null) {
                                        try {
                                            String sqlNuevoRegistro = "INSERT INTO clientes VALUES (null, '" + nombre + "','" + email + "', '" + tlfn + "', null)";
                                            db3.execSQL(sqlNuevoRegistro);

                                            intent.putExtra("nombre", nombre);
                                            intent.putExtra("tlfn", tlfn);
                                            intent.putExtra("email", email);
                                            intent.putExtra("aDomicilio", aDomicilio);
                                            startActivity(intent);
                                        } catch(Exception e) {

                                        }
                                    }
                                    db3.close();
                                }
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            }
                        }
                        db.close();
                    }
                }
            }
        });
    }

    //Validacion del email
    public boolean emailValidator (String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = getResources().getString(R.string.emailValidator);
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}