package br.gov.sp.educacao.minhaescola.util;


import com.google.gson.Gson;

import br.gov.sp.educacao.minhaescola.model.FotoEnvio;

/**
 * Created by T. Rocha on 04/07/2018.
 */

public class GsonParser {

    public static String fotoEnvioToJson(FotoEnvio fotoEnvio) {

        Gson gson = new Gson();

        return gson.toJson(fotoEnvio);
    }
}
