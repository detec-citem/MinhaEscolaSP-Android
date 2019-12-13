package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.FotoEnvio;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

import static br.gov.sp.educacao.minhaescola.util.GsonParser.fotoEnvioToJson;

public class EnviarFotoRequest {

    private int countTentativas = 0;

    private UsuarioQueries usuarioQueries;

    public int executeRequest(byte[] fotoByteArray, String token, Context ctx){

        usuarioQueries = new UsuarioQueries(ctx);

        try {

            countTentativas ++;

            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("POST", UrlServidor.URL_ENVIAR_FOTO, token);

            httpsURLConnection.setRequestProperty("Content-Type", "application/json");

            FotoEnvio fotoEnvio = new FotoEnvio(fotoByteArray);

            String fotoEncode = fotoEnvioToJson(fotoEnvio);

            JSONObject bodyJson = new JSONObject(fotoEncode);

            byte[] bodyJsonBytes = bodyJson.toString().getBytes("UTF-8");

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

            if (statusCode == 201
                    || statusCode == 200) {

                String requestResponse = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                //Cria um objeto JSON a partir da String recebida da conexão

                int responseCode;

                try {

                    responseCode = Integer.parseInt(requestResponse);
                }
                catch (Exception e) {

                    e.printStackTrace();
                    responseCode = -8;
                }

                return responseCode;
            }
            else if(statusCode == 400
                    && countTentativas == 1) {

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                String newToken = revalidaTokenRequest.executeRequest(ctx);

                usuarioQueries.atualizarToken(newToken);

                return this.executeRequest(fotoByteArray, token, ctx);
            }
            else {

                //Se o StatusCode != 200 || 201 ocorreu erro na conexão, retorna null
                return -1;
            }

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }
}
