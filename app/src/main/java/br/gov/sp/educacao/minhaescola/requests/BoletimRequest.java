package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.model.DadosBoletim;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;

public class BoletimRequest {

    public String executeBoletimRequest(DadosBoletim dadosBoletim) {

        try {

            //Cria o objeto para conexão, com o URL e método POST
            HttpsURLConnection httpsURLConnection =
                    ConnectionFactory.createHttpsUrlConnection("POST", UrlServidor.URL_GERAR_BOLETIM_UNIFICADO, null);

            httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            //Cria o objeto JSON para a conexão, adiciona o login e senha no objeto JSON
            String corpo = "nrRa=" + dadosBoletim.getRa() + "&nrDigRa=" + dadosBoletim.getDig() + "&dsUfRa=" + dadosBoletim.getUf() +
                    "&dtNascimento=" + dadosBoletim.getDataNascimento() + "&nrAnoLetivo=" + dadosBoletim.getAno();

            //Transforma o objeto JSON em um objeto byte[] com padrão UTF-8
            byte[] loginJsonBytes = corpo.getBytes("UTF-8");

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
            if (statusCode == 201 || statusCode == 200) {

                String response = ConnectionReader.readStringFromHttpsURLConnection(true, httpsURLConnection);

                //Fecha a conexão
                httpsURLConnection.disconnect();

                return response;
            }
            else{

                //Se o StatusCode != 200 || 201 ocorreu erro na conexão, retorna null
                String loginResponse = ConnectionReader.readStringFromHttpsURLConnection(false, httpsURLConnection);
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
