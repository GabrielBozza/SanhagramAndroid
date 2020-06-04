package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarMsgm&dispositivo=android";
    String URL = "";
    String PrefixoURL,nomeUSU;

    AsyncHttpClient client;
    JSONObject json;

    Button Chat,BotaoSalvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conversas);

        String resultado = getIntent().getStringExtra("ListaConversas");

        BotaoSalvas = findViewById(R.id.BotaoMensagensSalvas);

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);

        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");

        if(nomeUSU.length()==0) {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LOGIN", getIntent().getStringExtra("Login")); //GUARDA O NOME DO USUARIO LOGADO
            editor.commit();
            nomeUSU = getIntent().getStringExtra("Login");

        }

        try {

            JSONObject response = new JSONObject(resultado);

            try {
                GradientDrawable Titulo = new GradientDrawable();
                Titulo.setColor(getResources().getColor(R.color.transparent));
                Titulo.setShape(GradientDrawable.RECTANGLE);
                Titulo.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                final LinearLayout layout = findViewById(R.id.Listaconversas);
                Chat = new Button(this);
                Chat.setText("Conversas");
                Chat.setTransformationMethod(null);
                Chat.setWidth(850);
                Chat.setPadding(10,10,10,10);
                Chat.setTextSize(23);
                Chat.setBackground(Titulo);
                Chat.setGravity(Gravity.CENTER);

                Chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BotaoSalvas.setClickable(false);
                        for(int i=0;i<layout.getChildCount();i++){
                            View child=layout.getChildAt(i);
                            child.setClickable(false);
                            child.setFocusableInTouchMode(false);
                        }
                        Refresh(v);
                    }
                });

                layout.addView(Chat);

                for (int i = 0; i < response.getJSONArray("CONVERSAS").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.Conversa));
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String amigo = response.getJSONArray("CONVERSAS").get(i).toString();
                    Chat = new Button(this);
                    Chat.setText(amigo);
                    Chat.setTransformationMethod(null);
                    Chat.setWidth(950);
                    Chat.setPadding(24,12,16,12);
                    Chat.setTextSize(21);
                    Chat.setBackground(Opcao_Conversa);
                    Chat.setGravity(Gravity.START);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(16,10,16,10);
                    params.gravity=Gravity.CENTER;
                    Chat.setLayoutParams(params);

                    Chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BotaoSalvas.setClickable(false);
                            for(int i=0;i<layout.getChildCount();i++){
                                View child=layout.getChildAt(i);
                                child.setClickable(false);
                                child.setFocusableInTouchMode(false);
                            }
                            VerChat(amigo);
                        }
                    });

                    layout.addView(Chat);
                }

                Chat = new Button(this);
                Chat.setText("\n\n\n");
                Chat.setBackground(Titulo);
                layout.addView(Chat);//ADICIONA ESPACO VAZIO APOS A UTLIMA MENSAGEM

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }

    }

    public void VerChat(String b) {

        final String destinatario = b;

        URL = PrefixoURL + IdentificadorURL + "&remetente=" + nomeUSU + "&destinatario=" + destinatario;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Destinatario", destinatario);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ListaConversas.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void VerMensagensSalvas(View view) {

        BotaoSalvas.setClickable(false);

        URL = PrefixoURL + IdentificadorURL + "&remetente=" + nomeUSU
                + "&destinatario=ADefinirUsuario";

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), MensagensSalvas.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Destinatario", "ADefinirUsuario");
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoSalvas.setClickable(true);
                Toast.makeText(ListaConversas.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void abrirTelaEnviarMensagemDireto(View view){

        Intent intent = new Intent( this, EnviarMensagemDireto.class);
        startActivity(intent);

    }

    public void Sair(View view){

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Intent intent = new Intent( this, MainActivity.class);
        intent.putExtra("PrefixoURL", prefs.getString("PREFIXO_URL", ""));

        editor.putString("LOGIN", "");     //RESET TO DEFAULT VALUE
        editor.putString("PREFIXO_URL", "");     //RESET TO DEFAULT VALUE
        editor.commit();

        startActivity(intent);

    }
    public void Refresh(View view) {

        URL =  PrefixoURL+ "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android"
                + "&login=" + nomeUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ListaConversas.this, "Lista atualizada!", Toast.LENGTH_SHORT).show();

                json = response;

                Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                intent.putExtra("ListaConversas", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(ListaConversas.this,"Para realizar logout clique no botão sair!",Toast.LENGTH_LONG).show();
    }
}
