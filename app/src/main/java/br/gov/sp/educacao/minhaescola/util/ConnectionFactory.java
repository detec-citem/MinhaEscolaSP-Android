package br.gov.sp.educacao.minhaescola.util;

import java.io.IOException;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Classe para parametrizar detalhes da conexão Https
 */

public class ConnectionFactory {

    public static HttpsURLConnection createHttpsUrlConnection(String httpMethod,
                                                              String url,
                                                              String token)
            throws IOException {

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();

        //Setar os tempos de timeouts

        httpsURLConnection.setConnectTimeout(0);

        httpsURLConnection.setReadTimeout(0);

        //Setar o tipo de método recebido (GET, POST, etc)

        httpsURLConnection.setRequestMethod(httpMethod);

        //Setar a propriedade do Request

        httpsURLConnection.setRequestProperty("Content-Type", "application/json");

        if (httpMethod.equals("POST")) {

            httpsURLConnection.setDoOutput(true);
        }
        if (token != null) {

            //Setar o token como value da key Authorization

            httpsURLConnection.setRequestProperty("Authorization", token);
        }
        return httpsURLConnection;
    }
}

