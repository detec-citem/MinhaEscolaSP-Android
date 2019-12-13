package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

public class BuscarHorariosRequest {

    private int countTentativas = 0;

    public JSONObject executeRequest(String token, int cd_turma, Context ctx) {

        try {

            //Cria o objeto para conexão, com o URL e método POST
            String urlRequest = (UrlServidor.URL_BUSCAR_HORARIOS + "?turma=" + String.valueOf(cd_turma));

            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("GET", urlRequest, token);

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
                JSONObject horariosJsonResponse = new JSONObject(loginResponse);

                //Caso tenha mensagem de erro na resposta retorna null
                if (horariosJsonResponse.has("Erro")) {

                    return null;
                }
                else {

                    //Se não houver mensagem de erro retorna o objeto JSON
                    JSONArray jsonAulas = horariosJsonResponse.getJSONArray("Aulas");

                    return horariosJsonResponse;
                }
            }
            else if(statusCode == 400
                        && countTentativas < 2) {

                countTentativas ++;

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                String tokenRevalidado = revalidaTokenRequest.executeRequest(ctx);

                return this.executeRequest(tokenRevalidado, cd_turma, ctx);
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
