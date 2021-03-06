package com.example.aulalabprogiii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChatUsuario extends AppCompatActivity {

    Button BotaoEnviar, Texto_Mensagem,NomeConversa;
    EditText Mensagem;
    ScrollView mScrollView;

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";
    String PrefixoURL,nomeUSU,chaveUSU;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_usuario);

        Mensagem = findViewById(R.id.Mensagem);
        BotaoEnviar = findViewById(R.id.BotaoEnviar);
        mScrollView = findViewById(R.id.chatScrollView);

        String resultado = getIntent().getStringExtra("MensagensConversa");

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

        GradientDrawable Botao_Enviar = new GradientDrawable();
        Botao_Enviar.setColor(getResources().getColor(R.color.colorAccent));
        Botao_Enviar.setShape(GradientDrawable.RECTANGLE);
        Botao_Enviar.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});
        BotaoEnviar.setBackground(Botao_Enviar);

        try {

            JSONObject response = new JSONObject(resultado);

            try {

                GradientDrawable CabecalhoChat = new GradientDrawable();
                CabecalhoChat.setColor(getResources().getColor(R.color.transparent));
                CabecalhoChat.setShape(GradientDrawable.RECTANGLE);
                CabecalhoChat.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                final LinearLayout layoutCabecalho = findViewById(R.id.Cabecalho);
                NomeConversa = new Button(this);
                NomeConversa.setText(getIntent().getStringExtra("Destinatario"));
                NomeConversa.setTransformationMethod(null);
                NomeConversa.setWidth(800);
                NomeConversa.setPadding(32,16,10,16);
                NomeConversa.setTextSize(22);
                NomeConversa.setBackground(CabecalhoChat);
                NomeConversa.setGravity(Gravity.START);
                LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                paramss.setMargins(16,0,16,0);
                NomeConversa.setLayoutParams(paramss);

                NomeConversa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<layoutCabecalho.getChildCount();i++){
                            View child=layoutCabecalho.getChildAt(i);
                            child.setClickable(false);
                            child.setFocusableInTouchMode(false);
                        }
                        Refresh(v);
                    }
                });

                layoutCabecalho.addView(NomeConversa);

                if(response.getJSONArray("MENSAGENS").getJSONObject(0).get("flag_grupo").toString().equals("1")){

                    GradientDrawable Botao_SairGrupo = new GradientDrawable();
                    Botao_SairGrupo.setColor(getResources().getColor(R.color.colorBotaoSairGrupo));
                    Botao_SairGrupo.setShape(GradientDrawable.RECTANGLE);
                    Botao_SairGrupo.setCornerRadii(new float[]{35, 35, 35, 35, 35, 35, 35, 35});

                    NomeConversa = new Button(this);
                    NomeConversa.setText("Sair");
                    NomeConversa.setTransformationMethod(null);
                    NomeConversa.setWidth(70);
                    NomeConversa.setTextColor(getResources().getColor(R.color.white));
                    NomeConversa.setPadding(8,8,8,8);
                    NomeConversa.setTextSize(15);
                    NomeConversa.setBackground(Botao_SairGrupo);
                    NomeConversa.setGravity(Gravity.CENTER);
                    paramss.setMargins(16,0,32,2);
                    NomeConversa.setLayoutParams(paramss);

                    NomeConversa.setOnClickListener(new View.OnClickListener() {//ABRE ALERTA PARA PERGUNTAR SE A ACAO EH DESEJAVEL
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatUsuario.this);
                            builder.setCancelable(true);
                            builder.setTitle("Sair do "+getIntent().getStringExtra("Destinatario"));
                            builder.setMessage("Você deseja sair do grupo?");
                            builder.setPositiveButton("Sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SairGrupo(getIntent().getStringExtra("Destinatario"));
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

                    Space sp =new Space(this);
                    layoutCabecalho.addView(sp);
                    layoutCabecalho.addView(NomeConversa);

                }

                for(int i=0;i<response.getJSONArray("MENSAGENS").length();i++) {

                    int raio =35;
                    GradientDrawable Bolha_esquerda = new GradientDrawable();
                    Bolha_esquerda.setColor(getResources().getColor(R.color.colorBolhaEsquerda));
                    Bolha_esquerda.setShape(GradientDrawable.RECTANGLE);
                    Bolha_esquerda.setCornerRadii(new float[]{raio, raio, raio, raio, raio, raio, 5, 5});

                    GradientDrawable Bolha_AvisoGrupo = new GradientDrawable();
                    Bolha_AvisoGrupo.setColor(getResources().getColor(R.color.colorBolha_AvisoGrupo));
                    Bolha_AvisoGrupo.setShape(GradientDrawable.RECTANGLE);
                    Bolha_AvisoGrupo.setCornerRadii(new float[]{raio, raio, raio, raio, raio, raio, 5, 5});

                    GradientDrawable Bolha_direita = new GradientDrawable();
                    Bolha_direita.setColor(getResources().getColor(R.color.colorBolhaDireita));
                    Bolha_direita.setShape(GradientDrawable.RECTANGLE);
                    Bolha_direita.setCornerRadii(new float[]{raio, raio, raio, raio, 5, 5, raio, raio});

                    String remetente = response.getJSONArray("MENSAGENS").getJSONObject(i).get("remetente").toString();
                    final String idmensagem = response.getJSONArray("MENSAGENS").getJSONObject(i).get("idmensagem").toString();
                    String destinatario = response.getJSONArray("MENSAGENS").getJSONObject(i).get("destinatario").toString();
                    String texto = response.getJSONArray("MENSAGENS").getJSONObject(i).getString("texto_mensagem");
                    String hora_envio = response.getJSONArray("MENSAGENS").getJSONObject(i).get("data_envio").toString().substring(11,16);
                    String bolha_mensagem_esquerda = " "+hora_envio+"\n "+remetente+"\n "+texto;
                    String bolha_mensagem_direita = hora_envio+" \n "+texto+" ";

                    SpannableString stringBolhaMsgmEsquerda = new SpannableString(bolha_mensagem_esquerda);
                    stringBolhaMsgmEsquerda.setSpan(new AbsoluteSizeSpan(12, true), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBolhaMsgmEsquerda.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorChat)), 8, 8+remetente.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SpannableString stringBolhaAvisoGrupo = new SpannableString(bolha_mensagem_esquerda);
                    stringBolhaAvisoGrupo.setSpan(new AbsoluteSizeSpan(12, true), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBolhaAvisoGrupo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorChatAvisoGrupo)), 8, 8+remetente.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SpannableString stringBolhaMsgmDireita = new SpannableString(bolha_mensagem_direita);
                    stringBolhaMsgmDireita.setSpan(new AbsoluteSizeSpan(12, true), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    LinearLayout layout = findViewById(R.id.ConversaUsuario);
                    Texto_Mensagem = new Button(this);
                    Texto_Mensagem.setTransformationMethod(null);
                    Texto_Mensagem.setTextColor(getResources().getColor(R.color.white));

                    if(remetente.equals(nomeUSU)) {
                        Texto_Mensagem.setBackground(Bolha_direita);
                        Texto_Mensagem.setText(stringBolhaMsgmDireita);
                        Texto_Mensagem.setGravity(Gravity.END);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.gravity=Gravity.END;
                        params.setMargins(16,10,8,10);
                        Texto_Mensagem.setLayoutParams(params);

                        Texto_Mensagem.setOnLongClickListener(new View.OnLongClickListener() {//ABRE ALERTA PARA PERGUNTAR SE A ACAO EH DESEJAVEL
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatUsuario.this);
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

                    }
                    else if (remetente.equals(destinatario)){
                        Texto_Mensagem.setBackground(Bolha_AvisoGrupo);
                        Texto_Mensagem.setText(stringBolhaAvisoGrupo);
                        Texto_Mensagem.setGravity(Gravity.START);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8,10,16,10);
                        Texto_Mensagem.setLayoutParams(params);
                        Texto_Mensagem.setClickable(false);
                    }
                    else {
                        Texto_Mensagem.setBackground(Bolha_esquerda);
                        Texto_Mensagem.setText(stringBolhaMsgmEsquerda);
                        Texto_Mensagem.setGravity(Gravity.START);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8,10,16,10);
                        Texto_Mensagem.setLayoutParams(params);
                        Texto_Mensagem.setClickable(false);
                    }
                    Texto_Mensagem.setPadding(16,16,16,16);
                    Texto_Mensagem.setTextSize(15);

                    layout.addView(Texto_Mensagem);
                }

                GradientDrawable Espaco_vazio = new GradientDrawable();
                Espaco_vazio.setColor(getResources().getColor(R.color.transparent));
                Espaco_vazio.setShape(GradientDrawable.RECTANGLE);

                LinearLayout layout = findViewById(R.id.ConversaUsuario);
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

    @Override
    public void onBackPressed() {

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
                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void EnviarMensagem(View view) {

        String texto_mensagem = Mensagem.getText().toString();
        final String remetente = nomeUSU;
        final String destinatario = getIntent().getStringExtra("Destinatario");

        if (texto_mensagem.matches("")) {
            Toast.makeText(this, "Escreva alguma coisa!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URLenviar = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=enviarMsgm&dispositivo=android";

        params = new RequestParams();
        params.put("login",remetente);
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
                intent.putExtra("Destinatario", destinatario);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoEnviar.setClickable(true);
                Toast.makeText(ChatUsuario.this, "Erro - Usuário removido do sistema!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SairGrupo(String a){

        String nomeGrupo = a ;

        URL =  PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=sairDoGrupo&dispositivo=android"
                + "&login=" + nomeUSU + "&nomeGrupo=" + nomeGrupo+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ChatUsuario.this, "Você saiu do grupo!", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApagarMensagem(String a) {

        final String idMensagem = a;

        URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=excluirMsgm&dispositivo=android"
                + "&login=" + nomeUSU + "&destinatario=" + getIntent().getStringExtra("Destinatario")
                + "&idmensagem=" + idMensagem+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ChatUsuario.this, "Mensagem apagada!", Toast.LENGTH_SHORT).show();
                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Destinatario", getIntent().getStringExtra("Destinatario"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Refresh(View view) {

        URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=listarMsgm&dispositivo=android"
                + "&login=" + nomeUSU + "&destinatario=" + getIntent().getStringExtra("Destinatario")+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ChatUsuario.this, "Chat atualizado!", Toast.LENGTH_SHORT).show();
                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Destinatario", getIntent().getStringExtra("Destinatario"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
