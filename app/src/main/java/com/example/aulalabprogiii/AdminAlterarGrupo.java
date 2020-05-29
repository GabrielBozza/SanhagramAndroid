package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminAlterarGrupo extends AppCompatActivity {

    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alterar_grupo);
        TextView textView3 = (TextView)findViewById(R.id.textView3);

        String resultado = getIntent().getStringExtra("UsuariosGrupo");
        final String login = getIntent().getStringExtra("Login");

        try {
            JSONObject response = new JSONObject(resultado);
            textView3.setText(resultado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        final String login = getIntent().getStringExtra("Login");

        String URL = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=listarGrupos&dispositivo=android" + "&login=" + login;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;
                if (getIntent().getStringExtra("Login").equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminListarGrupos.class);
                    intent.putExtra("Login", login);
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaGrupos", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminAlterarGrupo.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
