package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

/**
 * Classe que faz o request para verificar se o aluno pode fazer a avaliação no dia
 * ou se já foi feita.
 */

public class AlimentacaoRequest {

    //Controla o número de tentativas (Máx - 3)

    private static int mTentativas = 0;

    //Método que prepara o JSON, estabelece a conexão e recebe a resposta

    public static int executeAlimentacaoRequest(Context context, String token) {



        try {

            //Cria o objeto para conexão, com o URL, token do usuário e método GET

            HttpsURLConnection httpsURLConnection = ConnectionFactory.createHttpsUrlConnection("GET",
                                                                                               UrlServidor.URL_PODE_RESPONDER,
                                                                                               token);
            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            //Soma uma tentativa

            mTentativas++;

            //Estado 200 e 201 significa conexão com sucesso

            if (statusCode == 201
                    || statusCode == 200) {

                String requestResponse = ConnectionReader.readStringFromHttpsURLConnection(true,
                                                                                         httpsURLConnection);
                //Fecha a conexão

                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão

                JSONObject requestJsonResponse = new JSONObject(requestResponse);

                //Caso tenha mensagem de erro na resposta retorna null

                if (requestJsonResponse.has("Erro")) {

                    //Se houver mensagem de erro retorna 2 (Conexão com sucesso, mas resposta com erro)

                    Log.e("Pode responder", "Erro");

                    return 2;
                }
                else {

                    String podeResponder = requestJsonResponse.getString("PodeResponder");

                    if (podeResponder.equals("true")) {

                        //Zera o número de tentativas

                        mTentativas = 0;

                        Log.e("Pode responder", "true");

                        //Se a reposta for 'ture' retorna 1 (Pode avaliar no dia)

                        return 1;
                    }

                    else {

                        //Se a resposta for 'false' retorna 0 (Já avaliou no dia)

                        Log.e("Pode responder", "false");

                        return 0;
                    }
                }
            }
            else {

                //Se o código de reposta for != 200 || 201 tente revalidar o token

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                String newToken = revalidaTokenRequest.executeRequest(context);

                //Cria o objeto para fazer queries

                UsuarioQueries usuarioQueries = new UsuarioQueries(context);

                //O número de tentativas para revalidar o token é limitado em 3

                if(newToken != null
                        && mTentativas < 3) {

                    //Atualiza o token do usuário no SQLite

                    usuarioQueries.atualizarToken(newToken);

                    //Executa novamente o executeRequest desta classe

                    return executeAlimentacaoRequest(context, newToken);
                }
                else {

                    //Se o token estiver nulo ou o número máximo de tentativas for alcançado
                    //retorna 2 (Ocorreu um erro)

                    return 2;
                }
            }
        }
        catch (IOException | JSONException e) {

            //Se houver exceção retorna 2 (Ocorreu um erro)

            return 2;
        }
    }
}

