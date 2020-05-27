package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChatUsuario extends AppCompatActivity {

    TextView corpochat;
    String PrefixoURL = "http://192.168.15.5:8080";//PARTE QUE MUDA QUANDO EU USO O LACALHOST RUN (PARA Q PESSOAS DE FORA DA MINHA REDE POSSAM ACESSAR)
    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = PrefixoURL+IdentificadorURL;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_usuario);

        TextView corpochat = (TextView)findViewById(R.id.corpochat);

        String resultado = getIntent().getStringExtra("MensagensConversa");

        String Mensagens ="";

        try {

            JSONObject response = new JSONObject(resultado);

            try {
                for(int i=0;i<response.getJSONArray("MENSAGENS").length();i++) {
                    //TextView mensagem = new TextView(TelaCadastro.this);
                    //mensagem.setText(response.getJSONArray("MENSAGENS").getJSONObject(i).get("texto_mensagem").toString());
                    Mensagens += response.getJSONArray("MENSAGENS").getJSONObject(i).get("remetente").toString();
                    Mensagens += " : ";
                    Mensagens += response.getJSONArray("MENSAGENS").getJSONObject(i).get("texto_mensagem").toString();
                    Mensagens += " - ";
                    Mensagens += response.getJSONArray("MENSAGENS").getJSONObject(i).get("data_envio").toString().substring(11,16);
                    Mensagens += "\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

        corpochat.setText(Mensagens);


    }

    @Override
    public void onBackPressed() {

        final String login = getIntent().getStringExtra("Login");

        URL = URL + "&login=" + login;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent( getApplicationContext(), ListaConversas.class);
                intent.putExtra("Login",login);
                intent.putExtra("ListaConversas",json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

}
