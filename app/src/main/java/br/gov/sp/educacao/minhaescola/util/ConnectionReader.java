package br.gov.sp.educacao.minhaescola.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

/**
 * Classe que recebe a resposta da Conexão, le os dados e retorna a informação em String
 */

public class ConnectionReader {

    public static String readStringFromHttpsURLConnection(boolean sucess,
                                                          HttpsURLConnection httpsURLConnection)
            throws IOException {

        StringBuilder result = new StringBuilder();

        InputStream inputStream;

        if (sucess) {

            inputStream = httpsURLConnection.getInputStream();
        }
        else {

            inputStream = httpsURLConnection.getErrorStream();
        }
        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int character = bufferedReader.read();

            while (character != -1) {

                result.append((char) character);

                character = bufferedReader.read();
            }
            bufferedReader.close();

            inputStreamReader.close();

            inputStream.close();
        }
        httpsURLConnection.disconnect();

        return result.toString();
    }
}



