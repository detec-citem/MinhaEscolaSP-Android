package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;

public class LoginRequest {

    public JSONObject executeLoginRequest(String username, String password, Context ctx) {

        MyPreferences mPref = new MyPreferences(ctx);

        try {

            //Cria o objeto para conexão, com o URL e método POST
            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("POST", UrlServidor.URL_LOGIN, null);

            //Cria o objeto JSON para a conexão, adiciona o login e senha no objeto JSON
            JSONObject loginJson = new JSONObject().put("user", username)
                    .put("senha", password)
                    .put("RefLogin","App Minha Escola SP");

            if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO) {

                loginJson.put("aluno",true);
            }

            //Transforma o objeto JSON em um objeto byte[] com padrão UTF-8
            byte[] loginJsonBytes = loginJson.toString().getBytes("UTF-8");

            httpsURLConnection.setFixedLengthStreamingMode(loginJsonBytes.length);

            OutputStream outputStream = httpsURLConnection.getOutputStream();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(loginJsonBytes);
            bufferedOutputStream.flush();

            outputStream.flush();

            bufferedOutputStream.close();

            outputStream.close();

            //Estabelece a conexão
            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            //Estado 200 e 201 significa conexão com sucesso
            if (statusCode == 201
                    || statusCode == 200) {

                String loginResponse = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão
                JSONObject loginJsonResponse = new JSONObject(loginResponse);

                //Caso tenha mensagem de erro na resposta retorna null
                if (loginJsonResponse.has("Erro")) {

                    return null;
                }
                else {

                    //Se não houver mensagem de erro retorna o objeto JSON
                    return loginJsonResponse;
                }
            }
            else {

                //Se o StatusCode != 200 || 201 ocorreu erro na conexão, retorna null
                String loginResponse = ConnectionReader.readStringFromHttpsURLConnection(false, httpsURLConnection);

                return null;
            }

        }
        catch (Exception e) {

            //Se houver exceção retorna null
            if (IS_DEBUG) {

                e.printStackTrace();
            }

            return null;
        }
    }

    public boolean executeSelecionarPerfilRequest(String token, Context ctx) {

        MyPreferences mPref = new MyPreferences(ctx);

        String url = UrlServidor.URL_SELECIONAR_PERFIL + "?perfilSelecionado=7&token=" + token;

        try {

            //Cria o objeto para conexão, com o URL e método GET
            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("GET", url, null);

            //Estabelece a conexão
            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            //Estado 200 e 201 significa conexão com sucesso
            if (statusCode == 201
                    || statusCode == 200) {

                String loginResponse = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                //Caso tenha mensagem OK, retorna true
                if (loginResponse.contains("OK")) {

                    return true;
                }
                //Se não houver mensagem OK retorna false
                else {

                    return false;
                }
            }
            else{

                return false;
            }
        }
        catch (IOException e) {

            e.printStackTrace();

            return false;
        }
    }
}
