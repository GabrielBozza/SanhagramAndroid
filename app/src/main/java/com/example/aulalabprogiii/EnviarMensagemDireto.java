package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class EnviarMensagemDireto extends AppCompatActivity {
    Button BotaoEnviar;
    EditText Mensagem,Destinatario;

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";
    String PrefixoURL,nomeUSU,chaveUSU;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensagem_direto);

        Mensagem = findViewById(R.id.MensagemDireta);
        Destinatario = findViewById(R.id.NomeDestinatario);
        BotaoEnviar = findViewById(R.id.BotaoEnviar_Direto);

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

    }

    @Override
    public void onBackPressed() {//VOLTAR PARA LISTA DE CONVERSAS RECENTES

        URL =  PrefixoURL + IdentificadorURL + "&login=" + nomeUSU+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;
                if(nomeUSU.equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                    intent.putExtra("ListaConversas", json.toString());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                    intent.putExtra("ListaConversas", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(EnviarMensagemDireto.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void EnviarMensagem_Direto(View view) throws UnsupportedEncodingException {

        String texto_mensagem = Mensagem.getText().toString();
        String destinatario = Destinatario.getText().toString();

        if (texto_mensagem.matches("") || destinatario.matches("")) {
            Toast.makeText(this, "Escreva alguma coisa!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URLenviar = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=enviarMsgm&dispositivo=android";

        params = new RequestParams();
        params.put("login",nomeUSU);
        params.put("destinatario",destinatario);
        params.put("texto_mensagem",texto_mensagem);
        params.put("chaveUSU",chaveUSU);

        Mensagem.setText("");//Limpa o campo da mensagem
        BotaoEnviar.setClickable(false);

        client = new AsyncHttpClient();
        client.post(URLenviar,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Destinatario", Destinatario.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoEnviar.setClickable(true);
                Toast.makeText(EnviarMensagemDireto.this, "Erro - Usuário/Grupo não encontrado!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
