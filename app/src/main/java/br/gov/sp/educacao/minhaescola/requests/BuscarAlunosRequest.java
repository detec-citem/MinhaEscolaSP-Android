package br.gov.sp.educacao.minhaescola.requests;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;

public class BuscarAlunosRequest {

    public JSONObject executeBuscarAlunosRequest(String token) {

        try {

            //Cria o objeto para conexão, com o URL e método POST
            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("GET", UrlServidor.URL_BUSCAR_ALUNOS, token);

            //Estabelece a conexão
            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            //Estado 200 e 201 significa conexão com sucesso
            if (statusCode == 201 || statusCode == 200) {

                String loginResponse = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão
                JSONObject AlunosJsonResponse = new JSONObject(loginResponse);

                //Caso tenha mensagem de erro na resposta retorna null
                if (AlunosJsonResponse.has("Erro")) {

                    return null;
                }
                else {

                    //Se não houver mensagem de erro retorna o objeto JSON
                    return AlunosJsonResponse;
                }
            }
            else{

                //Se o StatusCode != 200 || 201 ocorreu erro na conexão, retorna null
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
}
