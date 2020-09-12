package com.alumno.proyectopizzascebanc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private TextView phoneNumberText;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Hacemos un listener para lanzar un nuevo activity al darle al botón.
        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Listener para el boton para lanzar el activity donde manejaremos la base de datos
        Button btnDB = findViewById(R.id.botonDB);
        btnDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, DatabaseActivity.class);
                startActivity(intent);
            }
        });

        // Hacemos que al pulsar el botón correspondiente a la dirección nos abra el google maps
        ImageButton btMaps = findViewById(R.id.imageButton2);
        btMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent = intent.setData(Uri.parse(getResources().getString(R.string.geoCoord)));
                startActivity(intent);
            }
        });


        // Hacemos que al pulsar el botón correspondiente al correo nos abra el gmail
        ImageButton btCorreo = findViewById(R.id.imageButton3);
        btCorreo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(getResources().getString(R.string.mailType));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.correo)});
                intent.setPackage(getResources().getString(R.string.packageMail));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(InfoActivity.this, getResources().getString(R.string.emailAlert), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Hacemos que al pulsar el boton del telefono llame al numero que aparece
        phoneNumberText = findViewById(R.id.textView);
        ImageButton btPhone = findViewById(R.id.imageButton);
        btPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberText.getText().toString();
                if (phoneNumber != null) {
                    //comprobar version actual de android
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    } else {
                        OlderVersions(phoneNumber);
                    }
                }
            }

            private void OlderVersions(String phoneNumber) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.tel) + phoneNumber));

                if (checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(InfoActivity.this, getResources().getString(R.string.telAlert), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Caso del telefono
        switch (requestCode) {
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //Comprobar si ha sido aceptado o denegado la peticion de permiso
                    if(result == PackageManager.PERMISSION_GRANTED) {
                        //Concedido permiso
                        String phoneNumber = phoneNumberText.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.tel) + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(InfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intentCall);
                    } else {
                        //No concedió el permiso
                        Toast.makeText(InfoActivity.this, R.string.accesDeclined, Toast.LENGTH_SHORT);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
