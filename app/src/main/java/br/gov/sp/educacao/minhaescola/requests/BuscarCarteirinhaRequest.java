package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

public class BuscarCarteirinhaRequest {

    private MyPreferences mPref;

    private UsuarioQueries usuarioQueries;

    private int countTentativas = 0;

    public JSONObject executeRequest(String token, int cd_aluno, Context ctx) {

        usuarioQueries = new UsuarioQueries(ctx);

        try {

            mPref = new MyPreferences(ctx);

            countTentativas ++;

            //Cria o objeto para conexão, com o URL e método POST
            String urlRequest = (UrlServidor.URL_BUSCAR_CARTEIRINHA);

            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("POST", urlRequest, token);

            if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                JSONObject bodyJson = new JSONObject()
                        .put("CodigoAluno", cd_aluno);

                byte[] bodyJsonBytes = bodyJson.toString().getBytes("UTF-8");

                httpsURLConnection.setFixedLengthStreamingMode(bodyJsonBytes.length);

                OutputStream outputStream = httpsURLConnection.getOutputStream();

                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                bufferedOutputStream.write(bodyJsonBytes);
                bufferedOutputStream.flush();

                outputStream.flush();

                bufferedOutputStream.close();

                outputStream.close();
            }

            //Estabelece a conexão
            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            //Estado 200 e 201 significa conexão com sucesso
            if (statusCode == 201
                    || statusCode == 200) {

                String cartResponse = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão
                JSONObject carteirinhaJsonResponse = new JSONObject(cartResponse);

                return carteirinhaJsonResponse;
            }
            else if(statusCode == 400
                    && countTentativas == 1) {

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                //Revalida Token
                String newToken = revalidaTokenRequest.executeRequest(ctx);

                //Atualiza o Token
                usuarioQueries.atualizarToken(newToken);

                //Executa novo request com novo tolen
                return this.executeRequest(newToken, cd_aluno, ctx);
            }
            else {

                //Se o StatusCode != 200 || 201 ocorreu erro na conexão, retorna null
                return null;
            }
        }
        catch (Exception e){

            e.printStackTrace();

            return null;
        }
    }
}
