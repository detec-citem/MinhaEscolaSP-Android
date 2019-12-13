package br.gov.sp.educacao.minhaescola.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by T. Rocha on 16/07/2018.
 */

public class Utils {

    public static StringBuilder lerArquivoEstatico(int fileID, Context context){

        try{

            BufferedReader br = new BufferedReader(

                    new InputStreamReader(context.getResources().openRawResource(fileID), "UTF-8")
            );

            StringBuilder jsonString = new StringBuilder();

            String line;

            while((line = br.readLine()) != null) {

                line = line.trim();

                if(line.length() > 0) {

                    jsonString.append(line);
                }
            }
            return jsonString;
        }
        catch (Exception e){

            e.printStackTrace();

            return null;
        }
    }

    public static String formatarTelefone(String telefone){

        if(telefone != null){
            StringBuilder stringBuilder = new StringBuilder(telefone);
            stringBuilder.insert(telefone.length() - 4, "-");
            stringBuilder.insert(0, "(");
            stringBuilder.insert(3, ")");

            if(stringBuilder.substring(5,6).equals("0")){

                stringBuilder.delete(5,6);
            }

            String telFormatado = stringBuilder.toString();


            return telFormatado;
        }
        else{

            return "Não disponível";
        }
    }

    public static String formatarData(String data){

        String ano = data.substring(0,4);
        String mes = data.substring(5,7);
        String dia = data.substring(8,10);
        //"/".concat(mes.concat("/".concat(ano)))
        return (dia+"/"+mes+"/"+ano);
    }

    public static int getAnoData(String data){

        String anoS = data.substring(0,4);

        int ano = Integer.parseInt(anoS);

        return (ano);
    }

    public static boolean isOnline(Context ctx) {

        // Pego a conectividade do contexto
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Crio o objeto netInfo que recebe as informacoes da Network
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) {

            //Se tem conectividade retorna true
            return true;
        }
        else{
            //Se nao tem conectividade retorna false
            return false;
        }
    }
}
