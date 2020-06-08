package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TelaLogin extends AppCompatActivity {

    EditText NomeUsuario,Senha;
    Button Login;

    String nomeUSU,senha,PrefixoURL,URL,chaveUSU;
    String IdentificadorURL = "/SanhagramServletsJSP/autenticador?dispositivo=android";

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

        URL = PrefixoURL+IdentificadorURL;

        NomeUsuario = (EditText)findViewById(R.id.NomeUsuario);
        Senha = (EditText)findViewById(R.id.Senha);
        Login = (Button)findViewById(R.id.BotaoLogin);

        if(nomeUSU.length()>0){//USUARIO LOGOU E NAO SAIU DA CONTA

            URL =  PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android" + "&login=" + nomeUSU
            +"&chaveUSU="+chaveUSU;
            client = new AsyncHttpClient();
            client.get(URL, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Toast.makeText(TelaLogin.this, "Login automático "+nomeUSU+" !", Toast.LENGTH_SHORT).show();
                    json = response;

                        if(nomeUSU.equals("admin")) {
                            Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                            intent.putExtra("Login", nomeUSU);
                            intent.putExtra("ListaConversas", json.toString());
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                            intent.putExtra("Login", nomeUSU);
                            intent.putExtra("ListaConversas", json.toString());
                            startActivity(intent);
                        }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(TelaLogin.this, "Erro!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                nomeUSU = NomeUsuario.getText().toString();
                senha = Senha.getText().toString();

                if (nomeUSU.matches("")||senha.matches("")) {
                    Toast.makeText(TelaLogin.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Login.setClickable(false);//BLOQUEIA O BOTAO E SOH O HABILITA NOVAMENTE SE OCORRER ALGUM ERRO

                params = new RequestParams();
                params.put("nome",nomeUSU);
                params.put("senha",senha);

                client = new AsyncHttpClient();
                client.post(URL,params,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        super.onSuccess(statusCode, headers,response);

                        json = response;

                        try {
                            if(json.getString("RESULTADO").equals("SUCESSO") && nomeUSU.equals("admin")) {
                                Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                                intent.putExtra("Login", nomeUSU);
                                intent.putExtra("ListaConversas", json.toString());
                                startActivity(intent);
                            }
                            else if (json.getString("RESULTADO").equals("SUCESSO")){
                                Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                                intent.putExtra("Login", nomeUSU);
                                intent.putExtra("ListaConversas", json.toString());
                                startActivity(intent);
                            }
                            else {
                                Login.setClickable(true);
                                Toast.makeText(TelaLogin.this, "Erro!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                        super.onFailure(statusCode,headers,responseString,throwable);
                        Login.setClickable(true);
                        Toast.makeText(TelaLogin.this,"Erro-Login e/ou senha incorretos!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(TelaLogin.this,"Você não está logado!",Toast.LENGTH_SHORT).show();
    }

}
