package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ListaConversas extends AppCompatActivity {

    String PrefixoURL = "http://192.168.15.5:8080";//PARTE QUE MUDA QUANDO EU USO O LACALHOST RUN (PARA Q PESSOAS DE FORA DA MINHA REDE POSSAM ACESSAR)
    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarMsgm&dispositivo=android";
    String URL = PrefixoURL + IdentificadorURL;

    AsyncHttpClient client;
    JSONObject json;

    Button Conversa, Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conversas);

        Conversa = (Button) findViewById(R.id.Conversa);

        String resultado = getIntent().getStringExtra("ListaConversas");
        final String login = getIntent().getStringExtra("Login");

        try {

            JSONObject response = new JSONObject(resultado);

            try {
                for (int i = 0; i < response.getJSONArray("CONVERSAS").length(); i++) {

                    final String amigo = response.getJSONArray("CONVERSAS").get(i).toString();
                    LinearLayout layout = (LinearLayout) findViewById(R.id.Listaconversas);
                    Chat = new Button(this);
                    Chat.setText(amigo);
                    Chat.setId(i+1);

                    Chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VerChat(login, amigo);
                        }
                    });


                    layout.addView(Chat);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }

        Conversa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String remetente = "gbozza";
                String destinatario = "mbozza";

                URL = URL + "&remetente=" + remetente + "&destinatario=" + destinatario;

                client = new AsyncHttpClient();
                client.get(URL, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        json = response;

                        Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                        intent.putExtra("MensagensConversa", json.toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        Toast.makeText(ListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
            }
        });

    }

    public void VerChat(String a, String b) {
        final String remetente = a;
        String destinatario = b;

        URL = URL + "&remetente=" + remetente + "&destinatario=" + destinatario;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Login", remetente);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(ListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }


}
