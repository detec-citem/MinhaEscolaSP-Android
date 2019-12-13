package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Turma;

import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

public class BuscarNotasFrequenciaRequest {

    private UsuarioQueries usuarioQueries;

    private TurmaQueries turmaQueries;

    private MyPreferences mPref;

    private int mCount = 0;

    private String token;

    private Turma turma;

    public JSONArray executeRequest(int cd_aluno, int cd_turma, Context ctx) {

        usuarioQueries = new UsuarioQueries(ctx);

        turmaQueries = new TurmaQueries(ctx);

        token = usuarioQueries.getToken();

        turma = turmaQueries.getTurma(cd_turma);

        mPref = new MyPreferences(ctx);

        try {

            HttpsURLConnection httpsURLConnection = ConnectionFactory.createHttpsUrlConnection("POST",
                    UrlServidor.URL_BUSCAR_NOTAS_FREQUENCIA, token);

            JSONObject corpoJson = new JSONObject()
                    .put("CodigoTurma", turma.getCd_turma())
                    .put("AnoLetivo", turma.getAno_letivo());

            if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                corpoJson = new JSONObject()
                        .put("CodigoTurma", turma.getCd_turma())
                        .put("CodigoAluno", cd_aluno)
                        .put("AnoLetivo", turma.getAno_letivo());
            }

            byte[] loginJsonBytes = corpoJson.toString()
                    .getBytes("UTF-8");

            httpsURLConnection.setFixedLengthStreamingMode(loginJsonBytes.length);

            OutputStream outputStream = httpsURLConnection.getOutputStream();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            bufferedOutputStream.write(loginJsonBytes);

            bufferedOutputStream.flush();

            outputStream.flush();

            bufferedOutputStream.close();

            outputStream.close();

            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            if(statusCode == 201
                    || statusCode == 200) {

                String responseRequest = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                if(responseRequest.contains("Dados não encontrados")){

                    JSONArray jsonArray = new JSONArray(new String[]{"Dados não encontrados"});

                    return jsonArray;
                }

                httpsURLConnection.disconnect();

                JSONArray jsonObjectResponse = new JSONArray(responseRequest);

                if(jsonObjectResponse.length() >= 1) {

                    return jsonObjectResponse;
                }
                else {

                    return null;
                }
            }
            else if(statusCode == 400
                    && mCount < 2) {

                mCount++;

                httpsURLConnection.disconnect();

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                token = revalidaTokenRequest.executeRequest(ctx);

                return executeRequest(cd_aluno, cd_turma, ctx);
            }
            else {

                httpsURLConnection.disconnect();

                return null;
            }
        }
        catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}
