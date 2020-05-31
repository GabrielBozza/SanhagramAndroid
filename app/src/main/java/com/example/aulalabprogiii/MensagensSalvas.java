package com.example.aulalabprogiii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class MensagensSalvas extends AppCompatActivity {

    Button BotaoSalvar, Texto_Mensagem,NomeConversa;
    EditText Mensagem;
    ScrollView mScrollView;

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens_salvas);

        Mensagem = findViewById(R.id.Mensagem);
        BotaoSalvar = findViewById(R.id.BotaoSalvar);
        mScrollView = findViewById(R.id.chatScrollView);

        String resultado = getIntent().getStringExtra("MensagensConversa");

        GradientDrawable Botao_Enviar = new GradientDrawable();
        Botao_Enviar.setColor(getResources().getColor(R.color.colorAccent));
        Botao_Enviar.setShape(GradientDrawable.RECTANGLE);
        Botao_Enviar.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});
        BotaoSalvar.setBackground(Botao_Enviar);


        try {

            JSONObject response = new JSONObject(resultado);

            try {

                GradientDrawable CabecalhoChat = new GradientDrawable();
                CabecalhoChat.setColor(getResources().getColor(R.color.transparent));
                CabecalhoChat.setShape(GradientDrawable.RECTANGLE);
                CabecalhoChat.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                LinearLayout layoutCabecalho = (LinearLayout) findViewById(R.id.Cabecalho);
                NomeConversa = new Button(this);
                NomeConversa.setText("Mensagens Salvas");
                NomeConversa.setTransformationMethod(null);
                NomeConversa.setWidth(550);
                NomeConversa.setPadding(44,16,10,16);
                NomeConversa.setTextSize(22);
                NomeConversa.setBackground(CabecalhoChat);
                NomeConversa.setGravity(Gravity.START);
                NomeConversa.setClickable(false);
                layoutCabecalho.addView(NomeConversa);

                for(int i=0;i<response.getJSONArray("MENSAGENS").length();i++) {

                    int raio =35;

                    GradientDrawable Bolha_direita = new GradientDrawable();
                    Bolha_direita.setColor(getResources().getColor(R.color.colorBolhaDireita));
                    Bolha_direita.setShape(GradientDrawable.RECTANGLE);
                    Bolha_direita.setCornerRadii(new float[]{raio, raio, raio, raio, raio, raio, raio, raio});

                    final String idmensagem = response.getJSONArray("MENSAGENS").getJSONObject(i).get("idmensagem").toString();
                    final String texto = response.getJSONArray("MENSAGENS").getJSONObject(i).get("texto_mensagem").toString();
                    String data_envio = response.getJSONArray("MENSAGENS").getJSONObject(i).get("data_envio").toString().substring(8,10)
                            +"/"+response.getJSONArray("MENSAGENS").getJSONObject(i).get("data_envio").toString().substring(5,7)
                            +"/"+response.getJSONArray("MENSAGENS").getJSONObject(i).get("data_envio").toString().substring(0,4);

                    String mensagem_salva = data_envio+"\n"+texto;

                    SpannableString stringBolhaMsgmDireita = new SpannableString(mensagem_salva);
                    stringBolhaMsgmDireita.setSpan(new AbsoluteSizeSpan(12, true), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    LinearLayout layout = findViewById(R.id.ListaMensagensSalvas);
                    Texto_Mensagem = new Button(this);

                    Texto_Mensagem.setTransformationMethod(null);
                    Texto_Mensagem.setTextColor(getResources().getColor(R.color.white));

                    Texto_Mensagem.setBackground(Bolha_direita);
                    Texto_Mensagem.setText(stringBolhaMsgmDireita);
                    Texto_Mensagem.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                               LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity=Gravity.CENTER;
                    params.setMargins(16,10,16,10);
                    Texto_Mensagem.setLayoutParams(params);
                    Texto_Mensagem.setPadding(16,16,16,16);
                    Texto_Mensagem.setTextSize(15);
                    Texto_Mensagem.setWidth(750);

                    Texto_Mensagem.setOnClickListener(new View.OnClickListener() {//ABRE ALERTA PARA PERGUNTAR SE A ACAO EH DESEJAVEL
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MensagensSalvas.this);
                            builder.setCancelable(true);
                            builder.setTitle("Enviar Mensagem");
                            builder.setMessage("Você deseja enviar esta mensagem à alguém?");
                            builder.setPositiveButton("Sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EscolherDestinatarioMensagem(texto);
                                        }
                                    });
                            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });

                    Texto_Mensagem.setOnLongClickListener(new View.OnLongClickListener() {//ABRE ALERTA PARA PERGUNTAR SE A ACAO EH DESEJAVEL
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MensagensSalvas.this);
                            builder.setCancelable(true);
                            builder.setTitle("Apagar Mensagem");
                            builder.setMessage("Você deseja apagar a mensagem?");
                            builder.setPositiveButton("Sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ApagarMensagem(idmensagem);
                                        }
                                    });
                            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            return true;
                        }
                    });

                    layout.addView(Texto_Mensagem);
                }

                GradientDrawable Espaco_vazio = new GradientDrawable();
                Espaco_vazio.setColor(getResources().getColor(R.color.transparent));
                Espaco_vazio.setShape(GradientDrawable.RECTANGLE);

                LinearLayout layout = findViewById(R.id.ListaMensagensSalvas);
                Texto_Mensagem = new Button(this);
                Texto_Mensagem.setText("\n\n\n");
                Texto_Mensagem.setBackground(Espaco_vazio);
                layout.addView(Texto_Mensagem);//ADICIONA ESPACO VAZIO APOS A UTLIMA MENSAGEM

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

        mScrollView.post(new Runnable() {//VAI P FINAL DA CONVERSA

            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    public void ApagarMensagem(String a) {

        final String idMensagem = a;

        URL = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=excluirMsgm&dispositivo=android"
                + "&remetente=" + getIntent().getStringExtra("Login") + "&destinatario=" + getIntent().getStringExtra("Destinatario")
                + "&idmensagem=" + idMensagem;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(MensagensSalvas.this, "Mensagem apagada!", Toast.LENGTH_SHORT).show();
                json = response;

                Intent intent = new Intent(getApplicationContext(), MensagensSalvas.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                intent.putExtra("Destinatario", getIntent().getStringExtra("Destinatario"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MensagensSalvas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

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

                Toast.makeText(MensagensSalvas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void SalvarMensagem(View view) throws UnsupportedEncodingException {

        String texto_mensagem = Mensagem.getText().toString();

        if (texto_mensagem.matches("")) {
            Toast.makeText(this, "Escreva alguma coisa!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URLenviar = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=enviarMsgm&dispositivo=android";

        params = new RequestParams();
        params.put("remetente",getIntent().getStringExtra("Login"));
        params.put("destinatario","ADefinirUsuario");
        params.put("texto_mensagem",texto_mensagem);

        Mensagem.setText("");//Limpa o campo da mensagem
        BotaoSalvar.setClickable(false);

        client = new AsyncHttpClient();
        client.post(URLenviar,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), MensagensSalvas.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL",getIntent().getStringExtra("PrefixoURL"));
                intent.putExtra("Destinatario", "ADefinirUsuario");
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoSalvar.setClickable(true);
                Toast.makeText(MensagensSalvas.this, "Erro!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void EscolherDestinatarioMensagem(String a){

        String texto_mensagem =a;

        Intent intent = new Intent( this, EnviarMensagemSalva.class);
        intent.putExtra("Login", getIntent().getStringExtra("Login"));
        intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
        intent.putExtra("TextoMensagemSalva", texto_mensagem);
        startActivity(intent);

    }

}
