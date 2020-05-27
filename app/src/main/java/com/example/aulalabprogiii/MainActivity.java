package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (TextView) findViewById(R.id.textView);
    }

  public void abrirTelaCadastro(View view){
        Intent intent = new Intent( this, ListaConversas.class);
        startActivity(intent);
    }

    public void abrirTelaLogin(View view){
        Intent intent = new Intent( this, TelaLogin.class);
        startActivity(intent);
    }

    public void alterartexto(View view){

        texto.setText("Novo texto");

    }
}
