package br.gov.sp.educacao.minhaescola.requests;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.model.Dispositivo;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;

public class EnviarIdPushRequest {

    public void executeRequest(String token, Dispositivo disp) {

        try {

            HttpsURLConnection httpsURLConnection = ConnectionFactory.createHttpsUrlConnection("POST",
                    UrlServidor.URL_ENVIAR_ID_PUSH, token);

            JSONObject corpoJson = new JSONObject().put("Dispositivo", disp.getDispositivo());

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

            if (statusCode == 201
                    || statusCode == 200) {

                httpsURLConnection.disconnect();

                Log.e("Enviou ID", "sucesso");
            }
            else{

                if(IS_DEBUG)
                    Log.e("erro enviarID", ConnectionReader.readStringFromHttpsURLConnection(false, httpsURLConnection));
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
}
