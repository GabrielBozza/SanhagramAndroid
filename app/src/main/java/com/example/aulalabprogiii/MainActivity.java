package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText PrefixoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefixoURL = (EditText) findViewById(R.id.PrefixoURL);

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        String Prefixo = prefs.getString("PREFIXO_URL", "");

        if(Prefixo.length()>0){
            Intent intent = new Intent( this, TelaLogin.class);
            startActivity(intent);
        }

    }

    public void abrirTelaLogin(View view){
        String prefixoURL = PrefixoURL.getText().toString();

        if (prefixoURL.matches("")) {
            Toast.makeText(MainActivity.this, "Preencha o Prefixo da URL!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PREFIXO_URL", PrefixoURL.getText().toString());     //RESET TO DEFAULT VALUE
        editor.commit();

        Intent intent = new Intent( this, TelaLogin.class);
        startActivity(intent);
    }

}
