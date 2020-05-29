package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText PrefixoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefixoURL = (EditText) findViewById(R.id.PrefixoURL);
    }

    public void abrirTelaLogin(View view){
        String URL = "http://"+PrefixoURL.getText().toString();
        Intent intent = new Intent( this, TelaLogin.class);
        intent.putExtra("PrefixoURL",URL);
        startActivity(intent);
    }

}
