package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

public class EnviarMatriculaRequest {

    private UsuarioQueries usuarioQueries;

    private int countTentativas = 0;

    public JSONObject executeRequest(JSONObject jsonEnvio, Context ctx){

        usuarioQueries = new UsuarioQueries(ctx);

        String token = usuarioQueries.getToken();

        try{

            countTentativas ++;

            String urlRequest = (UrlServidor.URL_REMATRICULA);

            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("POST", urlRequest, token);

            byte[] bodyJsonBytes = jsonEnvio.toString().getBytes("UTF-8");

            httpsURLConnection.setFixedLengthStreamingMode(bodyJsonBytes.length);

            OutputStream outputStream = httpsURLConnection.getOutputStream();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(bodyJsonBytes);
            bufferedOutputStream.flush();

            outputStream.flush();

            bufferedOutputStream.close();

            outputStream.close();

            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            if (statusCode == 201 || statusCode == 200) {

                String stringResponse = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão
                JSONObject jsonResponse = new JSONObject(stringResponse);

                return jsonResponse;
            }
            else if(statusCode == 400 && countTentativas == 1) {

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                //Revalida Token
                String newToken = revalidaTokenRequest.executeRequest(ctx);

                //Atualiza o Token
                usuarioQueries.atualizarToken(newToken);

                //Executa novo request com novo tolen
                return this.executeRequest(jsonEnvio, ctx);
            }
            else if(statusCode == 400 && countTentativas > 1){

                return new JSONObject().put("Erro", "Erro de conexão com o servidor, aguarde um momento e tente novamente.");

            }
            else {

                try {

                    return new JSONObject(ConnectionReader.readStringFromHttpsURLConnection(false, httpsURLConnection));
                }
                catch (JSONException e1) {
                    e1.printStackTrace();
                    return null;
                }
            }
        }
        catch (Exception e){

            e.printStackTrace();

            try {

                return new JSONObject().put("Erro", "Erro na requisição");
            }
            catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }
}
