package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.AvaliacaoAlimentacao;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;


 //Classe que estabelece a conexão e envia a avaliação alimentar


public class EnviarAvaliacaoAlimentacaoRequest {

    //Controla o número de tentativas (Máx - 2)

    private static int mTentativas = 0;

    String token;

    //Método que prepara o JSON, estabelece a conexão e recebe a resposta

    public int executeRequest(Context context, AvaliacaoAlimentacao avaliacaoAlimentacao, int cd_aluno, int cd_turma) {

        UsuarioQueries usuarioQueries = new UsuarioQueries(context);

        token = usuarioQueries.getToken();

        if(avaliacaoAlimentacao.getQuestao3() == null){

            avaliacaoAlimentacao.setQuestao3(new int[]{});
        }

        if(avaliacaoAlimentacao.getQuestao4() == null){

            avaliacaoAlimentacao.setQuestao4(new int[]{});
        }

        if(avaliacaoAlimentacao.getQuestao5() == null){

            avaliacaoAlimentacao.setQuestao5(new int[]{});
        }

        try {

            //Cria o objeto para conexão, com o URL, método POST e token do usuário

            HttpsURLConnection httpsURLConnection = ConnectionFactory.createHttpsUrlConnection("POST",
                    UrlServidor.URL_ENVIAR_AVALIACAO_ALIMENTACAO,
                    token);
            //Cria o objeto JSON para a conexão, adiciona o login e senha no objeto JSON

            JSONObject loginJson = new JSONObject().put("Questao1", new JSONArray(avaliacaoAlimentacao.getQuestao1()))
                    .put("Questao2", new JSONArray(avaliacaoAlimentacao.getQuestao2()))
                    .put("Questao3", new JSONArray(avaliacaoAlimentacao.getQuestao3()))
                    .put("Questao4", new JSONArray(avaliacaoAlimentacao.getQuestao4()))
                    .put("Questao5", new JSONArray(avaliacaoAlimentacao.getQuestao5()))
                    .put("CodigoTurma", cd_turma);
            if(IS_DEBUG) {

                Log.e("Json Alimentação:::",loginJson.toString());
            }

            //Soma uma tentativa

            mTentativas++;

            //Transforma o objeto JSON em um objeto byte[] com padrão UTF-8

            byte[] loginJsonBytes = loginJson.toString()
                    .getBytes("UTF-8");

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

                String requestResponse = ConnectionReader.readStringFromHttpsURLConnection(true,
                        httpsURLConnection);
                //Fecha a conexão

                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão

                JSONObject requestJsonResponse = new JSONObject(requestResponse);

                if (requestJsonResponse.has("Erro")) {

                    //Caso tenha mensagem de erro na resposta retorna 400

                    return 400;
                }
                else {

                    //Zera o número de tentativas

                    mTentativas = 0;

                    //Se não houver mensagem de erro retorna o código da resposta 200 || 201

                    return statusCode;
                }
            }
            else {

                if(mTentativas < 3) {

                    RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                    token = revalidaTokenRequest.executeRequest(context);

                    return executeRequest(context, avaliacaoAlimentacao, cd_aluno, cd_turma);
                }
                else {

                    //Se o token estiver nulo ou o número máximo de tentativas for alcançado
                    //retorna o código da resposta (Ocorreu um erro)

                    return statusCode;
                }
            }
        }
        catch (IOException | JSONException e) {

            //Se houver exceção retorna 400

            if(IS_DEBUG){

                Log.e("LoginRequest", "Erro: " + e);
            }

            return 400;
        }
    }
}
