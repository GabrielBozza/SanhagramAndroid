package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TelaCadastro extends AppCompatActivity {

    TextView ListaConversas;
    Button Conversa;
    String URL = "http://192.168.15.5:8080/SanhagramServletsJSP/UsuarioControlador?acao=lismsgm&dispositivo=android";
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        String resultado = getIntent().getStringExtra("ListaConversas");
        String login = getIntent().getStringExtra("Login");
        ListaConversas = (TextView)findViewById(R.id.ListaConversas);
        ListaConversas.setText(login+"\n"+resultado);

        Conversa = (Button)findViewById(R.id.Conversa);

        Conversa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                String remetente = "gbozza";
                String destinatario = "mbozza";
                URL = URL+"&remetente="+remetente+"&destinatario="+destinatario;
                client = new AsyncHttpClient();
                client.get(URL,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        super.onSuccess(statusCode, headers,response);
                        //Toast.makeText(TelaCadastro.this,"Deu certo!!!!"+response,Toast.LENGTH_LONG).show();
                        json = response;
                        try {
                            ListaConversas.setText(response.getJSONArray("MENSAGENS").getJSONObject(0).get("texto_mensagem").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //System.out.println(json);
                        //Intent intent = new Intent( getApplicationContext(), TelaCadastro.class);
                        //intent.putExtra("ListaConversas",json.toString());
                        //startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                        super.onFailure(statusCode,headers,responseString,throwable);
                        Toast.makeText(TelaCadastro.this,"Erro!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
            }
        });

    }
}
