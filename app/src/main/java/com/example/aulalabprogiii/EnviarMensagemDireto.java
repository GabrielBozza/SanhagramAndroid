package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class EnviarMensagemDireto extends AppCompatActivity {
    Button BotaoEnviar;
    EditText Mensagem,Destinatario;

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensagem_direto);

        Mensagem = (EditText)findViewById(R.id.MensagemDireta);
        Destinatario = (EditText)findViewById(R.id.NomeDestinatario);
        BotaoEnviar = (Button)findViewById(R.id.BotaoEnviar_Direto);

    }

    @Override
    public void onBackPressed() {//VOLTAR PARA LISTA DE CONVERSAS RECENTES

        final String login = getIntent().getStringExtra("Login");

        URL =  getIntent().getStringExtra("PrefixoURL") + IdentificadorURL + "&login=" + login;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;
                if(getIntent().getStringExtra("Login").equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                    intent.putExtra("Login", login);
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaConversas", json.toString());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                    intent.putExtra("Login", login);
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaConversas", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(EnviarMensagemDireto.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void EnviarMensagemDireto(View view) throws UnsupportedEncodingException {

        String texto_mensagem = Mensagem.getText().toString();
        String destinatario = Destinatario.getText().toString();

        if (texto_mensagem.matches("") || destinatario.matches("")) {
            Toast.makeText(this, "Escreva alguma coisa!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URLenviar = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=enviarMsgm&dispositivo=android";

        params = new RequestParams();
        params.put("remetente",getIntent().getStringExtra("Login"));
        params.put("destinatario",destinatario);
        params.put("texto_mensagem",texto_mensagem);

        client = new AsyncHttpClient();
        client.post(URLenviar,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL",getIntent().getStringExtra("PrefixoURL"));
                intent.putExtra("Destinatario", Destinatario.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(EnviarMensagemDireto.this, "Erro - Usuário/Grupo não encontrado!", Toast.LENGTH_SHORT).show();
                return;

            }
        });

    }

}
