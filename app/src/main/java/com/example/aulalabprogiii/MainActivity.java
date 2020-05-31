package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    }

    public void abrirTelaLogin(View view){
        String prefixoURL = PrefixoURL.getText().toString();

        if (prefixoURL.matches("")) {
            Toast.makeText(MainActivity.this, "Preencha o Prefixo da URL!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = PrefixoURL.getText().toString();
        Intent intent = new Intent( this, TelaLogin.class);
        intent.putExtra("PrefixoURL",URL);
        startActivity(intent);
    }

}
